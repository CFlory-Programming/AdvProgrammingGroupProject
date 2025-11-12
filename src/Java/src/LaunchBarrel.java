import processing.core.PImage;
import processing.core.PConstants;

public class LaunchBarrel extends LevelObject
{
    int direction;
    int power;

    public LaunchBarrel(PImage sprite, int x, int y, int height, int width, int direction, int power)
    {
        super(sprite, x, y, height, width);
        this.direction = direction;
        this.power = power;
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
            // If we're performing the actual launch, make sure the player
            // is not mounted to something else and is visible again so
            // movement/physics take effect properly.
            player.mount = null;
            player.visible = true;
            player.launching = false;
        switch (direction) {
            case 1: // Up
                player.speedY = -power;
                break;
            case 3: // Right
                player.speedX = power;
                break;
            case 5: // Down
                player.speedY = power;
                break;
            case 7: // Left
                player.speedX = -power;
                break;
            case 2: // Up-Right
                player.speedX = power*0.7071;
                player.speedY = -power*0.7071;
                break;
            case 4: // Down-Right
                player.speedX = power*0.7071;
                player.speedY = power*0.7071;
                break;
            case 6: // Down-Left
                player.speedX = -power*0.7071;
                player.speedY = power*0.7071;
                break;
            case 8: // Up-Left
                player.speedX = -power*0.7071;
                player.speedY = -power*0.7071;
                break;
        }
        // After applying the launch velocity, nudge the player just outside
        // the barrel so the collision test won't immediately detect them as
        // still inside and re-center them on the next frame.
        // Position depends on launch direction.
        int centerX = x + (width - player.width) / 2;
        int centerY = y + (height - player.height) / 2;
        switch (direction) {
            case 1: // Up
                player.x = centerX;
                player.y = y - player.height - 1;
                break;
            case 3: // Right
                player.x = x + width + 1;
                player.y = centerY;
                break;
            case 5: // Down
                player.x = centerX;
                player.y = y + height + 1;
                break;
            case 7: // Left
                player.x = x - player.width - 1;
                player.y = centerY;
                break;
            case 2: // Up-Right
                player.x = x + width + 1;
                player.y = y - player.height - 1;
                break;
            case 4: // Down-Right
                player.x = x + width + 1;
                player.y = y + height + 1;
                break;
            case 6: // Down-Left
                player.x = x - player.width - 1;
                player.y = y + height + 1;
                break;
            case 8: // Up-Left
                player.x = x - player.width - 1;
                player.y = y - player.height - 1;
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
