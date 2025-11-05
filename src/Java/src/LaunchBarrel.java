import processing.core.PImage;
import processing.core.PConstants;

public class LaunchBarrel extends LevelObject
{
    int direction;

    public LaunchBarrel(PImage sprite, int x, int y, int height, int width, int direction)
    {
        super(sprite, x, y, height, width);
        this.direction = direction;
    }

    public void update(Player player)
    {
        if (collidesWith(player)) {
            launch(player);
        }
    }
    
    public void launch(Player player)
    {
        // Launch the player in the barrel's direction.
        switch (direction) {
            case 1: // Up
                player.speedY = -15;
                break;
            case 3: // Right
                player.speedX = 15;
                break;
            case 5: // Down
                player.speedY = 15;
                break;
            case 7: // Left
                player.speedX = -15;
                break;
            case 2: // Up-Right
                player.speedX = 10;
                player.speedY = -10;
                break;
            case 4: // Down-Right
                player.speedX = 10;
                player.speedY = 10;
                break;
            case 6: // Down-Left
                player.speedX = -10;
                player.speedY = 10;
                break;
            case 8: // Up-Left
                player.speedX = -10;
                player.speedY = -10;
                break;
        }

    }

    public void display(int camX, int camY)
    {
        // Draw rotated around the barrel's center.
        // We translate to the barrel center, rotate, then draw the sprite centered at 0,0.
        KonQuestGame.sketch.pushMatrix();
        KonQuestGame.sketch.translate(x - camX + width / 2, y - camY + height / 2);
        KonQuestGame.sketch.rotate((float)(Math.PI / 4 * (direction - 1)));
        // Draw centered so rotation happens around the barrel center. Temporarily use CENTER mode.
        KonQuestGame.sketch.imageMode(PConstants.CENTER);
        KonQuestGame.sketch.image(sprite, 0, 0);
        // Restore default image mode (CORNER) to avoid affecting other draw calls.
        KonQuestGame.sketch.imageMode(PConstants.CORNER);
        KonQuestGame.sketch.popMatrix();
    }
}
