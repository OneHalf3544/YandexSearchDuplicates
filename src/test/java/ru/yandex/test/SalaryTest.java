package ru.yandex.test;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Тест класса заралаты
 *
 * @author OneHalf
 */
public class SalaryTest {

    @Test
    public void testConstructor0() {
        System.out.println("constructor()");
        Salary expected = new Salary();    
        assertEquals(expected, new Salary());
    }

    @Test
    public void testConstructor1() {
        System.out.println("constructor(int)");
        Salary instance = new Salary(4000);
        assertEquals(Integer.valueOf(4000), instance.getMinimum());
        assertEquals(Integer.valueOf(4000), instance.getMaximum());
    }
    
    @Test
    public void testConstructor2() {
        System.out.println("constructor(int, int)");
        final Salary instance = new Salary(1000, 5000);
        assertEquals("Нижняя граница не совпадает с заданной", Integer.valueOf(1000), instance.getMinimum());
        assertEquals("Верхняя граница не совпадает с заданной", Integer.valueOf(5000), instance.getMaximum());
    }
    
    /**
     * Test of isPermissible method, of class Salary.
     */
    @Test
    public void testPermissible() {
        System.out.println("isPermissible");
        
        Salary instance1 = new Salary(null, 10000);
        Salary instance2 = new Salary(2000, 10000);
        assertTrue(instance1.isPermissible(instance2));
        
        instance1 = new Salary(3000, 9000);
        instance2 = new Salary(2000, 10000);
        assertTrue(instance1.isPermissible(instance2));
        
        instance1 = new Salary(null, null);
        instance2 = new Salary(2000, 10000);
        assertTrue(instance1.isPermissible(instance2));
        
        instance1 = new Salary(null, 5000);
        instance2 = new Salary(null, 10000);
        assertTrue(instance1.isPermissible(instance2));
        
        instance1 = new Salary(5000, null);
        instance2 = new Salary(10000, null);
        assertTrue(instance1.isPermissible(instance2));
        
        instance1 = new Salary(1000, 9000);
        instance2 = new Salary(10000, 20000);
        assertFalse(instance1.isPermissible(instance2));
        
        instance1 = new Salary(null, 9000);
        instance2 = new Salary(10000, null);
        assertFalse(instance1.isPermissible(instance2));
        
        instance1 = new Salary(9000, null);
        instance2 = new Salary(null, 8000);
        assertFalse(instance1.isPermissible(instance2));
    }
    
    @Test
    public void testToString() {
        System.out.println("toString");
        String expected;
        Salary salary;
        
        expected = "50\u00a0000";
        salary = new Salary(50000);
        assertEquals(expected, salary.toString());
        
        expected = "До 70\u00a0000";
        salary = new Salary(null, 70000);
        assertEquals(expected, salary.toString());
        
        expected = "От 40\u00a0000";
        salary = new Salary(40000, null);
        assertEquals(expected, salary.toString());
        
        expected = "От 50\u00a0000 до 80\u00a0000";
        salary = new Salary(50000, 80000);
        assertEquals(expected, salary.toString());
        
        expected = "Зарплата не указана";
        salary = new Salary();
        assertEquals(expected, salary.toString());
    }

}
