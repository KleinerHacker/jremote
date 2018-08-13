package org.pcsoft.framework.jremote.core.test;

import org.junit.*;
import org.pcsoft.framework.jremote.core.RemoteClient;
import org.pcsoft.framework.jremote.core.RemoteClientBuilder;
import org.pcsoft.framework.jremote.core.test.api.TestPushService;
import org.pcsoft.framework.jremote.core.test.api.TestRemoteModel;
import org.pcsoft.framework.jremote.core.test.api.TestRemoteObserver;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.LogManager;

public class MainTest {
    private RemoteClient remoteClient;

    @BeforeClass
    public static void i() throws IOException {
        LogManager.getLogManager().readConfiguration(MainTest.class.getResourceAsStream("/logging.properties"));
    }

    @Before
    public void init() throws Exception {
        remoteClient = RemoteClientBuilder.create("localhost", 9999)
                .withRemoteModel(TestRemoteModel.class)
                .withRemoteObserver(TestRemoteObserver.class)
                .withRemotePushService(TestPushService.class)
                .build();

        remoteClient.open();
    }

    @After
    public void done() throws Exception {
        remoteClient.close();
    }

    @Test
    public void run() throws Exception {
        final AtomicInteger nameChangeCounter = new AtomicInteger(0);
        final AtomicInteger valueChangeCounter = new AtomicInteger(0);

        final TestRemoteModel remoteModel = remoteClient.getData().getRemoteModel(TestRemoteModel.class);
        Assert.assertNotNull(remoteModel);
        final TestRemoteObserver remoteObserver = remoteClient.getData().getRemoteObserver(TestRemoteObserver.class);
        Assert.assertNotNull(remoteObserver);

        remoteObserver.addNameListener(nameChangeCounter::incrementAndGet);
        remoteObserver.addValueListener(valueChangeCounter::incrementAndGet);

        Thread.sleep(10000);
    }
}
