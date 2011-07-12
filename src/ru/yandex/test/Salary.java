package ru.yandex.test;

import java.text.MessageFormat;

/**
 * Класс, представляющий зарплату. Определяет возможные границы.
 * Объекты являются неизменяемыми и границы должны указываться в конструкторе
 * 
 * @author OneHalf
 */
public class Salary {
    private final Integer minimum;
    private final Integer maximum;

    /**
     * Создание объекта для случая когда зарплата не указана
     */
    public Salary() {
        this(null);
    }
    
    /**
     * Создание объекта для случая когда в вакансии указана конкретная зарплата
     * а не диапазон
     * @param concrete Уровень зарплаты
     */
    public Salary(Integer concrete) {
        this(concrete, concrete);
    }
    
    /**
     * Создание объекта для случаев когда указаны границы зарплаты.
     * Если вместо числа передается null, то считается, что с этой стороны 
     * ограничений нет. Т.е. <code>new Salary(50000, null)</code> означает "От 50 000"
     * @param minimum Минимальная граница
     * @param maximum Максимальная граница
     */
    public Salary(Integer minimum, Integer maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    /**
     * Получить минимальную границу
     * @return Минимальная граница
     */
    public Integer getMinimum() {
        return minimum;
    }

    /**
     * Получить максимальную границу
     * @return Максимальная граница
     */
    public Integer getMaximum() {
        return maximum;
    }
    
    /**
     * Определение перекрываемости диапазонов зарплат
     * @param other Другой объект, с которым производится сравнение
     * @return true, если диапазоны зарплат перекрываются
     */
   public boolean isPermissible(Salary other) {
        final boolean part1
                = this.minimum == null 
                || other.maximum == null 
                || this.minimum < other.maximum;
        
        final boolean part2 
                = this.maximum == null
                || other.minimum == null
                || this.maximum > other.minimum;
        
        return part1 && part2;
    }

    @Override
    public String toString() {        
        if (minimum != null && minimum.equals(maximum)) {
            return MessageFormat.format("{0}", minimum);
        }            
        if (minimum != null && maximum != null) {
            return MessageFormat.format("От {0} до {1}", minimum, maximum);
        }
        else if (minimum == null && maximum == null) {
            return "Зарплата не указана";
        }
        else if (maximum != null) {
            return MessageFormat.format("До {0}", maximum);
        }
        else {
            return MessageFormat.format("От {0}", minimum);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Salary other = (Salary) obj;
        if (this.minimum != other.minimum && (this.minimum == null || !this.minimum.equals(other.minimum))) {
            return false;
        }
        if (this.maximum != other.maximum && (this.maximum == null || !this.maximum.equals(other.maximum))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + (this.minimum != null ? this.minimum.hashCode() : 0);
        hash = 79 * hash + (this.maximum != null ? this.maximum.hashCode() : 0);
        return hash;
    }
}
