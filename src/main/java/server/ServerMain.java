package server;

import org.json.*;
import webcam.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class ServerMain {




    private ServerSocket serverSocket;
    private static int port = 11000;
    private int version = 1;
    private GameController gameController;


    public static void main(String[] args){
        System.out.println("Server is starting");
        new webcam.Server();
        new ServerMain();

    }

    public ServerMain(){
        try {
            gameController = new GameController();
            serverSocket = new ServerSocket(port);
            startScanner();
            waitingForNewClients();

        } catch (IOException e) {
            System.out.println("Error creating Server socket");
            System.out.println("Server shutdown");
            System.exit(0);
        }



    }


    private void startScanner(){
        new Thread(() -> {
            String input;
            Scanner scanner = new Scanner(System.in);
            while(true){
                input = scanner.next();
                switch (input){
                    case "quit":
                        System.exit(0);
                    default:
                        System.out.println("Unknown command: " + input);
                }
            }
        }).start();
    }

    private void waitingForNewClients(){

        Socket client;
        while  (!serverSocket.isClosed()){
            try {
                System.out.println("Server is started");
                client = serverSocket.accept();
                System.out.println("New Server socket: " + client.getInetAddress().toString());
                newInputThread(gameController,client);


            } catch (IOException e) {
                System.out.println("Error connecting with socket");
            }
        }
    }



    private void newInputThread(GameController gameController, Socket client){
        new Thread(() -> {
            System.out.println("Start listening to new client");
            new InputListener(gameController, client);
        }).start();
    }

    public static int getPort() {
        return port;
    }
}
