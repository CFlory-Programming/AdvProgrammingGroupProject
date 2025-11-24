import processing.core.PImage;

public class PowerUp extends LevelObject{

    public boolean hasTimer;
    public int timerDuration; // in milliseconds
    public boolean disapearsOnHit;
    public int timeCollected; // in milliseconds
    public boolean isActive;

    public PowerUp(PImage sprite, int x, int y, int height, int width, boolean hasTimer, int timerDuration, boolean disapearsOnHit) {
        super(sprite, x, y, height, width);
        this.hasTimer = hasTimer;
        this.timerDuration = timerDuration;
        this.disapearsOnHit = disapearsOnHit;

        timeCollected = 0;
        isActive = false;
    }


    public void update(Player player) {
        if (collidesWith(player) && !isActive) {
            isActive = true;
            timeCollected = KonQuestGame.sketch.millis();
            applyEffect(player);
        }

        if (isActive && hasTimer) {
            int currentTime = KonQuestGame.sketch.millis();
            if (currentTime - timeCollected >= timerDuration) {
                removeEffect(player);
                isActive = false;
            }
        }

        if (player.attacked && disapearsOnHit && isActive) {
            removeEffect(player);
            isActive = false;
        }

        if (isActive) {
            applyEffect(player);
        }

    }

    public void applyEffect(Player player) {
        // To be overridden by subclasses
    }

    public void removeEffect(Player player) {
        // To be overridden by subclasses
    }
}
