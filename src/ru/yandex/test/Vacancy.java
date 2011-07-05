package ru.yandex.test;

/**
 *
 * @author OneHalf
 */
public interface Vacancy {
    
    /**
     * Предлагаемая должность
     * @return Название должности
     */
    public String getVacancyName();
    
    /**
     * Установка предлагаемой должности
     * @param vacancyName 
     */
    public void setVacancyName(String vacancyName);
    
    /**
     * Ссылка на вакансию
     * @return Название должности
     */
    public String getVacancyUrl();
    
    /**
     * Ссылка на вакансию
     * @param vacancyUrl 
     */
    public void setVacancyUrl(String vacancyUrl);
    
    /**
     * Уровень зарплаты
     * @return 
     */
    public Salary getSalary();
    
    /**
     * Уровень зарплаты
     * @param salary 
     */
    public void setSalary(Salary salary);
    
    /**
     * Название компании
     * @return 
     */
    public String getCompanyName();
    
    /**
     * Название компании
     * @param companyName 
     */
    public void setCompanyName(String companyName);
    
    /**
     * Описание вакансии
     * @return 
     */
    public String getDescription();
    
    /**
     * Описание вакансии
     * @param string 
     */
    public void setDescription(String string);
    
    /**
     * Город, в котором предлагается работа
     * @return Название города
     */
    public String getCity();
    
    /**
     * Город, в котором предлагается работа
     * @param cityName 
     */
    public void setCity(String cityName);
    
    /**
     * Интернет-адрес работодателя
     * @return 
     */
    public String getCompanyUrl();
    
    /**
     * Установка интернет-адреса работодателя
     * @param value 
     */
    public void setCompanyUrl(String value);
    
    /**
     * Уровень сходства текущей вакансии с указанной
     * @param other
     * @return 
     */
    public Double getLevelOfSimilarity(Vacancy other);

}
