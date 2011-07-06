package ru.yandex.test.webharvest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import ru.yandex.test.Salary;

/**
 *
 * @author OneHalf
 */
public class RecognizeSalaryPluginTest {
    
    public RecognizeSalaryPluginTest() {
    }

    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getName method, of class RecognizeSalaryPlugin.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");

        RecognizeSalaryPlugin instance = new RecognizeSalaryPlugin();
        String expResult = "salaryparse";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    @Test
    public void testOneSideLimitSalary() {
        System.out.println("OneSideLimitSalary");
        
        Salary expected = new Salary(1000, null);
        Salary result = RecognizeSalaryPlugin.recognizeSalary("От 1000");
        assertEquals(expected, result);
        
        expected = new Salary(null, 5000);
        result = RecognizeSalaryPlugin.recognizeSalary("до $5000");
        assertEquals(expected, result);
        
        expected = new Salary(null, 5000);
        result = RecognizeSalaryPlugin.recognizeSalary("менее 5000");
        assertEquals(expected, result);
        
        expected = new Salary(8000, null);
        result = RecognizeSalaryPlugin.recognizeSalary("Более 8000\n рублей");
        assertEquals(expected, result);
        
        expected = new Salary(null, 1000);
        result = RecognizeSalaryPlugin.recognizeSalary("до 1000");
        assertEquals(expected, result);
    }
    
    /**
     * Test of recognizeSalary method, of class RecognizeSalaryPlugin.
     */
    @Test
    public void testTwoSideLiminSalary() {
        System.out.println("TwoSideLiminSalary");
        
        Salary expected = new Salary(1000, 2000);
        Salary result = RecognizeSalaryPlugin.recognizeSalary("От\n$1000\nдо\n$2000");
        assertEquals(expected, result);
                
        expected = new Salary(4000, 6000);
        result = RecognizeSalaryPlugin.recognizeSalary("4000   -  6000");
        assertEquals(expected, result);
                
        expected = new Salary(4000, 6000);
        result = RecognizeSalaryPlugin.recognizeSalary("€4000-€6000");
        assertEquals(expected, result);
                
        expected = new Salary(60000, 75000);
        result = RecognizeSalaryPlugin.recognizeSalary("60 000 – 75 000 руб. в месяц");
        assertEquals(expected, result);

        expected = new Salary(1320000, 1400000);
        result = RecognizeSalaryPlugin.recognizeSalary("1 320 000 – 1 400 000 руб. в месяц");
        assertEquals(expected, result);
    }
    
    @Test
    public void testUnlimitSalary() {
        System.out.println("UnlimitSalary");
        
        Salary expected = new Salary(null, null);
        Salary result = RecognizeSalaryPlugin.recognizeSalary("З/п не указана");
        assertEquals(expected, result);
        
        expected = new Salary(null, null);
        result = RecognizeSalaryPlugin.recognizeSalary("Х.З. скока");
        assertEquals(expected, result);
    }
    
    @Test
    public void testConcreteSalary() {
        System.out.println("ConcreteSalary");
        
        Salary expected = new Salary(50000, 50000);
        Salary result = RecognizeSalaryPlugin.recognizeSalary("50 000 руб/мес.");
        assertEquals(expected, result);
        
        expected = new Salary(70000, 70000);
        result = RecognizeSalaryPlugin.recognizeSalary("70000 рублей");
        assertEquals(expected, result);
        
        expected = new Salary(2000, 2000);
        result = RecognizeSalaryPlugin.recognizeSalary("$2000");
        assertEquals(expected, result);
        
        expected = new Salary(4000, 4000);
        result = RecognizeSalaryPlugin.recognizeSalary("€4000");
        assertEquals(expected, result);
    }
    
    @Test
    public void testNumericSeparator() {
        System.out.println("NumericSeparator");
        
        Salary expected = new Salary(1000, 2000);
        Salary result = RecognizeSalaryPlugin.recognizeSalary("От 1 000 до 2 000");
        assertEquals(expected, result);
        
        expected = new Salary(1000, 2000);
        result = RecognizeSalaryPlugin.recognizeSalary("От 1.000 до 2.000");
        assertEquals(expected, result);
        
        expected = new Salary(3500, 5500);
        result = RecognizeSalaryPlugin.recognizeSalary("От €3 500 до €5 500");
        assertEquals(expected, result);
        
        expected = new Salary(1000, 2000);
        result = RecognizeSalaryPlugin.recognizeSalary("От 1,000 до 2,000");
        assertEquals(expected, result);
    }
}
