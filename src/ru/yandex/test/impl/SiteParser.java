package ru.yandex.test.impl;

import org.webharvest.definition.DefinitionResolver;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import ru.yandex.test.Vacancy;
import ru.yandex.test.VacancySource;
import ru.yandex.test.webharvest.RecognizeSalaryPlugin;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author OneHalf
 */
public class SiteParser implements VacancySource {
    private static final Logger LOGGER = Logger.getLogger(SiteParser.class.getName());
    private static final String WORKING_DIRECTORY = "..";

    private final String siteName;
    private final String configResourceName;

    private String searchText;
    private int itemsCount;
    private List<Vacancy> vacancies;

    static {
        // Регистрируем плагин
        DefinitionResolver.registerPlugin(RecognizeSalaryPlugin.class);
    }

    /**
     * Создание парсера по имени сайта и файла конфигурации WebHarvest
     * @param siteName Название сайта
     * @param configResourceName Устанавливаемый файл конфигурации
     */
    public SiteParser(String siteName, String configResourceName) {
        this.configResourceName = configResourceName;
        this.siteName = siteName;
    }

    @Override
    public String getSourceName() {
        return siteName;
    }

    /**
     * Установка числа вакансий, котоые нужно искать
     * @param itemsCount Максимальное число вакансий при поиске
     */
    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
        vacancies = null;
    }

    /**
     * Установка текста, по которому будут искаться вакансии
     * @param searchText Текст запроса
     */
    public void setSearchText(String searchText) {
        this.searchText = searchText;
        vacancies = null;
    }

    @Override
    public List<Vacancy> getVacancies() {
        if (vacancies == null) {
            vacancies = getVacancies(true);
        }
        return vacancies;
    }
    
    /**
     * Сбор вакансий с сайта
     * @param deleteOnExit Удалять ли файл после закрытия программы
     * @return Список вакансий
     */
    List<Vacancy> getVacancies(boolean deleteOnExit) {
        VacancySource result = null;
        LOGGER.log(Level.INFO, "Сбор информации с сайта {0}", siteName);
        try {
            ScraperConfiguration config;
            // Пытаемся получить URL внутреннего ресурса по имени
            URL internalConfig = SiteParser.class.getResource(configResourceName);
            if (internalConfig != null) {
                config = new ScraperConfiguration(internalConfig);
            }
            else {
                // Если такого ресурса нет, попытаемся использовать имя ресурса как имя файла
                config = new ScraperConfiguration(configResourceName);
            }
            
            Scraper scraper = new Scraper(config, WORKING_DIRECTORY);
            
            File tempFile = File.createTempFile("Vacancy", ".xml");
            scraper.addVariableToContext("search", searchText);
            scraper.addVariableToContext("itemsCount", itemsCount);
            scraper.addVariableToContext("outputFile", tempFile);
            
            scraper.execute();
            LOGGER.log(Level.FINE, "Окончание сбора информации с сайта {0}", siteName);

            result = new VacancyXmlFileParser(siteName, tempFile);

            if (deleteOnExit)  {
                tempFile.deleteOnExit();
            }
            
            return result.getVacancies();
        }
        catch (IOException ex) {
            LOGGER.log(Level.WARNING, null, ex);
            throw new SiteParseException(String.format("Ошибка парсинга сайта \"%s\"", siteName), ex);
        }
    }

    @Override
    public String getQuery() {
        return searchText;
    }

    @Override
    public String toString() {
        return String.format("SiteParser{site: %s, query: %s}", siteName, searchText);
    }

    private static class SiteParseException extends RuntimeException {
        
        public SiteParseException(Throwable cause) {
            super(cause);
        }

        public SiteParseException(String message, Throwable cause) {
            super(message, cause);
        }

        public SiteParseException(String message) {
            super(message);
        }
    }
}
