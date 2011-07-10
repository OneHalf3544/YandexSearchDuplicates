package ru.yandex.test;

/**
 *
 * @author OneHalf
 */
public interface Vacancy {
    
    /**
     * Предлагаемая должность
     * @return Название должности (вакансии)
     */
    public String getVacancyName();
    
    /**
     * Установка предлагаемой должности
     * @param vacancyName Устанавливаемое название вакансии
     */
    public void setVacancyName(String vacancyName);
    
    /**
     * Ссылка на вакансию
     * @return url вакансии
     */
    public String getVacancyUrl();
    
    /**
     * Получение ссылки на вакансию
     * @param vacancyUrl устанавливаемый url
     */
    public void setVacancyUrl(String vacancyUrl);
    
    /**
     * Уровень зарплаты
     * @return Уровень зарплаты
     */
    public Salary getSalary();
    
    /**
     * Установка уровеня зарплаты
     * @param salary Уровень зарплаты
     */
    public void setSalary(Salary salary);
    
    /**
     * Получение названия вакансии
     * @return Название компании
     */
    public String getCompanyName();
    
    /**
     * Установка названия компании
     * @param companyName Название компании
     */
    public void setCompanyName(String companyName);
    
    /**
     * Описание вакансии
     * @return Описание вакансии
     */
    public String getDescription();
    
    /**
     * Установка описания вакансии
     * @param description Устанавливаемое описание
     */
    public void setDescription(String description);
    
    /**
     * Город, в котором предлагается работа
     * @return Название города
     */
    public String getCity();
    
    /**
     * Установка города, в котором предлагается работа
     * @param cityName Название города
     */
    public void setCity(String cityName);
    
    /**
     * Интернет-адрес работодателя
     * @return url компании-работодателя
     */
    public String getCompanyUrl();
    
    /**
     * Установка интернет-адреса работодателя
     * @param value Новый url
     */
    public void setCompanyUrl(String value);
    
    /**
     * Уровень сходства текущей вакансии с указанной.
     * @param other Вакансия, с которой производится сравнение
     * @return Уровень "похожести" вакансии. 
     *   Должен быть в пределах от 0.0 до 1.0
     */
    public Double getLevelOfSimilarity(Vacancy other);

}
