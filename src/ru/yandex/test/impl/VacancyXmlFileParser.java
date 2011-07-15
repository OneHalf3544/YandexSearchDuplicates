package ru.yandex.test.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import ru.yandex.test.Vacancy;
import ru.yandex.test.VacancySource;
import ru.yandex.test.impl.vacancyparser.VacancySaxParserHandler;

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
    private final VacancySaxParserHandler vacancyCreatorHandler = new VacancySaxParserHandler();
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
            query = vacancyCreatorHandler.getQuery();
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

}
