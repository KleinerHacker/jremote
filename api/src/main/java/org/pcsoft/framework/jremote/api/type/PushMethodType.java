package org.pcsoft.framework.jremote.api.type;

/**
 * Type of push method
 */
public enum PushMethodType {
    /**
     * A default field is pushed / updated (contains complete list and array updates)
     */
    Default,
    /**
     * A single item of a list / array is pushed / updated
     */
    SingleListItem
}
