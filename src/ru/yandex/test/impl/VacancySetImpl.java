package ru.yandex.test.impl;

import com.sun.org.apache.xerces.internal.parsers.SAXParser;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import ru.yandex.test.Salary;
import ru.yandex.test.Vacancy;
import ru.yandex.test.VacancySet;

/**
 * Класс набора вакансий.
 * 
 * @author OneHalf
 */
public class VacancySetImpl implements VacancySet {
    private Set<Vacancy> vacancies = new HashSet<Vacancy>();

    @Override
    public void parse(File file) {
        try {
            XMLReader reader = new SAXParser();
            reader.setContentHandler(new VacancyCreatorHandler());
            reader.parse(new InputSource(new FileReader(file)));
        } catch(Exception e){
            System.out.println(e);
        }
    }

    @Override
    public Set<Vacancy> getVacansies() {
        return Collections.unmodifiableSet(vacancies);
    }

    private class VacancyCreatorHandler extends DefaultHandler {

        private Integer minSalary;
        private Integer maxSalary;
        
        private Vacancy currentVacancy;
        private Tag currentTag;
        
        private VacancyCreatorHandler() {
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if (localName.equals("vacancy")) {
                currentVacancy = new VacancyImpl();
            }
            else if (localName.equals(Tag.VACANCY_NAME.TAG_NAME)) {
                currentTag = Tag.VACANCY_NAME;
                currentVacancy.setVacancyUrl(attributes.getValue("", "url"));
            }
            else if (localName.equals(Tag.COMPANY_NAME.TAG_NAME)) {
                currentTag = Tag.COMPANY_NAME;
                currentVacancy.setCompanyUrl(attributes.getValue("", "companyUrl"));
            }
            else if (localName.equals(Tag.SALARY.TAG_NAME)) {
                currentTag = Tag.SALARY;
                minSalary = null; maxSalary = null;
            }
            else if (localName.equals(Tag.MINIMUM_SALARY.TAG_NAME)) {
                currentTag = Tag.MINIMUM_SALARY;
            }
            else if (localName.equals(Tag.MAXIMUM_SALARY.TAG_NAME)) {
                currentTag = Tag.MAXIMUM_SALARY;
            }
            else if (localName.equals(Tag.CITY.TAG_NAME)) {
                currentTag = Tag.CITY;
            }
            else if (localName.equals(Tag.DESCRIPTION.TAG_NAME)) {
                currentTag = Tag.DESCRIPTION;
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (currentTag == null || currentTag == Tag.SALARY) {
                return;
            }
            String value = new String(ch, start, length);
            if (currentTag == Tag.CITY) {
                currentVacancy.setCity(value);
            }
            else if (currentTag == Tag.COMPANY_NAME) {
                currentVacancy.setCompanyName(value);
            }
            else if (currentTag == Tag.MAXIMUM_SALARY) {
                try {
                    maxSalary = Integer.parseInt(value);
                } catch (NumberFormatException ex) {
                    maxSalary = null;
                }
            }
            else if (currentTag == Tag.MINIMUM_SALARY) {
                try {
                    minSalary = Integer.parseInt(value);
                } catch (NumberFormatException ex) {
                    minSalary = null;
                }
            }
            else if (currentTag == Tag.VACANCY_NAME) {
                currentVacancy.setVacancyName(value);
            }
            else if (currentTag == Tag.DESCRIPTION) {
                currentVacancy.setDescription(value);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (currentTag == Tag.MINIMUM_SALARY || currentTag == Tag.MAXIMUM_SALARY) {
                currentTag = Tag.SALARY;
            }
            else if (currentTag == Tag.SALARY) {
                currentVacancy.setSalary(new Salary(minSalary, maxSalary));
            }
            currentTag = null;
            
            if (localName.equals(Tag.VACANCY.TAG_NAME)) {
                vacancies.add(currentVacancy);
                currentVacancy = null;
            }
        }
        
    }
    
    private enum Tag {
        VACANCY("vacancy"),
        VACANCY_NAME("vacancyName"),
        COMPANY_NAME("companyName"),
        CITY("vacancyCity"), 
        SALARY("salary"),
        MINIMUM_SALARY("minimum"),
        MAXIMUM_SALARY("maximum"),
        DESCRIPTION("description");
        
        /**
         * Название соответствующего тега в xml-документе
         */
        public final String TAG_NAME;

        Tag(String tagName) {
            this.TAG_NAME = tagName;
        }
    }
}
