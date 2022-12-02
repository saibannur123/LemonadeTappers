//Represents the main character

public class MainEntity extends Entity {

    private Game game; // the game in which the character exists


    // Creates/displays the main character with sprite & x/y coordinates
    public MainEntity(Game g, String r, int newX, int newY) {
        super(r, newX, newY); // calls the constructor in Entity
        game = g;
    } // constructor


    // How the main character moves
    public void move(long delta) {

        // Main characters moving restrictions
        if ((dy < 0) && (y <= 540) && (y >= 480)) {
            x = 810;
            y = 350;
        } else if ((dy < 0) && (y <= 349) && (y >= 320)) {
            x = 960;
            y = 680;
        } else if ((dy < 0) && (y <= 679) && (y >= 649)) {
            x = 890;
            y = 520;
        } // else if

        // Main characters moving restrictions
        if ((dy > 0) && (y >= 330) && (y <= 370)) {
            x = 890;
            y = 510;
        } else if ((dy > 0) && (y >= 650) && (y <= 700)) {
            x = 810;
            y = 330;
        } else if ((dy > 0) && (y >= 500) && (y <= 530)) {
            x = 970;
            y = 660;
        } // else if   


        super.move(delta); // calls the move method in Entity
    } // move


    // Main character has collided with an other object
    public void collidedWith(Entity other) {

        // remove the empty glass if it collides with it
        if (other instanceof GlassEntity) {

            game.removeEntity(other);

        } // if
    } // collidedWith    

} // MainEntity