public class Coin
{

    public int x;
    public int y;
    public int value;

    // default constructor
    public Coin()
    {
        x = 0;
        y = 0;
        value = 1;
    }

    // overload constructor
    public Coin(int newX, int newY, int newValue)
    {
        x = newX;
        y = newY;
        value = newValue;
    }

    // not implemented until later
    public boolean isCollected()
    {
        return true;
    }

    public int getValue()
    {
        return value;
    }
    
    public void display()
    {
        // Use the Processing sketch stored in DonkeyKongGame.sketch to draw the coin.
        DonkeyKongGame.sketch.noStroke(); // formatting
        DonkeyKongGame.sketch.fill(255, 204, 0); // yellow
        DonkeyKongGame.sketch.ellipse(x, y, 12, 12); // draw a circle centered at (x, y)
    }
}
