//import java.lang.reflect.Array;
import java.util.*;
import processing.core.PApplet;
import processing.core.PImage;

public class KonQuestGame extends PApplet
{
    // static reference to the running Processing sketch so other classes can draw
    public static PApplet sketch;

    // UI is handled by a separate helper to keep this class slimmer
    private GameUI ui;

    public static boolean pauseMenu = false;

    public static boolean animationPlaying = false;

    public static int animationFrame = 0;

    public static boolean interact = false;
    public static boolean interacting = false;

    // shared coins list so main() can populate it before the sketch starts
    public static ArrayList<Coin> coins = new ArrayList<>();

    public static Player p1;
    public static ArrayList<ArrayList<Enemy>> enemyStorage = new ArrayList<ArrayList<Enemy>>();
    public static ArrayList<Enemy> enemies = new ArrayList<>();

    //public static LaunchBarrel barrel;
    //public static Crate crate;
    public static ArrayList<LevelObject> levelObjects = new ArrayList<>();
    public static Mount mount;

    public static int score = 0, lives = 3, level = 1, camX = 50, camY = 50, tileSize = 50;
    public static int cameraLeftMargin = 50;
    public static int cameraRightMargin = 50;
    public static int cameraTopMargin = 50;
    public static int cameraBottomMargin = 0;
    public static int[][] tiles = new int[102][102];

    public static boolean[] keys = new boolean[5];
    // Track previous-frame key state to detect "just pressed" events
    public static boolean[] prevKeys = new boolean[5];

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

        LevelGeneration level1 = new LevelGeneration(tiles, tileSize);
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
        TallEnemy e;
        p1 = new Player(2*tileSize, tileSize, 50, 50, score, lives);
        for(int i = 0; i < 20; i++) {
            l = new Lizard(100*i + 100, 100);
            e = new TallEnemy(100*i + 100, 100);
            enemies.add(e);
            enemies.add(l);
        }
        Thrower t = new Thrower(2000, 4950);
        Cannon c = new Cannon(4900, 4950);
        enemies.add(c);
        enemies.add(t);
        enemies.add(new Lizard(800, 4900));
        enemyStorage.add(enemies);

