import java.util.*;
import processing.core.PApplet;
import processing.core.PImage;

public class KonQuestGame extends PApplet
{
    // static reference to the running Processing sketch so other classes can draw
    public static PApplet sketch;

    public static boolean mainMenu = true;

    public static PImage mainMenuImg;

    public static boolean pauseMenu = false;

    public static boolean animationPlaying = false;

    public static int animationFrame = 0;

    // Menu buttons
    private Button playButton;
    private Button optionsButton;
    private Button exitButton;
    private Button backButton;
    // Options menu state
    private boolean optionsMenu = false;

    // shared coins list so main() can populate it before the sketch starts
    public static ArrayList<Coin> coins = new ArrayList<>();

    public static Player p1;
    public static ArrayList<Enemy> enemies = new ArrayList<>();

    public static LevelObject barrel;

    public static int score = 0, lives = 3, level = 1, camX = 50, camY = 50, tileSize = 50;
    public static int[][] tiles = new int[102][102];

    public static boolean[] keys = new boolean[4];
    private char[] keyBindings = {'a', 'd', 'w', 's'};  // Default bindings for left, right, jump, sprint
    private final char[] DEFAULT_BINDINGS = {'a', 'd', 'w', 's'};  // Store defaults for reset
    private int selectedBinding = -1;  // -1 means no key is being rebound
    private boolean waitingForKey = false;
    private String bindingError = "";  // Show error message when key is already in use
    private int errorTimer = 0;        // How long to show the error

    public static PImage[] tilesImg = new PImage[4];
    public static int[] collisionTiles = {1, 2};

    public static void main(String[] args)
    {
        // Random random = new Random();

        // for (int i = 0; i < tiles.length; i++) {
        //     for (int j = 0; j < tiles[i].length; j++) {
        //         //randomly assign 1 or 0 to each tile
        //         if (random.nextDouble() < 0.1 || j == tiles[i].length - 1 || i == 0 || i == tiles.length - 1 || j == 0) {
        //             if (random.nextDouble() < 0.5) {
        //                 tiles[i][j] = 2;
        //             } else {
        //                 tiles[i][j] = 1;
        //             }
        //             if (random.nextDouble() < 0.15 && j != 0 && (j != 1 && tiles[i][j-2] != 3)) {
        //                 tiles[i][j-1] = 3;
        //             }
        //         }
        //         else {
        //             tiles[i][j] = 0;
        //         }
        //     }
        // }
        // tiles[1][1] = 0;
        // tiles[1][2] = 0;

        Level level1 = new Level(tiles, tileSize);
        level1.readFromFile("data/1.txt");
        tiles = level1.getTiles();

        // print out the tiles array to the console for verification
        // for (int j = 0; j < tiles[0].length; j++) {
        //     String row = "";
        //     for (int i = 0; i < tiles.length; i++) {
        //         row += tiles[i][j];
        //     }
        //     System.out.println(row);
        // }
        
        Lizard l;
        Enemy e;
        p1 = new Player(2*tileSize, tileSize, 50, 50, score, lives);
        for(int i = 0; i < 20; i++) {
            l = new Lizard(100*i + 100, 100);
            e = new Enemy(100*i + 100, 100);
            enemies.add(e);
            enemies.add(l);
        }
        
        // create a couple of test coins before starting the Processing sketch
        coins.add(new Coin(100, 100, 1));
        coins.add(new Coin(200, 150, 1));
        coins.add(new Coin(300, 80, 1));


        // start the Processing sketch
        PApplet.main("KonQuestGame");
    }

    public static void setPosition(int x, int y, int width, int height)
    {
        p1.x = x;
        p1.speedX = 0;
        p1.y = y;
        p1.speedY = 0;
        if (p1.x-50 < width / 2) {
            camX = 50;
        } else if (p1.x+50 > tiles.length * tileSize - width / 2) {
            camX = tiles.length * tileSize - width;
        } else {
            camX = p1.x - width / 2;
        }
        if (p1.y-50 < height / 2) {
            camY = 50;
        } else if (p1.y+50 > (tiles[0].length + 1) * tileSize - height / 2) {
            camY = (tiles[0].length + 1) * tileSize - height - 50;
        } else {
            camY = p1.y - height / 2;
        }
    }

