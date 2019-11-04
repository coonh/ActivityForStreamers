package webcam;

import org.bytedeco.javacv.FrameFilter;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Iterator;

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
        writeImage(out);
        myImg.flush();
        out.flush();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        myImg = readImage(in);
    }

    public BufferedImage readImage(InputStream in) throws IOException {


        ImageInputStream stream = ImageIO.createImageInputStream(in);
        try{
            Iterator<ImageReader> iter = ImageIO.getImageReaders(stream);
            while (iter.hasNext()) {

                ImageReader reader = iter.next();

                reader.setInput(stream);
                BufferedImage image = null;
                try {
                    image = reader.read(0);

                } catch (IIOException e) {
                    e.printStackTrace();
                }
                finally {
                    reader.dispose();
                    return image;
                }
            }
            return null;
        }
        finally {
            if (stream != null){
                try {
                    stream.close();
                } catch (Exception e){ }
            }
        }
    }

    public void writeImage(OutputStream out) throws IOException {


        ImageInputStream stream = ImageIO.createImageOutputStream(out);
        try{
            Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("JPG");
            while (iter.hasNext()) {

                ImageWriter writer = iter.next();

                writer.setOutput(stream);
                try {
                    writer.write(myImg);
                } catch (IIOException e) {
                    e.printStackTrace();
                }
                finally {
                    writer.dispose();
                }
            }
        }
        finally {
            if (stream != null){
                try {
                    stream.close();
                } catch (Exception e){ }
            }
        }
    }
}
