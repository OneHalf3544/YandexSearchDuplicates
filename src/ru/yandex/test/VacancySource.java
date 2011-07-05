package ru.yandex.test;

/**
 * Источник набора вакансий
 * 
 * @author OneHalf
 */
public interface VacancySource {
    
    /**
     * Установка запроса, которому должны соответствовать вакансии
     * @param searchText 
     */
    public void setSearchText(String searchText);

    /**
     * Название источника вакансий. Например, имя файла или сайта
     * @return Название источника вакансий
     */
    public String getSourceName();
    
    /**
     * Получение набора вакансий
     * @return Набор вакансий
     */
    public VacancySet getVacancySet();
}
