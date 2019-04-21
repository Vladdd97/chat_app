package sample;

import javafx.application.Platform;
import javafx.scene.control.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Controller {

    //const
    private final String CHAT = "chat";
    private final String CONNECT = "connect";
    private final String DISCONNECT = "disconnect";

    //app properties
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    Socket socket;
    private boolean isConnected = false;
    private String username;


    //javaFx controls
    public Button sendButton;
    public Button connectButton;
    public ListView chatListView;
    public TextArea messageTextArea;
    public TextField usernameTextField;
    public TextField serverTextField;
    public TextField portTextField;


    public void onClick_sendButton() {
        String message = messageTextArea.getText();
        messageTextArea.clear();
        try {
            dataOutputStream.writeUTF(username + ": " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void onClick_connectButton() {
//        username = usernameTextField.getText();
//        String address = serverTextField.getText();
//        int port = Integer.valueOf(portTextField.getText());
        username = "Daniel";
        String address = "127.0.0.1";
        int port = 8082;


        if (!isConnected) {
            try {
                socket = new Socket(address, port);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataOutputStream.writeUTF(username + " has connected");
                isConnected = true;

                Thread clientListener = new Thread(new ClientListener());
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

    public class ClientListener implements Runnable {
        @Override
        public void run() {
            String[] data;
            String message;

            try {
                while ((message = dataInputStream.readUTF()) != null) {
                    //comment
                    System.out.println("Received Message: " + message);
                    String finalMessage = message;
                    Platform.runLater(() -> chatListView.getItems().add(finalMessage));

//
//                    if (data[0].equals(CHAT))
//                    {
//                        Platform.runLater(() -> chatListView.getItems().add(sendMessage));
//
//                    }
//                    else if (data[0].equals(CONNECT))
//                    {
//                        Platform.runLater(() -> chatListView.getItems().add(sendMessage));
//                    }
//                    else if (data[0].equals(DISCONNECT))
//                    {
//                        Platform.runLater(() -> chatListView.getItems().add(sendMessage));
//                    }
                }
            } catch (Exception ex) {
            }
        }
    }


}
