package ru.yandex.test.impl;

import ru.yandex.test.Salary;
import ru.yandex.test.Vacancy;
import ru.yandex.test.shingles.Shingle;

/**
 *
 * @author OneHalf
 */
public class VacancyImpl implements Vacancy {
    
    private String cityName;
    private String companyName;
    private String companyUrl;
    private String description;
    private String vacancyUrl;
    private String vacancyName;
    private Salary salary;

    private VacancyShingles shingles;

    public VacancyImpl() {
        
    }

    @Override
    public String getCity() {
        return cityName;
    }

    @Override
    public String getCompanyName() {
        return companyName;
    }

    @Override
    public String getCompanyUrl() {
        return companyUrl;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Salary getSalary() {
        return salary;
    }

    @Override
    public String getVacancyName() {
        return vacancyName;
    }

    @Override
    public String getVacancyUrl() {
        return vacancyUrl;
    }

    @Override
    public void setCity(String cityName) {
        this.cityName = cityName;
    }

    @Override
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    @Override
    public void setVacancyName(String vacancyName) {
        this.vacancyName = vacancyName;
    }

    @Override
    public void setVacancyUrl(String vacancyUrl) {
        this.vacancyUrl = vacancyUrl;
    }


    @Override
    public Double getLevelOfSimilarity(Vacancy o) {
        if (shingles == null) {
            updateShingles();
        }
        VacancyImpl other = (VacancyImpl) o;
        if (other.shingles == null) {
            other.updateShingles();
        }
        Double result = 0.0;
        
        // Сумма коэфициентов перед слагаемыми должна быть равно единице
        
        // Равенство названий вакансии
        result += 0.15 * Shingle.corellation(
                this.shingles.vacancyNameShingle, 
                other.shingles.vacancyNameShingle);
        
        // Город
        result += 0.15 * Shingle.corellation(
                this.shingles.cityShingles, 
                other.shingles.cityShingles);
        
        // Компания
        result += 0.3 * Shingle.corellation(
                this.shingles.companyNameShingle, 
                other.shingles.companyNameShingle);
        
        // Эквивалентность зарплат
        result += 0.1 * (this.salary.permissible(other.salary) ? 1 : 0);
        
        // Описание
        result += 0.3 * Shingle.corellation(
                this.shingles.descriptionShingle, 
                other.shingles.descriptionShingle);
        
        return result;
    }

    private void updateShingles() {
        shingles = new VacancyShingles();
    }
    
    private class VacancyShingles {
        private Shingle cityShingles;
        private Shingle vacancyNameShingle;
        private Shingle companyNameShingle;
        private Shingle descriptionShingle;

        VacancyShingles() {
            cityShingles = new Shingle(cityName, 1);
            vacancyNameShingle = new Shingle(vacancyName, 1);
            companyNameShingle = new Shingle(companyName, 1);
            descriptionShingle = new Shingle(description, 6);
        }
    }
}
