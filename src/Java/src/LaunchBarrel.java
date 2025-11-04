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
    
    public void launch(Player player)
    {
        if (direction == 1) {
            player.speedX = 15;
        } else if (direction == 2) {
            player.speedX = 15;
            player.speedY = 15;
        } else if (direction == 3) {
            player.speedY = 15;
        } else if (direction == 4) {
            player.speedX = -15;
            player.speedY = 15;
        } else if (direction == 5) {
            player.speedX = -15;
        } else if (direction == 6) {
            player.speedX = -15;
            player.speedY = -15;
        } else if (direction == 7) {
            player.speedY = -15;
        } else if (direction == 8) {
            player.speedX = 15;
            player.speedY = -15;
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
