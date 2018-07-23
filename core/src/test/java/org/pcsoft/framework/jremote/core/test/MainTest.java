package org.pcsoft.framework.jremote.core.test;

import org.junit.Test;
import org.pcsoft.framework.jremote.core.RemoteClient;
import org.pcsoft.framework.jremote.core.RemoteClientBuilder;
import org.pcsoft.framework.jremote.core.test.api.TestRemoteModel;

public class MainTest {

    @Test
    public void run() throws Exception {
        final RemoteClient remoteClient = RemoteClientBuilder.create()
                .withRemoteModel(TestRemoteModel.class)
                .build();

        remoteClient.open();

        Thread.sleep(1000);

        remoteClient.close();
    }
}
