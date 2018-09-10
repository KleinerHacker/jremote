package org.pcsoft.framework.jremote.ext.up.impl.queued.internal;

import org.pcsoft.framework.jremote.ext.up.impl.queued.QueuedUpdatePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;

public final class InvocationConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(InvocationConfiguration.class);

    private static final String SYS_PROP_FILE = "up.queued.config";
    private static final String PROP_FILE = "up-queued.properties";

    private static final String KEY_MODEL = "model";
    private static final String KEY_MODEL_COUNT = KEY_MODEL + ".count";
    private static final String KEY_MODEL_DELAY = KEY_MODEL + ".delay";
    private static final String KEY_MODEL_BUFFER_SIZE = KEY_MODEL + ".buffer-size";

    private static final String KEY_EVENT = "event";
    private static final String KEY_EVENT_COUNT = KEY_EVENT + ".count";
    private static final String KEY_EVENT_DELAY = KEY_EVENT + ".delay";
    private static final String KEY_EVENT_BUFFER_SIZE = KEY_EVENT + ".buffer-size";

    static {
        System.setProperty(SYS_PROP_FILE, PROP_FILE);
    }

    private static final InvocationConfiguration INSTANCE = new InvocationConfiguration();

    public static InvocationConfiguration getInstance() {
        return INSTANCE;
    }

    private int modelInvocationCount = 1;
    private int modelInvocationDelay = 10;
    private int modelInvocationBufferSize = 256;

    private int eventInvocationCount = 1;
    private int eventInvocationDelay = 10;
    private int eventInvocationBufferSize = 256;

    private InvocationConfiguration() {
        try {
            final Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream("/" + System.getProperty(SYS_PROP_FILE)));

            try {
                modelInvocationCount = Integer.parseInt(properties.getProperty(KEY_MODEL_COUNT));
            } catch (NumberFormatException e) {
                LOGGER.warn("Unable to parse value for " + KEY_MODEL_COUNT + ", use default instead", e);
            }
            try {
                modelInvocationDelay = Integer.parseInt(properties.getProperty(KEY_MODEL_DELAY));
            } catch (NumberFormatException e) {
                LOGGER.warn("Unable to parse value for " + KEY_MODEL_DELAY + ", use default instead", e);
            }
            try {
                modelInvocationBufferSize = Integer.parseInt(properties.getProperty(KEY_MODEL_BUFFER_SIZE));
            } catch (NumberFormatException e) {
                LOGGER.warn("Unable to parse value for " + KEY_MODEL_BUFFER_SIZE + ", use default instead", e);
            }

            try {
                eventInvocationCount = Integer.parseInt(properties.getProperty(KEY_EVENT_COUNT));
            } catch (NumberFormatException e) {
                LOGGER.warn("Unable to parse value for " + KEY_EVENT_COUNT + ", use default instead", e);
            }
            try {
                eventInvocationDelay = Integer.parseInt(properties.getProperty(KEY_EVENT_DELAY));
            } catch (NumberFormatException e) {
                LOGGER.warn("Unable to parse value for " + KEY_EVENT_DELAY + ", use default instead", e);
            }
            try {
                eventInvocationBufferSize = Integer.parseInt(properties.getProperty(KEY_EVENT_BUFFER_SIZE));
            } catch (NumberFormatException e) {
                LOGGER.warn("Unable to parse value for " + KEY_EVENT_BUFFER_SIZE + ", use default instead", e);
            }
        } catch (IOException e) {
            LOGGER.warn("Unable to find configuration file for " + QueuedUpdatePolicy.class.getName() + " (" + PROP_FILE + "), use default values");
        }
    }

    public int getModelInvocationCount() {
        return modelInvocationCount;
    }

    public int getModelInvocationDelay() {
        return modelInvocationDelay;
    }

    public int getModelInvocationBufferSize() {
        return modelInvocationBufferSize;
    }

    public int getEventInvocationCount() {
        return eventInvocationCount;
    }

    public int getEventInvocationDelay() {
        return eventInvocationDelay;
    }

    public int getEventInvocationBufferSize() {
        return eventInvocationBufferSize;
    }
}
