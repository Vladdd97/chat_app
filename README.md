# Laboratory work nr.5 (PR)  
## Task: create a chat app in which you will use sockets and TCP protocols 
  
## What is socket?
A **socket** is one endpoint of a two-way communication link between two programs running on the network. A socket is bound to a port number so that the TCP layer can identify the application that data is destined to be sent to.

Normally, a server runs on a specific computer and has a socket that is bound to a specific port number. The server just waits, listening to the socket for a client to make a connection request.

On the client-side: The client knows the hostname of the machine on which the server is running and the port number on which the server is listening. To make a connection request, the client tries to rendezvous with the server on the server's machine and port. The client also needs to identify itself to the server so it binds to a local port number that it will use during this connection. This is usually assigned by the system.


![image](https://user-images.githubusercontent.com/29525730/56489685-2efdfc00-64eb-11e9-845b-3d63a85ba7a1.png)
If everything goes well, the server accepts the connection. Upon acceptance, the server gets a new socket bound to the same local port and also has its remote endpoint set to the address and port of the client. It needs a new socket so that it can continue to listen to the original socket for connection requests while tending to the needs of the connected client.

![image](https://user-images.githubusercontent.com/29525730/56489767-7c7a6900-64eb-11e9-9548-9d7087c45855.png)
On the client side, if the connection is accepted, a socket is successfully created and the client can use the socket to communicate with the server.
The client and server can now communicate by writing to or reading from their sockets.

## TCP (from wikipedia)
The **Transmission Control Protocol** (**TCP**) is one of the main [protocols](https://en.wikipedia.org/wiki/Communications_protocol "Communications protocol") of the [Internet protocol suite](https://en.wikipedia.org/wiki/Internet_protocol_suite "Internet protocol suite"). It originated in the initial network implementation in which it complemented the [Internet Protocol](https://en.wikipedia.org/wiki/Internet_Protocol "Internet Protocol") (IP). Therefore, the entire suite is commonly referred to as _TCP/IP_. TCP provides [reliable](https://en.wikipedia.org/wiki/Reliability_(computer_networking) "Reliability (computer networking)"), ordered, and [error-checked](https://en.wikipedia.org/wiki/Error_detection_and_correction "Error detection and correction") delivery of a stream of [octets](https://en.wikipedia.org/wiki/Octet_(computing) "Octet (computing)") (bytes) between applications running on hosts communicating via an IP network. Major internet applications such as the [World Wide Web](https://en.wikipedia.org/wiki/World_Wide_Web "World Wide Web"), [email](https://en.wikipedia.org/wiki/Email "Email"), [remote administration](https://en.wikipedia.org/wiki/Remote_administration "Remote administration"), and [file transfer](https://en.wikipedia.org/wiki/File_transfer "File transfer") rely on TCP. Applications that do not require reliable data stream service may use the [User Datagram Protocol](https://en.wikipedia.org/wiki/User_Datagram_Protocol "User Datagram Protocol") (UDP), which provides a [connectionless](https://en.wikipedia.org/wiki/Connectionless_communication "Connectionless communication")  [datagram](https://en.wikipedia.org/wiki/Datagram "Datagram") service that emphasizes reduced [latency](https://en.wikipedia.org/wiki/Latency_(engineering) "Latency (engineering)") over reliability.

## Task Implementation
For this tasks I created **chat_server** and **chat_client** projects.
**chat_server** - will receive messages and will redirect them to  all clients which are connected to server.
**chat_client** - will send messages to server and will receive messages from server.


**chat_server**

![chat_server](https://user-images.githubusercontent.com/29525730/56491150-307df300-64f0-11e9-9e62-029e924bfba1.PNG)


**chat_client**
![chat_client](https://user-images.githubusercontent.com/29525730/56491177-4db2c180-64f0-11e9-936b-2d39431761a4.PNG)

