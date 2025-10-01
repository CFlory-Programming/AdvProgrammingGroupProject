import processing.core.PApplet;

public class DonkeyKongGame extends PApplet
{
    // static reference to the running Processing sketch so other classes can draw
    public static PApplet sketch;

    // shared coins list so main() can populate it before the sketch starts
    public static java.util.ArrayList<Coin> coins = new java.util.ArrayList<>();
    public static void main(String[] args)
    {
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
        size(500, 500);
    }

    @Override
    public void setup() {
        // store reference to sketch so other classes can draw
        // Set initial background color
        sketch = this;
        background(255);
    }

    @Override
    public void draw() {
        // clear
        background(255);

        // draw coins added from main/setup and compute total value
        int totalCoins = 0;
        for (Coin c : coins) { // for each loop
            // call the coin's display method which will use the static sketch reference
            c.display();
            totalCoins += c.getValue();
        }

        // draw total value in red at the top-right corner
        fill(255, 0, 0);
        textAlign(RIGHT, TOP);
        textSize(16);
        text("Total value: " + totalCoins, width - 10, 10); //x is 10px from the right edge, y is 10px from the top
    }
}
