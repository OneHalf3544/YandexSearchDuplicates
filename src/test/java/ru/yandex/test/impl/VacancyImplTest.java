package ru.yandex.test.impl;

import org.junit.Test;
import ru.yandex.test.Salary;
import ru.yandex.test.Vacancy;

import static org.junit.Assert.assertEquals;

/**
 *
 * @author OneHalf
 */
public class VacancyImplTest {
    
    private static final Vacancy VACANCY_1 = new VacancyImpl(
            "Java Developer", "url1", 
            "Яндекс", "url2", 
            "Санкт-Петербург", new Salary(), 
            "Яндекс.Услуги — это сервис, с помощью которого сотни тысяч человек каждый месяц\n"
            + " подбирают банковские продукты: вклады, потребительские и автокредиты, ипотеку."
            + "Мы ищем специалиста, который будет помогать развивать существующие направления\n"
            + " сервиса и создавать новые. Он должен будет быстро разобраться в инфраструктуре \n"
            + "Яндекс.Услуг и научиться эффективно модифицировать сервис, сохраняя высокую надёжность."
            + "\n"
            + "Требования:\n"
            + "* хорошее знание Java;\n"
            + "*знание классических алгоритмов и структур данных;\n"
            + "* опыт использования Oracle или других реляционных СУБД;\n"
            + "* ответственность и аккуратность;\n"
            + "* умение тестировать собственный код и работать с чужим.\n"
            + "Желательно:\n"
            + "* опыт разработки под Unix/Linux;\n"
            + "* опыт разработки многопоточных приложений;\n"
            + "* опыт оптимизации приложений, работающих с БД;\n"
            + "* опыт создания распределённых систем с большой нагрузкой;\n"
            + "* опыт использования XML/XSLT;\n"
            + "* опыт разработки веб-интерфейсов.\n"
            + "Условия:\n"
            + "* работа в офисе Яндекса в Санкт-Петербурге полный рабочий день.");
    
    private static final Vacancy VACANCY_2 = new VacancyImpl(
            "Senior Java Developer", "url_1", 
            "Яндекс.Деньги", "url_2", 
            "Санкт-Петербург", new Salary(40000, 70000), 
            "Яндекс.Деньги — удобный и безопасный способ платить за телефон, "
            + "интернет и многие другие товары и услуги без комиссии и без очередей."
            + "\n"
            + "Мы ищем специалиста, который будет помогать развивать существующие направления\n"
            + " сервиса и создавать новые. Он должен будет быстро разобраться в инфраструктуре \n"
            + "Яндекс.Денег и научиться эффективно модифицировать сервис, сохраняя высокую надёжность."
            + "\n"
            + "Требования:\n"
            + "* хорошее знание Java;\n"
            + "*знание классических алгоритмов и структур данных;\n"
            + "* опыт использования Oracle или других реляционных СУБД;\n"
            + "* ответственность и аккуратность;\n"
            + "* умение тестировать собственный код и работать с чужим.\n"
            + "Желательно:\n"
            + "* опыт разработки под Unix/Linux;\n"
            + "* опыт разработки многопоточных приложений;\n"
            + "* опыт оптимизации приложений, работающих с БД;\n"
            + "* опыт создания распределённых систем с большой нагрузкой;\n"
            + "* опыт использования XML/XSLT;\n"
            + "* опыт разработки веб-интерфейсов.\n"
            + "Условия:\n"
            + "* работа в офисе Яндекса в Санкт-Петербурге полный рабочий день.");
    
    private static final Vacancy VACANCY_3 = new VacancyImpl(
            "vacancyName", "vacancyUrl",
            "companyName", "companyUrl",
            "cityName", 
            new Salary(100000, null),
            "Платим очень-очень много денег. \n"
            + "Разрабатываем программу %programName%");
    
    public VacancyImplTest() {
    }

