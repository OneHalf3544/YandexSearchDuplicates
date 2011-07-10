package ru.yandex.test.webharvest;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.webharvest.runtime.variables.Variable;
import ru.yandex.test.Salary;

/**
 * Тип данных представляющий зарплату в WebHarvest
 * 
 * @author OneHalf
 */
public class SalaryVariable extends Variable {
    
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
            Logger.getLogger(SalaryVariable.class.getName()).log(Level.SEVERE, null, ex);
            return null;
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
