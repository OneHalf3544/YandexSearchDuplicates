package ru.yandex.test.impl;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.yandex.test.Vacancy;

/**
 *
 * @author OneHalf
 */
public class VacancyXmlFileParserTest {
    
    public VacancyXmlFileParserTest() {
    }

    /**
     * Test of parse method, of class VacancyXmlFileParser.
     * @throws UnsupportedEncodingException 
     */
    @Test
    public void testParse() throws UnsupportedEncodingException {
        System.out.println("parse");
        VacancyXmlFileParser instance = new VacancyXmlFileParser("vacancyexample.xml", "vacancyexample.xml");
        
        List<Vacancy> v = instance.getVacancies();
        Vacancy[] vacancies = v.toArray(new Vacancy[]{});
        
        assertEquals("Java Developer", vacancies[0].getVacancyName());
        assertEquals("Actimind", vacancies[0].getCompanyName());
        assertEquals(Integer.valueOf(40000), vacancies[0].getSalary().getMinimum());
        assertEquals(Integer.valueOf(70000), vacancies[0].getSalary().getMaximum());
        
        assertEquals("Java developer", vacancies[1].getVacancyName());
        assertEquals("Actimind Inc. ", vacancies[1].getCompanyName());
        assertEquals(Integer.valueOf(40000), vacancies[1].getSalary().getMinimum());
        assertEquals(Integer.valueOf(70000), vacancies[1].getSalary().getMaximum());
        
        assertEquals(0.90, vacancies[0].getLevelOfSimilarity(vacancies[1]), 0.10);
    }
}
