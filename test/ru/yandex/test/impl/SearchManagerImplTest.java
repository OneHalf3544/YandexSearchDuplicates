package ru.yandex.test.impl;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.*;

import ru.yandex.test.*;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: OneHalf
 * Date: 22.07.11
 * Time: 19:47
 */
public class SearchManagerImplTest {

    @Test @Ignore
    public void testInitializeSources() {
        fail("This is a prototype");
    }

    @Test
    public void testSetSearchInSelf() throws Exception {
        System.out.println("testSetSearchInSelf");
        Set<VacancyXmlFileParser> preparsedFiles;
//--------------------------------------------------------------------------------------------------------
        preparsedFiles = new HashSet<VacancyXmlFileParser>();
        preparsedFiles.add(new VacancyXmlFileParserImpl("file1", "/ru/yandex/test/impl/vacancyExample2.xml"));
        preparsedFiles.add(new VacancyXmlFileParserImpl("file2", "/ru/yandex/test/impl/vacancyExample3.xml"));

        SearchManager searchManager = new SearchManagerImpl();
        searchManager.setPreparsedXmlFiles(preparsedFiles);
        searchManager.setSearchInSelf(false);
        searchManager.setThresholdOfEquivalence(0.55);
        searchManager.initializeSources();
        Set<Duplicate> duplicates = searchManager.searchDuplicates();
//--------------------------------------------------------------------------------------------------------
        preparsedFiles = new HashSet<VacancyXmlFileParser>();
        preparsedFiles.add(new VacancyXmlFileParserImpl("file1", "/ru/yandex/test/impl/vacancyExample2.xml"));
        preparsedFiles.add(new VacancyXmlFileParserImpl("file2", "/ru/yandex/test/impl/vacancyExample3.xml"));

        SearchManager searchManager2 = new SearchManagerImpl();
        searchManager2.setPreparsedXmlFiles(preparsedFiles);
        searchManager2.setSearchInSelf(true);
        searchManager2.setThresholdOfEquivalence(0.55);
        searchManager2.initializeSources();
        Set<Duplicate> duplicates2 = searchManager2.searchDuplicates();
//--------------------------------------------------------------------------------------------------------
        assertTrue("Число дубликатов", duplicates.size() != duplicates2.size());
    }

    @Test
    public void testSearchDuplicates() throws Exception {
        Set<VacancyXmlFileParser> preparsedFiles = new HashSet<VacancyXmlFileParser>();
        preparsedFiles.add(new VacancyXmlFileParserImpl("file1", "/ru/yandex/test/impl/vacancyExample2.xml"));
        preparsedFiles.add(new VacancyXmlFileParserImpl("file2", "/ru/yandex/test/impl/vacancyExample3.xml"));
//        preparsedFiles.add(new VacancyXmlFileParserMock());

        SearchManager searchManager = new SearchManagerImpl();
        searchManager.setPreparsedXmlFiles(preparsedFiles);
        searchManager.setSearchInSelf(true);
        searchManager.setThresholdOfEquivalence(0.55);
        searchManager.initializeSources();
        Set<Duplicate> duplicates = searchManager.searchDuplicates();

        assertEquals("Число дубликатов", 2, duplicates.size());
    }

    @Test
    public void testDuplicatesFromSelf() throws Exception {
        System.out.println("testDuplicatesFromSelf");

        VacancyXmlFileParser file = new VacancyXmlFileParserImpl(
                "file1", "/ru/yandex/test/impl/vacancyExample2.xml");

        SearchManagerImpl searchManager = new SearchManagerImpl();
        searchManager.setThresholdOfEquivalence(0.55);
        Set<Duplicate> duplicates = searchManager.duplicatesFromOneSource(file);
        assertEquals("Число дубликатов", 1, duplicates.size());
    }

    @Test
    public void testDuplicatesFromDifferent() throws Exception {
        Set<VacancyXmlFileParser> preparsedFiles = new HashSet<VacancyXmlFileParser>();
        preparsedFiles.add(new VacancyXmlFileParserImpl("file1", "/ru/yandex/test/impl/vacancyExample2.xml"));
        preparsedFiles.add(new VacancyXmlFileParserImpl("file2", "/ru/yandex/test/impl/vacancyExample3.xml"));

        SearchManagerImpl searchManager = new SearchManagerImpl();
        searchManager.setPreparsedXmlFiles(preparsedFiles);
        searchManager.setSearchInSelf(false);
        searchManager.setThresholdOfEquivalence(0.55);
        searchManager.initializeSources();
        Set<Duplicate> duplicates = searchManager.duplicatesFromDifferentSources();

        assertEquals("Число дубликатов", 1, duplicates.size());
    }

    private static class VacancyXmlFileParserMock implements VacancyXmlFileParser {

        final static Vacancy vacancy1 = new VacancyImpl("Программист", "url.ru", "Рога и копыта", "url2", "Питер",
                new Salary(), "Описание вакансии должно быть больше 6 слов, чтобы сработал " +
                "алгоритм шинглов. Бла Бла Бла. ");
        final static Vacancy vacancy2 = new VacancyImpl("Какая-то другая вакансия",
                "url.ru",
                "Еще какая-то фирма",
                "url2",
                "Масква",
                new Salary(10000, 20000),
                "Тут тоже много-много-много слов, счтобы правильно посчитались хеши из подстрок текста");
        final static Vacancy vacancy3 = new VacancyImpl("Курьер, (Топ-Топ-Менеджер)",
                "url.ru",
                "Рог и копыто", "url2", "Питер",
                new Salary(null, 100000), "Описание вакансии должно быть больше 6 слов, чтобы сработал " +
                "алгоритм шинглов. Бла Бла Бла. ");
        final static Vacancy vacancy4 = new VacancyImpl("Программист", "url.ru", "Рога и копыта", "url2", "Питер",
                new Salary(), "Описание вакансии должно быть больше 6 слов, чтобы сработал " +
                "алгоритм шинглов. Бла Бла Бла. ");

        private List<Vacancy> vacanciesList = new LinkedList<Vacancy>();
        {
            vacanciesList.add(vacancy1);
            vacanciesList.add(vacancy2);
            vacanciesList.add(vacancy3);
            vacanciesList.add(vacancy4);
        }

        @Override
        public List<Vacancy> getVacancies() {
            return vacanciesList;
        }

        @Override
        public String getSourceName() {
            return "СайтПоПоискуРаботы.рф";
        }

        @Override
        public String getQuery() {
            return "BlaBlaDeveloper";
        }
    }
}
