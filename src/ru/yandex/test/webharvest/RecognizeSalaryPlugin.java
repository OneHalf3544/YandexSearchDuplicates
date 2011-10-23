package ru.yandex.test.webharvest;

import org.apache.log4j.Logger;
import org.webharvest.runtime.Scraper;
import org.webharvest.runtime.ScraperContext;
import org.webharvest.runtime.processors.WebHarvestPlugin;
import org.webharvest.runtime.variables.Variable;
import ru.yandex.test.Salary;

/**
 * Плагин для WebHarvest, который распознает строку с зарплатой
 * и создает переменную salary.
 *
 * <p>Применение:</p>
 * <p>Код: {@code &lt;salary&gt;От 40 000 до 50 000&lt;/salary&gt;}
 * создаст переменную salary, которую можно будет использовать дальше в конфиге WebHarvest</p>
 *
 * @author OneHalf
 */
public class RecognizeSalaryPlugin extends WebHarvestPlugin {
    private static final Logger log = Logger.getLogger(RecognizeSalaryPlugin.class);
    private static final String PLUGIN_TAG_NAME = "salaryparse";

    private static final String WITHOUT_DIGITS = "\\D*";
    private static final String FROM = "(?i)\\s*([Бб]олее|[Оо]т)\\s*(\\$|€)?\\d+([,.\\s]?\\d{3})*\\D*";
    private static final String TO = "(?i)\\s*([Мм]енее|[Дд]о|-|–)\\s*(\\$|€)?\\d+([,.\\s]?\\d{3})*\\D*";
    private static final String CONCRETE = "\\s*(\\$|€)?\\d+([,.\\s]?\\d{3})*\\D*";
    private static final String FROM_TO = "(?i)\\s*([Оо]т)?\\s*(\\$|€)?\\d+([,.\\s]?\\d{3})*\\D*\\s*([Дд]о|-|–)\\s*(\\$|€)?\\d+([,.\\s]?\\d{3})*\\D*";


    @Override
    public String getName() {
        return PLUGIN_TAG_NAME;
    }

    @Override
    public Variable executePlugin(Scraper scrpr, ScraperContext sc) {
        String strSalary = executeBody(scrpr, sc).toString();
        
        Salary salary;
        try {
            salary = recognizeSalary(strSalary);
        }
        catch (Exception e) {
            log.warn(String.format("Строка зарплаты не распознана: '%s'", strSalary));
            salary = new Salary();
        }
        
        final SalaryVariable salaryVariable = new SalaryVariable(salary);
        sc.setVar("salary", salaryVariable);
        return salaryVariable;
    }

    /**
     * Парсер строки зарплаты. Создает объект Salary на основе строки типа 
     * "От 40000", "З/п не указана" и т.д.
     * @param strSalary Строка для распознавания
     * @return Объект зарплаты, созданный на основе строки
     */
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
        if (strSalary.matches(CONCRETE)) {
            int concrete = Integer.parseInt(strSalary.replaceAll("\\D", ""));
            return new Salary(concrete, concrete);
        }
        if (strSalary.matches(FROM_TO)) {
            String[] twoString = strSalary.split("(?i)[Дд]о|-|–", 2);
            
            Integer minimum = Integer.valueOf(twoString[0].replaceAll("\\D", ""));
            Integer maximum = Integer.valueOf(twoString[1].replaceAll("\\D", ""));
            
            return new Salary(minimum, maximum);
        }
        throw new IllegalArgumentException("Не удается разобрать строку зарплаты");
    }
    
}
