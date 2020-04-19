package server;


import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WordReader {

    private WordStack d3,d4,d5,p3,p4,p5,e3,e4,e5;

    private ArrayList<WordStack> allStacks;
    private static WordReader instance;

    private WordReader(){

        // Wordstack is filling himself with the correct word data
        e3 = new WordStack("explain3.txt");
        e4 = new WordStack("explain4.txt");
        e5 = new WordStack("explain5.txt");
        d3 = new WordStack("drawing3.txt");
        d4 = new WordStack("drawing4.txt");
        d5 = new WordStack("drawing5.txt");
        p3 = new WordStack("pantom3.txt");
        p4 = new WordStack("pantom4.txt");
        p5 = new WordStack("pantom5.txt");

        allStacks = new ArrayList<>(Arrays.asList(d3,d4,d5,p3,p4,p5,e3,e4,e5));

        //allStacks.forEach(stack -> readInStack(stack,stack.getFilePath()));
    }

    static WordReader getInstance(){
        if (WordReader.instance == null){
            WordReader.instance = new WordReader();
        }
        return WordReader.instance;
    }

    private void readInStack(WordStack stack, String s) {
        stack.readInStack();
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
                    return "Card Value needs to be a combination of e, p, d and a value 3, 4, 5";
            }

            return "Word has been added!";
        }
    }

    private void saveWord(WordStack stack,String word) {
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


    private class WordStack extends Stack<String>{
        String name;
        String filePath;
        File myFile;
        private final String path = System.getProperty("user.dir")+ File.separator + "worddata"+ File.separator;

        public WordStack(String fileName) {
            super();
            this.name = fileName;
            filePath = path+fileName;

            myFile = new File(filePath);
            if(!myFile.exists()){
                loadDefaultValues();
            }
            else {
                readInStack();
            }

        }

        public void readInStack(){

            System.out.println(name + " successfully loaded. Loading the values");
            BufferedReader reader;
            try {
                FileReader file = new FileReader(filePath);
                reader = new BufferedReader(file);

                String line = reader.readLine();
                while(line != null){
                    this.push(line);
                    line = reader.readLine();
                }
                Collections.shuffle(this);
                reader.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        /**
         * Is triggered, when no data in the folder worddata exists.
         * Default values from the resource worddata will be load
         */
        private void loadDefaultValues(){


            System.out.println(name + " not found. Loading default values");

            try {
                InputStreamReader file = new InputStreamReader(Class.forName("ServerConnector").getResourceAsStream("/worddata/" + name));
                BufferedReader reader = new BufferedReader(file);
                reader.lines().forEach(s -> this.push(s));
            } catch (ClassNotFoundException e){
                e.printStackTrace();
                System.exit(1);
            }

            System.out.println("Creating: " + name + " in /worddata");


            new File(path).mkdirs();

            try {

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(myFile), StandardCharsets.UTF_8));
                StringBuilder output = new StringBuilder();

                //Whole stack into one String with line breaks
                this.forEach(s -> output.append(s).append("\n"));

                output.deleteCharAt(output.length()-1);
                writer.write(output.toString());
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Collections.shuffle(this);

        }

        public String getFilePath() {
            return filePath;
        }
    }
}
