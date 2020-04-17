import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import server.ServerMain;



public class Main extends Application {

    private Stage main_stage;


    public static void main(String[] args) {
        if (args.length > 0 && args[0].toLowerCase().equals("server")){
            System.out.println("Starte Server");
            // new Server
            new ServerMain();
            return;
        }
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        main_stage = primaryStage;
        main_stage.getIcons().add(new Image(Main.class.getResourceAsStream("/img/LOGO.png")));
        Mainmenu m = new Mainmenu(primaryStage);
        m.show(primaryStage);

    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.exit(0);
    }
}
