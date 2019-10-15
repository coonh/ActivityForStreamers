import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {

        if (args.length > 0 && args[0].toLowerCase().equals("server")){
            System.out.println("Starte server");
            // new Server
            return;

        }
        System.out.println("Hello World");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Gameboard game = new Gameboard();
        game.show(primaryStage);
    }
}
