import java.awt.Graphics;
import java.awt.Image;

//Stores imaage
public class Sprite {

    public Image image; // the image to be drawn for this sprite

    public Sprite(Image i) {
        image = i;
    } // Sprite

    // return width of image in pixels
    public int getWidth() {
        return image.getWidth(null);
    } // getWidth

    // return height of image in pixels
    public int getHeight() {
        return image.getHeight(null);
    } // getHeight

    // draw the sprite in the graphics object provided at location (x,y)
    public void draw(Graphics g, int x, int y) {
        g.drawImage(image, x, y, null);
    } // draw

} // Sprite