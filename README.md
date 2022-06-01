# Travel proposal application and RESTful API
 A RESTful API & a Java application

---

## Description

#### Technologies used

- Java
- The Jersey RESTful Web Services framework
- API
- HTTP Authentication
- RabbitMQ
- Tomcat

<br/>

This project contains a Java travel proposal application supported by a RESTful API which allows users to post, query travel proposals and submit travel intents to the trips' proposer.

<p align="center">
  <img src="https://github.com/Anth0nyK/SCCC-RESTful/blob/main/README_Images/1.png" width="350" title="App structure">
</p>

The RESTful web service was built on Java using the Jersey framework running on local Tomcat server and connected to 2 external web APIs (RANDOM.ORG and World Weather Online) and a RabbitMQ server. It has 7 methods exposed as REST methods for external clients to consume.

<p align="center">
  <img src="https://github.com/Anth0nyK/SCCC-RESTful/blob/main/README_Images/2.png" width="550" title="API structure">
</p>

2 of the methods are related to account registration and login. The user can use the **account/PUT** method to create a new account to use the web service and they can use the **login/POST** method to verify their credential.  The login method should be used on the official client where the user can login in the client. As the main methods of the web service requires HTTP Basic authentication, If the credential is correct, the client will save the credential and the login status for the user, so that they do not need to keep identifying themselves for every method calls.
The other 4 methods are related to the main functionalities of the web service. These 4 methods require HTTP Basic authentication. These 4 methods are: **proposal/POST**, **proposal/GET**, **intent/POST** and **intent/GET**. 

For the **proposal/POST** method, it allows the user to submit trip proposal message. The web service will check if the message contains the needed and correct data. If so, the system will call the random number generator API to generate a message ID for that message and call the weather API to get the weather data for that place in that date. After that, the completed message will be pushed to the fanout exchange in the RabbitMQ server, so every user can consume it. 

For the **proposal/GET** method, it only consumes the “userID” as a parameter. It allows the user to get the proposal which related to them by consuming their own fanout queue which bound to the fanout exchange. 

For the **intent/POST** method, it allows the user to send a travel intent message to a specific user. The system will check if the message contains the needed and correct data. For example, checking if the receiver is existed. After that, the system will call the random number generator API to generate a message ID for the message. Then, the system will send the travel intent to the receiver’s queue in RabbitMQ which bound to the direct exchange, so that only the specified receiver can receive the message. 

For the **intent/GET** method, it only consumes “userID” as a parameter. It allows the user to get the intent message which related to them by consuming to their own direct message queue which bound to the direct exchange in the RabbitMQ.

The last method is a “admin” only method. After doing the HTTP Basic authentication, the system will be able to tell the role of the user. If the user’s role is “admin”, they will be able to use the **account/DELETE** method which will delete the user data from the system with the username specified.

<p align="center">
  <img src="https://github.com/Anth0nyK/SCCC-RESTful/blob/main/README_Images/3.png" width="550" title="design diagram">
</p>
<p align="center">REST web service design diagram</p>

[Back To The Top](#travel-proposal-application-restful-api)

---
