import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

class ServerConnector {

    private Socket me;

    private static ServerConnector instance;
    private PrintWriter writer;
    private BufferedReader reader;

    private Gameboard gameboard;





    private ServerConnector(){
    }


    static ServerConnector getInstance(){
        if (ServerConnector.instance == null){
            ServerConnector.instance = new ServerConnector();
        }
        return ServerConnector.instance;
    }


    void sendMessage(String message){
            writer.println(message);
            writer.flush();
    }

    void startRecieving(){
        new Thread(() -> {

            System.out.println("Start recieving");

            String message;
            try {


                while (me.isConnected()) {
                    if ((message = reader.readLine()) != null) {
                        JSONObject input = new JSONObject(message);
                        System.out.println("Recieved message: " + input.toString());

                        switch (input.getString("event")) {
                            case "moveStone":

                                gameboard.placeStone(input.getString("color"), input.getInt("position"));

                                break;
                            case "recieveCard":
                                gameboard.drawCard(input.getString("word"), input.getInt("value"));

                                break;
                            default:
                                System.out.println("Unknown type " + input.getString("event"));
                        }
                    }


                }
            } catch (IOException e) {
                System.err.println("Error listening");

            } catch (JSONException ex) {
                System.err.println("Error listening json");
            }
        }).start();

    }

    private boolean doHandshake(){
        // Hier kommt der Handshake rein


        String answer = "ERROR";

        try {

            String message = new JSONStringer().object().key("version").value(1).endObject().toString();
            System.out.println("Sending message: " + message);

            sendMessage(message);




            System.out.println("Waiting for answer of server");
            if ((answer = reader.readLine())!= null){

                System.out.println("Recieving server answer: " + answer);

                if ( new JSONObject(answer).getString("status").equals("OK")){
                    sendMessage(new JSONObject(answer).toString());

                }
            }



        } catch (IOException e) {
            System.err.println("IO EXCEPTION");
            e.printStackTrace();
            return false;
        } catch (JSONException jsonException){
            System.err.println("Error connecting to Server");
            System.err.println(answer);
            return  false;
        }


        return true;
    }


    boolean connectToServer(String IpAddress, int portNumber){
        try {
            me = new Socket(IpAddress,portNumber);
            reader = new BufferedReader(new InputStreamReader(me.getInputStream()));
            writer = new PrintWriter(me.getOutputStream());

        } catch (IOException e) {
            System.err.println("Host with " + IpAddress + " is not online or doesn't even exist");
            return false;
        }

        return doHandshake();

    }

    void setGameboard(Gameboard gameboard) {
        this.gameboard = gameboard;
    }
}
