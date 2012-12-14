package ru.yandex.test.impl;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import ru.yandex.test.Vacancy;
import ru.yandex.test.VacancyXmlFileParser;
import ru.yandex.test.impl.vacancyparser.VacancySaxParserHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

/**
 * Класс представляющий собранный Webharvest'ом файл
 * С помощью SAX-парсера создает список собранных вакансий.
 * 
 * @author OneHalf
 */
 public class VacancyXmlFileParserImpl implements VacancyXmlFileParser {
    
    private static final Logger log = Logger.getLogger(VacancyXmlFileParserImpl.class);
    
    private List<Vacancy> vacancies;
    private String query;
    private final VacancySaxParserHandler vacancyCreatorHandler = new VacancySaxParserHandler();
    private String sourceName;

    /**
     * Конструктор объекта с указанием файла, из которого нужно брать данные.
     * Названием источника будет имя файла
     * @param xmlFile Файл, созданный WebHarvest'ом
     * @throws FileNotFoundException if file not found
     */
    public VacancyXmlFileParserImpl(File xmlFile) throws FileNotFoundException {
        this(xmlFile.getName(), xmlFile);
    }

    /**
     * Конструктор объекта с указанием файла, из которого нужно брать данные 
     * и названия источника
     * @param sourceName Название источника
     * @param xmlFile Файл, созданный WebHarvest'ом
     * @throws FileNotFoundException if file not found
     */
    public VacancyXmlFileParserImpl(String sourceName, File xmlFile) throws FileNotFoundException {
        this.sourceName = sourceName;
        this.parse(new FileInputStream(xmlFile));
    }

    /**
     * Создание объекта по имени и строке, которая может быть адресом ресурса
     * или именем файла
     * @param name Имя источника
     * @param resourceName Имя ресурса
     * @throws FileNotFoundException if file not found
     */
    public VacancyXmlFileParserImpl(String name, String resourceName) throws FileNotFoundException {
        this.sourceName = name;
        this.parse(getClass().getResourceAsStream(resourceName));
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
            log.debug(String.format("begin parse xml-stream \"%s\" for vacancies", sourceName));

            SAXParserFactory SAXFactory = SAXParserFactory.newInstance();
            SAXParser saxParser = SAXFactory.newSAXParser();
            saxParser.parse(inputStream, vacancyCreatorHandler);
            
            vacancies = vacancyCreatorHandler.getVacancies();
            query = vacancyCreatorHandler.getQuery();

            log.debug("Окончание парсинга xml-потока \"" + sourceName + "\" с вакансиями");
        }
        catch(Exception e){
            throw new IllegalStateException(
                    String.format("error parse xml-stream '%s'", sourceName), e);
        }
        finally {
            IOUtils.closeQuietly(inputStream);
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
