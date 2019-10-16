package server;

import org.json.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerMain {




    private ServerSocket serverSocket;
    private int port = 11000;
    private int version = 1;
    private GameController gameController;

    public ServerMain(){
        try {
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
        new Thread(new Runnable() {
            @Override
            public void run() {
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
            }
        }).start();
    }

    private void waitingForNewClients(){

        Socket client = null;
        while  (!serverSocket.isClosed()){
            try {
                client = serverSocket.accept();
                if (doHandshake(client)){
                    gameController.addPlayer(client);
                    newInputThread(gameController,client);
                }

            } catch (IOException e) {
                System.out.println("Error connecting with socket");
            }
        }
    }


    private boolean doHandshake(Socket client){
        BufferedWriter sender = null;
        BufferedReader reader = null;

        JSONObject recieved;
        JSONObject answer;

        String tempInput;

        try {
            sender = new BufferedWriter(new OutputStreamWriter((client.getOutputStream())));
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            return false;
        }


        if (reader == null || sender == null){
            return closeConnection(reader,sender);
        }

        try {
            wait(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            String tempAnswer = "";
            while ((tempInput = reader.readLine()) != null){
                tempAnswer = tempAnswer + tempInput;
            }
            recieved = new JSONObject(tempAnswer);
            if (recieved.getInt("version") != version){
                sender.append(new JSONStringer().object().key("ERROR").value("Wrong version").endObject().toString());
                sender.flush();
                client.close();
                return closeConnection(reader,sender);
            }
            sender.append(new JSONStringer().object().key("Status").value("OK").key("Version").value(1).toString());
            sender.flush();

            tempAnswer = "";

            while ((tempInput = reader.readLine()) != null){
                tempAnswer = tempAnswer + tempInput;
            }

            recieved = new JSONObject(tempAnswer);
            if (recieved.getString("Status").equals("OK")){
                System.out.println("New Socket connected \nIP-Adresse:" + client.getLocalAddress().getHostName() + "\nListenig for new Connections");
                return true;
            }
            return false;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException ex){
            return  closeConnection(reader, sender);
        }

        return false;

    }

    private boolean closeConnection(BufferedReader reader,BufferedWriter writer){
        try {
            reader.close();
            writer.close();
        } catch (IOException e){

        }
        return  false;
    }


    private void newInputThread(GameController gameController, Socket client){
        new Thread(new Runnable() {
            @Override
            public void run() {
                new InputListener(gameController, client);
            }
        }).start();
    }
}
