package server;

import java.io.*;
import java.net.URL;
import java.util.*;

public class WordReader {

    private WordStack<String> d3,d4,d5,p3,p4,p5,e3,e4,e5;

    private ArrayList<WordStack<String>> allStacks;
    private static WordReader instance;
    private final String path = System.getProperty("user.dir")+File.separator+"src"+File.separator+"main"+File.separator+"resources"+File.separator+"worddata";

    private WordReader(){
        e3 = new WordStack<>(path+File.separator+"explain3.txt");
        e4 = new WordStack<>(path+File.separator+"explain4.txt");
        e5 = new WordStack<>(path+File.separator+"explain5.txt");
        d3 = new WordStack<>(path+File.separator+"drawing3.txt");
        d4 = new WordStack<>(path+File.separator+"drawing4.txt");
        d5 = new WordStack<>(path+File.separator+"drawing5.txt");
        p3 = new WordStack<>(path+File.separator+"pantom3.txt");
        p4 = new WordStack<>(path+File.separator+"pantom4.txt");
        p5 = new WordStack<>(path+File.separator+"pantom5.txt");

        allStacks = new ArrayList<>(Arrays.asList(d3,d4,d5,p3,p4,p5,e3,e4,e5));

        allStacks.forEach(stack -> readInStack(stack,stack.getFilePath()));
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

    public String addWord(String activity, int value, String word) {
        String card = activity + value;
        if (isItin(word)) {
            return "Word already in Database";
        } else {
            switch (card) {
                case "e3":
                    e3.push(word);
                    saveWord(e3,word);
                    break;
                case "e4":
                    e4.push(word);
                    saveWord(e4,word);
                    break;
                case "e5":
                    e5.push(word);
                    saveWord(e5,word);
                    break;
                case "d3":
                    d3.push(word);
                    saveWord(d3,word);
                    break;
                case "d4":
                    d4.push(word);
                    saveWord(d4,word);
                    break;
                case "d5":
                    d5.push(word);
                    saveWord(d5,word);
                    break;
                case "p3":
                    p3.push(word);
                    saveWord(p3,word);
                    break;
                case "p4":
                    p4.push(word);
                    saveWord(p4,word);
                    break;
                case "p5":
                    p5.push(word);
                    saveWord(p5,word);
                    break;
                default:
                    System.err.println("Card Value needs to be a combination of e, p, d and a value 3, 4, 5");
                    break;
            }

            return "Word has been added!";
        }
    }

    private void saveWord(WordStack<String> stack,String word) {
        try {
            File file = new File(stack.getFilePath());
            BufferedWriter writer = new BufferedWriter(new FileWriter((file),true));
            writer.newLine();
            writer.write(word);
            writer.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private boolean isItin(String word) {
        for(WordStack stack : allStacks){
            if(stack.contains(word)){
                return true;
            }
        }
        return false;
    }


    public void reset() {
        WordReader.instance = new WordReader();
    }


    private class WordStack<T> extends Stack<T>{
        String filePath;

        public WordStack(String filePath) {
            super();
            this.filePath = filePath;
        }

        public String getFilePath() {
            return filePath;
        }
    }
}
