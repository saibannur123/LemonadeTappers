public class cosEntity extends Entity {


    private Game game;
    private double moveSpeed = 75;
    private boolean used = false;

    public cosEntity(final Game g, final String r, final int newX, final int newY, final int s) {


        super(r, newX, newY);
        this.game = g;
        this.dx = s;
    } // cosEntity

    @Override
    public void move(final long delta) {

        if (this.dx < 0.0 && this.y == 320 && this.x < 280.0) {
            this.game.removeEntity(this);
        } else if (this.dx < 0.0 && this.y == 500 && this.x < 210.0) {
            this.game.removeEntity(this);
        } else if (this.dx < 0.0 && this.y == 650 && this.x < 96.0) {
            this.game.removeEntity(this);
        } // if else if


        if (this.dx > 0.0 && this.y == 320 && this.x > 730.0) {
            game.notifyDeath();
        } else if (this.dx > 0.0 && this.y == 500 && this.x > 770.0) {
            game.notifyDeath();
        } else if (this.dx > 0.0 && this.y == 650 && this.x > 850.0) {
            game.notifyDeath();
        } // else if
        super.move(delta);


    } // move


    @Override
    public void collidedWith(Entity other) {

        if (used) {
            return;
        } // if

        // if it has hit a glass, reverse direction
        if (other instanceof GlassEntity) {

            dx = -moveSpeed * 4;

        } // if

    } // collidedWith


} // cosEntity