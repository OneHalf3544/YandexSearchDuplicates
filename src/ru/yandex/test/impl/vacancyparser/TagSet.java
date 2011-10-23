package ru.yandex.test.impl.vacancyparser;

import org.xml.sax.Attributes;
import ru.yandex.test.Salary;
import ru.yandex.test.Vacancy;
import ru.yandex.test.impl.VacancyImpl;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Теги документа. Содержат функции для обработки xml-документа
 */
class TagSet {
    
    private Integer minSalary;
    private Integer maxSalary;

    private String query;
    private StringBuilder textBuffer;

    private Vacancy currentVacancy;
    Tag currentTag;

    private List<Vacancy> vacancies;

    private Map<String, Tag> tags = new HashMap<String, Tag>();

    private Tag VACANCY_NAME = new VacancyNameTag(this);

    private Tag SALARY = new SalaryTag(this);

    TagSet() {
        Tag VACANCIES = new VacanciesTag(this);
        Tag QUERY = new QueryTag(this);
        Tag VACANCY = new VacancyTag(this);
        Tag COMPANY_NAME = new CompanyNameTag(this);
        Tag CITY = new CityTag(this);
        Tag MINIMUM_SALARY = new MinimumSalaryTag(this);
        Tag MAXIMUM_SALARY = new MaximumSalaryTag(this);
        Tag DESCRIPTION = new DescriptionTag(this);

        tags.put(VACANCIES.TAG_NAME, VACANCIES);
        tags.put(QUERY.TAG_NAME, QUERY);
        tags.put(VACANCY.TAG_NAME, VACANCY);

        tags.put(VACANCY_NAME.TAG_NAME, VACANCY_NAME);
        tags.put(COMPANY_NAME.TAG_NAME, COMPANY_NAME);
        tags.put(CITY.TAG_NAME, CITY);

        tags.put(SALARY.TAG_NAME, SALARY);
        tags.put(MINIMUM_SALARY.TAG_NAME, MINIMUM_SALARY);
        tags.put(MAXIMUM_SALARY.TAG_NAME, MAXIMUM_SALARY);

        tags.put(DESCRIPTION.TAG_NAME, DESCRIPTION);
    }

    public List<Vacancy> getVacancies() {
        return vacancies;
    }

    public String getQuery() {
        return query;
    }

    public Tag byName(String tagName) {
        return tags.get(tagName);
    }

    private static class VacanciesTag extends Tag {
        public VacanciesTag(TagSet tagSet) {
            super("vacancies", tagSet);
        }

        @Override
        public void starting(Attributes attributes) {
            super.starting(attributes);
            tagSet.vacancies = new LinkedList<Vacancy>();
        }
    }

    private static class QueryTag extends Tag {
        public QueryTag(TagSet tagSet) {
            super("query", tagSet);
        }

        @Override
        public void starting(Attributes attributes) {
            tagSet.query = attributes.getValue("value");
        }
    }

    private static class VacancyTag extends Tag {
        public VacancyTag(TagSet tagSet) {
            super("vacancy", tagSet);
        }

        @Override
        public void starting(Attributes attributes) {
            tagSet.currentVacancy = new VacancyImpl();
        }

        @Override
        public void ending() {
            tagSet.vacancies.add(tagSet.currentVacancy);
            tagSet.currentVacancy = null;
        }
    }

    private static class VacancyNameTag extends Tag {

        public VacancyNameTag(TagSet tagSet) {
            super("vacancyName", tagSet);
        }

        @Override
        public void starting(Attributes attributes) {
            tagSet.currentTag = tagSet.VACANCY_NAME;
            final String url = attributes.getValue("", "url").replaceAll("(^\\s+)|(\\s+$)", "");
            tagSet.currentVacancy.setVacancyUrl(url);
            tagSet.textBuffer = new StringBuilder();
        }

        @Override
        public void text(char[] ch, int start, int length) {
            tagSet.textBuffer.append(ch, start, length);
        }

        @Override
        public void ending() {
            tagSet.currentVacancy.setVacancyName(tagSet.textBuffer.toString().replaceAll("(^\\s+)|(\\s+$)", ""));
            super.ending();
        }
    }

    private static class CompanyNameTag extends Tag {
        public CompanyNameTag(TagSet tagSet) {
            super("companyName", tagSet);
        }

        @Override
        public void starting(Attributes attributes) {
            super.starting(attributes);
            tagSet.currentVacancy.setCompanyUrl(attributes.getValue("", "companyUrl"));
            tagSet.textBuffer = new StringBuilder();
        }

        @Override
        public void text(char[] ch, int start, int length) {
            tagSet.textBuffer.append(ch, start, length);
        }

        @Override
        public void ending() {
            tagSet.currentVacancy.setCompanyName(tagSet.textBuffer.toString());
            super.ending();
        }

    }

    private static class CityTag extends Tag {

        public CityTag(TagSet tagSet) {
            super("vacancyCity", tagSet);
        }

        @Override
        public void starting(Attributes attributes) {
            super.starting(attributes);
            tagSet.textBuffer = new StringBuilder();
        }

        @Override
        public void text(char[] ch, int start, int length) {
            tagSet.textBuffer.append(ch, start, length);
        }

        @Override
        public void ending() {
            tagSet.currentVacancy.setCity(tagSet.textBuffer.toString());
            super.ending();
        }
    }

    private static class SalaryTag extends Tag {
        public SalaryTag(TagSet tagSet) {
            super("salary", tagSet);
        }

        @Override
        public void starting(Attributes attributes) {
            super.starting(attributes);
            tagSet.minSalary = null;
            tagSet.maxSalary = null;
        }

        @Override
        public void ending() {
            tagSet.currentVacancy.setSalary(new Salary(tagSet.minSalary, tagSet.maxSalary));
        }
    }

    private static class MinimumSalaryTag extends Tag {

        public MinimumSalaryTag(TagSet tagSet) {
            super("minimum", tagSet);
        }

        @Override
        public void starting(Attributes attributes) {
            super.starting(attributes);
        }

        @Override
        public void ending() {
            tagSet.currentTag = tagSet.SALARY;
        }

        @Override
        public void text(char[] ch, int start, int length) {
            String value = new String(ch, start, length);
            try {
                tagSet.minSalary = Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                tagSet.minSalary = null;
            }
        }

    }

    private static class MaximumSalaryTag extends Tag {

        public MaximumSalaryTag(TagSet tagSet) {
            super("maximum", tagSet);
        }

        @Override
        public void starting(Attributes attributes) {
            super.starting(attributes);
        }

        @Override
        public void ending() {
            tagSet.currentTag = tagSet.SALARY;
        }

        @Override
        public void text(char[] ch, int start, int length) {
            String value = new String(ch, start, length);
            try {
                tagSet.maxSalary = Integer.parseInt(value);
            } catch (NumberFormatException ex) {
                tagSet.maxSalary = null;
            }
        }

    }

    private static class DescriptionTag extends Tag {

        public DescriptionTag(TagSet tagSet) {
            super("description", tagSet);
        }

        @Override
        public void starting(Attributes attributes) {
            super.starting(attributes);
            tagSet.textBuffer = new StringBuilder();

        }

        @Override
        public void text(char[] ch, int start, int length) {
            tagSet.textBuffer.append(ch, start, length);
        }

        @Override
        public void ending() {
            tagSet.currentVacancy.setDescription(tagSet.textBuffer.toString());
            tagSet.textBuffer = null;
            super.ending();
        }

    }
}
