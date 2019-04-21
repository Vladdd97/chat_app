package sample;

import javafx.application.Platform;
import javafx.scene.control.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Controller {

    //const
    private final String CHAT = "CHAT";
    private final String CONNECT = "CONNECT";
    private final String DISCONNECT = "DISCONNECT";

    //app properties
    private Thread clientListener;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Socket socket;
    private boolean isClientConnected = false;
    private String username;


    //javaFx controls
    public Button sendButton;
    public Button connectButton;
    public Button disconnectButton;
    public ListView chatListView;
    public TextArea messageTextArea;
    public TextField usernameTextField;
    public TextField serverTextField;
    public TextField portTextField;


    public void onClick_sendButton() {
        if (isClientConnected) {
            String message = messageTextArea.getText();
            messageTextArea.clear();
            try {
                dataOutputStream.writeUTF(CHAT + "::" + username + ": " + message);
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No connection");
            alert.setHeaderText("Wait a minute...");
            alert.setContentText("You have to be connected to the server in order to send messages");
            alert.showAndWait();
        }

    }


    public void onClick_connectButton() {
        username = usernameTextField.getText();
        String address = serverTextField.getText();
        int port = Integer.valueOf(portTextField.getText());


        if (!isClientConnected) {
            try {
                socket = new Socket(address, port);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(CONNECT + "::" + username + " has connected...");
                dataOutputStream.flush();
                isClientConnected = true;
                usernameTextField.setEditable(false);

                clientListener = new Thread(new ClientListener());
                clientListener.start();
            } catch (IOException e) {
                e.printStackTrace();
            }


        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("You are already connected");
            alert.setHeaderText("Wait a minute...");
            alert.setContentText("You are already connected!!!");
            alert.showAndWait();
        }

    }

    public void onClick_disconnectButton() {

        if (isClientConnected) {
            try {
                dataOutputStream.writeUTF(DISCONNECT + "::" + username + ": has disconnected");
                dataOutputStream.flush();
                chatListView.getItems().add(username + ": has disconnected");
                socket.close();
                clientListener.stop();
                isClientConnected = false;
                usernameTextField.setEditable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("You are already disconnected");
            alert.setHeaderText("Wait a minute...");
            alert.setContentText("You are already disconnected!!!");
            alert.showAndWait();
        }
    }

    public class ClientListener implements Runnable {
        @Override
        public void run() {
            String message;

            try {
                while (isClientConnected) {
                    message = dataInputStream.readUTF();
                    //comment
                    System.out.println("Received Message: " + message);
                    String finalMessage = message;
                    Platform.runLater(() -> chatListView.getItems().add(finalMessage));

                }
            } catch (Exception ex) {
            }
        }
    }


}
