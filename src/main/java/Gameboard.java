
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;


import java.util.ArrayList;



public class Gameboard {
    Scene scene;
    double mainSceneX, mainSceneY;
    ArrayList<Rectangle> rects;
    Stage stage;
    double window_width;
    double window_height;

    public Gameboard(Stage stage, double width, double height){
        this.stage = stage;
        window_width=width;
        window_height=height;
        Pane stack = new Pane();
        Pane backframe= new Pane();
        backframe.setMinWidth(window_width);
        backframe.setMinHeight(window_height);


        /*Image field_graf_1 = new Image("file:img"+ File.separator+"field_1_v2.png");
        Image field_graf_2 = new Image("file:img"+ File.separator+"field_2_v2.png");
        Image field_graf_3 = new Image("file:img"+ File.separator+"field_5_v2.png");
        Image field_graf_4 = new Image("file:img"+ File.separator+"field_4_v2.png");
        Image img_player_1 = new Image("file:img"+ File.separator+"player_1.png");
        Image img_player_2 = new Image("file:img"+ File.separator+"player_2.png");
        Image img_start = new Image("file:img"+ File.separator+"start.png");
        Image img_goal = new Image("file:img"+ File.separator+"goal.png");*/

        Image field_graf_1 = new Image(this.getClass().getResourceAsStream("/img/field_1_v2.png"));
        Image field_graf_2 = new Image(this.getClass().getResourceAsStream("/img/field_2_v2.png"));
        Image field_graf_3 = new Image(this.getClass().getResourceAsStream("/img/field_5_v2.png"));
        Image field_graf_4 = new Image(this.getClass().getResourceAsStream("/img/field_4_v2.png"));
        Image img_player_1 = new Image(this.getClass().getResourceAsStream("/img/player_1.png"));
        Image img_player_2 = new Image(this.getClass().getResourceAsStream("/img/player_2.png"));
        Image img_start = new Image(this.getClass().getResourceAsStream("/img/start.png"));
        Image img_goal = new Image(this.getClass().getResourceAsStream("/img/goal.png"));

        ImagePattern field_patt_1 = new ImagePattern(field_graf_1);
        ImagePattern field_patt_2 = new ImagePattern(field_graf_2);
        ImagePattern field_patt_3 = new ImagePattern(field_graf_3);
        ImagePattern field_patt_4 = new ImagePattern(field_graf_4);
        ImagePattern start_patt = new ImagePattern(img_start);
        ImagePattern goal_patt = new ImagePattern(img_goal);

        rects= new ArrayList<Rectangle>();


        int fields_in_a_collumn = 7;
        double field_size = window_height/fields_in_a_collumn;
        int fields_in_a_row = 12;
        System.out.println(fields_in_a_row);
        Rectangle start_rec = new Rectangle(field_size,field_size);
        start_rec.setFill(start_patt);
        backframe.getChildren().add(start_rec);
        start_rec.setX(0);
        start_rec.setY(field_size);




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


        ImagePattern ip_p_1 = new ImagePattern(img_player_1);
        ImagePattern ip_p_2 = new ImagePattern(img_player_2);

        Rectangle player1 = new Rectangle((field_size/2)-field_size*0.1,(field_size)*0.6);
        player1.setFill(ip_p_1);

        Rectangle player2 = new Rectangle((field_size/2)-field_size*0.1,(field_size)*0.6);
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
            double offsetX = t.getSceneX() - mainSceneX;
            double offsetY = t.getSceneY() - mainSceneY;

            Rectangle r = (Rectangle) (t.getSource());

            r.setX(r.getX() + offsetX);
            r.setY(r.getY() + offsetY);

            mainSceneX = t.getSceneX();
            mainSceneY = t.getSceneY();
        });
        player1.setOnMouseReleased((t) -> {
            checkBounds(player1,true);
        });

        /*
         * Handle evrything u can do with player2
         * */
        player2.setOnMousePressed((t) -> {
            mainSceneX = t.getSceneX();
            mainSceneY = t.getSceneY();

            Rectangle r = (Rectangle) (t.getSource());
            r.toFront();
        });
        player2.setOnMouseDragged((t) -> {
            double offsetX = t.getSceneX() - mainSceneX;
            double offsetY = t.getSceneY() - mainSceneY;

            Rectangle r = (Rectangle) (t.getSource());

            r.setX(r.getX() + offsetX);
            r.setY(r.getY() + offsetY);

            mainSceneX = t.getSceneX();
            mainSceneY = t.getSceneY();
        });
        player2.setOnMouseReleased((t) -> {
            checkBounds(player2,false);
        });

        /*
        / Making Borders for Streamers Cams
         */
        ArrayList<Rectangle> cams = new ArrayList<Rectangle>();

        for(int i=0;i<6;i++){
            Rectangle cam = new Rectangle(3*field_size-10,2*field_size-10);
            cam.setStroke(Color.rgb(36, 123, 160));
            cam.setArcHeight(10);
            cam.setArcWidth(10);
            cam.setStrokeWidth(10);
            cam.setFill(Color.rgb(0,255,0));
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
        for(int i=4;i<6;i++){
            cams.get(i).setX(cams.get(i-1).getX()+3*field_size);
            cams.get(i).setY(4*field_size+5);
            backframe.getChildren().add(cams.get(i));
        }

        backframe.setBackground(new Background(new BackgroundFill(Color.rgb(16, 35, 68),CornerRadii.EMPTY, Insets.EMPTY)));
        player1.setX(start_rec.getX()+start_rec.getWidth()/2-player1.getWidth());
        player1.setY(start_rec.getY()+start_rec.getHeight()/2-player1.getHeight()/2);
        player2.setX((start_rec.getX()+start_rec.getWidth()/2-player1.getWidth())+player1.getWidth());
        player2.setY(start_rec.getY()+start_rec.getHeight()/2-player2.getHeight()/2);
        stack.getChildren().addAll(player1,player2);
        scene = new Scene(stack);
    }

    private void checkBounds(Rectangle player1,boolean red) {



        for (Rectangle stone : rects) {
                if (player1.getBoundsInParent().intersects(stone.getBoundsInParent())) {
                    if(red){
                        player1.setX((stone.getX()+stone.getWidth()/2-player1.getWidth()/2)-player1.getWidth()/2);
                    }else{
                        player1.setX((stone.getX()+stone.getWidth()/2-player1.getWidth()/2)+player1.getWidth()/2);
                    }

                    player1.setY(stone.getY()+stone.getHeight()/2-player1.getHeight()/2);
                    return;
                }

            }
        }


    public void show(Stage stage){
        stage.setScene(scene);
        stage.show();
    }

}
