
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import org.json.JSONStringer;


import java.io.File;
import java.lang.reflect.Array;
import java.util.*;


class Gameboard {
    Scene scene;
    private Popup_card popup;
    private double mainSceneX, mainSceneY;
    private ArrayList<Rectangle> rects;
    private Stage stage;
    double window_width;
    double window_height;
    double field_size;

    private DrawingWindow d;

    private Text time;
    private Text counter_txt;
    private boolean drawMode;

    private int counter;
    private int active_card_value;

    private ImageView stopwatch;
    private ImageView trashcan;
    private Image img_stopwatch_anim, img_stopwatch;

    private AudioClip beep1, beep2;

    private ArrayList<Rectangle> cams;

    private Pane stack;
    private Rectangle player1;
    private Rectangle player2;
    private ImageView [] cards;
    private Pane frame;

    Gameboard(Stage stage, double width, double height){
        this.stage = stage;
        window_width=width;
        window_height=height;
        stack = new Pane();
        stack.setPrefHeight(height);
        stack.setMinHeight(height);
        Pane backframe= new Pane();
        backframe.setMinWidth(window_width);
        backframe.setMinHeight(window_height);




        active_card_value = 3;
        drawMode = false;

        counter = 0;

        Scale scale = new Scale(1,1);
        scale.xProperty().bind(stack.widthProperty().divide(width));
        scale.yProperty().bind(stack.widthProperty().divide(width));

        stack.getTransforms().add(scale);

        stack.heightProperty().addListener((observable, oldValue, newValue) -> stage.setHeight(stage.getWidth()/16*9+35));

        beep1 = new AudioClip(this.getClass().getResource("/sound/beep1.wav").toExternalForm());
        beep2 = new AudioClip(this.getClass().getResource("/sound/beep2.wav").toExternalForm());



        Image field_graf_1 = new Image(this.getClass().getResourceAsStream("/img/field_1_v2.png"));
        Image field_graf_2 = new Image(this.getClass().getResourceAsStream("/img/field_2_v2.png"));
        Image field_graf_3 = new Image(this.getClass().getResourceAsStream("/img/field_5_v2.png"));
        Image field_graf_4 = new Image(this.getClass().getResourceAsStream("/img/field_4_v2.png"));
        Image field_graf_6 = new Image(this.getClass().getResourceAsStream("/img/field_6.png"));

        Image img_draw = new Image(this.getClass().getResourceAsStream("/img/drawButton.png"));
        Image img_new_game = new Image(this.getClass().getResourceAsStream("/img/newGameButton.png"));

        Image img_player_1 = new Image(this.getClass().getResourceAsStream("/img/player_1.png"));
        Image img_player_2 = new Image(this.getClass().getResourceAsStream("/img/player_2.png"));
        Image img_start = new Image(this.getClass().getResourceAsStream("/img/start.png"));
        Image img_goal = new Image(this.getClass().getResourceAsStream("/img/goal.png"));
        Image img_card_pack_3 = new Image(this.getClass().getResourceAsStream("/img/card_pack_3.png"));
        Image img_card_pack_4 = new Image(this.getClass().getResourceAsStream("/img/card_pack_4.png"));
        Image img_card_pack_5 = new Image(this.getClass().getResourceAsStream("/img/card_pack_5.png"));
        Image img_trash_can = new Image(this.getClass().getResourceAsStream("/img/trashcan.png"));

        img_stopwatch_anim = new Image(this.getClass().getResourceAsStream("/img/stopwatch.gif"));
        img_stopwatch = new Image(this.getClass().getResourceAsStream("/img/stopwatch.png"));
        trashcan = new ImageView(img_trash_can);

        ImagePattern field_patt_1 = new ImagePattern(field_graf_1);
        ImagePattern field_patt_2 = new ImagePattern(field_graf_2);
        ImagePattern field_patt_3 = new ImagePattern(field_graf_3);
        ImagePattern field_patt_4 = new ImagePattern(field_graf_4);
        ImagePattern field_patt_6 = new ImagePattern(field_graf_6);

        ImagePattern start_patt = new ImagePattern(img_start);
        ImagePattern goal_patt = new ImagePattern(img_goal);
        ImagePattern card_pack_3 = new ImagePattern(img_card_pack_3);
        ImagePattern card_pack_4 = new ImagePattern(img_card_pack_4);
        ImagePattern card_pack_5 = new ImagePattern(img_card_pack_5);

        rects= new ArrayList<>();

        int fields_in_a_collumn = 7;
        field_size = window_height/fields_in_a_collumn;
        int fields_in_a_row = 12;
        Rectangle start_rec = new Rectangle(field_size,field_size);
        start_rec.setFill(start_patt);
        backframe.getChildren().add(start_rec);
        start_rec.setX(0);
        start_rec.setY(field_size);


        rects.add(start_rec);
        // Create game field
        for(int i=0;i<40;i++){
            Rectangle r = new Rectangle(field_size,field_size);
            if(i%3==0){
                if (i>fields_in_a_row-1 && i<fields_in_a_row+3){
                    r.setFill(field_patt_3);
                    r.setX(rects.get(rects.size()-1).getX());
                    r.setY((i-(fields_in_a_row-1))*field_size);
                }else if(i>fields_in_a_row+2 && i<2*fields_in_a_row+2) {
                    r.setFill(field_patt_1);
                    r.setX(rects.get(rects.size()-1).getX()-field_size);
                    r.setY(3*field_size);
                }else if(i>25 && i<29) {
                    r.setFill(field_patt_3);
                    r.setX(0);
                    r.setRotate(180);
                    r.setY(rects.get(rects.size()-1).getY()+field_size);
                }else if(i>28 && i<40) {
                    r.setFill(field_patt_1);
                    r.setX(rects.get(rects.size()-1).getX()+field_size);
                    r.setY(rects.get(rects.size()-1).getY());
                } else{
                    r.setFill(field_patt_1);
                    r.setX(i*field_size);
                }
            }else if(i%3==1){
                if (i>fields_in_a_row-1 && i<fields_in_a_row+3){
                    r.setFill(field_patt_4);
                    r.setX(rects.get(rects.size()-1).getX());
                    r.setY((i-(fields_in_a_row-1))*field_size);
                }else if(i>fields_in_a_row+2 && i<2*fields_in_a_row+2) {
                    r.setFill(field_patt_2);
                    r.setX(rects.get(rects.size()-1).getX()-field_size);
                    r.setY(3*field_size);
                }else if(i>25 && i<29) {
                    r.setFill(field_patt_4);
                    r.setX(0);
                    r.setRotate(180);
                    r.setY(rects.get(rects.size()-1).getY()+field_size);
                }else if(i>28 && i<40) {
                    r.setFill(field_patt_2);
                    r.setX(rects.get(rects.size()-1).getX()+field_size);
                    r.setY(rects.get(rects.size()-1).getY());
                }else{
                    r.setFill(field_patt_2);
                    r.setX(i*field_size);
                }
            }else if(i%3==2){
                if (i>fields_in_a_row-1 && i<fields_in_a_row+3){
                    r.setFill(field_patt_6);
                    r.setRotate(90);
                    r.setX(rects.get(rects.size()-1).getX());
                    r.setY((i-(fields_in_a_row-1))*field_size);
                }else if(i>fields_in_a_row+2 && i<2*fields_in_a_row+2) {
                    r.setFill(field_patt_6);
                    r.setX(rects.get(rects.size()-1).getX()-field_size);
                    r.setY(3*field_size);
                }else if(i>25 && i<29) {
                    r.setFill(field_patt_6);
                    r.setX(0);
                    r.setRotate(180);
                    r.setY(rects.get(rects.size()-1).getY()+field_size);
                }else if(i>28 && i<40) {
                    r.setFill(field_patt_6);
                    r.setX(rects.get(rects.size()-1).getX()+field_size);
                    r.setY(rects.get(rects.size()-1).getY());
                } else{
                    r.setFill(field_patt_6);
                    r.setX(i*field_size);
                }
            }
            rects.add(r);
            backframe.getChildren().add(r);


        }
       stack.getChildren().add(backframe);

        // Open a Drawfield
        ImageView drawBtn = new ImageView(img_draw);
        drawBtn.setFitHeight(field_size/2);
        drawBtn.setPreserveRatio(true);
        drawBtn.setTranslateX(11*field_size);
        drawBtn.setTranslateY(4*field_size);

        trashcan.setFitHeight(field_size/2);
        trashcan.setPreserveRatio(true);
        trashcan.setTranslateX(2.5*field_size);
        trashcan.setTranslateY(1.5*field_size);


        frame = new Pane();
        Rectangle re = new Rectangle(926,514);
        re.setFill(Color.WHITE);
        re.setTranslateX(2*field_size);
        re.setTranslateY(field_size);
        frame.getChildren().add(re);

        drawBtn.setOnMouseClicked(event -> {
            if(d==null) ServerConnector.getInstance().openDrawingWindow();
            else ServerConnector.getInstance().closeDrawingWindow();
        });
        stack.getChildren().add(drawBtn);

        // new game
        ImageView newGame = new ImageView(img_new_game);
        newGame.setFitHeight(field_size/2);
        newGame.setPreserveRatio(true);
        newGame.setTranslateX(11*field_size);
        newGame.setTranslateY(9*field_size/2);
        newGame.setOnMouseClicked(event -> {
            generateTeams();
        });
        stack.getChildren().add(newGame);

        //Timer GUI
        VBox time_frame = new VBox();
        HBox t_top = new HBox();
        HBox t_bottom = new HBox();
        time_frame.setMaxSize(field_size,field_size);

        Text timer_label = new Text("Timer: ");
        time = new Text("90");
        Text sek = new Text("s");
        timer_label.setFont(new Font("Berlin Sans FB",26));
        time.setFont(new Font("Berlin Sans FB",75));
        sek.setFont(new Font("Berlin Sans FB",40));

        timer_label.setFill(Color.WHITE);
        time.setFill(Color.WHITE);
        sek.setFill(Color.WHITE);


        t_top.getChildren().add(timer_label);
        t_bottom.getChildren().addAll(time,sek);

        t_top.setAlignment(Pos.BASELINE_LEFT);
        t_bottom.setAlignment(Pos.CENTER);

        time_frame.getChildren().addAll(t_top,t_bottom);
        time_frame.setTranslateX(field_size+5);
        time_frame.setTranslateY(4*field_size+5);
        stack.getChildren().add(time_frame);

        stopwatch = new ImageView(img_stopwatch);
        stopwatch.setTranslateX(field_size);
        stopwatch.setTranslateY(5*field_size);
        stopwatch.setFitHeight(field_size);
        stopwatch.setPreserveRatio(true);


        stack.getChildren().add(stopwatch);


        // Scoreboard
        Text counter_label = new Text("Punkte: ");
        counter_label.setFont(new Font("Berlin Sans FB",26));
        counter_label.setFill(Color.WHITE);

        counter_txt = new Text("23");
        counter_txt.setFont(new Font("Berlin Sans FB",80));
        counter_txt.setFill(Color.WHITE);

        VBox score = new VBox(-20);
        score.setMaxSize(field_size,field_size);
        score.getChildren().addAll(counter_label, counter_txt);

        score.setTranslateX(field_size+5);
        score.setTranslateY(field_size+5);
        score.setAlignment(Pos.CENTER);

        stack.getChildren().add(score);

        // Goal and Start field
        Rectangle goal_rect = new Rectangle(field_size,field_size);
        goal_rect.setFill(goal_patt);
        backframe.getChildren().add(goal_rect);
        goal_rect.setX(rects.get(rects.size()-1).getX());
        goal_rect.setY(rects.get(rects.size()-1).getY()-field_size);
        rects.add(goal_rect);

        ImagePattern ip_p_1 = new ImagePattern(img_player_1);
        ImagePattern ip_p_2 = new ImagePattern(img_player_2);

        player1 = new Rectangle((field_size/2)-field_size*0.1,(field_size)*0.6);
        player1.setFill(ip_p_1);

        player2 = new Rectangle((field_size/2)-field_size*0.1,(field_size)*0.6);
        player2.setFill(ip_p_2);

        player1.setCursor(Cursor.HAND);
        player2.setCursor(Cursor.HAND);

        /*
        * Handle everything u can do with player 1
        * */
        player1.setOnMousePressed((t) -> {
            mainSceneX = t.getSceneX();
            mainSceneY = t.getSceneY();

            Rectangle r = (Rectangle) (t.getSource());
            r.toFront();
        });
        player1.setOnMouseDragged((t) -> {

            player1.yProperty().set(t.getY() - player2.getHeight()/2);
            player1.xProperty().set(t.getX() - player2.getWidth()/2);
        });
        player1.setOnMouseReleased((t) -> {
            checkBounds(player1,true);
        });

        /*
         * Handle everything u can do with player2
         * */
        player2.setOnMousePressed((t) -> {
            mainSceneX = t.getSceneX();
            mainSceneY = t.getSceneY();

            Rectangle r = (Rectangle) (t.getSource());
            r.toFront();
        });
        player2.setOnMouseDragged((t) -> {

            player2.yProperty().set(t.getY() - player2.getHeight()/2);
            player2.xProperty().set(t.getX() - player2.getWidth()/2);

        });
        player2.setOnMouseReleased((t) -> {
            checkBounds(player2,false);
        });
        /*
        * Adding the cards and make em click
        */

        ImageView card3 = new ImageView(img_card_pack_3);
        ImageView card4 = new ImageView(img_card_pack_4);
        ImageView card5 = new ImageView(img_card_pack_5);
        cards = new ImageView[3];
        cards[0] = card3;
        cards[1] = card4;
        cards[2] = card5;

        for(int i=0;i<cards.length;i++){
            cards[i].setCursor(Cursor.HAND);
            cards[i].setPreserveRatio(true);
            cards[i].setFitHeight(field_size-10);
            cards[i].setY(2*field_size+5);
            cards[i].setX(i*(5+cards[i].getBoundsInParent().getWidth()));
            backframe.getChildren().add(cards[i]);
            int value = i+3;
            cards[i].setOnMouseClicked(event -> {
                String message =  new JSONStringer().object()
                        .key("event").value("drawCard")
                        .key("type").value(value)
                        .endObject().toString();

                System.out.println("Sending message: " +message);

                ServerConnector.getInstance().sendMessage(message);

                String message2 =  new JSONStringer().object()
                        .key("event").value("timer")
                        .key("action").value("start")
                        .endObject().toString();

                System.out.println("Sending message: " +message2);
                ServerConnector.getInstance().sendMessage(message2);

            });
        }


        /*
        / Making Borders for Streamers Cams
         */
        cams = new ArrayList<Rectangle>();

        for(int i=0;i<6;i++){
            Rectangle cam = new Rectangle(3*field_size-10,2*field_size-10);
            cam.setStroke(Color.rgb(36, 123, 160));
            cam.setStrokeWidth(10);
            //cam.setFill(Color.rgb(0,255,0));
            cams.add(cam);
        }
        cams.get(0).setX(2*field_size+5);
        cams.get(0).setY(field_size+5);
        backframe.getChildren().add(cams.get(0));
        for(int i=1;i<3;i++){
            cams.get(i).setX(cams.get(i-1).getX()+3*field_size);
            cams.get(i).setY(field_size+5);
            backframe.getChildren().add(cams.get(i));
        }
        cams.get(3).setX(2*field_size+5);
        cams.get(3).setY(4*field_size+5);
        backframe.getChildren().add(cams.get(3));
        cams.get(3).setStroke(Color.rgb(242,95,92));
        for(int i=4;i<6;i++){
            cams.get(i).setStroke(Color.rgb(242,95,92));
            cams.get(i).setX(cams.get(i-1).getX()+3*field_size);
            cams.get(i).setY(4*field_size+5);
            backframe.getChildren().add(cams.get(i));
        }

        /*
        * Adding Nametags to every Streamers Cam
        * */
        String [] streamer = {"coonh","xxthemagics","candynyaa","schniekelaramel","i407250234i","koksyy"};

        for (Rectangle cam : cams){
            Text name_tag = new Text("twitch.tv/"+streamer[cams.indexOf(cam)]);
            HBox name_1 = new HBox();
            name_1.setMinWidth(cam.getWidth()/2);
            name_1.getChildren().add(name_tag);
            name_tag.setFill(Color.rgb(80,81,79));
            name_tag.setFont(new Font("Nova Round",20));
            name_1.setBackground(new Background(new BackgroundFill(Color.rgb(255,255,255),CornerRadii.EMPTY, Insets.EMPTY)));
            name_1.setLayoutX(cam.getBoundsInParent().getMinX()+cam.getStrokeWidth());
            name_1.setLayoutY(cam.getBoundsInParent().getMaxY()-cam.getStrokeWidth()-(name_1.getBoundsInParent().getHeight()));
            backframe.getChildren().add(name_1);
        }

        generateTeams();

        backframe.setBackground(new Background(new BackgroundFill(Color.rgb(36, 97, 133),CornerRadii.EMPTY, Insets.EMPTY)));
        player1.setX(start_rec.getX()+start_rec.getWidth()/2-player1.getWidth());
        player1.setY(start_rec.getY()+start_rec.getHeight()/2-player1.getHeight()/2);
        player2.setX((start_rec.getX()+start_rec.getWidth()/2-player1.getWidth())+player1.getWidth());
        player2.setY(start_rec.getY()+start_rec.getHeight()/2-player2.getHeight()/2);


        stack.getChildren().addAll(player1,player2);
        scene = new Scene(stack);
    }

