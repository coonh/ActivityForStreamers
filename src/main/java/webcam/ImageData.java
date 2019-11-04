package webcam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ImageData implements Serializable {

    private transient BufferedImage myImg;
    private String name;



    public ImageData(BufferedImage myImg, String name) {
        this.myImg = myImg;
        this.name = name;
    }




    public BufferedImage getMyImg() {
        return myImg;
    }

    public String getName() {
        return name;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        ImageIO.write(myImg, "jpg", out);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        myImg = ImageIO.read(in);

    }
}