        enemies = cloneEnemies(enemyStorage.get(0));
        
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
        if (p1.x-cameraLeftMargin < width / 2) {
            camX = cameraLeftMargin;
        } else if (p1.x+cameraRightMargin > tiles.length * tileSize - width / 2) {
            camX = tiles.length * tileSize - width - cameraRightMargin;
        } else {
            camX = p1.x - width / 2;
        }
        if (p1.y-cameraTopMargin < height / 2) {
            camY = cameraTopMargin;
        } else if (p1.y+cameraBottomMargin > (tiles[0].length) * tileSize - height / 2) {
            camY = (tiles[0].length) * tileSize - height - cameraBottomMargin;
        } else {
            camY = p1.y - height / 2;
        }
    }

    public static void nextLevel()
    {
        level++;
        if (enemyStorage.size()>=level) {
            enemies = cloneEnemies(enemyStorage.get(level-1));
        }
        LevelGeneration level = new LevelGeneration(tiles, tileSize);
        level.readFromFile("data/" + level + ".txt");
        tiles = level.getTiles();
        setPosition(50, 4950, sketch.width, sketch.height);
    }

    public void displayDetails()
    {
        fill(255, 0, 0);
        textAlign(LEFT, TOP);
        textSize(16);
        String details = "";
        details += "Player: (x=" + p1.x + ", y=" + p1.y + ")\n";
        details += "speedX=" + p1.speedX + ", speedY=" + p1.speedY + "\n";
        details += "\n";
        text(details, 260, 10);
        details = "";
        for (int i = 0; i < enemies.size(); i++) {
            details += "Enemy " + i + ": (x=" + enemies.get(i).x + ", y=" + enemies.get(i).y + ")\n";
            details += "speedX=" + enemies.get(i).speedX + ", speedY=" + enemies.get(i).speedY + "\n";
            details += "inAir=" + enemies.get(i).inAir + ", cy=" + enemies.get(i).cy + "\n";
            details += "tryJump=" + enemies.get(i).tryJump + "\n";
            details += "\n";
            if (details.length() > 700) {
                text(details, 260 + 40 * i, 10);
                details = "";
            }
        }
    }

    public void drawLevel()
    {
        // clear
        //background(0,118,248);
        background(161, 44, 95);
        //background((int)random(255), (int)random(255), (int)random(255));


        // Display HUD (hide when pause menu open)
        if (ui != null && !ui.pauseMenu) {
            HUD.display(p1);
        }
        
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
        p1.display(camX, camY);
        for(Enemy e : enemies) {
            e.display(camX, camY);
        }
        if (mount != null) {
            mount.display(camX, camY);
        }
        for (LevelObject lo : levelObjects) {
            lo.update(p1);
            lo.display(camX, camY);
        }
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

        frameRate(60);
        p1.loadSprites();

        background(0,118,248);
        setPosition(50, 4950, width, height);

        // Initialize UI helper (delegates menu and options UI)
        ui = new GameUI(this);
        ui.setupUI();
        // Load tile images
        for (int i = 0; i < tilesImg.length; i++) {
            if (i != 0)
            tilesImg[i] = loadImage("Tile" + i + ".png");
        }

        levelObjects.add(new LaunchBarrel(loadImage("Barrel.png"), 500, 5000, 50, 50, 2, 30));
        levelObjects.add(new Crate(loadImage("Crate.png"), 600, 5000, 50, 50, "Score"));
        levelObjects.add(new Crate(loadImage("Crate.png"), 2700, 5000, 50, 50, "Mount"));
        levelObjects.add(new SpeedBoost(loadImage("Crate.png"), 3500, 5000, 30, 30, 2.0f));
        //mount = new Mount(loadImage("Barrel.png"), 2000, 4900, 100, 70);
        mount = null;

    // main menu image and buttons initialized in GameUI.setupUI()
    }

    public static ArrayList<Enemy> cloneEnemies(ArrayList<Enemy> enemies)
    {
        ArrayList<Enemy> newEnemies = new ArrayList<Enemy>();
        for (Enemy e : enemies) {
            newEnemies.add(e.deepCopy(e));
        }
        return newEnemies;
    }

    @Override
    public void draw() {
        if (ui != null && ui.mainMenu) {
            ui.drawMainMenu();
            return;
        }
        else if (ui != null && ui.pauseMenu) {
            drawLevel();
            ui.drawPauseMenu();
            displayDetails();
            return;
        }
        else if(animationPlaying) {
            if (p1.dead) {
                if (animationFrame < 30) {
                    drawLevel();

                    //barrel.display(camX, camY);

                    // Play death animation (simple fade out for example)
                    fill(0, 0, 0, map(animationFrame, 0, 30, 0, 255));
                    rect(0, 0, width, height);
                } else if (animationFrame == 30) {
                    p1.outOfBounds = false;
                    setPosition(50, 4950, width, height);
                    if (enemyStorage.size()>=level) {
                        enemies = cloneEnemies(enemyStorage.get(level-1));
                    }
                } else if (animationFrame < 45) {
                    // Hold black screen for a moment
                } else if (animationFrame < 75) {
                    drawLevel();

                    //barrel.display(camX, camY);

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
            drawLevel();

            //Update Player
            if (keyPressed) {
                if (p1.mount != null) {
                    /*if (keys[4]) {
                        p1.mount.dismount(p1);
                    }*/
                    if (keys[0]) {
                        if (keys[3]) {
                            p1.mount.speedX = -8;
                        } else 
                        {
                            p1.mount.speedX = -4;
                        }
                    } else if (keys[1]) {
                        if (keys[3]) {
                            p1.mount.speedX = 8;
                        } else 
                        {
                            p1.mount.speedX = 4;
                        }
                    } else {
                        p1.mount.speedX = 0;
                    }

                    if (keys[2]) {
                        if (!p1.mount.inAir) {
                            p1.mount.jump();
                            p1.mount.inAir = true;
                        }
                    }
                } else {
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
                // (Note: interact edge detection handled below every frame)
            }

            // Edge-detect the interact key so we get a clean "just pressed" event
            // This avoids odd toggling where the interact key needs another key
            // to be pressed before it will work again.
            boolean justPressedInteract = keys[4] && !prevKeys[4];
            interact = justPressedInteract;
            // Keep interacting as a simple reflection of the physical key state
            interacting = keys[4];
            // Update prevKeys for the next frame
            prevKeys[4] = keys[4];

            p1.update(tiles, collisionTiles, keys[0] || keys[1]);
            
            for(int i = 0; i<enemies.size(); i++) {
                // enemies.get(i).ai(tiles, p1, collisionTiles, enemies);
                enemies.get(i).update(tiles, p1, collisionTiles, enemies);
            }

            // barrel.update(p1, !interact);
            // barrel.display(camX, camY);

            // crate.update(p1);
            // crate.display(camX, camY);
            for (LevelObject lo : levelObjects) {
                lo.update(p1);
            }

            //mount.mount(p1);
            if (mount != null) {
                mount.update(p1, interact);
            }

            if (p1.outOfBounds && p1.x >= tiles.length * tileSize) {
                nextLevel();
            }

            if (p1.outOfBounds && p1.y > (tiles[0].length + 1) * tileSize) {
                p1.die();
            }

            if (p1.dead) {
                animationPlaying = true;
            }

            for (int i = 0; i < enemies.size(); i++) {
                // Check if enemy is out of bounds
                if (enemies.get(i).outOfBounds && enemies.get(i).y > (tiles[0].length + 1) * tileSize) {
                    enemies.get(i).exists = false;
                }

                // Check if enemy is dead
                else if (enemies.get(i).health <= 0) {
                    enemies.get(i).exists = false;
                }
            }

            for (int i = 0; i < enemies.size(); i++) {
                if (!enemies.get(i).exists) {
                    enemies.remove(i);
                    i--;
                }
            }

            // Camera slowly follows player
            if (p1.x-cameraLeftMargin < width / 2) {
                camX += -(camX-cameraLeftMargin) * 0.05;
            } else if (p1.x+cameraRightMargin > tiles.length * tileSize - width / 2) {
                camX += (tiles.length * tileSize - (camX+cameraRightMargin) - width) * 0.05;
            } else {
                camX += (p1.x - (camX) - width / 2) * 0.05;
            }
            if (p1.y-cameraTopMargin < height / 2) {
                camY += -(camY-cameraTopMargin) * 0.05;
            } else if (p1.y+cameraBottomMargin > (tiles[0].length) * tileSize - height / 2) {
                camY += ((tiles[0].length) * tileSize - (camY+cameraBottomMargin) - height) * 0.05;
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

            // draw total value in red at the top-right corner (hide when pause menu open)
            if (ui != null && !ui.pauseMenu) {
                fill(255, 0, 0);
                textAlign(RIGHT, TOP);
                textSize(16);
                text("Total value: " + totalCoins + " (keys[4]=" + keys[4] + ", interacting=" + interacting + ", interact=" + interact + ")", width - 10, 10); //x is 10px from the right edge, y is 10px from the top
            }
        }
    }

    @Override
    public void keyPressed() {
        // Delegate keyboard handling for UI (rebinding, ESC for menu, and custom bindings)
        if (ui != null && ui.handleKeyPressed()) {
            return;
        }
    }

    @Override
    public void keyReleased() {
        if (ui != null) ui.handleKeyReleased();
    }
}
