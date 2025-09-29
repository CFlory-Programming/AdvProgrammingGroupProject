import java.util.ArrayList;
import processing.core.PApplet;

public class DonkeyKongGame extends PApplet
{
    // static reference to the running Processing sketch so plain model classes can draw
    public static PApplet sketch;

    // shared coins list so main() can populate it before the sketch starts
    public static java.util.ArrayList<Coin> coins = new java.util.ArrayList<>();
    public static void main(String[] args)
    {
        // create a couple of test coins before starting the Processing sketch
        coins.add(new Coin(100, 100));
        coins.add(new Coin(200, 150));
        coins.add(new Coin(300, 80));

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
        // Set initial background color
        // store reference so model classes can draw
        sketch = this;
        background(255);
    }

    @Override
    public void draw() {
        // Draw a moving circle
        background(255);

        // draw coins added from main/setup
        for (Coin c : coins) {
            // call the coin's display method which will use the static sketch reference
            c.display();
        }
    }
}
