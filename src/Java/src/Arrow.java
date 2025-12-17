public class Arrow{
    float x, y, speedX, speedY, direction;
    int width, height, distance;
    boolean exists, outOfBounds;
    public Arrow(float x, float y, float direction, float speed, int width, int height) {
        this.x = x;
        this.y = y;
        this.speedX = (float)(Math.cos(direction) * speed);
        this.speedY = (float)(Math.sin(direction) * speed);
        this.direction = direction;
        this.width = width;
        this.height = height;
        this.exists = true;
    }

    public void update() {
        physics();
        x += speedX;
        y += speedY;
        distance += Math.sqrt((speedX*speedX) + (speedY*speedY));
        if(distance > 1000) {
            exists = false;
        }
        if(outOfBounds) {
            exists = false;
        }
        if (checkHit(KonQuestGame.tiles, KonQuestGame.collisionTiles)) {
            exists = false;
        }
    }

    private boolean checkHit(int[][] tiles, int[] collisionTiles) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (checkCollision((float) (x + i*Math.cos(direction)), (float) (y + j*Math.sin(direction)), 50, tiles, collisionTiles)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void physics() {
        // Gravity effect
        speedY += 0.1;
        direction = (float) Math.atan2(speedY, speedX);
        if (speedX < 0) {
            direction += Math.PI;
        }
    }

    public void display(int camX, int camY) {
        KonQuestGame.sketch.pushMatrix();
        KonQuestGame.sketch.translate(x + width / 2 - camX, y + height / 2 - camY);
        KonQuestGame.sketch.rotate(direction);
        KonQuestGame.sketch.fill(150, 75, 0);
        KonQuestGame.sketch.rectMode(3);
        KonQuestGame.sketch.rect(0, 0, width, height);
        KonQuestGame.sketch.rectMode(0);
        KonQuestGame.sketch.popMatrix();
    }

    private boolean checkCollision(float tileX, float tileY, int tileSize, int[][] tiles, int[] collisionTiles) {
        try {
            int tileindex = tiles[((int) tileX) / tileSize][((int) tileY) / tileSize];
            outOfBounds = false;
            for (int ct : collisionTiles) {
                if (tileindex == ct) {
                    return true;
                }
            }
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            outOfBounds = true;
            return false;
        }
    }
}
