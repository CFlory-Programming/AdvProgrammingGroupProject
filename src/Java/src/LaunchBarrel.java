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

    public void update(Player player, boolean wait)
    {
        if (collidesWith(player)) {
            player.launching = true;
            player.launched = true;
            player.visible = false;
            launch(player, wait);
        } else {
            player.launching = false;
            player.visible = true;
        }
    }
    
    public void launch(Player player, boolean wait)
    {
        // Launch the player in the barrel's direction.
        if (wait) {
            player.speedX = 0;
            player.speedY = 0;
            player.x = x + (width - player.width) / 2;
            player.y = y + (height - player.height) / 2;
        } else {
        switch (direction) {
            case 1: // Up
                player.speedY = -50;
                break;
            case 3: // Right
                player.speedX = 50;
                break;
            case 5: // Down
                player.speedY = 50;
                break;
            case 7: // Left
                player.speedX = -50;
                break;
            case 2: // Up-Right
                player.speedX = 30;
                player.speedY = -30;
                break;
            case 4: // Down-Right
                player.speedX = 30;
                player.speedY = 30;
                break;
            case 6: // Down-Left
                player.speedX = -30;
                player.speedY = 30;
                break;
            case 8: // Up-Left
                player.speedX = -30;
                player.speedY = -30;
                break;
        }
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
