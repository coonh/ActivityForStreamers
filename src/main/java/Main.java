import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.ServerMain;



public class Main extends Application {

    private Stage main_stage;


    public static void main(String[] args) {

        if (args.length > 0 && args[0].toLowerCase().equals("server")){
            System.out.println("Starte server");
            // new Server
            new ServerMain();
            return;

        }
        //System.out.println(Main.class.getResource("img/field_1.png").toString());
        System.out.println("Hello World");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        main_stage = primaryStage;
        Mainmenu m = new Mainmenu(primaryStage);
        m.show(primaryStage);
    }

}
