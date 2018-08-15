package org.pcsoft.framework.jremote.commons.type;

import org.pcsoft.framework.jremote.api.Control;
import org.pcsoft.framework.jremote.api.RemoteControlService;

@RemoteControlService
public interface Interf1 {
    @Control
    void doAny();
}
