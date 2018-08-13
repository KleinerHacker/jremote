package org.pcsoft.framework.jremote.commons.test;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class LoggingFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        final String dateTime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                .withLocale(Locale.getDefault())
                .withZone(ZoneId.systemDefault())
                .format(record.getInstant());

        final String level;
        switch (record.getLevel().getName().toLowerCase()) {
            case "severe":
                level = "ERROR";
                break;
            case "warning":
                level = "WARN";
                break;
            case "fine":
                level = "DEBUG";
                break;
            case "finest":
                level = "TRACE";
                break;
            default:
                level = record.getLevel().getName();
                break;
        }

        return StringUtils.leftPad(dateTime, 20) + " [" + StringUtils.leftPad(level, 5) + "] " + record.getMessage() + SystemUtils.LINE_SEPARATOR;
    }
}
