
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import org.json.JSONStringer;


import java.util.ArrayList;



class Gameboard {
    Scene scene;
    private Popup_card popup;
    private double mainSceneX, mainSceneY;
    private ArrayList<Rectangle> rects;
    private Stage stage;
    double window_width;
    double window_height;
    double field_size;

    private boolean drawMode;

    private Pane stack;
    private Rectangle player1;
    private Rectangle player2;
    private ImageView [] cards;

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

        drawMode = false;


        Scale scale = new Scale(1,1);
        scale.xProperty().bind(stack.widthProperty().divide(width));
        scale.yProperty().bind(stack.widthProperty().divide(width));

        stack.getTransforms().add(scale);

        stack.heightProperty().addListener((observable, oldValue, newValue) -> stage.setHeight(stage.getWidth()/16*9+35));

        Image field_graf_1 = new Image(this.getClass().getResourceAsStream("/img/field_1_v2.png"));
        Image field_graf_2 = new Image(this.getClass().getResourceAsStream("/img/field_2_v2.png"));
        Image field_graf_3 = new Image(this.getClass().getResourceAsStream("/img/field_5_v2.png"));
        Image field_graf_4 = new Image(this.getClass().getResourceAsStream("/img/field_4_v2.png"));
        Image img_player_1 = new Image(this.getClass().getResourceAsStream("/img/player_1.png"));
        Image img_player_2 = new Image(this.getClass().getResourceAsStream("/img/player_2.png"));
        Image img_start = new Image(this.getClass().getResourceAsStream("/img/start.png"));
        Image img_goal = new Image(this.getClass().getResourceAsStream("/img/goal.png"));
        Image img_card_pack_3 = new Image(this.getClass().getResourceAsStream("/img/card_pack_3.png"));
        Image img_card_pack_4 = new Image(this.getClass().getResourceAsStream("/img/card_pack_4.png"));
        Image img_card_pack_5 = new Image(this.getClass().getResourceAsStream("/img/card_pack_5.png"));

        ImagePattern field_patt_1 = new ImagePattern(field_graf_1);
        ImagePattern field_patt_2 = new ImagePattern(field_graf_2);
        ImagePattern field_patt_3 = new ImagePattern(field_graf_3);
        ImagePattern field_patt_4 = new ImagePattern(field_graf_4);
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

        for(int i=0;i<40;i++){
            Rectangle r = new Rectangle(field_size,field_size);
            if(i%2==0){
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
            }else if(i%2!=0){
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
            }
            rects.add(r);
            backframe.getChildren().add(r);


        }
       stack.getChildren().add(backframe);

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

            });
        }


        /*
        / Making Borders for Streamers Cams
         */
        ArrayList<Rectangle> cams = new ArrayList<Rectangle>();

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
                popup = new Popup_card(word, field_size + 10,this,value);
                popup.setTranslateX(rects.get(21).getBoundsInLocal().getMinX() - popup.getBoundsInParent().getWidth() / 2);
                popup.setTranslateY(rects.get(21).getBoundsInLocal().getMinY() - 8);
                stack.getChildren().add(popup);

                for (ImageView card : cards) {
                    card.setDisable(true);
                }
            }else{
                stack.getChildren().remove(popup);
                popup = new Popup_card(word, field_size + 10,this,value);
                popup.setTranslateX(rects.get(21).getBoundsInLocal().getMinX() - popup.getBoundsInParent().getWidth() / 2);
                popup.setTranslateY(rects.get(21).getBoundsInLocal().getMinY() - 8);
                stack.getChildren().add(popup);
            }
        });

    }


}
