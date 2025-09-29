public class Coin
{

    public int x;
    public int y;
    public int value;

    public Coin()
    {
        x = 0;
        y = 0;
        value = 1;
    }

    // convenience constructor
    public Coin(int x, int y)
    {
        this.x = x;
        this.y = y;
        this.value = 1;
    }

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
        if (DonkeyKongGame.sketch != null) {
            DonkeyKongGame.sketch.noStroke();
            DonkeyKongGame.sketch.fill(255, 204, 0); // yellow
            // draw a circle centered at (x, y)
            DonkeyKongGame.sketch.ellipse(x, y, 12, 12);
        }
    }
}
