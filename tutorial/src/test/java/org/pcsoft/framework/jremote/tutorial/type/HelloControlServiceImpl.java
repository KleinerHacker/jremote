package org.pcsoft.framework.jremote.tutorial.type;

import java.rmi.RemoteException;
import java.util.function.Supplier;

public class HelloControlServiceImpl implements HelloControlService {
    private final Supplier<HelloEventService> eventService;
    private final Supplier<HelloPushService> pushService;

    public HelloControlServiceImpl(Supplier<HelloEventService> eventService, Supplier<HelloPushService> pushService) {
        this.eventService = eventService;
        this.pushService = pushService;
    }

    @Override
    public void sayHello(String name) throws RemoteException {
        final int value = GreetingManager.addGreeting();

        pushService.get().pushGreetingCount(value);
        eventService.get().onGreeting("Hello, " + name);
    }
}
