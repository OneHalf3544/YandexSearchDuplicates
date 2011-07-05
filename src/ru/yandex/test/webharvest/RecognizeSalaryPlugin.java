package ru.yandex.test.webharvest;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.ScraperContext;
import org.webharvest.runtime.processors.WebHarvestPlugin;
import org.webharvest.runtime.variables.Variable;
import ru.yandex.test.Salary;

/**
 *
 * @author OneHalf
 */
public class RecognizeSalaryPlugin extends WebHarvestPlugin {
    private static final String WITHOUT_DIGITS = "\\D*";
    private static final String FROM = "(?i)\\s*([Бб]олее|[Оо]т)\\s*(\\$|€)?\\d+([,.\\s]?\\d{3})*\\D*";
    private static final String TO = "(?i)\\s*([Мм]енее|[Дд]о|-|–)\\s*(\\$|€)?\\d+([,.\\s]?\\d{3})*\\D*";
    private static final String CONRETE = "\\s*(\\$|€)?\\d+([,.\\s]?\\d{3})*\\D*";
    private static final String FROM_TO = "(?i)\\s*([Оо]т)?\\s*(\\$|€)?\\d+([,.\\s]?\\d{3})*\\D*\\s*([Дд]о|-|–)\\s*(\\$|€)?\\d+([,.\\s]?\\d{3})*\\D*";
    
    private final static Logger LOGGER = Logger.getLogger(RecognizeSalaryPlugin.class.getName());

    @Override
    public String getName() {
        return "salaryparse";
    }

    @Override
    public Variable executePlugin(Scraper scrpr, ScraperContext sc) {
        String strSalary = executeBody(scrpr, sc).toString();
        
        Salary salary = null;
        try {
            salary = recognizeSalary(strSalary);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Строка зарплаты не распознана: \"{0}\"", strSalary);
            salary = new Salary();
        }
        
        final SalaryVariable salaryVariable = new SalaryVariable(salary);
        sc.setVar("salary", salaryVariable);
        return salaryVariable;
    }

    static Salary recognizeSalary(String strSalary) {
        if (strSalary.matches(WITHOUT_DIGITS)) { // Нет 
            return new Salary(null, null);
        }
        if (strSalary.matches(FROM)) {
            int minimum = Integer.parseInt(strSalary.replaceAll("\\D", ""));
            return new Salary(minimum, null);
        }
        if (strSalary.matches(TO)) {
            int maximum = Integer.parseInt(strSalary.replaceAll("\\D", ""));
            return new Salary(null, maximum);
        }
        if (strSalary.matches(CONRETE)) {
            int conrcete = Integer.parseInt(strSalary.replaceAll("\\D", ""));
            return new Salary(conrcete, conrcete);
        }
        if (strSalary.matches(FROM_TO)) {
            String[] twoString = strSalary.split("(?i)[Дд]о|-|–", 2);
            
            int minimum = Integer.parseInt(twoString[0].replaceAll("\\D", ""));
            int maximum = Integer.parseInt(twoString[1].replaceAll("\\D", ""));
            
            return new Salary(minimum, maximum);
        }
        throw new IllegalArgumentException("Не удается разобрать строку зарплаты");
    }
    
}
