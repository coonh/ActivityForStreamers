package server;

import org.json.JSONException;
import org.json.JSONObject;

import java.beans.Expression;
import java.io.*;
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

        stones.put("red", 0);
        stones.put("blue", 0);
    }

    protected void addPlayer(Socket socket){
        players.add(socket);

        for (String color: stones.keySet()) {
            moveStone(0,color);
        }
    }


    private void sendMessage(String message, Socket reciever){
        try{
            PrintWriter writer = new PrintWriter(reciever.getOutputStream());
            JSONObject toSend = new JSONObject(message);
            writer.println(toSend.toString());
            writer.flush();
            System.out.println("Sended message: " + toSend.toString() + " TO " + reciever.getInetAddress().toString());

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

                case "DrawingWindow":
                    handleDrawingEvent(input,sender);
                    break;
                default:
                    System.out.println("Unknown event: " + input.get("Event"));
            }
        } catch (Exception e){
            System.out.println("Error with " + input.toString());
        }

    }

    private void moveStone(int newPosition, String color){


        if (newPosition != -2) stones.replace(color,newPosition);

        JSONObject answer = new JSONObject();
        answer.put("event", "moveStone");
        answer.put("position", stones.get(color));
        answer.put("color", color);

        players.forEach(player -> sendMessage(answer.toString(),player));



    }

    private void drawCard(int type, Socket sender){
        String word;
        int value;
        switch (type){
            case 3:
                System.out.println("Bin in der 3");
                value = 3;
                word = "Apfelkuchen";
                break;
            case 4:
                value = 4;
                word = "Käsekuchen";
                break;
            case 5:
                value = 5;
                word = "Ananas";
                break;
            default:
                value = -1;
                word = "ERROR";
        }

        JSONObject answer = new JSONObject();

        answer.put("event", "recieveCard");
        answer.put("word", word);
        answer.put("value",value);

        sendMessage(answer.toString(),sender);

    }


    private void handleDrawingEvent(JSONObject event, Socket sender){
        switch (event.getString("action")){
            case "open":
                openDrawingWindow(sender);
                break;
            case "close":
                closeDrawingWindow();
                break;
            case "beginPath":
                beginPathDrawingWindow(
                        event.getDouble("x"),
                        event.getDouble("y"),
                        event.getDouble("thickness"),
                        event.getString("color")
                );
                break;
            case "endPath":
                endPathDrawingWindow();
                break;
            case "drawLine":
                drawLine(
                        event.getDouble("x"),
                        event.getDouble("y")
                );
                break;
            default:
                System.out.println("Unknown drawing action: " +event.getString("action"));
        }

    }


    private void openDrawingWindow(Socket sender){
        JSONObject answerOne = new JSONObject();

        answerOne.put("event", "openDrawingWindow");
        answerOne.put("active", false);
        answerOne.put("action", "open");

        JSONObject answerTwo = new JSONObject();
        answerTwo.put("event", "openDrawingWindow");
        answerTwo.put("active", true);
        answerTwo.put("action", "open");

        for (Socket reciever: players) {
            if (reciever.equals(sender)) sendMessage(answerTwo.toString(),reciever);
            else sendMessage(answerOne.toString(),reciever);
        }
    }

    private void closeDrawingWindow(){
        JSONObject answer= new JSONObject();

        answer.put("event", "DrawingWindow");
        answer.put("action", "close");

        players.forEach(player -> sendMessage(answer.toString(),player));
    }

    private void beginPathDrawingWindow(double x, double y, double thickness, String color){

        JSONObject answer = new JSONObject();

        answer.put("event", "DrawingWindow");
        answer.put("action", "beginPath");
        answer.put("x", x);
        answer.put("y", y);
        answer.put("thickness", thickness);
        answer.put("color", color);

        players.forEach(player -> sendMessage(answer.toString(),player));
    }

    private void endPathDrawingWindow(){

        JSONObject answer = new JSONObject();

        answer.put("event", "DrawingWindow");
        answer.put("action", "endPath");

        players.forEach(player -> sendMessage(answer.toString(),player));
    }

    private void drawLine(double x, double y){

        JSONObject answer = new JSONObject();

        answer.put("event", "DrawingWindow");
        answer.put("action", "drawLine");
        answer.put("x", x);
        answer.put("y", y);

        players.forEach(player -> sendMessage(answer.toString(),player));
    }


}
