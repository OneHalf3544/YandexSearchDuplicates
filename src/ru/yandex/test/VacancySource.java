package ru.yandex.test;

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
    public Set<Vacancy> getVacancies();

    public String getSourceName();
}
