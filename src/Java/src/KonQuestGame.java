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

    // Menu buttons
    private Button playButton;
    private Button optionsButton;
    private Button exitButton;
    private static final int MAIN_MENU_BUTTON_WIDTH = 200;
    private static final int MAIN_MENU_BUTTON_HEIGHT = 60;

    // shared coins list so main() can populate it before the sketch starts
    public static ArrayList<Coin> coins = new ArrayList<>();

    public static Player p1;
    public static ArrayList<Enemy> enemies = new ArrayList<>();
    public static ArrayList<Lizard> lizards = new ArrayList<>();

    public static int score = 0, lives = 3, level = 1, camX = 50, camY = 50, tileSize = 50;
    public static int[][] tiles = new int[102][102];

    public static boolean[] keys = new boolean[4];

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
            lizards.add(l);
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

        // Initialize menu buttons
        int buttonY = height - 200;
        playButton = new Button(width/2 - MAIN_MENU_BUTTON_WIDTH - 120, buttonY, MAIN_MENU_BUTTON_WIDTH, MAIN_MENU_BUTTON_HEIGHT, "PLAY", color(0, 150, 0), color(100, 200, 100));
        
        optionsButton = new Button(width/2 - MAIN_MENU_BUTTON_WIDTH/2, buttonY, MAIN_MENU_BUTTON_WIDTH, MAIN_MENU_BUTTON_HEIGHT, "OPTIONS", color(0, 0, 150), color(100, 100, 200));
        
        exitButton = new Button(width/2 + 120, buttonY, MAIN_MENU_BUTTON_WIDTH, MAIN_MENU_BUTTON_HEIGHT, "EXIT", color(150, 0, 0), color(200, 100, 100));

        // Load tile images
        for (int i = 0; i < tilesImg.length; i++) {
            if (i != 0)
            tilesImg[i] = loadImage("Tile" + i + ".png");
        }

        mainMenuImg = loadImage("MainMenu.png");
        mainMenuImg.resize(width, height);
    }

    @Override
    public void draw() {
        if (mainMenu) {
            background(0, 0, 0);
            image(mainMenuImg, 0, 0);
            
            // Update and display buttons
            playButton.update(mouseX, mouseY, mousePressed);
            optionsButton.update(mouseX, mouseY, mousePressed);
            exitButton.update(mouseX, mouseY, mousePressed);
            
            playButton.display();
            optionsButton.display();
            exitButton.display();
            
            // Handle button clicks
            if (playButton.isClicked()) {
                mainMenu = false;
            }
            if (exitButton.isClicked()) {
                exit();
            }
            // Options button has no functionality yet
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
            for(Lizard l : lizards) {
                l.ai(tiles, p1, collisionTiles);
                l.update(tiles, p1,  collisionTiles);
            }

            p1.display(camX, camY);
            for(Enemy e : enemies) {
                e.display(camX, camY);
            }
            for(Lizard l : lizards) {
                l.display(camX, camY);
            }

            if (p1.outOfBounds && p1.x >= tiles.length * tileSize) {
                nextLevel();
            }

            if (p1.outOfBounds && p1.y > (tiles[0].length + 1) * tileSize) {
                p1.lives--;
                p1.outOfBounds = false;
                setPosition(50, 4950, width, height);
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
        if (key == ESC) {
            key = 0; // This prevents Processing from closing on ESC key
            // Add your custom ESC key behavior here
            // For example: pauseMenu = !pauseMenu;
            mainMenu = !mainMenu;
        }
        if ((key == 'a' || key == 'A')) {
            keys[0] = true;
        }
        if ((key == 'd' || key == 'D')) {
            keys[1] = true;
        }
        if ((key == 'w' || key == 'W')) {
            keys[2] = true;
        }
        if (key == CODED) {
            if (keyCode == SHIFT) {
                keys[3] = true;
            }
        }
    }

    @Override
    public void keyReleased() {
        if ((key == 'a' || key == 'A')) {
            keys[0] = false;
        }
        if ((key == 'd' || key == 'D')) {
            keys[1] = false;
        }
        if ((key == 'w' || key == 'W')) {
            keys[2] = false;
        }
        if (key == CODED) {
            if (keyCode == SHIFT) {
                keys[3] = false;
            }
        }
    }
}
