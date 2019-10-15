package server;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class InputListener {

    GameController gameController;
    Socket me;

    protected InputListener(GameController gameController, Socket me){

        this.gameController = gameController;
        this.me = me;
    }

    private void listening(){
        BufferedReader reader;
        String message = "";

        try{
            reader = new BufferedReader(new InputStreamReader(me.getInputStream()));
        } catch (IOException e){
            System.out.println("Error with socket from IP: " +me.getInetAddress() );
            return;
        }

        while(me.isConnected()){
            try{

                if ((message = reader.readLine()) != null){


                    JSONObject input = new JSONObject(message);
                }
            } catch (IOException e){

            } catch (JSONException ex){
                System.out.println("Error building JSON from: " + message);
            }


        }



    }

    private void selection(JSONObject input ){

    }

}
