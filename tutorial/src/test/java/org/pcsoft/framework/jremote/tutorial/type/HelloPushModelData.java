package org.pcsoft.framework.jremote.tutorial.type;

public class HelloPushModelData implements HelloPushModel {
    @Override
    public int getGreetingCount() {
        return GreetingManager.getGreetings();
    }
}
