// The lemonade to be served
public class ShotEntity extends Entity {

    private boolean used = false; // true if shot hits something
    private Game game;
    private int returnedGlass;
    private int posX;
    private int posY;

    // Information needed to create the lemonade
    public ShotEntity(Game g, String r, int newX, int newY, int moveSpeed) {
        super(r, newX, newY); // calls the constructor in Entity
        game = g;
        dx = moveSpeed;
    } // constructor

    // Movement of each glass
    public void move(long delta) {
        super.move(delta); // calls the move method in Entity

        // Movement restrictions
        if (x < 200) {
            game.notifyDeath();
        } // if
        else if (this.dx > 0.0 && this.y < 340 && this.y > 260 && this.x > 805.0) {
            game.notifyDeath();
        } else if (this.dx > 0.0 && this.y < 530 && this.y > 500 && this.x > 900.0) {
            game.notifyDeath();
        } else if (this.dx > 0.0 && this.y < 680 && this.y > 620 && this.x > 990.0) {
            game.notifyDeath();
        } // else if

    } // move



    // The lemonade glass has collided with another object
    public void collidedWith(Entity other) {

        // Prevents one lemonade given to two people 
        if (used) {
            return;
        } // if



        // if it has hits a customer, return glass or remove it
        if (other instanceof cosEntity) {


            posX = this.getX();
            posY = this.getY();

            game.entities.remove(this);
            returnedGlass = 1 + (int)(Math.random() * 7);

            // A chance it returns an empty glass
            if (returnedGlass == 3 || returnedGlass == 6 || returnedGlass == 2) {

                ShotEntity shot = new ShotEntity(game, "sprites/emptyGlass.png", posX, posY, 90);
                game.entities.add(shot);
                used = true;
            } //  if

        } // if

    } // collidedWith

} // ShotEntity class