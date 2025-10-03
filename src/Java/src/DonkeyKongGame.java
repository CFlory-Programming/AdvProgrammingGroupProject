import java.util.Random;
import processing.core.PApplet;
import processing.core.PImage;

public class DonkeyKongGame extends PApplet
{
    // static reference to the running Processing sketch so other classes can draw
    public static PApplet sketch;

    // shared coins list so main() can populate it before the sketch starts
    public static java.util.ArrayList<Coin> coins = new java.util.ArrayList<>();

    public static Player p1;
    public static Enemy e1;

    public static int score = 0, lives = 3, level = 1, camX = 50, camY = 50, tileSize = 50;
    public static int[][] tiles = new int[102][102];

    public static boolean[] keys = new boolean[3];

    public static PImage[] tilesImg = new PImage[2];
    public static void main(String[] args)
    {
        Random random = new Random();

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                //randomly assign 1 or 0 to each tile
                if (random.nextDouble() < 0.1 || j == tiles[i].length - 1 || i == 0 || i == tiles.length - 1 || j == 0) {
                    tiles[i][j] = 1;
                } else {
                    tiles[i][j] = 0;
                }
            }
        }
        tiles[1][1] = 0;
        tiles[1][2] = 0;

        p1 = new Player(2*tileSize, tileSize, 50, 50, score, lives);
        e1 = new Enemy();
        
        // create a couple of test coins before starting the Processing sketch
        coins.add(new Coin(100, 100, 1));
        coins.add(new Coin(200, 150, 1));
        coins.add(new Coin(300, 80, 1));

        // start the Processing sketch
        PApplet.main("DonkeyKongGame");
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
        background(255);

        // Load tile images
        tilesImg[1] = loadImage("Tile1.png");
    }

    @Override
    public void draw() {
        // clear
        background(255);

        // Draw tiles
        fill(0);
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                if (tiles[i][j] == 1) {
                    //rect(i * tileSize - camX, j * tileSize - camY, tileSize, tileSize);
                    image(tilesImg[1], i * tileSize - camX, j * tileSize - camY, tileSize, tileSize);
                }
            }
        }

        //Update Player
        if (keyPressed) {
            if (keys[0]) {
                p1.walk('l');
            } else if (keys[1]) {
                p1.walk('r');
            } else {
                p1.speedX = 0;
            }
            if (keys[2]) {
                if (!p1.inAir) {
                    p1.jump();
                    p1.inAir = true;
                }
            }
        }

        p1.update(tiles);
        e1.update(tiles, p1);

        p1.display(camX, camY);
        e1.display(camX, camY);

        // Camera slowly follows player
        if (p1.x-50 < width / 2) {
            camX += -(camX-50) * 0.05;
        } else if (p1.x-50 > tiles.length * tileSize - width / 2) {
            camX += (tiles.length * tileSize - (camX-50) - width) * 0.05;
        } else {
            camX += (p1.x - (camX) - width / 2) * 0.05;
        }
        if (p1.y-50 < height / 2) {
            camY += -(camY-50) * 0.05;
        } else if (p1.y+50 > tiles[0].length * tileSize - height / 2) {
            camY += (tiles[0].length * tileSize - (camY+50) - height) * 0.05;
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

    @Override
    public void keyPressed() {
        if ((key == 'a' || key == 'A')) {
            keys[0] = true;
        }
        if ((key == 'd' || key == 'D')) {
            keys[1] = true;
        }
        if ((key == 'w' || key == 'W')) {
            keys[2] = true;
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
    }
}
