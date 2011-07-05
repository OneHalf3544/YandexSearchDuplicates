package ru.yandex.test;

import java.text.MessageFormat;

/**
 *
 * @author OneHalf
 */
public class Salary {
    private final Integer minimum;
    private final Integer maximum;

    public Salary() {
        this.minimum = null;
        this.maximum = null;
    }
    
    public Salary(Integer concrete) {
        this.minimum = concrete;
        this.maximum = concrete;
    }
    
    public Salary(Integer minimum, Integer maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public Integer getMinimum() {
        return minimum;
    }

    public Integer getMaximum() {
        return maximum;
    }
    
    public boolean permissible(Salary other) {
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
