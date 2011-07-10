package ru.yandex.test.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 *
 * @author OneHalf
 */
public class LogFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        return MessageFormat.format("{0} {1}: {2}", new Date(record.getMillis()), record.getLevel(), record.getMessage());
    }
}
