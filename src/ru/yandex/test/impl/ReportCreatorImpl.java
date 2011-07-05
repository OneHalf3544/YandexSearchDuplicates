package ru.yandex.test.impl;

import java.io.File;
import java.util.Set;
import ru.yandex.test.ReportCreator;
import ru.yandex.test.Vacancy;

/**
 *
 * @author OneHalf
 */
public class ReportCreatorImpl implements ReportCreator {

    @Override
    public void setVacancies(Set<Vacancy> vacancies) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public File getReport() {
        return null;
    }
}
