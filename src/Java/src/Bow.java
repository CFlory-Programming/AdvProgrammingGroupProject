import processing.core.PImage;

public class Bow extends PowerUp{
    public Bow(PImage image, float x, float y, int width, int height) {
        super(image, (int)x, (int)y, width, height, false, 0, true);
    }

    public void applyEffect(Player player) {
        player.canShoot = true;
    }

    public void removeEffect(Player player) {
        player.canShoot = false;
    }
}
