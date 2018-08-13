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
        final String dateTime = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(Locale.ROOT)
                .withZone(ZoneId.systemDefault())
                .format(record.getInstant());

        return StringUtils.leftPad(dateTime, 20) + " [" + StringUtils.leftPad(record.getLevel().getName(), 8) + "] " + record.getMessage() + SystemUtils.LINE_SEPARATOR;
    }
}
