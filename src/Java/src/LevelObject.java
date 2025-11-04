import processing.core.PImage;

public class LevelObject
{
    int x;
    int y;
    int height;
    int width;
    PImage sprite;

    public LevelObject(PImage sprite, int x, int y, int height, int width)
    {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;

        sprite.resize(width, height);
    }
    
    public void display(int camX, int camY)
    {
        KonQuestGame.sketch.image(sprite, x - camX, y - camY);
    }

    public boolean collidesWith(Player player)
    {
        if (player.x + player.width > x && player.x < x + width &&
            player.y + player.height > y && player.y < y + height) {
            return true;
        }
        return false;
    }
}