    /**
     * Test of getCity/setCity method, of class VacancyImpl.
     */
    @Test
    public void testGetSetCity() {
        System.out.println("GetSetCity");
        VacancyImpl instance = new VacancyImpl();
        String expResult = "Москва";
        
        instance.setCity(expResult);
        
        String result = instance.getCity();
        assertEquals(expResult, result);
    }

    /**
     * Test of get-/setCompanyName method, of class VacancyImpl.
     */
    @Test
    public void testGetSetCompanyName() {
        System.out.println("GetSetCompanyName");
        VacancyImpl instance = new VacancyImpl();
        String expResult = "Яндекс.Деньги";
        instance.setCompanyName(expResult);
        
        String result = instance.getCompanyName();
        assertEquals(expResult, result);
    }

    /**
     * Test of get-/setCompanyUrl method, of class VacancyImpl.
     */
    @Test
    public void testGetSetCompanyUrl() {
        System.out.println("GetSetCompanyUrl");
        VacancyImpl instance = new VacancyImpl();
        String expResult = "company.yandex.ru";
        
        instance.setCompanyUrl(expResult);
        
        String result = instance.getCompanyUrl();
        assertEquals(expResult, result);
    }

    /**
     * Test of get-/setDescription method, of class VacancyImpl.
     */
    @Test
    public void testGetSetDescription() {
        System.out.println("GetSetDescription");
        VacancyImpl instance = new VacancyImpl();
        String expResult = "Очень крутая работа :)";
        
        instance.setDescription(expResult);
        
        String result = instance.getDescription();
        assertEquals(expResult, result);
    }

    /**
     * Test of get-/setSalary method, of class VacancyImpl.
     */
    @Test
    public void testGetSetSalary() {
        System.out.println("getSetSalary");
        VacancyImpl instance = new VacancyImpl();
        Salary expResult = new Salary(1000, 3000);
        
        instance.setSalary(expResult);
        Salary result = instance.getSalary();
        assertEquals(expResult, result);
    }

    /**
     * Test of get-/setVacancyName method, of class VacancyImpl.
     */
    @Test
    public void testGetSetVacancyName() {
        System.out.println("getSetVacancyName");
        VacancyImpl instance = new VacancyImpl();
        String expResult = "Very Senior Java Developer";
        
        instance.setVacancyName(expResult);
        String result = instance.getVacancyName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getVacancyUrl method, of class VacancyImpl.
     */
    @Test
    public void testGetSetVacancyUrl() {
        System.out.println("getVacancyUrl");
        VacancyImpl instance = new VacancyImpl();
        String expResult = "urlexample.com";
        
        instance.setVacancyUrl(expResult);
        
        String result = instance.getVacancyUrl();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLevelOfSimilarity method, of class VacancyImpl.
     */
    @Test
    public void testGetLevelOfSimilarity() {
        System.out.println("getLevelOfSimilarity");

        Double expResult;
        Double result;
        double delta;
        
        expResult = 1.0; delta = 0.01;
        result = VACANCY_1.getLevelOfSimilarity(VACANCY_1);
        assertEquals(expResult, result, delta);

        expResult = 1.0; delta = 0.01;
        result = VACANCY_3.getLevelOfSimilarity(VACANCY_3);
        assertEquals(expResult, result, delta);

        expResult = 0.0; delta = 0.01;
        result = VACANCY_1.getLevelOfSimilarity(VACANCY_3);
        assertEquals(expResult, result, delta);

        //------------
        expResult = 0.75; delta = 0.10;
        result = VACANCY_1.getLevelOfSimilarity(VACANCY_2);
        assertEquals(expResult, result, delta);

        double otherResult = VACANCY_2.getLevelOfSimilarity(VACANCY_1);
        assertEquals(result, otherResult, 0.001);
        //------------
    }

    /**
     * Test of toString method, of class VacancyImpl.
     */
    @Test
    public void testToString() {
        System.out.println("toString");

        String expResult = "Вакансия: Java Developer (Яндекс) url: url1";
        String result = VACANCY_1.toString();

        assertEquals(expResult, result);
    }
}