    private void checkBounds(Rectangle player1,boolean red) {

        System.out.println("Collision check");

        String color;
        if (red) color = "red";
        else color = "blue";

        for (Rectangle stone : rects) {
                if (player1.getBoundsInParent().intersects(stone.getBoundsInParent())) {

                    System.out.println("Collision found");

                    String message =  new JSONStringer().object()
                            .key("event").value("moveStone")
                            .key("position").value(rects.indexOf(stone))
                            .key("color").value(color).endObject().toString();

                    System.out.println("Sending message: " +message);

                    ServerConnector.getInstance().sendMessage(message);
                    return;
                }

            }

        String message =  new JSONStringer().object()
                .key("event").value("moveStone")
                .key("position").value(-2)
                .key("color").value(color).endObject().toString();

        System.out.println("Sending message: " +message);

        ServerConnector.getInstance().sendMessage(message);
        }

    void placeStone(String color, int position){
        if (position == -2 ) return;

        Rectangle stone = rects.get(position);
        switch (color){
            case "red":
                player1.setX((stone.getX()+stone.getWidth()/2-player1.getWidth()/2)-player1.getWidth()/2);
                player1.setY(stone.getY()+stone.getHeight()/2-player1.getHeight()/2);
                break;
            default:
                player2.setX((stone.getX()+stone.getWidth()/2-player2.getWidth()/2)+player2.getWidth()/2);
                player2.setY(stone.getY()+stone.getHeight()/2-player2.getHeight()/2);

        }
    }


