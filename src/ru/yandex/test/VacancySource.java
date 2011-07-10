package ru.yandex.test;

import java.util.List;
import java.util.Set;

/**
 * Источник набора вакансий
 * 
 * @author OneHalf
 */
public interface VacancySource {
    /**
     * Получение набора вакансий
     * @return Набор вакансий
     */
    public List<Vacancy> getVacancies();

    public String getSourceName();
}
