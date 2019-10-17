package server;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class InputListener {

    GameController gameController;
    Socket me;
    PrintWriter sender = null;
    BufferedReader reader = null;
    private int version = 1;

    protected InputListener(GameController gameController, Socket me){

        this.gameController = gameController;
        this.me = me;
        System.out.println("Listener is now listening");
        if (doHandshake(me)) {
            gameController.addPlayer(me);
            listening();

        }
    }

    private void listening(){
        String message = "";

        System.out.println(me.isClosed());

        while(!me.isClosed()){
            try{

                if ((message = reader.readLine()) != null){
                    System.out.println("Recieving message: " + message);


                    JSONObject input = new JSONObject(message);
                    gameController.selectInput(input,me);
                }
            } catch (IOException e){
                System.err.println("error while listening");
                e.printStackTrace();
                return;
            } catch (JSONException ex){
                System.out.println("Error building JSON from: " + message);
            }


        }



    }


    private boolean doHandshake(Socket client){


        JSONObject recieved;
        JSONObject answer;

        String tempInput;

        System.out.println("Starting Handshake");

        try {
            sender = new PrintWriter(client.getOutputStream());
            reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
        } catch (IOException e) {
            return false;
        }


        if (reader == null || sender == null){
            return closeConnection(reader,sender);
        }

        try {
            String tempAnswer = "";
            if ((tempInput = reader.readLine()) != null){
                tempAnswer = tempAnswer + tempInput;
                System.out.println("Recieving answer: " + tempAnswer);
            }
            recieved = new JSONObject(tempAnswer);
            if (recieved.getInt("version") != version){
                sender.println(new JSONStringer().object().key("ERROR").value("Wrong version").endObject().toString());
                sender.flush();
                client.close();
                System.err.println("Wrong protokoll version");
                return closeConnection(reader,sender);
            }
            System.out.println("Good Version");
            tempAnswer = new JSONStringer().object().key("status").value("OK").key("version").value(1).endObject().toString();
            System.out.println("Sending answer: " + tempAnswer);

            sender.println(tempAnswer);
            sender.flush();
            tempAnswer = null;


            if((tempAnswer = reader.readLine())!= null) {
                System.out.println(tempAnswer);
            }


            recieved = new JSONObject(tempAnswer);
            if (recieved.getString("status").equals("OK")){
                System.out.println("New Socket connected \nIP-Adresse:" + client.getLocalAddress().getHostName() + "\nListenig for new Connections");
                return true;
            }
            return false;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException ex){
            System.err.println("Cant get JSONObject");
            return  closeConnection(reader, sender);
        }

        return false;

    }

    private boolean closeConnection(BufferedReader reader,PrintWriter writer){
        try {
            reader.close();
            writer.close();
        } catch (IOException e){

        }
        System.out.println("Closing connection");
        return  false;
    }


}
