package sample;


import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Controller {

    private final String CHAT = "chat";
    private final String CONNECT = "connect";
    private final String DISCONNECT = "disconnect";

    ArrayList<DataOutputStream> clientOutputStreams;
    ArrayList<String> clients;

    //javaFx controls
    public Button startButton;
    public ListView connectionInfoListView;

    //start server on a new thread
    public void onClick_startButton() {

        connectionInfoListView.getItems().add("start button was clicked");
        Thread startServer = new Thread(new StartServer());
        startServer.start();
        System.out.println("new thread!");
    }


    public class StartServer implements Runnable {
        @Override
        public void run() {
            clientOutputStreams = new ArrayList();
            clients = new ArrayList();

            try {
                ServerSocket serverSock = new ServerSocket(8082);

                Platform.runLater(() -> connectionInfoListView.getItems().add("Server Started... Waiting for connections"));


                while (true) {
                    //waiting till a new client will connect to server
                    Socket clientSock = serverSock.accept();
                    DataOutputStream dataOutputStream = new DataOutputStream(clientSock.getOutputStream());
                    clientOutputStreams.add(dataOutputStream);

                    Thread listener = new Thread(new ClientHandler(clientSock, dataOutputStream));
                    listener.start();


                    Platform.runLater(() -> connectionInfoListView.getItems().add("Got a connection"));

                }
            } catch (Exception ex) {
                Platform.runLater(() -> connectionInfoListView.getItems().add("Error making a connection."));

            }
        }
    }


    public class ClientHandler implements Runnable {
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;
        //BufferedReader reader;
        //Socket sock;
        //PrintWriter client;

        public ClientHandler(Socket clientSocket, DataOutputStream dataOutputStream) {
            // client = user;
            try {
                //sock = clientSocket;

                this.dataOutputStream = dataOutputStream;
                dataInputStream = new DataInputStream(clientSocket.getInputStream());
                //reader = new BufferedReader(isReader);
            } catch (Exception ex) {
                Platform.runLater(() -> connectionInfoListView.getItems().add("Unexpected error..."));
            }

        }

        @Override
        public void run() {
            String message;
            String[] data;

            try {
                while ((message = dataInputStream.readUTF()) != null) {
                    //comment
                    System.out.println("Received message: " + message);

                    String finalMessage = message;
                    Platform.runLater(() -> connectionInfoListView.getItems().add("Received: [" + finalMessage + "]\n"));


                    data = message.split(":");


                    if (data[0].equalsIgnoreCase(CONNECT)) {
                        sendMessageToEveryone(data[1]);
                    } else if (data[0].equalsIgnoreCase(DISCONNECT)) {
                        sendMessageToEveryone(data[1]);
                    } else if (data[0].equalsIgnoreCase(CHAT)) {
                        sendMessageToEveryone(data[1]);
                    } else {
                        Platform.runLater(() -> connectionInfoListView.getItems().add("No Conditions were met. \n"));
                    }

                }
            } catch (Exception ex) {
                //ta_chat.append("Lost a connection. \n");
                ex.printStackTrace();
                clientOutputStreams.remove(dataOutputStream);
            }
        }
    }

    public void sendMessageToEveryone(String message)
    {
        Iterator it = clientOutputStreams.iterator();

        while (it.hasNext())
        {
            try
            {
                DataOutputStream dataOutputStream = (DataOutputStream) it.next();
                dataOutputStream.writeUTF(message);
                Platform.runLater(() -> connectionInfoListView.getItems().add("Sending: [" + message + "]"));
                dataOutputStream.flush();
            }
            catch (Exception ex)
            {
                Platform.runLater(() -> connectionInfoListView.getItems().add("Error telling everyone"));
            }
        }
    }



}
