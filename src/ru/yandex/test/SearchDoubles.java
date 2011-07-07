package ru.yandex.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**
 *
 * @author OneHalf
 */
public class SearchDoubles {
    
    private static BeanFactory beanFactory;
    private static final Double THRESHOLD_OF_EQUIVALENCE = 0.5;

    static {
        Resource resource = new FileSystemResource("src/ru/yandex/test/config.xml");
        beanFactory = new XmlBeanFactory(resource);
    }
    
    private SearchDialog dialog;
    private ReportCreator reportCreator;
    private Map<VacancySource, VacancySet> vacancyMap;
    private static final Logger LOGGER = Logger.getLogger(SearchDoubles.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Logger.getLogger("ru.yandex.test").setLevel(Level.ALL);
        beanFactory.getBean("SearchDoubles");
    }
        
    public void setDialog(SearchDialog dialog) {
        this.dialog = dialog;
        dialog.addSearchListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadVacancySet();
                Set<Duplicate> duplicatedVacancies = searchDuplicateVacancy();
                SearchDoubles.this.dialog.setDuplicateVacancy(duplicatedVacancies);
            }
        });
    }
    
    public void setReportCreator(ReportCreator reportCreator) {
        this.reportCreator = reportCreator;
        dialog.addReportListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SearchDoubles.this.reportCreator.getReport();
            }
        });
    }

    public void loadVacancySet() {
        vacancyMap = new HashMap<VacancySource, VacancySet>();
        for (VacancySource siteParser : dialog.getVacancySources()) {
            siteParser.setSearchText(dialog.getSearchString());
            siteParser.setItemsCount(dialog.getItemsCount());
            vacancyMap.put(siteParser, siteParser.getVacancySet());
        }
    }
    
    private Set<Duplicate> searchDuplicateVacancy() {
        LOGGER.log(Level.FINE, "Поиск дубликатов");
        
        Set<Duplicate> result = new HashSet<Duplicate>();
        final VacancySet[] vacancySets = vacancyMap.values().toArray(new VacancySet[]{});
        
        for (int i = 0; i < vacancySets.length; i++) {
            for (int j = i+1; j < vacancySets.length; j++) {
                for (Vacancy k : vacancySets[i].getVacancies()) {
                    for (Vacancy l : vacancySets[j].getVacancies()) {
                        if (k.getLevelOfSimilarity(l) > THRESHOLD_OF_EQUIVALENCE) {
                            result.add(new Duplicate(k, l));
                            LOGGER.log(Level.FINER, "Найдены дубликаты: \n{0}, \n{1}", new Object[]{k, l});
                        }
                    }                    
                }
            }
        }
        LOGGER.log(Level.FINE, "Окончание поиска дубликатов");
        return result;
    }

    public static class Duplicate {

        private Set<Vacancy> duplicates = new HashSet<Vacancy>();
        
        public Duplicate(Vacancy ... vacancies) {
            duplicates.addAll(Arrays.asList(vacancies));
        }

        public Set<Vacancy> getDuplicates() {
            return Collections.unmodifiableSet(duplicates);
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            for (Vacancy vacancy : duplicates) {
                result.append(vacancy).append("\n");
            }
            return result.toString();
        }
    }

}