    void show(Stage stage){
        stage.setScene(scene);
        stage.show();
    }

    void drawCard(String word,int value) {
        Platform.runLater(()-> {
            if(!drawMode) {
                counter = 0;
                counter_txt.setText("0");

                active_card_value = value;
                popup = new Popup_card(word, field_size + 10,this,value);
                popup.setTranslateX(rects.get(21).getBoundsInLocal().getMinX() - popup.getBoundsInParent().getWidth() / 2);
                popup.setTranslateY(rects.get(21).getBoundsInLocal().getMinY() - 8);
                stack.getChildren().add(popup);
                drawMode = true;

                for (ImageView card : cards) {
                    card.setDisable(true);
                }
            }else{
                active_card_value = value;
                stack.getChildren().remove(popup);
                popup = new Popup_card(word, field_size + 10,this,value);
                popup.setTranslateX(rects.get(21).getBoundsInLocal().getMinX() - popup.getBoundsInParent().getWidth() / 2);

                if(d!=null) setPopupToFront();
                else popup.setTranslateY(rects.get(21).getBoundsInLocal().getMinY() - 8);

                stack.getChildren().add(popup);
            }
        });

    }


    public void increaseCounter() {
        counter = counter + active_card_value;
        counter_txt.setText(""+counter);
    }

