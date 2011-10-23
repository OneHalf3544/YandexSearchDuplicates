package ru.yandex.test;

/**
 * Date: 22.07.11
 * Time: 22:00
 *
 * @author OneHalf
 */
public interface SiteParser extends VacancySource {

    /**
     * Установка числа вакансий, котоые нужно искать
     * @param itemsCount Максимальное число вакансий при поиске
     */
    public void setItemsCount(int itemsCount);

    /**
     * Установка текста, по которому будут искаться вакансии
     * @param searchText Текст запроса
     */
    public void setSearchText(String searchText);
}
