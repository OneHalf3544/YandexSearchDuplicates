package ru.yandex.test.webharvest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.webharvest.runtime.variables.Variable;
import ru.yandex.test.Salary;

/**
 * Тип данных представляющий зарплату в WebHarvest
 * 
 * @author OneHalf
 */
// Все методы используются как объявлено в файлах конфигурации webharvest
@SuppressWarnings({"UnusedDeclaration"})
public class SalaryVariable extends Variable {

    private static final Logger log = Logger.getLogger(SalaryVariable.class);

    private Salary salary;

    public SalaryVariable(Salary salary) {
        this.salary = salary;
    }

    @Override
    public byte[] toBinary() {
        return toString().getBytes();
    }

    @Override
    public byte[] toBinary(String charset) {
        try {
            return toString().getBytes(charset);
        } catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(charset + " not supported", ex);
        }
    }

    @Override
    public String toString() {
        return salary.toString();
    }

    @Override
    public String toString(String charset) {
        return salary.toString();
    }

    @Override
    public List<?> toList() {
        List<Integer> result = new ArrayList<Integer>(2);
        result.add(salary.getMinimum());
        result.add(salary.getMaximum());
        
        return result;
    }

    @Override
    public boolean isEmpty() {
        return salary.getMinimum() == null && salary.getMaximum() == null;
    }

    @Override
    public Salary getWrappedObject() {
        return salary;
    }

    /**
     * Получение минимальной границы зарплаты
     * @return Минимальная граница зарплаты
     */
    public String getMinimum() {
        if (salary.getMinimum() == null) {
            return "";
        }
        return salary.getMinimum().toString();
    }

    /**
     * Получение максимальной границы зарплаты
     * @return Максимальная граница зарплаты
     */
    public String getMaximum() {
        if (salary.getMaximum() == null) {
            return "";
        }
        return salary.getMaximum().toString();
    }
}
