package ru.yandex.test.impl;

import org.junit.Test;
import ru.yandex.test.Duplicate;
import ru.yandex.test.SearchManager;
import ru.yandex.test.VacancyXmlFileParser;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by IntelliJ IDEA.
 * User: OneHalf
 * Date: 22.07.11
 * Time: 19:47
 */
public class SearchManagerImplTest {

    @Test
    public void testSetSearchInSelf() throws Exception {
        System.out.println("testSetSearchInSelf");
        Set<VacancyXmlFileParser> preparsedFiles;
//--------------------------------------------------------------------------------------------------------
        preparsedFiles = new HashSet<VacancyXmlFileParser>();
        preparsedFiles.add(new VacancyXmlFileParserImpl("file1", "/vacancyExample2.xml"));
        preparsedFiles.add(new VacancyXmlFileParserImpl("file2", "/vacancyExample3.xml"));

        SearchManager searchManager = new SearchManagerImpl();
        searchManager.setPreparsedXmlFiles(preparsedFiles);
        searchManager.setSearchInSelf(false);
        searchManager.setThresholdOfEquivalence(0.55);
        searchManager.initializeSources();
        Set<Duplicate> duplicates = searchManager.searchDuplicates();
//--------------------------------------------------------------------------------------------------------
        preparsedFiles = new HashSet<VacancyXmlFileParser>();
        preparsedFiles.add(new VacancyXmlFileParserImpl("file1", "/vacancyExample2.xml"));
        preparsedFiles.add(new VacancyXmlFileParserImpl("file2", "/vacancyExample3.xml"));

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
        preparsedFiles.add(new VacancyXmlFileParserImpl("file1", "/vacancyExample2.xml"));
        preparsedFiles.add(new VacancyXmlFileParserImpl("file2", "/vacancyExample3.xml"));

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
                "file1", "testResource/vacancyExample2.xml");

        SearchManagerImpl searchManager = new SearchManagerImpl();
        searchManager.setThresholdOfEquivalence(0.55);
        Set<Duplicate> duplicates = searchManager.duplicatesFromOneSource(file);
        assertEquals("Число дубликатов", 1, duplicates.size());
    }

    @Test
    public void testDuplicatesFromDifferent() throws Exception {
        Set<VacancyXmlFileParser> preparsedFiles = new HashSet<VacancyXmlFileParser>();
        preparsedFiles.add(new VacancyXmlFileParserImpl("file1", "/vacancyExample2.xml"));
        preparsedFiles.add(new VacancyXmlFileParserImpl("file2", "/vacancyExample3.xml"));

        SearchManagerImpl searchManager = new SearchManagerImpl();
        searchManager.setPreparsedXmlFiles(preparsedFiles);
        searchManager.setSearchInSelf(false);
        searchManager.setThresholdOfEquivalence(0.55);
        searchManager.initializeSources();
        Set<Duplicate> duplicates = searchManager.duplicatesFromDifferentSources();

        assertEquals("Число дубликатов", 1, duplicates.size());
    }
}
