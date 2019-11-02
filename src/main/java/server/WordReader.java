package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class WordReader {

    private WordStack<String> d3,d4,d5,p3,p4,p5,e3,e4,e5;

    private ArrayList<WordStack<String>> allStacks;


    private static WordReader instance;

    private WordReader(){
        e3 = new WordStack<>("/worddata/explain3.txt");
        e4 = new WordStack<>("/worddata/explain4.txt");
        e5 = new WordStack<>("/worddata/explain5.txt");
        d3 = new WordStack<>("/worddata/drawing3.txt");
        d4 = new WordStack<>("/worddata/drawing4.txt");
        d5 = new WordStack<>("/worddata/drawing5.txt");
        p3 = new WordStack<>("/worddata/pantom3.txt");
        p4 = new WordStack<>("/worddata/pantom4.txt");
        p5 = new WordStack<>("/worddata/pantom5.txt");

        allStacks = new ArrayList<>(Arrays.asList(d3,d4,d5,p3,p4,p5,e3,e4,e5));

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

            //allStacks.forEach(stack -> {if (stack.empty()) {readInStack(stack,stack.getFilePath()); Collections.shuffle(stack); }});
        }catch (EmptyStackException e){
            return "Keine Wörter mehr";
        }
        return word;
    }


    public void reset() {
        WordReader.instance = new WordReader();
    }


    private class WordStack<T> extends Stack<T>{
        String filePath;

        public WordStack(String filePath) {
            super();
            this.filePath = getClass().getResource(filePath).getPath();
        }

        public String getFilePath() {
            return filePath;
        }
    }
}
