package org.pcsoft.framework.jremote.io.commons.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pcsoft.framework.jremote.core.RemoteClient;
import org.pcsoft.framework.jremote.core.RemoteClientBuilder;
import org.pcsoft.framework.jremote.io.commons.test.api.TestPushService;
import org.pcsoft.framework.jremote.io.commons.test.api.TestRemoteModel;
import org.pcsoft.framework.jremote.io.commons.test.api.TestRemoteObserver;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class RemoteMainTestBase {
    private RemoteClient remoteClient;

    @BeforeEach
    public void init() throws Exception {
        remoteClient = RemoteClientBuilder.create("localhost", 9999)
                .withRemoteModel(TestRemoteModel.class)
                .withRemoteObserver(TestRemoteObserver.class)
                .withRemotePushService(TestPushService.class)
                .build();

        remoteClient.open();
    }

    @AfterEach
    public void done() throws Exception {
        remoteClient.close();
    }

    @Test
    public void run() throws Exception {
        final AtomicInteger nameChangeCounter = new AtomicInteger(0);
        final AtomicInteger valueChangeCounter = new AtomicInteger(0);

        final TestRemoteModel remoteModel = remoteClient.getData().getRemoteModel(TestRemoteModel.class);
        Assertions.assertNotNull(remoteModel);
        final TestRemoteObserver remoteObserver = remoteClient.getData().getRemoteObserver(TestRemoteObserver.class);
        Assertions.assertNotNull(remoteObserver);

        remoteObserver.addNameListener(nameChangeCounter::incrementAndGet);
        remoteObserver.addValueListener(valueChangeCounter::incrementAndGet);

        Thread.sleep(10000);
    }
}