    public static void nextLevel()
    {
        level++;
        Level level = new Level(tiles, tileSize);
        level.readFromFile("data/" + level + ".txt");
        tiles = level.getTiles();
        setPosition(50, 4950, sketch.width, sketch.height);
    }

    public void display()
    {
        
    }

    @Override
    public void settings() {
        // Set up the window size
        fullScreen();
    }

    @Override
    public void setup() {
        // store reference to sketch so other classes can draw
        // Set initial background color
        sketch = this;
        sketch.windowResizable(true);

        background(0,118,248);
        setPosition(50, 4950, width, height);

                // Initialize menu buttons - stacked vertically in top left
        int buttonX = 400;  // Distance from left edge
        int startY = 450;   // Distance from top edge
        int buttonSpacing = 150;  // Space between buttons

         int MAIN_MENU_BUTTON_WIDTH = 200;
        int MAIN_MENU_BUTTON_HEIGHT = 60;
        
        playButton = new Button(buttonX-50, startY-10, 
                              MAIN_MENU_BUTTON_WIDTH+100, MAIN_MENU_BUTTON_HEIGHT+20, "PLAY", 
                              color(0, 150, 0), color(100, 200, 100));
        
        optionsButton = new Button(buttonX, startY + MAIN_MENU_BUTTON_HEIGHT + buttonSpacing,
                                 MAIN_MENU_BUTTON_WIDTH, MAIN_MENU_BUTTON_HEIGHT, "OPTIONS",
                                 color(0, 0, 150), color(100, 100, 200));
        
        exitButton = new Button(buttonX, startY + (MAIN_MENU_BUTTON_HEIGHT + buttonSpacing) * 2,
                              MAIN_MENU_BUTTON_WIDTH, MAIN_MENU_BUTTON_HEIGHT, "EXIT",
                              color(150, 0, 0), color(200, 100, 100));

        // Load tile images
        for (int i = 0; i < tilesImg.length; i++) {
            if (i != 0)
            tilesImg[i] = loadImage("Tile" + i + ".png");
        }

        barrel = new LevelObject(loadImage("Barrel.png"), 500, 4900, 50, 50);

        mainMenuImg = loadImage("MainMenu.png");
        mainMenuImg.resize(width, height);
    }

