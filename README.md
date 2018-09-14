# JRemote
Remote API and IMPL for Java based on REST with multi client support, push support and more. For documentation see [JRemote Documentation](https://github.com/KleinerHacker/jremote/releases/download/0.1.0/jremote.chm) or [Wiki](https://github.com/KleinerHacker/jremote/wiki)

# Overview
This API provides a client and a server implementation to communicate between clients and server asynchronious. 
The __client__ gets one or more control services to control the server behavior or functions. Additional a client contains one or more 
remote models (here the received pushed data are stored) and one or more optional remote observers (contains all listener for model
change activities). For event handling there are one or more _Remote Event Receivers_ (contains all event listener for event calls). 
The __server__ gets one or more push or event services to call all clients are connected. 

Internal a client is registered in server (via a _registration service_) and check its own connection via a _keep alive service_ so 
if the client is disconnected from server it try reconnect automatically. So if you open the client he waits automatically for service.

# Quick Start
This example shows a hello world code. For more information see wiki pages.

```Java
@RemotePushModel
public interface HelloPushModel {
    String PROP_GREETING_COUNT = "greeting-count";

    @PushModelProperty(PROP_GREETING_COUNT)
    int getGreetingCount();
}
```
Now we define a _Remote Model_ where the data from server push is stored into. We store the count of greetings of the server here.

```Java
@RemotePushObserver
public interface HelloPushObserver {
    @PushObserverListener(property = HelloPushModel.PROP_GREETING_COUNT, modelClass = HelloPushModel.class)
    void addGreetingCountListener(PushChangedListener l);

    @PushObserverListener(property = HelloPushModel.PROP_GREETING_COUNT, modelClass = HelloPushModel.class)
    void removeGreetingCountListener(PushChangedListener l);
}
```
Now we define a _Remote Observer_ to listen that the server send a greeting counter update to us. The detection for _add_ or _remove_ listener runs automatically via prefix _add_ or _remove_. The annotation need the reference to the _Remote Model_ and the property to listen, here _HelloModel_ and _greeting_.

```Java
@RemotePushService
public interface HelloPushService extends Remote {
    @Push(property = HelloPushModel.PROP_GREETING_COUNT, modelClass = HelloPushModel.class)
    void pushGreetingCount(int count) throws RemoteException;
}
```
This interface is used for the server to push greetings to the connected clients. The greeting is pushed into the client remote model
with name _HelloModel_ into property with name _greeting_.

```Java
@RemoteEventReceiver
public interface HelloEventReceiver {
    String EVENT_GREETING = "greeting";

    @EventReceiverListener(EVENT_GREETING)
    void addGreetingListener(EventReceivedListener<String> l);

    @EventReceiverListener(EVENT_GREETING)
    void removeGreetingListener(EventReceivedListener<String> l);
}
```
We define an _EventReceiver_ to listen on greeting events from server. In client code we want to print out greetings from the event service below.

```Java
@RemoteEventService
public interface HelloEventService extends Remote {
    @Event(event = HelloEventReceiver.EVENT_GREETING, eventClass = HelloEventReceiver.class)
    void onGreeting(String greeting) throws RemoteException;
}
```
This defines the used event service with a method to send greetings to all clients.

```Java
public final class GreetingManager {
    private static final AtomicInteger COUNTER = new AtomicInteger(0);

    public static int addGreeting() {
        return COUNTER.incrementAndGet();
    }

    public static int getGreetings() {
        return COUNTER.get();
    }

    private GreetingManager() {
    }
}
```
To count greetings on server side we need a manager class they allow to access ``COUNTER`` from all classes.

```Java
public class HelloPushModelData implements HelloPushModel {
    @Override
    public int getGreetingCount() {
        return GreetingManager.getGreetings();
    }
}
```
Here we define which data are set if a client is registered on server. The server send this client initial all current data and initialize the client _Remote Model_. In this case we store the count of greetings in the manager (see above) and return it if a new client is connected. This is optional.

```Java
@RemoteControlService
public interface HelloControlService extends Remote {
    @Control
    void sayHello(String name) throws RemoteException;
}
```
This is the control interface to control the service. We want to instruct the server to say _Hello_ with a specified name.

```Java
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
```
At the end we need an implementation for the control service to push the greeting counter and throws the greetings as event to all clients. In this case we need suppliers cause the proxy is not created if this implementation class is initialized. See below.

```Java
public class ServerRunner {
  private static final AtomicReference<RemoteServer> REMOTE_SERVER = new AtomicReference<>();

  public static void main(String[] args) {
    final ServerConfiguration serverConfiguration = ServerFluentConfiguration.create()
                .setHost("localhost")
                .setPort(19999);
    //With default values, optional
    final ExtensionConfiguration extensionConfiguration = ExtensionConfigurationBuilder.create()
                .withNetworkProtocol(RmiProtocol.class)
                .withUpdatePolicy(DefaultUpdatePolicy.class)
                .build();
  
    System.out.println("Startup service...");
    REMOTE_SERVER.set(RemoteServerBuilder.create(serverConfiguration, extensionConfiguration)
            .withRemoteControlService(new HelloControlServiceImpl(
                    () -> REMOTE_SERVER.get().getBroadcast().getEventClient(HelloEventService.class),
                    () -> REMOTE_SERVER.get().getBroadcast().getPushClient(HelloPushService.class)
            ))
            .withRemotePushClient(HelloPushService.class)
            .withRemoteEventClient(HelloEventService.class)
            .withPushModelData(HelloPushModelData.class)
            .build()
    );
    REMOTE_SERVER.get().open();
    
    // Stay open until enter a key
    System.console().readLine();
    
    REMOTE_SERVER.get().close();
  }
}
```
In this snipped we create the remote server via a builder, put in all interfaces we need on server side (_HelloPushService_ / _HelloEventService_ as client, _HelloControlServiceImpl_ as concrete implementation (no proxy), _HelloPushModelData_ for initialize _Remote Model_ of a new connected client) and open it. After all we wait until user press enter and we close the service.

```Java
public class ClientRunner {
  private static final AtomicReference<RemoteClient> REMOTE_CLIENT = new AtomicReference<>();
  private static final String[] NAMES = new String[] {
            "Justin", "Jenny", "Nico", "Alexa", "Caroline"
  };

  public static void main(String[] args) throws Exception {
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
    
    final HelloPushModel pushModel = REMOTE_CLIENT.get().getData().getRemotePushModel(HelloPushModel.class);
    final HelloPushObserver pushObserver = REMOTE_CLIENT.get().getData().getRemotePushObserver(HelloPushObserver.class);
    final HelloEventReceiver eventReceiver = REMOTE_CLIENT.get().getData().getRemoteEventReceiver(HelloEventReceiver.class);
    final HelloControlService controlClient = REMOTE_CLIENT.get().getControl().getControlClient(HelloControlService.class);
    
    pushObserver.addGreetingCountListener(() -> System.out.println("New count of greetings: " + pushModel.getGreetingCount()));
    eventReceiver.addGreetingListener(greeting -> System.out.println("Receive greetings: " + greeting));
    
    for (final String name : NAMES) {
        System.out.println("Send 'Say Hello' for " + name);
        controlClient.sayHello(name);

        Thread.sleep(1000);
    }
    
    // Stay open until enter a key
    System.console().readLine();
    
    REMOTE_CLIENT.get().close();
  }
}
```
This is the client implementation. We use a builder, too, and register all needed client side interfaces. Than we get the proxies to add observer / event listener and control the server. After the user press enter the client is closed.

In default JRemote use RMI.

Thats all.
