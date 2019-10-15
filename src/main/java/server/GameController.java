package server;

import org.json.JSONException;
import org.json.JSONObject;

import java.beans.Expression;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameController {


    ArrayList<Socket> players;
    HashMap<String, Integer> stones;


    public GameController() {
        this.players = new ArrayList<>();
        stones = new HashMap<>();

        stones.put("red", 1);
        stones.put("blue", 1);
    }

    protected void addPlayer(Socket socket){
        players.add(socket);
    }


    private void sendMessage(String message, Socket reciever){
        try{
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(reciever.getOutputStream()));
            JSONObject toSend = new JSONObject(message);
            writer.append(toSend.toString());
            writer.flush();
            writer.close();

        } catch (JSONException e){

        } catch (IOException ex){

        }


    }


    protected synchronized void selectInput(JSONObject input, Socket sender){

        try {
            switch (input.getString("event")) {
                case "drawCard":
                    int type = input.getInt("type");
                    drawCard(type, sender);
                    break;
                case "moveStone":
                    moveStone(input.getInt("position"), input.getString("color"));
                    break;
                default:
                    System.out.println("Unknown event: " + input.get("Event"));
            }
        } catch (Exception e){
            System.out.println("Error with " + input.toString());
        }

    }

    private void moveStone(int newPosition, String color){

        stones.replace(color,newPosition);

        JSONObject answer = new JSONObject();
        answer.put("event", "moveStone");
        answer.put("position", stones.get(color));

        players.forEach(player -> sendMessage(answer.toString(),player));



    }

    private void drawCard(int type, Socket sender){

        String word;
        switch (type){
            case 3:
                word = "Apfelkuchen";
                break;
            case 4:
                word = "Käsekuchen";
                break;
            case 5:
                word = "Ananas";
                break;
            default:
                word = "ERROR";
        }

        JSONObject answer = new JSONObject();

        answer.put("event", "recieveCard");
        answer.put("word", word);

        sendMessage(answer.toString(),sender);



    }
}
