import processing.core.PImage;

public class SpeedBoost extends PowerUp {

    private float speedMultiplier;

    public SpeedBoost(PImage sprite, int x, int y, int height, int width, float speedMultiplier) {
        super(sprite, x, y, height, width, true, 10000, true);
        this.speedMultiplier = speedMultiplier;
    }

    @Override
    public void applyEffect(Player player) {
        isActive = true;
        player.speedMultiplier = speedMultiplier;
    }

    @Override
    public void removeEffect(Player player) {
        //player.speedX /= speedMultiplier;
        isActive = false;
        player.speedMultiplier = 1.0;
    }
    
}