    @Override
    public void draw() {
        if (mainMenu) {
            background(0, 0, 0);
            image(mainMenuImg, 0, 0);
            
                // If options menu is not open, show main buttons
                if (!optionsMenu) {
                    // Update and display buttons
                    playButton.update(mouseX, mouseY, mousePressed);
                    optionsButton.update(mouseX, mouseY, mousePressed);
                    exitButton.update(mouseX, mouseY, mousePressed);

                    playButton.display();
                    optionsButton.display();
                    exitButton.display();

                    // Handle button clicks (use consumeClick to act on single click)
                    if (playButton.consumeClick()) {
                        mainMenu = false;
                    }

                    if (optionsButton.consumeClick()) {
                        // Open options overlay
                        optionsMenu = true;
                    }

                    if (exitButton.consumeClick()) {
                        exit();
                    }
                }
                else {
                    // Options menu open: semi-transparent overlay
                    pushStyle();
                    fill(20, 20, 30, 200); // Added alpha for transparency
                    rect(0, 0, width, height);

                    // Options content
                        // Options content - Main title
                        fill(255);
                        textAlign(CENTER, TOP);
                        textSize(48);
                        text("OPTIONS", width/2, height/8);
                    
                        // Center the controls section in the middle of the screen
                        int centerX = width/2;
                        int startY = height/4;  // Start a quarter down the screen
                    
                        // Keybinds section title
                        textAlign(CENTER, TOP);
                        textSize(32);
                        text("CONTROLS", centerX, startY);
                    
                        // Calculate positions for the control section
                        int sectionStartX = centerX - 200;  // Left align section content
                        int bindingY = startY + 100;  // Start below the CONTROLS title
                        int bindingSpacing = 45;  // Increased spacing between bindings
                        String[] bindingLabels = {"Move Left", "Move Right", "Jump", "Sprint"};
                    
                        // Draw reset button centered above the bindings
                        pushStyle();
                        int resetBtnWidth = 160;
                        int resetBtnHeight = 35;
                        int resetBtnX = centerX - resetBtnWidth/2;
                        int resetBtnY = bindingY - resetBtnHeight - 50;
                    
                        if (mousePressed && mouseX >= resetBtnX && mouseX <= resetBtnX + resetBtnWidth &&
                            mouseY >= resetBtnY && mouseY <= resetBtnY + resetBtnHeight) {
                            fill(150, 0, 0);  // Darker red when clicked
                            // Reset bindings
                            for (int i = 0; i < keyBindings.length; i++) {
                                keyBindings[i] = DEFAULT_BINDINGS[i];
                            }
                        } else {
                            fill(200, 0, 0);  // Normal red
                        }
                        rect(resetBtnX, resetBtnY, resetBtnWidth, resetBtnHeight);
                        fill(255);
                        textAlign(CENTER, CENTER);
                        textSize(20);
                        text("Reset to Default", centerX, resetBtnY + resetBtnHeight/2);
                    
                    for (int i = 0; i < keyBindings.length; i++) {
                            // Draw each control row
                            textAlign(RIGHT, CENTER);
                            textSize(20);
                            text(bindingLabels[i] + ": ", sectionStartX + 150, bindingY + i * bindingSpacing);

                            // Key button background
                            int keyBtnX = sectionStartX + 170;
                            int keyBtnY = bindingY + i * bindingSpacing - 15;
                            if (selectedBinding == i) {
                                fill(200, 200, 0);  // Yellow when selected
                            } else {
                                fill(60, 60, 60);  // Dark gray normally
                            }
                            rect(keyBtnX, keyBtnY, 80, 30);

                            // Key text
                            fill(255);
                            textAlign(CENTER, CENTER);
                            if (selectedBinding == i && waitingForKey) {
                                text("Press Key", keyBtnX + 40, keyBtnY + 15);
                            } else {
                                text(String.valueOf(keyBindings[i]).toUpperCase(), keyBtnX + 40, keyBtnY + 15);
                            }
                        
                        // Check for clicks on key buttons
                            if (mousePressed && mouseX >= keyBtnX && mouseX <= keyBtnX + 80 &&
                                mouseY >= keyBtnY && mouseY <= keyBtnY + 30) {
                            selectedBinding = i;
                            waitingForKey = true;
                        }
                    }
                    
                    // Show binding error if any
                    if (!bindingError.isEmpty() && errorTimer > 0) {
                        pushStyle();
                        fill(200, 0, 0);
                            textAlign(CENTER, CENTER);
                            text(bindingError, centerX, bindingY + 6 * bindingSpacing);
                        errorTimer--;
                        if (errorTimer == 0) {
                            bindingError = "";
                        }
                    }
                    
                    if (waitingForKey) {
                        pushStyle();
                        fill(0, 0, 0, 200);
                        rect(width/2 - 200, height/2 - 50, 400, 100);
                        fill(255);
                        textAlign(CENTER, CENTER);
                        textSize(24);
                        text("Press any key to rebind\nESC to cancel", width/2, height/2);
                    }

                    // Update and display BACK button at bottom left
                    if (backButton == null) {
                        backButton = new Button(80, height - 100, 200, 60, "BACK", color(100,100,100), color(150,150,150));
                    }
                    backButton.update(mouseX, mouseY, mousePressed);
                    backButton.display();
                    if (backButton.consumeClick()) {
                        optionsMenu = false;
                    }
                }

            text(mouseX + ", " + mouseY, 50, 10);
        }
        else if(animationPlaying) {
            if (p1.dead) {
                if (animationFrame < 30) {
                    background(0,118,248);
                    fill(0);
                    for (int i = 0; i < tiles.length; i++) {
                        for (int j = 0; j < tiles[i].length; j++) {
                            if (tiles[i][j] != 0) {
                                //Only draw tiles that are on screen (with a little buffer)
                                if (i * tileSize - camX < -tileSize || i * tileSize - camX > width + tileSize || j * tileSize - camY < -tileSize || j * tileSize - camY > height + tileSize) {
                                continue;
                                }

                                //rect(i * tileSize - camX, j * tileSize - camY, tileSize, tileSize);
                                image(tilesImg[tiles[i][j]], i * tileSize - camX, j * tileSize - camY, tileSize, tileSize);
                            }
                        }
                    }
                    p1.display(camX, camY);
                    for(Enemy e : enemies) {
                        e.display(camX, camY);
                    }
                    barrel.display(camX, camY);
                    // Play death animation (simple fade out for example)
                    fill(0, 0, 0, map(animationFrame, 0, 30, 0, 255));
                    rect(0, 0, width, height);
                } else if (animationFrame == 30) {
                    p1.outOfBounds = false;
                    setPosition(50, 4950, width, height);
                } else if (animationFrame < 45) {
                    // Hold black screen for a moment
                } else if (animationFrame < 75) {
                    background(0,118,248);
                    fill(0);
                    for (int i = 0; i < tiles.length; i++) {
                        for (int j = 0; j < tiles[i].length; j++) {
                            if (tiles[i][j] != 0) {
                                //Only draw tiles that are on screen (with a little buffer)
                                if (i * tileSize - camX < -tileSize || i * tileSize - camX > width + tileSize || j * tileSize - camY < -tileSize || j * tileSize - camY > height + tileSize) {
                                continue;
                                }

                                //rect(i * tileSize - camX, j * tileSize - camY, tileSize, tileSize);
                                image(tilesImg[tiles[i][j]], i * tileSize - camX, j * tileSize - camY, tileSize, tileSize);
                            }
                        }
                    }
                    p1.display(camX, camY);
                    for(Enemy e : enemies) {
                        e.display(camX, camY);
                    }
                    barrel.display(camX, camY);
                    fill(0, 0, 0, map(75 - animationFrame, 0, 30, 0, 255));
                    rect(0, 0, width, height);
                } else {
                    animationPlaying = false;
                    animationFrame = 0;
                    p1.dead = false;
                }
            }
            animationFrame++;
        }
        else
        {
            // clear
            background(0,118,248);
            //background((int)random(255), (int)random(255), (int)random(255));
            
            // Draw tiles
            fill(0);
            for (int i = 0; i < tiles.length; i++) {
                for (int j = 0; j < tiles[i].length; j++) {
                    if (tiles[i][j] != 0) {
                        //Only draw tiles that are on screen (with a little buffer)
                        if (i * tileSize - camX < -tileSize || i * tileSize - camX > width + tileSize || j * tileSize - camY < -tileSize || j * tileSize - camY > height + tileSize) {
                            continue;
                        }

                        //rect(i * tileSize - camX, j * tileSize - camY, tileSize, tileSize);
                        image(tilesImg[tiles[i][j]], i * tileSize - camX, j * tileSize - camY, tileSize, tileSize);
                    }
                }
            }

            //Update Player
            if (keyPressed) {
                if (keys[0]) {
                    if (keys[3]) {
                        p1.run('l');
                    } else 
                    {
                        p1.walk('l');
                    }
                } else if (keys[1]) {
                    if (keys[3]) {
                        p1.run('r');
                    } else 
                    {
                        p1.walk('r');
                    }
                }
                if (keys[2]) {
                    if (!p1.inAir) {
                        p1.jump();
                        p1.inAir = true;
                    }
                }
            }

            p1.update(tiles, collisionTiles, keys[0] || keys[1]);
            for(Enemy e : enemies) {
                e.ai(tiles, p1, collisionTiles);
                e.update(tiles, p1, collisionTiles);
            }

            p1.display(camX, camY);
            for(Enemy e : enemies) {
                e.display(camX, camY);
            }
            barrel.display(camX, camY);

            if (p1.outOfBounds && p1.x >= tiles.length * tileSize) {
                nextLevel();
            }

            if ((p1.outOfBounds && p1.y > (tiles[0].length + 1) * tileSize) || p1.dead) {
                p1.die();
                animationPlaying = true;
            }

            for (int i = 0; i < enemies.size(); i++) {
                // Check if enemy is out of bounds
                if (enemies.get(i).outOfBounds && enemies.get(i).y > (tiles[0].length + 1) * tileSize) {
                    enemies.remove(i);
                    i--;
                }

                // Check if enemy is dead
                else if (enemies.get(i).health <= 0) {
                    enemies.remove(i);
                    i--;
                }
            }

            // Camera slowly follows player
            if (p1.x-50 < width / 2) {
                camX += -(camX-50) * 0.05;
            } else if (p1.x+50 > tiles.length * tileSize - width / 2) {
                camX += (tiles.length * tileSize - (camX+50) - width) * 0.05;
            } else {
                camX += (p1.x - (camX) - width / 2) * 0.05;
            }
            if (p1.y-50 < height / 2) {
                camY += -(camY-50) * 0.05;
            } else if (p1.y+50 > (tiles[0].length + 1) * tileSize - height / 2) {
                camY += ((tiles[0].length + 1) * tileSize - (camY+50) - height) * 0.05;
            } else {
                camY += (p1.y - (camY) - height / 2) * 0.05;
            }

            // draw coins added from main/setup and compute total value
            int totalCoins = 0;
            for (Coin c : coins) { // for each loop
                // call the coin's display method which will use the static sketch reference
                c.display(camX, camY);
                totalCoins += c.getValue();
            }

            // draw total value in red at the top-right corner
            fill(255, 0, 0);
            textAlign(RIGHT, TOP);
            textSize(16);
            text("Total value: " + totalCoins, width - 10, 10); //x is 10px from the right edge, y is 10px from the top
        }
    }

