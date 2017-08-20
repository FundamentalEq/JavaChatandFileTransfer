# Chat and File Transfer Application
This application has following features:
* Client and server can send messages to each other using TCP socket.(half- duplex communication)
* Client and server can send and receive files using both TCP and UDP

## Steps to Run the Application
* Compile: <br>
`javac Alice.java` <br>
`javac Bob.java` <br>
* Run Alice and Bob in separate directories <br>
`java Alice`
`java Bob`
* Send message by simply typing the message when ">>" prompt appears
* Send file using TCP: `Sending filename TCP`
* Send file using UDP: `Sending filename UDP`

##### Note: Alice acts as server and Bob acts as client.
