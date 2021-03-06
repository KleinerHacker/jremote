package org.pcsoft.framework.jremote.core.test.basic;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.pcsoft.framework.jremote.core.RemoteClient;
import org.pcsoft.framework.jremote.core.RemoteClientBuilder;
import org.pcsoft.framework.jremote.core.RemoteServer;
import org.pcsoft.framework.jremote.core.RemoteServerBuilder;
import org.pcsoft.framework.jremote.core.test.basic.api.*;
import org.pcsoft.framework.jremote.ext.config.api.ClientConfiguration;
import org.pcsoft.framework.jremote.ext.config.api.ServerConfiguration;
import org.pcsoft.framework.jremote.ext.config.impl.fluent.ClientFluentConfiguration;
import org.pcsoft.framework.jremote.ext.config.impl.fluent.ServerFluentConfiguration;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@SuppressWarnings("Duplicates")
class MainTest {
    private static RemoteServer remoteServer;
    private static RemoteClient remoteClient;

    private static AtomicInteger nameChangeCounter;
    private static AtomicInteger valueChangeCounter;
    private static AtomicInteger wordsChangeCounter;
    private static AtomicInteger sizesChangeCounter;
    private static AtomicInteger logCounter;

    private static AtomicReference<String> expectedEventValue;

    private static TestRemotePushModel remotePushModel;
    private static TestRemotePushObserver remotePushObserver;
    private static TestRemoteEventReceiver remoteEventObserver;
    private static TestRemoteController controlClient;

    @BeforeAll
    static void init() throws Exception {
        final ServerConfiguration serverConfiguration = ServerFluentConfiguration.create()
                .setHost("localhost")
                .setPort(9998);
        remoteServer = RemoteServerBuilder.create(serverConfiguration)
                .withRemotePushClient(TestRemotePushService.class)
                .withRemoteEventClient(TestRemoteEventService.class)
                .withRemoteControlService(new TestRemoteControllerImpl(
                        () -> remoteServer.getBroadcast().getPushClient(TestRemotePushService.class),
                        () -> remoteServer.getBroadcast().getEventClient(TestRemoteEventService.class)
                ))
                .withPushModelData(TestRemotePushModelData.class)
                .build();

        final ClientConfiguration clientConfiguration = ClientFluentConfiguration.create()
                .setHost("localhost")
                .setPort(9998)
                .setOwnPort(9999);
        remoteClient = RemoteClientBuilder.create(clientConfiguration)
                .withRemotePushModel(TestRemotePushModel.class)
                .withRemotePushObserver(TestRemotePushObserver.class)
                .withRemotePushService(TestRemotePushService.class)
                .withRemoteEventReceiver(TestRemoteEventReceiver.class)
                .withRemoteEventService(TestRemoteEventService.class)
                .withRemoteControlClient(TestRemoteController.class)
                .build();

        remoteServer.open();
        remoteClient.open();

        Thread.sleep(3000);

        nameChangeCounter = new AtomicInteger(0);
        valueChangeCounter = new AtomicInteger(0);
        wordsChangeCounter = new AtomicInteger(0);
        sizesChangeCounter = new AtomicInteger(0);
        logCounter = new AtomicInteger(0);
        expectedEventValue = new AtomicReference<>();

        remotePushModel = remoteClient.getData().getRemotePushModel(TestRemotePushModel.class);
        Assertions.assertNotNull(remotePushModel);
        Assertions.assertEquals(5 + 7, remotePushModel.calc(5, 7));
        remotePushObserver = remoteClient.getData().getRemotePushObserver(TestRemotePushObserver.class);
        Assertions.assertNotNull(remotePushObserver);
        Assertions.assertEquals((int) Math.pow(5 + 7, 2), remotePushObserver.calc(5, 7));
        remoteEventObserver = remoteClient.getData().getRemoteEventReceiver(TestRemoteEventReceiver.class);
        Assertions.assertNotNull(remoteEventObserver);
        Assertions.assertEquals((int) Math.pow(5 + 7, 3), remoteEventObserver.calc(5, 7));
        controlClient = remoteClient.getControl().getControlClient(TestRemoteController.class);
        Assertions.assertNotNull(controlClient);
        Assertions.assertEquals(5 * 7, controlClient.calc(5, 7));

        remotePushObserver.addNameListener(nameChangeCounter::incrementAndGet);
        remotePushObserver.addValueListener(valueChangeCounter::incrementAndGet);
        remotePushObserver.addWordListListener(wordsChangeCounter::incrementAndGet);
        remotePushObserver.addSizeListListener(sizesChangeCounter::incrementAndGet);
        remoteEventObserver.addLogListener(v -> {
            Assertions.assertEquals(expectedEventValue.get(), v);
            logCounter.incrementAndGet();
        });

        Assertions.assertEquals("Initial", remotePushModel.getName());
        Assertions.assertEquals(-3, remotePushModel.getValue());
        Assertions.assertNotNull(remotePushModel.getWordList());
        Assertions.assertEquals(2, remotePushModel.getWordList().size());
        Assertions.assertEquals(List.of("Hello", "World"), remotePushModel.getWordList());
        Assertions.assertNotNull(remotePushModel.getSizeList());
        Assertions.assertEquals(3, remotePushModel.getSizeList().length);
        Assertions.assertArrayEquals(new int[]{10, 20, 30}, remotePushModel.getSizeList());

        Assertions.assertEquals(0, nameChangeCounter.get());
        Assertions.assertEquals(0, valueChangeCounter.get());
        Assertions.assertEquals(0, wordsChangeCounter.get());
        Assertions.assertEquals(0, sizesChangeCounter.get());
        Assertions.assertEquals(0, logCounter.get());
    }

