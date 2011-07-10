package ru.yandex.test;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author OneHalf
 */
public class SalaryTest {
    private Salary salary;
    
    public SalaryTest() {
    }

    @Before
    public void setUp() {
        salary = new Salary();
    }
    
    @Test
    public void testConstructor0() {
        System.out.println("constructor()");
        Salary expected = new Salary();    
        assertEquals(expected, new Salary());
    }

    @Test
    public void testConstructor1() {
        System.out.println("constructor(int)");
        final Salary instance = new Salary(4000);
        assertEquals(new Integer(4000), instance.getMinimum());
        assertEquals(new Integer(4000), instance.getMaximum());
    }
    
    @Test
    public void testConstructor2() {
        System.out.println("constructor(int, int)");
        final Salary instance = new Salary(1000, 5000);
        assertEquals(new Integer(1000), instance.getMinimum());
        assertEquals(new Integer(5000), instance.getMaximum());
    }
    
    /**
     * Test of permissible method, of class Salary.
     */
    @Test
    public void testPermissible() {
        System.out.println("permissible");
        
        Salary instance1 = new Salary(null, 10000);
        Salary instance2 = new Salary(2000, 10000);
        assertTrue(instance1.permissible(instance2));
        
        instance1 = new Salary(3000, 9000);
        instance2 = new Salary(2000, 10000);
        assertTrue(instance1.permissible(instance2));
        
        instance1 = new Salary(null, null);
        instance2 = new Salary(2000, 10000);
        assertTrue(instance1.permissible(instance2));
        
        instance1 = new Salary(null, 5000);
        instance2 = new Salary(null, 10000);
        assertTrue(instance1.permissible(instance2));
        
        instance1 = new Salary(5000, null);
        instance2 = new Salary(10000, null);
        assertTrue(instance1.permissible(instance2));
        
        instance1 = new Salary(1000, 9000);
        instance2 = new Salary(10000, 20000);
        assertFalse(instance1.permissible(instance2));
        
        instance1 = new Salary(null, 9000);
        instance2 = new Salary(10000, null);
        assertFalse(instance1.permissible(instance2));
        
        instance1 = new Salary(9000, null);
        instance2 = new Salary(null, 8000);
        assertFalse(instance1.permissible(instance2));
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
