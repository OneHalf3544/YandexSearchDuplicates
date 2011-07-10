package ru.yandex.test.impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.webharvest.definition.DefinitionResolver;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import ru.yandex.test.Vacancy;
import ru.yandex.test.VacancySource;
import ru.yandex.test.webharvest.RecognizeSalaryPlugin;

/**
 *
 * @author OneHalf
 */
public class SiteParser implements VacancySource {
    private static final Logger LOGGER = Logger.getLogger(SiteParser.class.getName());
    private final String WORKING_DIRECTORY = "..";
    
    private String siteName;
    private String configResourceName;
    private String searchText;
    private int itemsCount;
    private List<Vacancy> vacancies;

    static {
        DefinitionResolver.registerPlugin(RecognizeSalaryPlugin.class);
    }
    
    public SiteParser() {
    }
    
    /**
     * Установка конфига для WebHarvest
     * @param configName Устанавливаемый файл конфигурации
     */
    public void setWebHarvestConfig(String configName) {
        this.configResourceName = configName;
    }

    /**
     * Установка названия сайта
     * @param siteName Название сайта
     */
    public void setSiteName(String siteName) {
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
    public List<Vacancy> getVacancies(boolean deleteOnExit) {
        VacancySource result = null;
        LOGGER.log(Level.INFO, "Сбор информации с сайта {0}", siteName);
        try {
            ScraperConfiguration config = new ScraperConfiguration(configResourceName);
            Scraper scraper = new Scraper(config, WORKING_DIRECTORY);
            
            final File tempFile = File.createTempFile("Vacancy", ".xml");
            scraper.addVariableToContext("search", searchText);
            scraper.addVariableToContext("itemsCount", itemsCount);
            scraper.addVariableToContext("outputFile", tempFile);
            
            scraper.execute();
            
            result = new VacancyXmlFileParser(siteName, new FileReader(tempFile));
            
            if (deleteOnExit)  {
                tempFile.deleteOnExit();
            }
        } 
        catch (IOException ex) {
            LOGGER.log(Level.WARNING, null, ex);
        }        
        LOGGER.log(Level.FINE, "Окончание сбора информации с сайта {0}", siteName);
        
        return result.getVacancies();
    }

    @Override
    public String toString() {
        return "SiteParser{" + "site: " + siteName + ", query: " + searchText + '}';
    }
    
}
