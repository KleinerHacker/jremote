package org.pcsoft.framework.jremote.api.type;

/**
 * Type of observer listener
 */
public enum ObserverListenerType {
    /**
     * Automatic detection via start with 'add' or 'remove' (default)
     */
    AutoDetection,
    /**
     * Mark as 'add' listener method
     */
    Add,
    /**
     * Mark as 'remove' listener method
     */
    Remove
}
