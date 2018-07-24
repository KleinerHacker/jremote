package org.pcsoft.framework.jremote.core.test;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.pcsoft.framework.jremote.core.RemoteClient;
import org.pcsoft.framework.jremote.core.RemoteClientBuilder;
import org.pcsoft.framework.jremote.core.test.api.TestPushService;
import org.pcsoft.framework.jremote.core.test.api.TestRemoteModel;
import org.pcsoft.framework.jremote.core.test.api.TestRemoteObserver;

import java.util.concurrent.atomic.AtomicInteger;

public class MainTest {
    private RemoteClient remoteClient;

    @Before
    public void init() {
        remoteClient = RemoteClientBuilder.create()
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
    }
}
