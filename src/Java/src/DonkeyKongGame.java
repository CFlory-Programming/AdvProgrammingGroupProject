import java.util.ArrayList;
import processing.core.PApplet;

public class DonkeyKongGame extends PApplet
{
    public static void main(String[] args)
    {

        PApplet.main("DonkeyKongGame");
    

        Player p1 = new Player();
        ArrayList<Enemy> enemies = new ArrayList<>();
        ArrayList<Coin> coins = new ArrayList<>();
        ArrayList<LevelObject> objects = new ArrayList<>();

        int score = 0;
        int level = 0;

        System.out.println("Donkey Kong Game");
        System.out.println("Coded by Cole Flory, Emil Gruenwald, and Jonathan Wu\n");
        
        System.out.println("This is a timing-oriented retro style 2D platformer game, with entertaining mechanics such as launch barrels and special collectibles. Each handcrafted level is fun and engaging; they are guaranteed to challenge most players. This game is referenced from Donkey Kong Country and Super Mario Brothers Wii. This game's features will be picked and chosen from each game, including the most fun aspects from each. Our platformer is created with a tile based level design, each level being created by hand for the most fun challanges.");
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
        background(255);
    }

    @Override
    public void draw() {
        // Draw a moving circle
        fill(255, 0, 0);
        text("Hello, World!", 100, 100);
    }
}
