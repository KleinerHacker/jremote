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
At the end we need an implementation for the control service to push the greeting to all clients. In this case we need a supplier cause the proxy is not created if this implementation class is initialized. See below.

```Java
public class ServerRunner {
  public static void main(String[] args) {
    final RemoteServer remoteServer = RemoteServerBuilder.create("localhost", 9998)
                .withPushClient(HelloPushService.class)
                .withRemoteControlService(new HelloControlServiceImpl(
                        () -> remoteServer.getBroadcast().getPushClient(HelloPushService.class)
                ))
                .withPushModelData(HelloModelData.class)
                .build();
    
    remoteServer.open();
    
    // Stay open until enter a key
    System.console().readLine();
    
    remoteServer.close();
  }
}
```
In this snipped we create the remote server via a builder, put in all interfaces we need on server side (_HelloPushService_ as client, _HelloControlServiceImpl_ as conrete implementation (no proxy), _HelloModelData_ for initialize _Remote Model_ of a new connected client) and open it. After all we wait until user press enter and we close the service.

```Java
public class ClientRunner {
  public static void main(String[] args) throws Exception {
    final RemoteClient remoteClient = RemoteClientBuilder.create("localhost", 9998, 9999)
                .withRemotePushModel(HelloModel.class)
                .withRemotePushObserver(HelloObserver.class)
                .withRemotePushService(HelloPushService.class)
                .withRemoteControlClient(HelloControlService.class)
                .build();
                
    remoteClient.open();
    
    Thread.sleep(1500);
    
    final HelloModel remotePushModel = remoteClient.getData().getRemotePushModel(HelloModel.class);
    final HelloObserver remotePushObserver = remoteClient.getData().getRemotePushObserver(HelloObserver.class);
    final HelloControlService controlClient = remoteClient.getControl().getControlClient(HelloControlService.class);
    
    //Register for greetings
    remotePushObserver.addGreetingListener(() -> System.out.println("New Greeting: " + remotePushModel.getGreeting()));
    
    //Print current greeting
    System.out.println("Current Greeting: " + remotePushModel.getGreeting());
    
    //Control server
    controlClient.sayHello("everybody");
    
    // Stay open until enter a key
    System.console().readLine();
    
    remoteClient.close();
  }
}
```
This is the client implementation. We use a builder, too, and register all needed client side interfaces. Than we get the proxies to add observer listener and control the server. After the user press enter the client is closed.

In default JRemote use RMI.

Thats all.
