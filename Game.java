/****************************************************************************
 * Name:        Lemonade Tapper's


 * Author:      By Sai Bannur & Surya Narayan
 * Date:        Feb 2021
 * Purpose:     To serve lemonade to customers
 *****************************************************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.ArrayList;
public class Game extends Canvas {


    private BufferStrategy strategy; // take advantage of accelerated graphics
    private boolean waitingForKeyPress = true;
    private boolean logicRequiredThisLoop = false; // true if logic
    private boolean upPressed = false;
    private boolean downPressed = false;
    private boolean servePressed = false; // true if firing
    private boolean gameRunning = true;
    private boolean showLevelUp;
    private boolean showWin;
    private boolean showLose;
    private boolean showMain;
    private boolean showInstructions;
    private int amountServed;
    private int lemonadeTarget;
    private int currentLevel;
    private int max;
    private int min;
    private int range;
    private int rand;
    private long lastServe = 0; // time last served
    private long servingInterval = 300; // interval between serve
    private double moveSpeed = 100; // speed of entities
    public ArrayList entities = new ArrayList(); // list of entities
    private ArrayList removeEntities = new ArrayList(); // list of entities (removed)
    private Row[] rows; // Rows to spawn customers on
    private Entity character; // main character
    private Sprite levelUpPage;
    private Sprite lose;
    private Sprite win;
    private Sprite background;
    private Sprite mainMenu;
    private Sprite instructions;

    //Construct game and set it running.
    public Game() {


        this.currentLevel = 1;
        this.lemonadeTarget = 25;
        this.amountServed = 0;
        this.showMain = true;
        this.showLevelUp = false;
        this.showWin = false;
        this.showLose = false;
        this.showInstructions = false;
        this.rows = new Row[3];

        // create a frame to contain game
        JFrame container = new JFrame("Lemonade Tapper's");

        // get hold the content of the frame
        JPanel panel = (JPanel) container.getContentPane();

        // set up the resolution of the game
        panel.setPreferredSize(new Dimension(1280, 924));
        panel.setLayout(null);

        // set up canvas size and add to frame
        setBounds(0, 0, 1280, 924);
        panel.add(this);

        setIgnoreRepaint(true);

        // makes the window visible
        container.pack();
        container.setResizable(false);
        container.setVisible(true);


        // if user closes window, shutdown game
        container.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            } // windowClosing
        });

        // adds key listener to this canvas
        addKeyListener(new KeyInputHandler());

        // request focus so key events are handled by this canvas
        requestFocus();

        // create buffer strategy to take advantage of accelerated graphics
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        // initialize entities
        initEntities();

        // start the game
        gameLoop();
    } // constructor



    // Initialize character and customer entities.
    // Then add to the array of entities.

    private void initEntities() {

        // creates main character
        character = new MainEntity(this, "sprites/character.png", 880, 510);
        entities.add(character);

        // creates customers for each row of map
        Row cos1 = new Row(this, getRandomSpeed(), 200, 320, this.currentLevel, 1);
        this.rows[0] = cos1;
        Row cos2 = new Row(this, getRandomSpeed(), 120, 500, this.currentLevel, 2);
        this.rows[1] = cos2;
        Row cos3 = new Row(this, getRandomSpeed(), 50, 650, this.currentLevel, 3);
        this.rows[2] = cos3;

    } // initEntities

    // Get the random speed depending on current level
    public int getRandomSpeed() {

        if (this.currentLevel == 1) {
            max = 125;
            min = 75;
            range = max - min + 1;
            rand = (int)(Math.random() * range) + min;
        } else if (this.currentLevel == 2) {
            max = 170;
            min = 125;
            range = max - min + 1;
            rand = (int)(Math.random() * range) + min;
        } else if (this.currentLevel == 3) {
            max = 200;
            min = 130;
            range = max - min + 1;
            rand = (int)(Math.random() * range) + min;
        } // else if


        return rand;
    } // getRandomSpeed

    /* Notification from a game entity that the logic of the game
     * should be run at the next opportunity 
     */
    public void updateLogic() {
        logicRequiredThisLoop = true;
    } // updateLogic

    // No longer draws the entity
    public void removeEntity(Entity entity) {
        removeEntities.add(entity);
    } // removeEntity

    // Move to the next level after completed
    public void levelUp() {

        if (this.currentLevel == 1) {
            this.currentLevel = 2;
            this.lemonadeTarget = 50;
            this.entities.clear();
            this.startGame();
        } else if (this.currentLevel == 2) {
            this.currentLevel = 3;
            this.lemonadeTarget = 100;
            this.entities.clear();
            this.startGame();
        } else if (this.currentLevel == 3) {
            this.entities.clear();
            this.notifyWin();
        } // else if
        this.waitingForKeyPress = true;
        if (!this.showWin && !this.showLose) {
            this.showLevelUp = true;

        } // if

    } // levelUp

    // notifies that the player has lost
    public void notifyDeath() {
        this.lemonadeTarget = 25;
        this.amountServed = 0;
        this.showLose = true;
        this.currentLevel = 1;
        waitingForKeyPress = true;
    } // notifyDeath


    // notifies that the player has beat all levels
    public void notifyWin() {
        this.lemonadeTarget = 25;
        this.amountServed = 0;
        this.showWin = true;
        this.currentLevel = 1;
        waitingForKeyPress = true;
    } // notifyWin

    // Attempts to serve lemonade to customers
    public void tryToFire() {

        // check that we've waited long enough to serve
        if ((System.currentTimeMillis() - lastServe) < servingInterval) {
            return;
        } // if

        // otherwise serve a lemonade
        lastServe = System.currentTimeMillis();
        GlassEntity shot = new GlassEntity(this, "sprites/fullGlass.png",
            character.getX() + 20, character.getY() - 0, -600);
        entities.add(shot);
    } // tryToFire

    /*
     * purpose: Main game loop. Runs throughout game play.
     *          Responsible for the following activities:
     *           - calculates speed of the game loop to update moves
     *           - moves the game entities
     *           - draws the screen contents (entities, text)
     *           - updates game events
     *           - checks input
     */
    public void gameLoop() {

        long lastLoopTime = System.currentTimeMillis();

        // keep loop running until game ends
        while (this.gameRunning) {


            this.mainMenu = SpriteStore.get().getSprite("sprites/blank.jpg");
            this.instructions = SpriteStore.get().getSprite("sprites/blank.jpg");
            this.levelUpPage = SpriteStore.get().getSprite("sprites/blank.jpg");
            this.lose = SpriteStore.get().getSprite("sprites/blank.jpg");
            this.win = SpriteStore.get().getSprite("sprites/blank.jpg");
            long delta = System.currentTimeMillis() - lastLoopTime;
            lastLoopTime = System.currentTimeMillis();

            // get graphics context for the accelerated surface and make it black
            Graphics2D g = (Graphics2D) strategy.getDrawGraphics();
            g.setColor(Color.black);
            g.fillRect(0, 0, 1200, 800);

            (this.background = SpriteStore.get().getSprite("sprites/background.png")).draw(g, 0, 0);

            // draw/moves entities & messages when game begins
            if (!waitingForKeyPress && !this.showMain && !this.showInstructions && !this.showLevelUp && !this.showWin && !this.showLose) {
                g.setFont(new Font("Bree Serif", Font.PLAIN, 20));
                g.setColor(Color.white);
                g.drawString("Target: " + this.lemonadeTarget, 1150, 50);
                g.drawString("Current:" + this.amountServed, 1150, 70);
                for (int i = 0; i < entities.size(); i++) {
                    Entity entity = (Entity) entities.get(i);
                    entity.move(delta);

                } // for
            } // if
            // checks rows from Row.java to spawn customers
            for (int x = 0; x < rows.length; ++x) {
                rows[x].rowCheck();
            }

            // draw all entities
            for (int i = 0; i < entities.size(); i++) {
                Entity entity = (Entity) entities.get(i);
                entity.draw(g);
            } // for


            // Collision between all entities
            for (int i = 0; i < entities.size(); i++) {
                for (int j = i + 1; j < entities.size(); j++) {
                    Entity me = (Entity) entities.get(i);
                    Entity him = (Entity) entities.get(j);

                    if (me.collidesWith(him)) {
                        me.collidedWith(him);
                        him.collidedWith(me);
                    } // if
                } // inner for
            } // outer for


            // remove dead entities
            entities.removeAll(removeEntities);
            removeEntities.clear();

            // run logic if required
            if (logicRequiredThisLoop) {
                for (int i = 0; i < entities.size(); i++) {
                    Entity entity = (Entity) entities.get(i);
                    entity.doLogic();
                } // for
                logicRequiredThisLoop = false;
            } // if

            // if waiting for "any key press", show required pages
            if (waitingForKeyPress) {

                if (this.showMain) {
                    (this.mainMenu = SpriteStore.get().getSprite("sprites/HomePage.png")).draw(g, 0, 0);

                } else if (this.showInstructions) {
                    (this.instructions = SpriteStore.get().getSprite("sprites/instructions.png")).draw(g, 0, 0);

                } else if (this.showLevelUp) {
                    (this.levelUpPage = SpriteStore.get().getSprite("sprites/LevelUp.png")).draw(g, 0, 0);
                    g.setFont(new Font("Bree Serif", Font.PLAIN, 40));
                    g.setColor(Color.white);
                    g.drawString((currentLevel - 1) + "", 720, 582);
                } else if (this.showLose) {
                    (this.lose = SpriteStore.get().getSprite("sprites/LoseScreen.png")).draw(g, 0, 0);
                } else if (this.showWin) {
                    (this.lose = SpriteStore.get().getSprite("sprites/WinScreen.png")).draw(g, 0, 0);
                } //else if
            } // if

            // clear graphics and flip buffer
            g.dispose();
            strategy.show();

            // main character should not move without user input
            character.setVerticalMovement(0);

            // respond to user moving ship
            if ((upPressed) && (!downPressed)) {
                character.setVerticalMovement(moveSpeed);
            } // if
            else if ((!upPressed) && (downPressed)) {
                character.setVerticalMovement(-moveSpeed);
            } // else


            // if spacebar pressed, serve lemonade
            if (servePressed) {

                this.amountServed++;

                // Required lemonade to serve to complete level
                if (this.currentLevel == 1 && this.amountServed == 25) {
                    levelUp();
                } else if (this.currentLevel == 2 && this.amountServed == 50) {
                    levelUp();
                } else if (this.currentLevel == 3 && this.amountServed == 100) {
                    levelUp();
                } // else if

                tryToFire();
            } // if

            // pause
            try {
                Thread.sleep(100);
            } catch (Exception e) {}

        } // while

    } // gameLoop



    //start a fresh game, clear old data
    private void startGame() {

        // clear out any existing entities and initalize a new set
        entities.clear();
        initEntities();
        this.amountServed = 0;
        this.showMain = false;

        // blank out any keyboard settings that might exist
        upPressed = false;
        downPressed = false;
        servePressed = false;

    } // startGame



    // handles keyboard input from the user
    private class KeyInputHandler extends KeyAdapter {

        // the number of key presses since
        // waiting for 'any' key press
        private int pressCount = 1;

        // Handle keyPressed,
        // keyReleased and keyTyped events.
        public void keyPressed(KeyEvent e) {

            // if waiting for keypress to start game, do nothing
            if (waitingForKeyPress) {
                return;
            } // if


            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = true;

            } // if

            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                servePressed = true;
            } // if

            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downPressed = true;
            } // if

        } // keyPressed

        public void keyReleased(KeyEvent e) {
            // if waiting for keypress to start game, do nothing
            if (waitingForKeyPress) {
                return;
            } // if

            // respond to move up, down or serve

            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = false;
            } // if

            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downPressed = false;
            } // if

            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                servePressed = false;
            } // if

        } // keyReleased

        // Tracks which key was pressed
        public void keyTyped(KeyEvent e) {

            if (waitingForKeyPress) {
                if (this.pressCount == 1) {
                    if (Game.this.showInstructions) {
                        Game.this.showInstructions = false;
                        Game.this.showMain = true;
                    } // if 
                    else if (Game.this.showLevelUp && e.getKeyChar() == 'n') {
                        Game.this.showLevelUp = false;
                        waitingForKeyPress = false;
                        Game.this.startGame();
                    } else if (Game.this.showLose && e.getKeyChar() == 'm') {
                        Game.this.showLose = false;

                        Game.this.showMain = true;
                    } else if (Game.this.showWin && e.getKeyChar() == 'm') {
                        Game.this.showWin = false;

                        Game.this.showMain = true;
                    } else if (Game.this.showMain && e.getKeyChar() == '1') {

                        Game.this.waitingForKeyPress = false;
                        Game.this.startGame();
                        this.pressCount = 0;
                    } // else if
                    else if (Game.this.showMain && e.getKeyChar() == '2') {
                        Game.this.showInstructions = true;
                        Game.this.showMain = false;
                    } // else if

                } // if
                else {
                    ++this.pressCount;
                } // else
            } // if

            // if escape is pressed, end game
            if (e.getKeyChar() == 27) {
                System.exit(0);
            } // if

        } // keyTyped

    } // class KeyInputHandler

    public void addEntity(final Entity e) {
        this.entities.add(e);
    } // addEntity




    public static void main(String[] args) {

        // instantiate this object
        new Game();
    } // main
} // Game
