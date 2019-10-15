import javafx.application.Application;
import javafx.stage.Stage;
import server.ServerMain;

public class Main extends Application {
    public static void main(String[] args) {

        if (args.length > 0 && args[0].toLowerCase().equals("server")){
            System.out.println("Starte server");
            // new Server
            new ServerMain();
            return;

        }
        System.out.println("Hello World");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
