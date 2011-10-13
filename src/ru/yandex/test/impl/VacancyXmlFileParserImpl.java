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
import ru.yandex.test.VacancyXmlFileParser;
import ru.yandex.test.impl.vacancyparser.VacancySaxParserHandler;

/**
 * Класс представляющий собранный Webharvest'ом файл
 * С помощью SAX-парсера создает список собранных вакансий.
 * 
 * @author OneHalf
 */
 public class VacancyXmlFileParserImpl implements VacancyXmlFileParser {
    
    private static final Logger log = Logger.getLogger(VacancyXmlFileParserImpl.class.getName());
    
    private List<Vacancy> vacancies;
    private String query;
    private final VacancySaxParserHandler vacancyCreatorHandler = new VacancySaxParserHandler();
    private String sourceName;

    /**
     * Конструктор объекта с указанием файла, из которого нужно брать данные.
     * Названием источника будет имя файла
     * @param xmlFile Файл, созданный WebHarvest'ом
     */
    public VacancyXmlFileParserImpl(File xmlFile) {
        this(xmlFile.getName(), xmlFile);
    }

    /**
     * Конструктор объекта с указанием файла, из которого нужно брать данные 
     * и названия источника
     * @param sourceName Название источника
     * @param xmlFile Файл, созданный WebHarvest'ом
     */
    public VacancyXmlFileParserImpl(String sourceName, File xmlFile) {
        this.sourceName = sourceName;
        try {
            this.parse(new FileInputStream(xmlFile));
        } catch (FileNotFoundException ex) {
            log.log(Level.WARNING, "Нет такого файла", ex);
        }
    }

    /**
     * Создание объекта по имени и строке, которая может быть адресом ресурса
     * или именем файла
     * @param name Имя источника
     * @param resourceName Имя ресурса 
     */
    public VacancyXmlFileParserImpl(String name, String resourceName) {
        this.sourceName = name;
        InputStream resourceStream = VacancyXmlFileParserImpl.class.getResourceAsStream(resourceName);
        try {
            if (resourceStream != null) {
                this.parse(resourceStream);
            } 
            else {
                this.parse(new FileInputStream(resourceName));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(VacancyXmlFileParserImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Конструктор объекта с указанием потока, из которого нужно брать данные 
     * и названия источника
     * @param name Название источника
     * @param inputStream Поток созданный на основе данных собранных WebHarvest'ом
     */
    public VacancyXmlFileParserImpl(String name, InputStream inputStream) {
        this.sourceName = name;
        this.parse(inputStream);
    }

    private void parse(InputStream inputStream) {
        try {
            log.log(Level.FINE, "Начало парсинга xml-потока \"{0}\" с вакансиями", sourceName);

            SAXParserFactory SAXFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = SAXFactory.newSAXParser();
            saxParser.parse(inputStream, vacancyCreatorHandler);
            
            vacancies = vacancyCreatorHandler.getVacancies();
            query = vacancyCreatorHandler.getQuery();

            log.log(Level.FINE, "Окончание парсинга xml-потока \"{0}\" с вакансиями", sourceName);
        }
        catch(Exception e){
            log.log(Level.WARNING, "Ошибка парсинга xml-потока \""+sourceName+"\"", e);
        }
        finally {
            try { inputStream.close(); } catch (IOException ignored) {}
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

    @Override
    public String getQuery() {
        return query;
    }
}