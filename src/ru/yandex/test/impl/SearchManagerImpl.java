package ru.yandex.test.impl;

import org.springframework.stereotype.Service;
import ru.yandex.test.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: OneHalf
 * Date: 22.07.11
 * Time: 17:19
 */
@Service
public class SearchManagerImpl implements SearchManager {

    private static final Logger LOGGER = Logger.getLogger(SearchManagerImpl.class.getName());

    private double thresholdOfEuivalence = 0.55;
    private boolean searchInSelf = true;

    private List<VacancySource> vacancySources;
    private Set<VacancyXmlFileParser> preparsedFiles = new HashSet<VacancyXmlFileParser>();
    private Set<SiteParser> siteParsers = new HashSet<SiteParser>();

    private int itemsCount;
    private String query;

    @Override
    public void initializeSources() throws InterruptedException {
        vacancySources = new LinkedList<VacancySource>();
        vacancySources.addAll(preparsedFiles);
        vacancySources.addAll(siteParsers);

        final CountDownLatch latch = new CountDownLatch(siteParsers.size());

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (final SiteParser siteParser : siteParsers) {
            // Установка данных поиска
            siteParser.setSearchText(query);
            siteParser.setItemsCount(itemsCount);

            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    // Получаем вакансии чтобы "обмануть" ленивую инициализацию
                    siteParser.getVacancies();
                    latch.countDown();
                }
            });
        }
        executorService.shutdown();

        latch.await();
    }

    /**
     * Установка уровня эквивалентности вакансий
     * @param thresholdOfEquivalence Порог эквивалентости определяющий уровень
     * "похожести" ввакансий при котором они считаются равными и попадают в отчет
     */
    @Override
    public void setThresholdOfEquivalence(double thresholdOfEquivalence) {
        this.thresholdOfEuivalence = thresholdOfEquivalence;
    }

    @Override
    public void setQueryString(String query) {
        this.query = query;
    }

    @Override
    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }

    @Override
    public double getThresholdOfEquivalence() {
        return thresholdOfEuivalence;
    }

    /**
     * Установка сайтов-источников. Добавляет сайты источники в программу и ChekBox'ы в диалог
     * @param siteParsers Сайты-источники
     */
    @Override
    public void setSiteParsers(Set<SiteParser> siteParsers) {
        this.siteParsers = siteParsers;
    }

    /**
     * Установка файлов-источников. Добавляет файлы-источники в программу и ChekBox'ы в диалог
     * @param files Файлы-источники
     */
    @Override
    public void setPreparsedXmlFiles(Set<VacancyXmlFileParser> files) {
        this.preparsedFiles = files;
    }

    /**
    * Поиск дублирующихся вакансий в разных источниках
    * @return Набор дубликатов
    */
    Set<Duplicate> duplicatesFromDifferentSources() {
        LOGGER.log(Level.FINE, "Поиск дубликатов");

        Set<Duplicate> result = new HashSet<Duplicate>();

        // Получаем пару источников вакансий
        for (int i = 0; i < vacancySources.size(); i++) {
            for (int j = i+1; j < vacancySources.size(); j++) {

                // Сравниваем все вакансии из данной пары источников
                for (Vacancy k : vacancySources.get(i).getVacancies()) {
                    for (Vacancy l : vacancySources.get(j).getVacancies()) {

                        if (k.getLevelOfSimilarity(l) > thresholdOfEuivalence) {
                            result.add(new Duplicate(k, l));
                            LOGGER.log(Level.FINE, "Найдены дубликаты: \n{0}, \n{1}",
                                    new Object[]{k, l});
                        }
                    }
                }
            }
        }
        LOGGER.log(Level.FINER, "Окончание поиска дубликатов");

        return result;
    }

    /**
     * Поиск дублирующихся данных в пределах одного источника
     *
     * @param vacancySource Источник вакансий
     * @return Дубликаты вакансий
     */
    Set<Duplicate> duplicatesFromOneSource(VacancySource vacancySource) {
        Set<Duplicate> result = new HashSet<Duplicate>();

        List<Vacancy> vacancies = vacancySource.getVacancies();

        for (int i = 0; i < vacancies.size(); i++) {
            for (int j = i+1; j < vacancies.size(); j++) {
                 if (vacancies.get(i).getLevelOfSimilarity(vacancies.get(j)) > thresholdOfEuivalence) {
                    result.add(new Duplicate(vacancies.get(i), vacancies.get(j)));
                    LOGGER.log(Level.FINE, "Найдены дубликаты: \n{0}, \n{1}",
                            new Object[]{vacancies.get(i), vacancies.get(j)});
                }
            }
        }
        return result;
    }

    @Override
    public void setSearchInSelf(boolean searchInSelf) {
        this.searchInSelf = searchInSelf;
    }

    @Override
    public Set<Duplicate> searchDuplicates() {
        Set<Duplicate> result = new HashSet<Duplicate>();

        result.addAll(duplicatesFromDifferentSources());

        if (searchInSelf) {
            for (VacancySource vacancySource : vacancySources) {
                result.addAll(duplicatesFromOneSource(vacancySource));
            }
        }
        return result;
    }
}