    @AfterAll
    static void done() throws Exception {
        remoteClient.close();
        remoteServer.close();

        Thread.sleep(1000);
    }

    @Test
    void simpleChangesTest() throws Exception {
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
    }

    @Test
    void listChangesTest() throws Exception {
        /*** CHANGES IN LISTS ***/
        controlClient.changeWordList(List.of("My", "Life", "Ready"));
        Assertions.assertEquals(1, wordsChangeCounter.get());
        Assertions.assertNotNull(remotePushModel.getWordList());
        Assertions.assertEquals(3, remotePushModel.getWordList().size());
        Assertions.assertEquals(List.of("My", "Life", "Ready"), remotePushModel.getWordList());

        controlClient.removeWord("Life");
        Assertions.assertEquals(2, wordsChangeCounter.get());
        Assertions.assertNotNull(remotePushModel.getWordList());
        Assertions.assertEquals(2, remotePushModel.getWordList().size());
        Assertions.assertEquals(List.of("My", "Ready"), remotePushModel.getWordList());

        controlClient.addWord("Head");
        Assertions.assertEquals(3, wordsChangeCounter.get());
        Assertions.assertNotNull(remotePushModel.getWordList());
        Assertions.assertEquals(3, remotePushModel.getWordList().size());
        Assertions.assertEquals(List.of("My", "Ready", "Head"), remotePushModel.getWordList());
    }

    @Test
    void arrayChangesTest() throws Exception {
        /*** CHANGES IN ARRAYS ***/
        controlClient.changeSizeList(new int[]{50, 70, 66, 90, 30});
        Assertions.assertEquals(1, sizesChangeCounter.get());
        Assertions.assertNotNull(remotePushModel.getSizeList());
        Assertions.assertEquals(5, remotePushModel.getSizeList().length);
        Assertions.assertArrayEquals(new int[]{50, 70, 66, 90, 30}, remotePushModel.getSizeList());

        controlClient.removeSize(66);
        Assertions.assertEquals(2, sizesChangeCounter.get());
        Assertions.assertNotNull(remotePushModel.getSizeList());
        Assertions.assertEquals(4, remotePushModel.getSizeList().length);
        Assertions.assertArrayEquals(new int[]{50, 70, 90, 30}, remotePushModel.getSizeList());

        controlClient.addSize(77);
        Assertions.assertEquals(3, sizesChangeCounter.get());
        Assertions.assertNotNull(remotePushModel.getSizeList());
        Assertions.assertEquals(5, remotePushModel.getSizeList().length);
        Assertions.assertArrayEquals(new int[]{50, 70, 90, 30, 77}, remotePushModel.getSizeList());
    }

    @Test
    void eventTest() throws Exception {
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