    public void finishRound() {
        stack.getChildren().remove(popup);
        drawMode = false;
        for (ImageView card : cards) {
            card.setDisable(false);
        }
    }

    public void timerUpdate(int sec) {
        if (sec<=10 && sec > 0){
            beep1.play();
        }else if(sec==0) beep2.play();
        stopwatch.setImage(img_stopwatch_anim);

        if(sec==0){
            Platform.runLater(()-> {
                finishRound();
                stopwatch.setImage(img_stopwatch);
            });
        }

        time.setText(Integer.toString(sec));

    }

    public void timerAnimation() {
        stopwatch.setImage(img_stopwatch_anim);
    }
    private void generateTeams(){
        Color blue = Color.rgb(36, 123, 160);
        Color red = Color.rgb(242,95,92);
        for(Rectangle cam : cams){
            cam.setStroke(blue);
        }
        Integer[] choice = new Integer[]{0, 1, 2, 3, 4, 5};

        List<Integer> l = Arrays.asList(choice);

        Collections.shuffle(l);
        for(int i=0;i<3;i++){
            cams.get(choice[i]).setStroke(red);
        }

        String message =  new JSONStringer().object()
                .key("event").value("moveStone")
                .key("position").value(0)
                .key("color").value("red").endObject().toString();
        String message2 =  new JSONStringer().object()
                .key("event").value("moveStone")
                .key("position").value(0)
                .key("color").value("blue").endObject().toString();

        System.out.println("Sending message: " +message);
        System.out.println("Sending message: " +message2);

        ServerConnector.getInstance().sendMessage(message);
        ServerConnector.getInstance().sendMessage(message2);

    }

    protected void openDrawingWindow(boolean active){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                d = new DrawingWindow();
                if (active) d.activateEventHandler();
                d.setTranslateX(2*field_size);
                d.setTranslateY(field_size);
                frame.getChildren().add(d);
                stack.getChildren().add(frame);
                setPopupToFront();

            }
        });

    }

    public DrawingWindow getDrawingWindow() {
        return d;
    }

    protected void closeDrawingWindow(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (d!=null){
                    frame.getChildren().remove(d);
                    d = null;
                    stack.getChildren().remove(frame);
                }
            }
        });



    }

    public void setPopupToFront() {
        if(popup!=null){
            popup.toFront();
            popup.setTranslateY(field_size/4);
        }

    }
}
