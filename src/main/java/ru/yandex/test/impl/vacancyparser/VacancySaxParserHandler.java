package ru.yandex.test.impl.vacancyparser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import ru.yandex.test.Vacancy;

import java.util.Collections;
import java.util.List;

/**
 * Класс, объекты которого должны оповещаться о событиях просмотра xml-файлов
 */
public class VacancySaxParserHandler extends DefaultHandler {

    private TagSet tagSet = new TagSet();

    public VacancySaxParserHandler() {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        Tag tag = tagSet.byName(qName);
        tag.starting(attributes);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (tagSet.currentTag != null) {
            tagSet.currentTag.text(ch, start, length);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        Tag tag = tagSet.byName(qName);
        tag.ending();
    }

    public List<Vacancy> getVacancies() {
        return Collections.unmodifiableList(tagSet.getVacancies());
    }

    public String getQuery() {
        return tagSet.getQuery();
    }
}
