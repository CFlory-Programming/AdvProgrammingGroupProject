import processing.core.PApplet;

public class Main extends PApplet {

    public static void main(String[] args) {
        // Start the PApplet in a new window
        PApplet.main("Main");
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
        ellipse(mouseX, mouseY, 50, 50);
    }
}
