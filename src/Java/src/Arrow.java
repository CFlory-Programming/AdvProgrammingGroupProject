public class Arrow extends AbstractProjectile {
    public Arrow(float x, float y, float direction, float speed, int width, int height) {
        super(x, y, direction, speed, width, height);
    }

    public void update() {
        physics();
        if (checkHit(KonQuestGame.tiles, KonQuestGame.collisionTiles)) {
            speedX = 0;
            speedY = 0;
        }
        x += speedX;
        y += speedY;
        distance += Math.sqrt((speedX*speedX) + (speedY*speedY));
        if(distance > 1000) {
            exists = false;
        }
        if(outOfBounds) {
            exists = false;
        }
    }

    public void physics() {
        if (speedX == 0 && speedY == 0) {
            return;
        }
        // Gravity effect
        speedY += 0.1;
        direction = (float) Math.atan2(speedY, speedX);
    }

    public void display(int camX, int camY) {
        KonQuestGame.sketch.pushMatrix();
        KonQuestGame.sketch.translate((float) (x - camX), (float) (y - camY));
        KonQuestGame.sketch.rotate(direction);
        KonQuestGame.sketch.fill(150, 75, 0);
        KonQuestGame.sketch.rect(-width/2, -height/2, width, height);
        KonQuestGame.sketch.popMatrix();
    }
}
