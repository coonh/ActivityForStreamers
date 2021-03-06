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

    void startReceiving(){
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
                                if(input.getBoolean("isDrawing"))gameboard.getDrawingWindow().clear();
                                gameboard.drawCard(input.getString("wordRed"),input.getString("wordBlue"), input.getInt("value"));

                                break;
                            case "DrawingWindow":
                                handleDrawingEvent(input);
                                break;
                            case "receiveTimer":
                                gameboard.timerAnimation();
                                gameboard.timerUpdate(input.getInt("sec"));
                                break;
                            case "newTeams":
                                gameboard.setTeams(input.getInt("value1"),input.getInt("value2"),input.getInt("value3"));
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


    private void handleDrawingEvent(JSONObject event){
        switch (event.getString("action")){
            case "open":
                gameboard.openDrawingWindow(event.getBoolean("active"));
                gameboard.setPopupToFront();
                break;
            case "close":
                gameboard.closeDrawingWindow();
                break;
            case "beginPath":
                gameboard.getDrawingWindow().beginPath(
                        event.getDouble("x"),
                        event.getDouble("y"),
                        event.getString("color"),
                        event.getDouble("thickness")
                );
                break;
            case "endPath":
                gameboard.getDrawingWindow().endPath();
                break;
            case "drawLine":
                gameboard.getDrawingWindow().drawLine(
                        event.getDouble("x"),
                        event.getDouble("y")
                );
                break;
            default:
                System.out.println("Unknown drawing action: " +event.getString("action"));
        }
    }


    public void openDrawingWindow(){
        JSONObject answer = new JSONObject();

        answer.put("event", "DrawingWindow");
        answer.put("action", "open");
        sendMessage(answer.toString());
    }


    public void closeDrawingWindow(){
        JSONObject answer= new JSONObject();

        answer.put("event", "DrawingWindow");
        answer.put("action", "close");
        sendMessage(answer.toString());
    }

    public void beginPathDrawingWindow(double x, double y, double thickness, String color){

        JSONObject answer = new JSONObject();

        answer.put("event", "DrawingWindow");
        answer.put("action", "beginPath");
        answer.put("x", x);
        answer.put("y", y);
        answer.put("thickness", thickness);
        answer.put("color", color);

        sendMessage(answer.toString());
    }

    public void endPathDrawingWindow(){

        JSONObject answer = new JSONObject();

        answer.put("event", "DrawingWindow");
        answer.put("action", "endPath");

        sendMessage(answer.toString());
    }

    public void drawLine(double x, double y){

        JSONObject answer = new JSONObject();

        answer.put("event", "DrawingWindow");
        answer.put("action", "drawLine");
        answer.put("x", x);
        answer.put("y", y);

        sendMessage(answer.toString());
    }

    public void pickTeams() {
        JSONObject answer = new JSONObject();

        answer.put("event","generateTeams");

        sendMessage(answer.toString());

    }

    public void newGame() {
        JSONObject answer = new JSONObject();

        answer.put("event","reset");

        sendMessage(answer.toString());
    }
}
