package sample;


import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class Controller {

    //const
    private final String CHAT = "chat";
    private final String CONNECT = "connect";
    private final String DISCONNECT = "disconnect";

    //app proprieties
    private ArrayList<DataOutputStream> clientOutputStreams;
    private ArrayList<Thread> clientConnections;
    private boolean isServerStarted = false;

    //javaFx controls
    public Button startButton;
    public ListView connectionInfoListView;


    public void onClick_startButton() {

        connectionInfoListView.getItems().add("start button was clicked");
        if (!isServerStarted) {
            Thread serverListener = new Thread(new ServerListener());
            serverListener.start();
            isServerStarted = true;
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Server is started!");
            alert.setHeaderText("Server is already started!!!");
            alert.showAndWait();
        }
    }


    public class ServerListener implements Runnable {
        @Override
        public void run() {
            clientOutputStreams = new ArrayList();
            clientConnections = new ArrayList<>();

            try {
                ServerSocket serverSock = new ServerSocket(8082);

                Platform.runLater(() -> connectionInfoListView.getItems().add("Server Started... Waiting for connections"));


                while (true) {
                    //waiting till a new client will connect to server
                    Socket clientSock = serverSock.accept();
                    DataInputStream dataInputStream = new DataInputStream(clientSock.getInputStream());
                    DataOutputStream dataOutputStream = new DataOutputStream(clientSock.getOutputStream());

                    Thread clientConnection = new Thread(new ClientConnection(dataInputStream, dataOutputStream));
                    clientConnection.start();

                    clientOutputStreams.add(dataOutputStream);
                    clientConnections.add(clientConnection);

                    Platform.runLater(() -> connectionInfoListView.getItems().add("Got a new connection"));

                }
            } catch (Exception ex) {
                Platform.runLater(() -> connectionInfoListView.getItems().add("Error making a connection."));

            }
        }
    }


    public class ClientConnection implements Runnable {
        DataInputStream dataInputStream;
        DataOutputStream dataOutputStream;


        public ClientConnection(DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
            this.dataOutputStream = dataOutputStream;
            this.dataInputStream = dataInputStream;
        }

        @Override
        public void run() {
            String message;
            String[] receivedData;

            try {
                while (isServerStarted) {
                    System.out.println("while is running");
                    message = dataInputStream.readUTF();
                    //comment
                    System.out.println("Received message: [" + message + "]");

                    String finalMessage = message;
                    Platform.runLater(() -> connectionInfoListView.getItems().add("Received: [" + finalMessage + "]\n"));

                    receivedData = message.split("::");

                    if (receivedData[0].equalsIgnoreCase(CONNECT)) {
                        sendMessageToEveryone(receivedData[1]);
                    } else if (receivedData[0].equalsIgnoreCase(DISCONNECT)) {
                        sendMessageToEveryone(receivedData[1]);
                        closeClientConnection(dataOutputStream);
                        break;
                    } else if (receivedData[0].equalsIgnoreCase(CHAT)) {
                        sendMessageToEveryone(receivedData[1]);
                    } else {
                        Platform.runLater(() -> connectionInfoListView.getItems().add("No Conditions were met. \n"));
                    }

                }
            } catch (Exception ex) {
                ex.printStackTrace();
                closeClientConnection(dataOutputStream);
            }
        }
    }

    public void closeClientConnection(DataOutputStream dataOutputStream) {

        for (int i = 0; i < clientOutputStreams.size(); i++) {
            if (clientOutputStreams.get(i).equals(dataOutputStream)) {
                clientOutputStreams.remove(i);
                clientConnections.get(i).stop();
                clientConnections.remove(i);
            }
        }
    }

    public void sendMessageToEveryone(String message) {
        Iterator it = clientOutputStreams.iterator();

        while (it.hasNext()) {
            try {
                DataOutputStream dataOutputStream = (DataOutputStream) it.next();
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
                Platform.runLater(() -> connectionInfoListView.getItems().add("Sending: [" + message + "]"));
            } catch (Exception ex) {
                Platform.runLater(() -> connectionInfoListView.getItems().add("Error Sending Message To Everyone"));
            }
        }
    }


}