    @Override
    public void keyPressed() {
        if (waitingForKey && optionsMenu) {
            if (key == ESC) {
                // Cancel rebinding
                waitingForKey = false;
                selectedBinding = -1;
            } else if (key != CODED) {
                // Check for duplicate keys
                char newKey = Character.toLowerCase(key);
                boolean isDuplicate = false;
                for (int i = 0; i < keyBindings.length; i++) {
                    if (i != selectedBinding && keyBindings[i] == newKey) {
                        isDuplicate = true;
                        bindingError = "Key '" + newKey + "' is already in use!";
                        errorTimer = 120; // Show error for 2 seconds (60 frames/second)
                        break;
                    }
                }
                
                if (!isDuplicate) {
                    // Assign new key binding
                    keyBindings[selectedBinding] = newKey;
                    waitingForKey = false;
                    selectedBinding = -1;
                }
            }
            key = 0;  // Prevent ESC from triggering menu
            return;
        }

        if (key == ESC) {
            key = 0; // This prevents Processing from closing on ESC key
            mainMenu = !mainMenu;
        }
        
        // Check custom keybindings
        char pressedKey = Character.toLowerCase(key);
        if (pressedKey == keyBindings[0]) {
            keys[0] = true;
        }
        if (pressedKey == keyBindings[1]) {
            keys[1] = true;
        }
        if (pressedKey == keyBindings[2]) {
            keys[2] = true;
        }
        if (pressedKey == keyBindings[3]) {
            keys[3] = true;
        }
    }

    @Override
    public void keyReleased() {
        // Check custom keybindings
        char releasedKey = Character.toLowerCase(key);
        if (releasedKey == keyBindings[0]) {
            keys[0] = false;
        }
        if (releasedKey == keyBindings[1]) {
            keys[1] = false;
        }
        if (releasedKey == keyBindings[2]) {
            keys[2] = false;
        }
        if (releasedKey == keyBindings[3]) {
            keys[3] = false;
        }
    }
}
