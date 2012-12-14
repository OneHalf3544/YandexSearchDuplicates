package ru.yandex.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Класс, представляющий дублирующиеся вакансии.
 * Массив дублирующихся вакансий передается конструктору. 
 * В дальнейшем этот набор вакансий не изменяется
 * 
 * @author OneHalf
 */
public class Duplicate {

    private Set<Vacancy> duplicates = new HashSet<Vacancy>();

    /**
     * Создание объекта с дублирующимися вакансиями
     * @param vacancies Массив дублирующихся вакансий
     */
    public Duplicate(Vacancy ... vacancies) {
        duplicates.addAll(Arrays.asList(vacancies));
    }

    /**
     * Получение набора дублирующихся вакансий, содержащегося в текущем объекте
     * @return Набор вакансий
     */
    public Set<Vacancy> getDuplicates() {
        return Collections.unmodifiableSet(duplicates);
    }

    /**
     * Выводит набор вакансий в виде списка
     * @return Строковое представление содержимого в виде списка
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Vacancy vacancy : duplicates) {
            result.append(vacancy).append("\n");
        }
        return result.toString();
    }
}
