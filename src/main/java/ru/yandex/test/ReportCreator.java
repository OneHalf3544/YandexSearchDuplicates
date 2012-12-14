package ru.yandex.test;

import java.io.File;
import java.util.Collection;

/**
 * Интерфейс для объекта, создающего отчеты по результатам поиска дублирующихся вакансий
 * 
 * @author OneHalf
 */
public interface ReportCreator {
    /**
     * Создание файла с отчетом.
     * @param duplicates Набор дублей, по которым нужно создать отчет
     * @return Файл, в котором содержится отчет
     */
    public File getReport(Collection<Duplicate> duplicates);
}
