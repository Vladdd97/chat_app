package sample;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws Exception {
        Socket clientSocket = new Socket("127.0.0.1", 8082);
        if (clientSocket.isConnected()) {
            System.out.println("Connected to server");
        }
        Scanner input = new Scanner(System.in);
        String message = "Start Chat...........";
        DataOutputStream dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
        dataOutputStream.writeUTF(message);
        System.out.println(message);
        DataInputStream din = new DataInputStream(clientSocket.getInputStream());
        while (true) {
            System.out.print("Client:\t");
            message = input.nextLine();
            dataOutputStream.writeUTF(message);
        }
    }
}