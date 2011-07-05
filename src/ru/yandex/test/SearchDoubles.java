package ru.yandex.test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
    private static final Double THRESHOLD_OF_EQUIVALENCE = 0.8;

    static {
        Resource resource = new FileSystemResource("src/ru/yandex/test/config.xml");
        beanFactory = new XmlBeanFactory(resource);
    }
    
    private SearchDialog dialog;
    private ReportCreator reportCreator;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        beanFactory.getBean("SearchDoubles");
    }
    private Map<VacancySource, VacancySet> vacancyMap;
        
    public void setDialog(SearchDialog dialog) {
        this.dialog = dialog;
        dialog.addSearchListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadVacancySet();
                searchDuplicateVacancy();
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
            vacancyMap.put(siteParser, siteParser.getVacancySet());
        }
    }
    
    private Set<Duplicate> searchDuplicateVacancy() {
        Set<Duplicate> result = new HashSet<Duplicate>();
        final VacancySet[] vacancySets = vacancyMap.values().toArray(new VacancySet[]{});
        
        for (int i = 0; i < vacancySets.length; i++) {
            for (int j = i+1; j < vacancySets.length; j++) {
                for (Vacancy k : vacancySets[i].getVacansies()) {
                    for (Vacancy l : vacancySets[j].getVacansies()) {
                        if (k.getLevelOfSimilarity(l) > THRESHOLD_OF_EQUIVALENCE) {
                            result.add(new Duplicate(k, l));
                        }
                    }                    
                }
            }
        }
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
    }

}
