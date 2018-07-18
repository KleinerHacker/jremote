package org.pcsoft.framework.jremote.api.type;

/**
 * Type of push method
 */
public enum PushMethodType {
    /**
     * A simple field is pushed / updated
     */
    Simple,
    /**
     * A single item of a list is pushed / updated
     */
    SingleListItem,
    /**
     * The complete list is pushed / updated
     */
    CompleteList
}
