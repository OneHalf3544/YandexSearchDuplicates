package ru.yandex.test;

import java.awt.event.ActionListener;
import java.util.Set;

/**
 *
 * @author OneHalf
 */
public interface SearchDialog {
    
    public String getSearchString();
    public int getItemsCount();

    public void addSearchListener(ActionListener listener);
    
    public void setSiteParsers(Set<VacancySource> siteParser);
    public Set<VacancySource> getVacancySources();
    
    public void addReportListener(ActionListener listener);

    public void setDuplicateVacancy(Set<SearchDoubles.Duplicate> duplicates);
}