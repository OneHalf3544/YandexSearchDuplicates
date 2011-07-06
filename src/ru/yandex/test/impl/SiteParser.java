package ru.yandex.test.impl;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.webharvest.definition.DefinitionResolver;
import org.webharvest.definition.ScraperConfiguration;
import org.webharvest.runtime.Scraper;
import ru.yandex.test.VacancySource;
import ru.yandex.test.VacancySet;
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

    static {
        DefinitionResolver.registerPlugin(RecognizeSalaryPlugin.class);
    }
    
    public SiteParser() {
    }
    
    public void setWebHarvestConfig(String configName) {
        this.configResourceName = configName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }


    @Override
    public String getSourceName() {
        return siteName;
    }

    @Override
    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    @Override
    public VacancySet getVacancySet() {
        return getVacancySet(false);
    }
    
    public VacancySet getVacancySet(boolean deleteOnExit) {
        VacancySet result = null;
        LOGGER.log(Level.INFO, "Сбор информации с сайта {0}", siteName);
        try {
            ScraperConfiguration config = new ScraperConfiguration(configResourceName);
            Scraper scraper = new Scraper(config, WORKING_DIRECTORY);
            
            final File tempFile = File.createTempFile("Vacancy", ".xml");
            scraper.addVariableToContext("search", searchText);
            scraper.addVariableToContext("outputFile", tempFile);
            
            scraper.execute();
            
            result = new VacancySetImpl();
            result.parse(new FileReader(tempFile));
            
            if (deleteOnExit)  {
                tempFile.delete();
            }
        } 
        catch (IOException ex) {
            LOGGER.log(Level.SEVERE, null, ex);
        }        
        LOGGER.log(Level.INFO, "Окончание сбора информации с сайта {0}", siteName);
        
        return result;
    }

    @Override
    public String toString() {
        return "SiteParser{" + "site: " + siteName + ", query: " + searchText + '}';
    }
    
}
