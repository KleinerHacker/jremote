package org.pcsoft.framework.jremote.tutorial;

import org.pcsoft.framework.jremote.core.*;
import org.pcsoft.framework.jremote.ext.config.api.ClientConfiguration;
import org.pcsoft.framework.jremote.ext.config.api.ServerConfiguration;
import org.pcsoft.framework.jremote.ext.config.impl.fluent.ClientFluentConfiguration;
import org.pcsoft.framework.jremote.ext.config.impl.fluent.ServerFluentConfiguration;
import org.pcsoft.framework.jremote.ext.np.impl.rmi.RmiProtocol;
import org.pcsoft.framework.jremote.ext.up.impl.def.DefaultUpdatePolicy;
import org.pcsoft.framework.jremote.tutorial.type.*;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class Tutorial {
    private static final AtomicReference<RemoteServer> REMOTE_SERVER = new AtomicReference<>();
    private static final AtomicReference<RemoteClient> REMOTE_CLIENT = new AtomicReference<>();

    private static final String[] NAMES = new String[] {
            "Justin", "Jenny", "Nico", "Alexa", "Caroline"
    };

    public static void main(String[] args) throws Exception {
        System.out.println("Initialize system...");
        final ServerConfiguration serverConfiguration = ServerFluentConfiguration.create()
                .setHost("localhost")
                .setPort(19999);
        final ClientConfiguration clientConfiguration = ClientFluentConfiguration.create()
                .setHost("localhost")
                .setPort(19999)
                .setOwnPort(19998);
        //With default values, optional
        final ExtensionConfiguration extensionConfiguration = ExtensionConfigurationBuilder.create()
                .withNetworkProtocol(RmiProtocol.class)
                .withUpdatePolicy(DefaultUpdatePolicy.class)
                .build();

        startServer(serverConfiguration, extensionConfiguration);
        startClient(clientConfiguration, extensionConfiguration);
        System.out.println("> Wait...");
        Thread.sleep(5000);
        System.out.println("Finish starting!");

        final HelloPushModel pushModel = REMOTE_CLIENT.get().getData().getRemotePushModel(HelloPushModel.class);
        final HelloPushObserver pushObserver = REMOTE_CLIENT.get().getData().getRemotePushObserver(HelloPushObserver.class);
        final HelloEventReceiver eventReceiver = REMOTE_CLIENT.get().getData().getRemoteEventReceiver(HelloEventReceiver.class);
        final HelloControlService controlClient = REMOTE_CLIENT.get().getControl().getControlClient(HelloControlService.class);

        pushObserver.addGreetingCountListener(() -> System.out.println("New count of greetings: " + pushModel.getGreetingCount()));
        eventReceiver.addGreetingListener(greeting -> System.out.println("Receive greetings: " + greeting));

        try {
            System.out.println("Initial count of greetings: " + pushModel.getGreetingCount());
            System.out.println("Start greetings...");

            for (final String name : NAMES) {
                System.out.println("Send 'Say Hello' for " + name);
                controlClient.sayHello(name);

                Thread.sleep(1000);
            }
        } finally {
            REMOTE_CLIENT.get().close();
            REMOTE_SERVER.get().close();
        }

        System.exit(0);
    }

    private static void startServer(ServerConfiguration serverConfiguration, ExtensionConfiguration extensionConfiguration) throws IOException {
        System.out.println("Startup service...");
        REMOTE_SERVER.set(RemoteServerBuilder.create(serverConfiguration, extensionConfiguration)
                .withRemoteControlService(new HelloControlServiceImpl(
                        () -> REMOTE_SERVER.get().getBroadcast().getEventClient(HelloEventService.class),
                        () -> REMOTE_SERVER.get().getBroadcast().getPushClient(HelloPushService.class)
                ))
                .withPushClient(HelloPushService.class)
                .withEventClient(HelloEventService.class)
                .withPushModelData(HelloPushModelData.class)
                .build()
        );
        REMOTE_SERVER.get().open();
    }

    private static void startClient(ClientConfiguration clientConfiguration, ExtensionConfiguration extensionConfiguration) throws IOException {
        System.out.println("Startup client...");
        REMOTE_CLIENT.set(RemoteClientBuilder.create(clientConfiguration, extensionConfiguration)
                .withRemotePushModel(HelloPushModel.class)
                .withRemotePushObserver(HelloPushObserver.class)
                .withRemotePushService(HelloPushService.class)
                .withRemoteEventReceiver(HelloEventReceiver.class)
                .withRemoteEventService(HelloEventService.class)
                .withRemoteControlClient(HelloControlService.class)
                .build()
        );
        REMOTE_CLIENT.get().open();
    }
}
