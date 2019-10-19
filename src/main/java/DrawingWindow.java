import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class DrawingWindow extends Canvas {
    
    GraphicsContext myGC;

    public DrawingWindow(){
        super(926,514);
        
        
        myGC = this.getGraphicsContext2D();
        

        myGC.setFill(Color.BLACK);
        myGC.setLineWidth(8);
        activateEventHandler();
    }
    
    
    public void activateEventHandler(){
        this.setOnMousePressed(event -> {
            if (event.getButton().equals(MouseButton.SECONDARY)){
                myGC.moveTo(event.getX(), event.getY());
                myGC.setStroke(Color.WHITE);
                myGC.setLineWidth(100);
                ServerConnector.getInstance().beginPathDrawingWindow(
                        event.getX(),
                        event.getY(),
                        100,
                        "white");

            }else {
                myGC.setLineWidth(8);
                myGC.moveTo(event.getX(), event.getY());
                myGC.setStroke(Color.BLACK);

                ServerConnector.getInstance().beginPathDrawingWindow(
                        event.getX(),
                        event.getY(),
                        8,
                        "black");
            }
        });

        this.setOnMouseReleased(event -> {
            myGC.beginPath();
            ServerConnector.getInstance().endPathDrawingWindow();
        });

        this.setOnMouseDragged(event -> {

            this.getGraphicsContext2D().moveTo(event.getX()-1,event.getY()-1);
            myGC.lineTo(event.getX(), event.getY());
            myGC.stroke();

            ServerConnector.getInstance().drawLine(
                    event.getX(),
                    event.getY()
            );
        });
    }




    public void beginPath(double x, double y, String color, double thickness){
        myGC.moveTo(x, y);
        Color myColor = Color.BLACK;
        switch (color){
            case "black":
                myColor = Color.BLACK;
                break;
            case "white":
                myColor = Color.WHITE;
            default:
        }

        myGC.setStroke(myColor);
        myGC.setLineWidth(thickness);
    }

    public void endPath(){
        myGC.beginPath();
    }

    public void drawLine(double x, double y){
        myGC.lineTo(x, y);
        myGC.stroke();
    }


}
