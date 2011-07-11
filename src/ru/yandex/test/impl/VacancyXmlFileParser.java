package ru.yandex.test.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.yandex.test.Salary;
import ru.yandex.test.Vacancy;
import ru.yandex.test.VacancySource;

/**
 * Класс представляющий собранный Webharvest'ом файл
 * С помощью SAX-парсера создает список собранных вакансий.
 * 
 * @author OneHalf
 */
 public class VacancyXmlFileParser implements VacancySource {
    
    private static final Logger LOGGER = Logger.getLogger(VacancyXmlFileParser.class.getName());
    
    private List<Vacancy> vacancies;
    private String query;
    private final VacancyCreatorHandler vacancyCreatorHandler = new VacancyCreatorHandler();
    private String sourceName;

    /**
     * Конструктор объекта с указанием файла, из которого нужно брать данные.
     * Названием источника будет имя файла
     * @param xmlFile Файл, созданный WebHarvest'ом
     */
    public VacancyXmlFileParser(File xmlFile) {
        this(xmlFile.getName(), xmlFile);
    }

    /**
     * Конструктор объекта с указанием файла, из которого нужно брать данные 
     * и названия источника
     * @param sourceName Название источника
     * @param xmlFile Файл, созданный WebHarvest'ом
     */
    public VacancyXmlFileParser(String sourceName, File xmlFile) {
        this.sourceName = sourceName;
        try {
            this.parse(new FileInputStream(xmlFile));
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.WARNING, "Нет такого файла", ex);
        }
    }

    /**
     * Создание объекта по имени и строке, которая может быть адресом ресурса
     * или именем файла
     * @param name Имя источника
     * @param resourceName Имя ресурса 
     */
    public VacancyXmlFileParser(String name, String resourceName) {
        this.sourceName = name;
        InputStream resourceStream = VacancyXmlFileParser.class.getResourceAsStream(resourceName);
        try {
            if (resourceStream != null) {
                this.parse(resourceStream);
            } 
            else {
                this.parse(new FileInputStream(resourceName));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VacancyXmlFileParser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Конструктор объекта с указанием потока, из которого нужно брать данные 
     * и названия источника
     * @param name Название источника
     * @param inputStream Поток созданный на основе данных собранных WebHarvest'ом
     */
    public VacancyXmlFileParser(String name, InputStream inputStream) {
        this.sourceName = name;
        this.parse(inputStream);
    }

    private void parse(InputStream inputStream) {
        try {
            LOGGER.log(Level.FINE, "Начало парсинга xml-потока \"{0}\" с вакансиями", sourceName);
            SAXParserFactory SAXFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = SAXFactory.newSAXParser();
            saxParser.parse(inputStream, vacancyCreatorHandler);
            
            vacancies = vacancyCreatorHandler.getVacancies();
            query = Tag.getQuery();
            LOGGER.log(Level.FINE, "Окончание парсинга xml-потока \"{0}\" с вакансиями", sourceName);
        } catch(Exception e){
            LOGGER.log(Level.WARNING, "Ошибка парсинга xml-потока \""+sourceName+"\"", e);
        }
        finally {
            try { inputStream.close(); } catch (IOException ex) {}
        }
    }

    @Override
    public List<Vacancy> getVacancies() {
        return vacancies;
    }

    @Override
    public String getSourceName() {
        return sourceName;
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
                if (qName.equals(tag.TAG_NAME)) {
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
                if (qName.equals(tag.TAG_NAME)) {
                    tag.ending();
                    break;
                }
            }
        }
        
        public List<Vacancy> getVacancies() {
            return Collections.unmodifiableList(Tag.vacancies);
        }
    }
    
    /**
     * Теги документа. Содержат функции для обработки xml-документа
     */
    private enum Tag {
        VACANCIES("vacancies") {
            @Override
            public void starting(Attributes attributes) {
                super.starting(attributes);
                vacancies = new LinkedList<Vacancy>();
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
                final String url = attributes.getValue("", "url").replaceAll("(^\\s+)|(\\s+$)", "");
                currentVacancy.setVacancyUrl(url);
                textBuffer = new StringBuilder();
            }

            @Override
            public void text(char[] ch, int start, int length) {
                textBuffer.append(ch, start, length);
            }

            @Override
            public void ending() {
                currentVacancy.setVacancyName(textBuffer.toString().replaceAll("(^\\s+)|(\\s+$)", ""));
                super.ending();
            }
        },
        COMPANY_NAME("companyName") {
            @Override
            public void starting(Attributes attributes) {
                super.starting(attributes);
                currentVacancy.setCompanyUrl(attributes.getValue("", "companyUrl"));
                textBuffer = new StringBuilder();
            }

            @Override
            public void text(char[] ch, int start, int length) {
                textBuffer.append(ch, start, length);
            }

            @Override
            public void ending() {
                currentVacancy.setCompanyName(textBuffer.toString());
                super.ending();
            }
            
        },
        CITY("vacancyCity") {

            @Override
            public void starting(Attributes attributes) {
                super.starting(attributes);
                textBuffer = new StringBuilder();
            }
            
            @Override
            public void text(char[] ch, int start, int length) {
                textBuffer.append(ch, start, length);
            }

            @Override
            public void ending() {
                currentVacancy.setCity(textBuffer.toString());
                super.ending();
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
                textBuffer = new StringBuilder();
                
            }

            @Override
            public void text(char[] ch, int start, int length) {
                textBuffer.append(ch, start, length);
            }

            @Override
            public void ending() {
                currentVacancy.setDescription(textBuffer.toString());
                textBuffer = null;
                super.ending();
            }
            
        };
        
        
        private static Integer minSalary;
        private static Integer maxSalary;
        
        private static String query;
        private static StringBuilder textBuffer;
        
        private static Vacancy currentVacancy;
        private static Tag currentTag;
        
        private static List<Vacancy> vacancies;
        
        /**
         * Название соответствующего тега в xml-документе
         */
        public final String TAG_NAME;

        Tag(String tagName) {
            this.TAG_NAME = tagName;
        }
        
        public static List<Vacancy> getVacancies() {
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
