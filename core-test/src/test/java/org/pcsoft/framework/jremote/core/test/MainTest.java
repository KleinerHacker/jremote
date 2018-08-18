package org.pcsoft.framework.jremote.core.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pcsoft.framework.jremote.core.RemoteClient;
import org.pcsoft.framework.jremote.core.RemoteClientBuilder;
import org.pcsoft.framework.jremote.core.RemoteServer;
import org.pcsoft.framework.jremote.core.RemoteServerBuilder;
import org.pcsoft.framework.jremote.core.test.api.*;

import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("Duplicates")
public class MainTest {
    private RemoteServer remoteServer;
    private RemoteClient remoteClient;

    @BeforeEach
    public void init() throws Exception {
        remoteServer = RemoteServerBuilder.create("localhost", 9998)
                .withPushClient(TestPushService.class, TestEventService.class)
                .withControlService(new TestControllerImpl(
                        () -> remoteServer.getBroadcast().getPushClient(TestPushService.class),
                        () -> remoteServer.getBroadcast().getPushClient(TestEventService.class)
                ))
                .withPushModelData(TestRemoteModelData.class)
                .build();

        remoteClient = RemoteClientBuilder.create("localhost", 9998, 9999)
                .withPushRemoteModel(TestRemoteModel.class)
                .withRemotePushObserver(TestRemoteObserver.class)
                .withRemotePushService(TestPushService.class, TestEventService.class)
                .withRemoteControlClient(TestController.class)
                .build();

        remoteServer.open();
        remoteClient.open();

        Thread.sleep(1000);
    }

    @AfterEach
    public void done() throws Exception {
        remoteClient.close();
        remoteServer.close();
    }

    @Test
    public void run() throws Exception {
        final AtomicInteger nameChangeCounter = new AtomicInteger(0);
        final AtomicInteger valueChangeCounter = new AtomicInteger(0);
        final AtomicInteger logCounter = new AtomicInteger(0);

        final TestRemoteModel remoteModel = remoteClient.getData().getRemotePushModel(TestRemoteModel.class);
        Assertions.assertNotNull(remoteModel);
        Assertions.assertEquals(5 + 7, remoteModel.calc(5, 7));
        final TestRemoteObserver remoteObserver = remoteClient.getData().getRemotePushObserver(TestRemoteObserver.class);
        Assertions.assertNotNull(remoteObserver);
        Assertions.assertEquals((int) Math.pow(5 + 7, 2), remoteObserver.calc(5, 7));
        final TestController controlClient = remoteClient.getControl().getControlClient(TestController.class);
        Assertions.assertNotNull(controlClient);
        Assertions.assertEquals(5 * 7, controlClient.calc(5, 7));

        remoteObserver.addNameListener(nameChangeCounter::incrementAndGet);
        remoteObserver.addValueListener(valueChangeCounter::incrementAndGet);
        remoteObserver.addLogListener(logCounter::incrementAndGet);

        //TODO: Move values on registration to client
        Assertions.assertEquals("Initial", remoteModel.getName());
        Assertions.assertEquals(-3, remoteModel.getValue());

        Assertions.assertEquals(0, nameChangeCounter.get());
        Assertions.assertEquals(0, valueChangeCounter.get());

        /*** START CHANGES ***/
        controlClient.changeName("Hello");
        Thread.sleep(100);
        Assertions.assertEquals(1, nameChangeCounter.get());
        Assertions.assertEquals("Hello", remoteModel.getName());

        controlClient.changeName("World");
        Thread.sleep(100);
        Assertions.assertEquals(2, nameChangeCounter.get());
        Assertions.assertEquals("World", remoteModel.getName());

        controlClient.changeValue(10);
        Thread.sleep(100);
        Assertions.assertEquals(1, valueChangeCounter.get());
        Assertions.assertEquals(10, remoteModel.getValue());

        /*** EVENTS ***/
        Assertions.assertEquals(0, logCounter.get());

        controlClient.log("Hello");
        Thread.sleep(100);
        Assertions.assertEquals(1, logCounter.get());

        controlClient.log("World");
        Thread.sleep(100);
        Assertions.assertEquals(2, logCounter.get());
    }
}
