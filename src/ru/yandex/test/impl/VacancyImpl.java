package ru.yandex.test.impl;

import java.text.MessageFormat;
import java.util.logging.Logger;

import ru.yandex.test.Salary;
import ru.yandex.test.Vacancy;
import ru.yandex.test.shingles.Shingle;

/**
 *
 * @author OneHalf
 */
public class VacancyImpl implements Vacancy {

    private final static Logger LOGGER = Logger.getLogger(VacancyImpl.class.getName());

    // Сумма коэфициентов перед слагаемыми должна быть равна единице
    private static final double CITY_NAME_INFLUENCE = 0.1;
    private static final double VACANCY_NAME_INFLUENCE = 0.1;
    private static final double COMPANY_NAME_INFLUENCE = 0.4;
    private static final double VACANCY_DESCRIPTION_INFLUENCE = 0.4;

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

    public VacancyImpl(
            String vacancyName, String vacancyUrl, 
            String companyName, String companyUrl, 
            String cityName, 
            Salary salary, 
            String description) {        
        
        this.vacancyName = vacancyName;
        this.vacancyUrl = vacancyUrl;
        
        this.companyName = companyName;
        this.companyUrl = companyUrl;
        
        this.cityName = cityName;
        this.salary = salary;
        this.description = description;
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

    /**
     * Уровень сходства текущей вакансии с указанной.
     * Алгоритм сравнения такой:
     * <p>Зарплата не влияет на процент сходства. Но если диапазоны зарплат
     * не пересекаются, то функция сразу возвращает 0.0</p>
     * <p>Далее по алгоритму шинглов сравниваются:
     * <ul>
     * <li> Название вакансии (вклад в результат - до 10%)
     * <li> Название компании-работодателя (вклад в результат - до 40%)</li>
     * <li> Регион, в котором предлагается работа (вклад в результат - до 10%)</li>
     * <li> Описание вакансии (вклад в результат - до 40%)</li>
     * </ul>
     * </p>
     *
     * @param o Вакансия, с которой производится сравнение
     * @return Уровень "похожести" вакансии. Находится в пределах от 0.0 до 1.0
     */
    @Override
    public double getLevelOfSimilarity(Vacancy o) {
        if (shingles == null) {
            updateShingles();
        }
        VacancyImpl other = (VacancyImpl) o;
        if (other.shingles == null) {
            other.updateShingles();
        }
        if (!this.salary.isPermissible(other.salary)) {
            /*
             * Эквивалентность зарплат. Не влияет на вероятность,
             * но если диапазоны не перекрываются, считаем, что вакансии разные
             */
            return 0.0;
        }

        double result = 0.0;

        // Равенство названий вакансии
        result += VACANCY_NAME_INFLUENCE * Shingle.correlation(
                this.shingles.vacancyNameShingle,
                other.shingles.vacancyNameShingle);

        // Город
        result += CITY_NAME_INFLUENCE * Shingle.correlation(
                this.shingles.cityShingles,
                other.shingles.cityShingles);

        // Компания
        result += COMPANY_NAME_INFLUENCE * Shingle.correlation(
                this.shingles.companyNameShingle,
                other.shingles.companyNameShingle);

        // Описание
        result += VACANCY_DESCRIPTION_INFLUENCE * Shingle.correlation(
                this.shingles.descriptionShingle,
                other.shingles.descriptionShingle);

        return result;
    }

    @Override
    public void setVacancyName(String vacancyName) {
        this.vacancyName = vacancyName;
    }


    @Override
    public void setVacancyUrl(String vacancyUrl) {
        this.vacancyUrl = vacancyUrl;
    }

    /** 
     * Обновление шинглов
     */
    private void updateShingles() {
        shingles = new VacancyShingles();
    }

    @Override
    public String toString() {
        return MessageFormat.format("Вакансия: {0} ({1}) url: {2}", vacancyName, companyName, vacancyUrl);
    }
    
    /**
     * Класс с набором шинглов
     */
    private class VacancyShingles {
        private final Shingle cityShingles;
        private final Shingle vacancyNameShingle;
        private final Shingle companyNameShingle;
        private final Shingle descriptionShingle;

        VacancyShingles() {
            cityShingles = new Shingle(cityName, 1);
            vacancyNameShingle = new Shingle(vacancyName, 1);
            companyNameShingle = new Shingle(companyName, 1);
            descriptionShingle = new Shingle(description, 6);
        }
    }
}
