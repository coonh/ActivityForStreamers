package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.Stack;

public class WordReader {

    private Stack<String> d3,d4,d5,p3,p4,p5,e3,e4,e5;


    private static WordReader instance;

    private WordReader(){
        d3 = new Stack<String>();
        d4 = new Stack<String>();
        d5 = new Stack<String>();
        p3 = new Stack<String>();
        p4 = new Stack<String>();
        p5 = new Stack<String>();
        e3 = new Stack<String>();
        e4 = new Stack<String>();
        e5 = new Stack<String>();

        readInStack(e3,getClass().getResource("/worddata/explain3.txt").getPath());
        readInStack(e4,getClass().getResource("/worddata/explain4.txt").getPath());
        readInStack(e5,getClass().getResource("/worddata/explain5.txt").getPath());

        readInStack(d3,getClass().getResource("/worddata/drawing3.txt").getPath());
        readInStack(d4,getClass().getResource("/worddata/drawing4.txt").getPath());
        readInStack(d5,getClass().getResource("/worddata/drawing5.txt").getPath());

        readInStack(p3,getClass().getResource("/worddata/pantom3.txt").getPath());
        readInStack(p4,getClass().getResource("/worddata/pantom4.txt").getPath());
        readInStack(p5,getClass().getResource("/worddata/pantom5.txt").getPath());
    }

    static WordReader getInstance(){
        if (WordReader.instance == null){
            WordReader.instance = new WordReader();
        }
        return WordReader.instance;
    }

    private void readInStack(Stack<String> stack, String s) {
        BufferedReader reader;
        try {
            FileReader file = new FileReader(s);
            reader = new BufferedReader(file);

            String line = reader.readLine();
            while(line != null){
                stack.push(line);
                line = reader.readLine();
            }
            Collections.shuffle(stack);
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getWord(int playerpos, int value){
        String activity;
        int positionType = playerpos % 3;
        switch (positionType){
            case 0:
                activity = "d";
                break;
            case 1:
                activity = "e";
                break;
            case 2:
                activity = "p";
                break;
            default:
                System.err.println("Invalid position of Player when card is drawn");
                activity = null;
                break;
        }
        String word = "ERR";
        String card = activity+value;
        try {
            switch (card) {
                case "e3":
                    word = e3.pop();
                    break;
                case "e4":
                    word = e4.pop();
                    break;
                case "e5":
                    word = e5.pop();
                    break;
                case "d3":
                    word = d3.pop();
                    break;
                case "d4":
                    word = d4.pop();
                    break;
                case "d5":
                    word = d5.pop();
                    break;
                case "p3":
                    word = p3.pop();
                    break;
                case "p4":
                    word = p4.pop();
                    break;
                case "p5":
                    word = p5.pop();
                    break;
                default:
                    System.err.println("Card Value needs to be a combination of e, p, d and a value 3, 4, 5");
                    break;
            }
        }catch (EmptyStackException e){
            return "Keine Wörter mehr";
        }
        return word;
    }


    public void reset() {
        WordReader.instance = new WordReader();
    }
}
