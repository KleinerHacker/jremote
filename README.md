# jremote
Remote API and IMPL for Java based on REST with multi client support, push support and more

# Overview
This API provides a client and a server implementation to communicate between clients and server asynchronious. 
The __client__ gets one or more control services to control the server behavior or functions. Additional a client contains one or more 
remote models (here the received pushed data are stored) and one or more optional remote observers (contains all listener for model
change activities). For event handling there are one or more _Remote Event Receivers_ (contains all event listener for event calls). 
The __server__ gets one or more push or event services to call all clients are connected. 

Internal a client is registered in server (via a _registration service_) and check its own connection via a _keep alive service_ so 
if the client is disconnected from server it try reconnect automatically. So if you open the client he waits automatically for service.
