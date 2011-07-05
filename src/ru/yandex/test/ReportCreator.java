package ru.yandex.test;

import java.io.File;
import java.util.Set;

/**
 *
 * @author OneHalf
 */
public interface ReportCreator {
    public void setVacancies(Set<Vacancy> vacancies);
    public File getReport();
}
