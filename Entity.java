import java.awt.*;

//Collision and Movement of all Entities in game
public abstract class Entity {


    protected double x;
    protected double y;
    protected Sprite sprite; // entities sprite
    protected double dx; // horizontal speed
    protected double dy; // vertical speed

    private Rectangle me = new Rectangle(); // bounding rectangle of this entity
    private Rectangle him = new Rectangle(); // bounding rectangle of other entities

    // Get sprite, x & y values of each Entity
    public Entity(String r, int newX, int newY) {
        this.me = new Rectangle();
        this.him = new Rectangle();
        x = newX;
        y = newY;
        sprite = (SpriteStore.get()).getSprite(r);
    } // constructor


    // update location of entity based on move speeds
    public void move(long delta) {

        x += (delta * dx) / 1000;
        y += (delta * dy) / 1000;
    } // move

    // get set velocities
    public void setHorizontalMovement(double newDX) {
        dx = newDX;
    } // setHorizontalMovement

    public void setVerticalMovement(double newDY) {
        dy = -newDY;
    } // setVerticalMovement

    public double getHorizontalMovement() {
        return dx;
    } // getHorizontalMovement

    public double getVerticalMovement() {
        return dy;
    } // getVerticalMovement

    // get X position
    public int getX() {
        return (int) x;
    } // getX

    // get Y position
    public int getY() {
        return (int) y;
    } // getY


    //Draw this entity provided at (x,y)
    public void draw(Graphics g) {
        sprite.draw(g, (int) x, (int) y);
    } // draw

    //Do the logic associated with this entity. 
    public void doLogic() {}

    //check if this entity collides with the other.
    public boolean collidesWith(Entity other) {
        me.setBounds((int) x, (int) y, sprite.getWidth(), sprite.getHeight());
        him.setBounds(other.getX(), other.getY(),
            other.sprite.getWidth(), other.sprite.getHeight());
        return me.intersects(him);
    } // collidesWith

    // notification that this entity collided with another
    public abstract void collidedWith(Entity other);

} // Entity