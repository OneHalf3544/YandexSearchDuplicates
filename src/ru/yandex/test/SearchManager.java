package ru.yandex.test;

import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: OneHalf
 * Date: 22.07.11
 * Time: 16:50
 */
public interface SearchManager {

    /**
     * @return Порог эквивалентости определяющий уровень
     * "похожести" ввакансий при котором они считаются равными и попадают в отчет
     */
    public double getThresholdOfEquivalence();

    /**
     * @param thresholdOfEquivalence Порог эквивалентости определяющий уровень
     * "похожести" ввакансий при котором они считаются равными и попадают в отчет
     */
    public void setThresholdOfEquivalence(double thresholdOfEquivalence);

    /**
     * Установка строки запроса для поиска по сайтам
     * @param query Строка запроса
     */
    public void setQueryString(String query);

    /**
     * Установка строки запроса для поиска по сайтам
     * @param itemsCount Число вакансий, которые нужно взять из каждого siteParser
     */
    public void setItemsCount(int itemsCount);

    /**
     * Установка сайтов-источников. Добавляет сайты источники в программу и ChekBox'ы в диалог
     * @param siteParsers Сайты-источники
     */
    public void setSiteParsers(Set<SiteParser> siteParsers);

    /**
     * Установка файлов-источников. Добавляет файлы-источники в программу и ChekBox'ы в диалог
     * @param files Файлы-источники
     */
    public void setPreparsedXmlFiles(Set<VacancyXmlFileParser> files);

    /**
     * Инициализация источников вакансий в соответствии с состояниями CheckBox'ов
     */
    public void initializeSources() throws InterruptedException;

    /**
     * Поиск дублирующихся вакансий
     * @return Дубликаты
     */
    public Set<Duplicate> searchDuplicates();

    /**
     * Определяет, искать ли дубликаты среди вакансий одного источника
     * @param searchInSelf true, если требуется искать дубликаты и впределах одного источника
     */
    public void setSearchInSelf(boolean searchInSelf);
}
