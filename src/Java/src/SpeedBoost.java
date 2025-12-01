import processing.core.PImage;

public class SpeedBoost extends PowerUp {

    private float speedMultiplier;

    public SpeedBoost(PImage sprite, int x, int y, int height, int width, float speedMultiplier) {
        super(sprite, x, y, height, width, true, 10000, true);
        this.speedMultiplier = speedMultiplier;
    }

    @Override
    public void applyEffect(Player player) {
        player.speedX *= speedMultiplier;
        if (Math.abs(player.speedX) > 20) {
            player.speedX *= 20 / Math.abs(player.speedX);
        }
        player.speedMultiplier = speedMultiplier;
    }

    @Override
    public void removeEffect(Player player) {
        //player.speedX /= speedMultiplier;
        player.speedMultiplier = 1.0;
    }
    
}
