# JRemote
Remote API and IMPL for Java based on REST with multi client support, push support and more

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
@RemoteControlService
public interface HelloServerControlService {
  @Control
  void sayHello(String name);
}
```
This is the control interface to control the service. We want to instruct the server to say _Hello_ with a specified name.

```Java
@RemotePushModel
public interface HelloModel {
  @PushModelProperty("greeting")
  String getGreeting();
}
```
Now we define a _Remote Model_ where the data from server push is stored into. We store the greeting of the server here.

```Java
@RemotePushObserver
public interface HelloObserver {
  @PushObserverListener(modelClass = HelloModel.class, property = "greeting")
  void addGreetingListener(PushChangedListener listener);
  
  @PushObserverListener(modelClass = HelloModel.class, property = "greeting")
  void removeGreetingListener(PushChangedListener listener);
```
New we define a _Remote Observer_ to listen that the server send greeting to us. The detection for _add_ or _remove_ listener runs
automatically via prefix _add_ or _remove_. The annotation need the reference to the _Remote Model_ and the property to listen.

```Java
@RemotePushService
public interface HelloPushService  {
  @Push(modelClass = HelloModel.class, property = "greeting")
  void pushGreeting(String greeting);
}
```
This interface is used for the server to push greetings to the connected clients. The greeting is pushed into the client remote model
with name _HelloModel_ into property with name _greeting_.
