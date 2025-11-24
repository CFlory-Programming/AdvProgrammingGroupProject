import processing.core.PImage;

public class SpeedBoost extends PowerUp {

    private float speedMultiplier;

    public SpeedBoost(PImage sprite, int x, int y, int height, int width, boolean hasTimer, int timerDuration, boolean disapearsOnHit, float speedMultiplier) {
        super(sprite, x, y, height, width, hasTimer, timerDuration, disapearsOnHit);
        this.speedMultiplier = speedMultiplier;
    }

    @Override
    public void applyEffect(Player player) {
        player.speedX *= speedMultiplier;
    }

    @Override
    public void removeEffect(Player player) {
        //player.speedX /= speedMultiplier;
    }
    
}
