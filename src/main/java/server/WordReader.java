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
        readInStack(e3,getClass().getResource("/worddata/explain3.txt").getPath());
    /*  readInStack(e4,getClass().getResource("/worddata/explain4.txt").getPath());
        readInStack(e5,getClass().getResource("/worddata/explain5.txt").getPath());

        readInStack(d3,getClass().getResource("/worddata/drawing3.txt").getPath());
        readInStack(d4,getClass().getResource("/worddata/drawing4.txt").getPath());
        readInStack(d5,getClass().getResource("/worddata/drawing5.txt").getPath());

        readInStack(p3,getClass().getResource("/worddata/pantom3.txt").getPath());
        readInStack(p4,getClass().getResource("/worddata/pantom4.txt").getPath());
        readInStack(p5,getClass().getResource("/worddata/pantom5.txt").getPath());
    */
    }

    static WordReader getInstance(){
        if (WordReader.instance == null){
            WordReader.instance = new WordReader();
        }
        return WordReader.instance;
    }

    private void readInStack(Stack stack, String s) {
        stack = new Stack<String>();
        BufferedReader reader;
        ArrayList<String> list = new ArrayList<String>();
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

    public String getWord(String cardType){
        switch (cardType){
            case "e3":
                return e3.pop();
            case "e4":
                return e4.pop();
            case "e5":
                return e5.pop();
            case "d3":
                return d3.pop();
            case "d4":
                return d4.pop();
            case "d5":
                return d5.pop();
            case "p3":
                return p3.pop();
            case "p4":
                return p4.pop();
            case "p5":
                return p5.pop();
            default:
                System.err.println("Card Value needs to be a combination of e, p, d and a value 3, 4, 5");
                return null;
        }
    }


}
