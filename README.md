Com S 309
Bomberman
Team E1

Landon Schropp
Qiyu (Felix) Zhao
Daniel Brouwer
Sen Tan

ABOUT THIS PROJECT

This project fulfills the group project requirement for Com S 309.  It consists of two applications, a client and a server.  The server hosts a game of Bomberman and allows multiple clients to connect simultaneously over an internet connection.  The client graphically displays the game to the user and handles input.

Much though has been put into the project design.  It is organized into three main packages: client, server and shared.  For more information on the design of the project, please read our group's submitted design documents.

Our group practiced test driven development which implementing this project to much success.  The code has been thoroughly tests.  We are proud to say we've written a total of 692 individual unit tests. Many of the tests are highly complex (for example CommunicatorTest and GameTest), and utilize the mock frameworks Mockito and PowerMock to isolate individual class components.

BUILDING AND RUNNING THE PROJECT

The simplest way to interact with the project is to import it into eclipse.  The entry main method for the client is located at client.Main and the entry main method for the server is located at server.Main.  To launch the application, simply open up these files and click the play button on each.

The server must begin hosting an internet connection before a client may connect.  To do this, in the server application click on the "Host Game" button.  To connect to the server in the  client application, click on the "Multiplayer" button, enter the address of the server to connect to, and then click "Join".  When all of the desired players are connected, in the server application click on "Start Game" to start the game.  The clients may then move around using the arrow keys and can place bombs by pressing space.
