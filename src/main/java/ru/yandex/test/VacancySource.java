package ru.yandex.test;

import java.util.List;

/**
 * Источник набора вакансий
 * 
 * @author OneHalf
 */
public interface VacancySource {
    /**
     * Получение набора вакансий из текущего источника
     * @return Набор вакансий
     */
    public List<Vacancy> getVacancies();

    /**
     * Возвращает название текущего источника
     * @return Название источника
     */
    public String getSourceName();

    /**
     * Получение запроса, по которому была собрана информация
     * @return Запрос
     */
    public String getQuery();
}
