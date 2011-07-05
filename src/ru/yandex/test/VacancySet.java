package ru.yandex.test;

import java.io.File;
import java.util.Set;

/**
 * Набор вакансий с одного сайта по одному запросу
 * 
 * @author OneHalf
 */
public interface VacancySet {
    
    /**
     * Распарсить xml-файл с результатами, собранными с сайта
     * @param file Файл с собранными данными
     */
    public void parse(File file);
    
    /**
     * Возвращает вакансии содержащиеся в объекте в виде множества
     * @return 
     */
    public Set<Vacancy> getVacansies();
}
