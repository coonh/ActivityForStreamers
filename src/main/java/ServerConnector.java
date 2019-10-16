import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.Buffer;

public class ServerConnector {

    Socket me;

    private static ServerConnector instance;



    private ServerConnector(){
    }


    public static ServerConnector getInstance(){
        if (ServerConnector.instance == null){
            ServerConnector.instance = new ServerConnector();
        }
        return ServerConnector.instance;
    }


    public void sendMessage(String message){

    }

    private void startRecieving(){
        new Thread(new Runnable() {



            @Override
            public void run() {

                String message;
                BufferedReader reader;
                try {
                    reader = new BufferedReader(new InputStreamReader(me.getInputStream()));


                    while (me.isConnected()) {
                        if ((message = reader.readLine()) != null) {
                            JSONObject input = new JSONObject(message);

                            switch (input.getString("event")) {
                                case "moveStone":

                                    //ToDo hier stein bewegen
                                    break;
                                case "drawCard":

                                    //ToDo hier kommt die Karte an
                                    break;
                                default:
                                    System.out.println("Unknown type " + input.getString("event"));
                            }
                        }


                    }
                } catch (IOException e) {
                    return;
                } catch (JSONException ex) {

                }
            }

        }).start();

    }

    private boolean doHandshake(){
        // Hier kommt der Handshake rein
    }


    public boolean connectToServer(String IpAddress, int portNumber){
        try {
            me = new Socket(IpAddress,portNumber);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return doHandshake();

    }
}
