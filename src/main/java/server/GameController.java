package server;

import org.json.JSONException;
import org.json.JSONObject;

import java.beans.Expression;
import java.io.*;
import java.net.Socket;
import java.util.*;

public class GameController {


    ArrayList<Socket> players;
    HashMap<String, Integer> stones;

    boolean timerRunning;
    boolean isDrawing;


    public GameController() {
        this.players = new ArrayList<>();
        stones = new HashMap<>();

        timerRunning = false;
        isDrawing = false;

        WordReader.getInstance();

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
            System.out.println("Sent message: " + toSend.toString() + " TO " + reciever.getInetAddress().toString());

        } catch (JSONException e){

        } catch (IOException ex){

        }


    }


    protected synchronized void selectInput(JSONObject input, Socket sender){

        try {
            switch (input.getString("event")) {
                case "drawCard":
                    int type = input.getInt("type");
                    //draw Card for player red
                    drawCard(type, sender,stones.get("red"));
                    //draw card for play

                    break;
                case "moveStone":
                    moveStone(input.getInt("position"), input.getString("color"));
                    break;

                case "DrawingWindow":
                    handleDrawingEvent(input,sender);
                    break;
                case "timer":
                    startTimer();
                    break;
                case "generateTeams":
                    generateTeams();
                    break;
                case "reset":
                    WordReader.getInstance().reset();
                    break;
                case "addword":
                    WordReader.getInstance().addWord(input.getString("activity"),input.getInt("value"),input.getString("word"));
                    break;
                default:
                    System.out.println("Unknown event: " + input.get("Event"));
            }
        } catch (Exception e){
            System.out.println("Error with " + input.toString());
        }

    }

    private void generateTeams() {
        ArrayList<Integer> w = new ArrayList<>();
        for(int i=0;i<6;i++){
            w.add(i);
        }
        Collections.shuffle(w);
        JSONObject answer = new JSONObject();
        answer.put("event","newTeams");
        answer.put("value1",w.get(0));
        answer.put("value2",w.get(1));
        answer.put("value3",w.get(2));

        players.forEach(player -> sendMessage(answer.toString(),player));

    }

    private void startTimer() {
        if(timerRunning){
            return;
        }else {
            timerRunning = true;
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                int sec = 60;

                @Override
                public void run() {
                    JSONObject answer = new JSONObject();
                    answer.put("event", "receiveTimer");
                    answer.put("sec", sec);


                    players.forEach(player -> sendMessage(answer.toString(), player));

                    if (sec <= 0){
                        timer.cancel();
                        timerRunning = false;
                    }
                    sec--;

                }
            };
            timer.scheduleAtFixedRate(task, 0, 1000);
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

    private void drawCard(int type, Socket sender, int playerPosition){
        String word1;
        String word2;


        word1 = WordReader.getInstance().getWord(stones.get("red"),type);
        word2 = WordReader.getInstance().getWord(stones.get("blue"),type);

        JSONObject answer = new JSONObject();

        answer.put("event", "recieveCard");
        answer.put("wordRed", word1);
        answer.put("wordBlue",word2);
        answer.put("value",type);
        answer.put("isDrawing", isDrawing);

        sendMessage(answer.toString(),sender);

    }


    private void handleDrawingEvent(JSONObject event, Socket sender){
        switch (event.getString("action")){
            case "open":
                isDrawing = true;
                openDrawingWindow(sender);
                break;
            case "close":
                isDrawing = false;
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

        answerOne.put("event", "DrawingWindow");
        answerOne.put("active", false);
        answerOne.put("action", "open");

        JSONObject answerTwo = new JSONObject();
        answerTwo.put("event", "DrawingWindow");
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


    protected void deletePlayer(Socket player){
        try{
            players.remove(player);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
