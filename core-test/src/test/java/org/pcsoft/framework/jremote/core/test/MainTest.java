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
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("Duplicates")
public class MainTest {
    private RemoteServer remoteServer;
    private RemoteClient remoteClient;

    @BeforeEach
    public void init() throws Exception {
        remoteServer = RemoteServerBuilder.create("localhost", 9998)
                .withPushClient(TestRemotePushService.class)
                .withEventClient(TestRemoteEventService.class)
                .withControlService(new TestRemoteControllerImpl(
                        () -> remoteServer.getBroadcast().getPushClient(TestRemotePushService.class),
                        () -> remoteServer.getBroadcast().getEventClient(TestRemoteEventService.class)
                ))
                .withPushModelData(TestRemotePushModelData.class)
                .build();

        remoteClient = RemoteClientBuilder.create("localhost", 9998, 9999)
                .withRemotePushModel(TestRemotePushModel.class)
                .withRemotePushObserver(TestRemotePushObserver.class)
                .withRemotePushService(TestRemotePushService.class)
                .withRemoteEventObserver(TestRemoteEventReceiver.class)
                .withRemoteEventService(TestRemoteEventService.class)
                .withRemoteControlClient(TestRemoteController.class)
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
        final AtomicReference<String> expectedEventValue = new AtomicReference<>();

        final TestRemotePushModel remotePushModel = remoteClient.getData().getRemotePushModel(TestRemotePushModel.class);
        Assertions.assertNotNull(remotePushModel);
        Assertions.assertEquals(5 + 7, remotePushModel.calc(5, 7));
        final TestRemotePushObserver remotePushObserver = remoteClient.getData().getRemotePushObserver(TestRemotePushObserver.class);
        Assertions.assertNotNull(remotePushObserver);
        Assertions.assertEquals((int) Math.pow(5 + 7, 2), remotePushObserver.calc(5, 7));
        final TestRemoteEventReceiver remoteEventObserver = remoteClient.getData().getRemoteEventObserver(TestRemoteEventReceiver.class);
        Assertions.assertNotNull(remoteEventObserver);
        Assertions.assertEquals((int) Math.pow(5 + 7, 3), remoteEventObserver.calc(5, 7));
        final TestRemoteController controlClient = remoteClient.getControl().getControlClient(TestRemoteController.class);
        Assertions.assertNotNull(controlClient);
        Assertions.assertEquals(5 * 7, controlClient.calc(5, 7));

        remotePushObserver.addNameListener(nameChangeCounter::incrementAndGet);
        remotePushObserver.addValueListener(valueChangeCounter::incrementAndGet);
        remoteEventObserver.addLogListener(v -> {
            Assertions.assertEquals(expectedEventValue.get(), v);
            logCounter.incrementAndGet();
        });

        //TODO: Move values on registration to client
        Assertions.assertEquals("Initial", remotePushModel.getName());
        Assertions.assertEquals(-3, remotePushModel.getValue());

        Assertions.assertEquals(0, nameChangeCounter.get());
        Assertions.assertEquals(0, valueChangeCounter.get());

        /*** START CHANGES ***/
        controlClient.changeName("Hello");
        Thread.sleep(100);
        Assertions.assertEquals(1, nameChangeCounter.get());
        Assertions.assertEquals("Hello", remotePushModel.getName());

        controlClient.changeName("World");
        Thread.sleep(100);
        Assertions.assertEquals(2, nameChangeCounter.get());
        Assertions.assertEquals("World", remotePushModel.getName());

        controlClient.changeValue(10);
        Thread.sleep(100);
        Assertions.assertEquals(1, valueChangeCounter.get());
        Assertions.assertEquals(10, remotePushModel.getValue());

        /*** EVENTS ***/
        Assertions.assertEquals(0, logCounter.get());

        expectedEventValue.set("Hello");
        controlClient.log("Hello");
        Thread.sleep(100);
        Assertions.assertEquals(1, logCounter.get());

        expectedEventValue.set("World");
        controlClient.log("World");
        Thread.sleep(100);
        Assertions.assertEquals(2, logCounter.get());
    }
}
