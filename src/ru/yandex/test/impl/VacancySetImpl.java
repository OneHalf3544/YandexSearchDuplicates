package ru.yandex.test.impl;

import com.sun.org.apache.xerces.internal.parsers.SAXParser;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    private static final Logger LOGGER = Logger.getLogger(VacancySetImpl.class.getName());
    
    private Set<Vacancy> vacancies;
    private String query;
    private final VacancyCreatorHandler vacancyCreatorHandler = new VacancyCreatorHandler();

    @Override
    public void parse(Reader reader) {
        LOGGER.log(Level.INFO, "Начало парсинга xml-потока с вакансиями");
        try {
            XMLReader parser = new SAXParser();
            parser.setContentHandler(vacancyCreatorHandler);
            parser.parse(new InputSource(reader));
            
            vacancies = vacancyCreatorHandler.getVacancies();
            query = Tag.getQuery();
        } catch(Exception e){
            LOGGER.log(Level.INFO, "Ошибка парсинга потока");
        }
        LOGGER.log(Level.INFO, "Окончание парсинга xml-потока с вакансиями");
    }

    @Override
    public Set<Vacancy> getVacancies() {
        return vacancies;
    }

    /**
     * Класс, объекты которого должны оповещаться о событиях просмотра xml-файлов
     */
    private class VacancyCreatorHandler extends DefaultHandler {

        private VacancyCreatorHandler() {
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            for (Tag tag : Tag.values()) {
                if (localName.equals(tag.TAG_NAME)) {
                    tag.starting(attributes);
                    break;
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (Tag.currentTag != null) {
                Tag.currentTag.text(ch, start, length);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            for (Tag tag : Tag.values()) {
                if (localName.equals(tag.TAG_NAME)) {
                    tag.ending();
                    break;
                }
            }
        }
        
        public Set<Vacancy> getVacancies() {
            return Collections.unmodifiableSet(Tag.vacancies);
        }
    }
    
    private enum Tag {
        VACANCIES("vacancies") {
            @Override
            public void starting(Attributes attributes) {
                super.starting(attributes);
                vacancies = new HashSet<Vacancy>();
            }            
        },
        QUERY("query") {
            @Override
            public void starting(Attributes attributes) {
                query = attributes.getValue("query");
            }
        },
        VACANCY("vacancy") {
            @Override
            public void starting(Attributes attributes) {
                currentVacancy = new VacancyImpl();
            }

            @Override
            public void ending() {
                vacancies.add(currentVacancy);
                currentVacancy = null;
            }
        },
        VACANCY_NAME("vacancyName") {

            @Override
            public void starting(Attributes attributes) {
                currentTag = Tag.VACANCY_NAME;
                currentVacancy.setVacancyUrl(attributes.getValue("", "url"));
            }

            @Override
            public void text(char[] ch, int start, int length) {
                String value = new String(ch, start, length);
                currentVacancy.setVacancyName(value);
            }
            
        },
        COMPANY_NAME("companyName") {
            @Override
            public void starting(Attributes attributes) {
                super.starting(attributes);
                currentVacancy.setCompanyUrl(attributes.getValue("", "companyUrl"));
            }

            @Override
            public void text(char[] ch, int start, int length) {
                String value = new String(ch, start, length);
                currentVacancy.setCompanyName(value);
            }
        },
        CITY("vacancyCity") {
            @Override
            public void text(char[] ch, int start, int length) {
                String value = new String(ch, start, length);
                currentVacancy.setCity(value);
            }
        }, 
        SALARY("salary") {
            @Override
            public void starting(Attributes attributes) {
                super.starting(attributes);
                minSalary = null; 
                maxSalary = null;
            }

            @Override
            public void ending() {
                currentVacancy.setSalary(new Salary(minSalary, maxSalary));
            }
        },
        MINIMUM_SALARY("minimum") {

            @Override
            public void starting(Attributes attributes) {
                super.starting(attributes);
            }

            @Override
            public void ending() {
                currentTag = Tag.SALARY;
            }

            @Override
            public void text(char[] ch, int start, int length) {
                String value = new String(ch, start, length);
                try {
                    minSalary = Integer.parseInt(value);
                } catch (NumberFormatException ex) {
                    minSalary = null;
                }
            }
            
        },
        MAXIMUM_SALARY("maximum") {

            @Override
            public void starting(Attributes attributes) {
                super.starting(attributes);
            }

            @Override
            public void ending() {
                currentTag = Tag.SALARY;
            }

            @Override
            public void text(char[] ch, int start, int length) {
                String value = new String(ch, start, length);
                try {
                    maxSalary = Integer.parseInt(value);
                } catch (NumberFormatException ex) {
                    maxSalary = null;
                }
            }
            
        },
        DESCRIPTION("description") {

            @Override
            public void starting(Attributes attributes) {
                super.starting(attributes);
            }

            @Override
            public void text(char[] ch, int start, int length) {
                String value = new String(ch, start, length);
                currentVacancy.setDescription(value);
            }
            
        };
        
        
        private static Integer minSalary;
        private static Integer maxSalary;
        
        private static String query;
        
        private static Vacancy currentVacancy;
        private static Tag currentTag;
        
        private static Set<Vacancy> vacancies;
        
        /**
         * Название соответствующего тега в xml-документе
         */
        public final String TAG_NAME;

        Tag(String tagName) {
            this.TAG_NAME = tagName;
        }
        
        public static Set<Vacancy> getVacancies() {
            return vacancies;
        }
        
        public static String getQuery() {
            return query;
        }
        
        public void starting(Attributes attributes) {
            currentTag = this;
        }
        
        public void text(char[] ch, int start, int length) {
        }
        
        public void ending() {
            currentTag = null;
        }
    }
}
