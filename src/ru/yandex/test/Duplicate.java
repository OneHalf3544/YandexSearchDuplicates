package ru.yandex.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author OneHalf
 */
public class Duplicate {

    private Set<Vacancy> duplicates = new HashSet<Vacancy>();

    public Duplicate(Vacancy ... vacancies) {
        duplicates.addAll(Arrays.asList(vacancies));
    }

    public Set<Vacancy> getDuplicates() {
        return Collections.unmodifiableSet(duplicates);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (Vacancy vacancy : duplicates) {
            result.append(vacancy).append("\n");
        }
        return result.toString();
    }
}