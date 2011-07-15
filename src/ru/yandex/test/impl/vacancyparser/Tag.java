package ru.yandex.test.impl.vacancyparser;

import org.xml.sax.Attributes;

/**
 * Created by IntelliJ IDEA.
 * User: OneHalf
 * Date: 13.07.11
 * Time: 19:13
 */
public class Tag {

    /**
     * Название соответствующего тега в xml-документе
     */
    public final String TAG_NAME;
    TagSet tagSet;

    public Tag(String tagName, TagSet tagSet) {
        this.TAG_NAME = tagName;
        this.tagSet = tagSet;
    }

    public void starting(Attributes attributes) {
        tagSet.currentTag = this;
    }

    public void text(char[] ch, int start, int length) {
    }

    public void ending() {
        tagSet.currentTag = null;
    }
}
