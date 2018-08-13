/*
 * Copyright (c) 2000, 2013, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */


package org.pcsoft.framework.jremote.commons.test;

import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;

public class ConsoleHandler extends StreamHandler {

    public ConsoleHandler() {
        // configure with specific defaults for ConsoleHandler
        super(System.out, new LoggingFormatter());
    }

    @Override
    public void publish(LogRecord record) {
        super.publish(record);
        flush();
    }

    @Override
    public void close() {
        flush();
    }
}
