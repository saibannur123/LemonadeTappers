// Displays & spawns random customer accordingly

public class Row {

    private int x;
    private int y;
    private int row;
    private int level;
    private int speed;
    private int randSprite;
    private long spawn;
    private long lastSpawned = 0;
    private Entity currentEntity;
    private Game game;

    // Information needed to create each customer
    public Row(Game g, int s, int posX, int posY, int l, int r) {

        this.game = g;
        this.y = posY;
        this.x = posX;
        this.speed = s;
        this.level = l;
        this.row = r;
        this.init();

    } // Row

    // Initializes and displays the random customers 
    // with their random speed, sprite, and coordinates
    public void init() {

        // Gets a random number so each sprites are different
        int sprite = getRandomPicture();

        // Costumer generation
        if (sprite == 1) {
            cosEntity costumer = new cosEntity(this.game, "sprites/boy2.png", this.x, this.y, this.speed);
            this.currentEntity = costumer;
            this.game.addEntity(costumer);

        } else if (sprite == 2) {
            cosEntity costumer = new cosEntity(this.game, "sprites/boy1.png", this.x, this.y, this.speed);
            this.currentEntity = costumer;
            this.game.addEntity(costumer);

        } else if (sprite == 3) {
            cosEntity costumer = new cosEntity(this.game, "sprites/hat.png", this.x, this.y, this.speed);
            this.currentEntity = costumer;
            this.game.addEntity(costumer);

        } else if (sprite == 4) {
            cosEntity costumer = new cosEntity(this.game, "sprites/girl.png", this.x, this.y, this.speed);
            this.currentEntity = costumer;
            this.game.addEntity(costumer);

        } // else if

    } // init

    // generates a random number so each of the
    // customers are different sprites    
    public int getRandomPicture() {

        randSprite = 1 + (int)(Math.random() * 4);

        return randSprite;
    } // getRandomPicture

    // Frequency of costumers depending on level
    public boolean spawnCostumer() {


        if (this.level == 1) {
            spawn = 500 + (long) Math.random() * 100;
            if ((System.currentTimeMillis() - lastSpawned) < spawn) {
                return false;
            } else {
                lastSpawned = System.currentTimeMillis();
                return true;
            } // else
        } // if
        else if (this.level == 2) {
            spawn = 300 + (long) Math.random() * 100;
            if ((System.currentTimeMillis() - lastSpawned) < spawn) {
                return false;
            } else {
                lastSpawned = System.currentTimeMillis();
                return true;
            } // else
        } else if (this.level == 3) {
            spawn = 200 + (long) Math.random() * 50;
            if ((System.currentTimeMillis() - lastSpawned) < spawn) {
                return false;
            } else {
                lastSpawned = System.currentTimeMillis();
                return true;
            } // else
        } else {

            return false;

        } // else

    } // spawnCostumer

    // Decides when to generate a new customer depending on 
    // time and distance of separation between each costumer  
    public void rowCheck() {

        boolean check = spawnCostumer();

        if (check == true && this.currentEntity.getX() > (290 + (int)(Math.random() * 300)) && row == 1) {
            this.init();
        } else if (check == true && this.currentEntity.getX() > 220 && row == 2) {
            this.init();
        } else if (check == true && this.currentEntity.getX() > 180 && row == 3) {
            this.init();
        } // else if

    } // rowCheck

    // The current entity
    public Entity getCurrentEntity() {
        return this.currentEntity;
    } // getCurrentEntity


} // Row