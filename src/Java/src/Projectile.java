public class Projectile {
    int x, y, width, height, distance, speed;
    float direction; //direction in radians
    boolean homing, exists, outOfBounds;
    public Projectile(int x, int y, int width, int height, int speed, boolean homing, Player p1) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.homing = homing;
        this.exists = true;
        this.distance = 0;
        outOfBounds = false;
        changeDirection(p1);
    }

    public void update(Player p1, int[][] tiles, int[] collisionTiles)
    {
        if(homing) {
            changeDirection(p1);
        }
        double speedX = speed*Math.cos(direction);
        double speedY = speed*Math.sin(direction);
        x += speedX;
        y += speedY;
        distance += speed;

        if (p1.visible && exists && p1.x + p1.speedX + p1.width >= x + speedX && p1.x + p1.speedX <= x + speedX + width && p1.y + p1.speedY + p1.height >= y + speedY && p1.y + p1.speedY <= y + speedY + height) {
            p1.speedX += speedX/speed*5;
            p1.speedY += speedY/speed*5;
            if (!p1.launched && !p1.attacked) {
                p1.health -= 5;
            }
            exists = false;
        }

        if (distance > 1000 || collideLevel(tiles, collisionTiles)) {
            exists = false;
        }
    }

    public void changeDirection(Player p1) {
        direction = (float) Math.atan((double) ((p1.y+p1.height/2)-y-height/2)/((p1.x+p1.width/2)-x-width/2));
        if (p1.x - x < 0) {
            direction += Math.PI;
        }
    }

    public boolean collideLevel(int[][] tiles, int[] collisionTiles) {
        for(int i = 0; i <= (width)/50; i++) {
            for(int j = 0; j <= (height)/50; j++) {
                if ((x%50 + width > 50*i) && (y%50 + height > 50*j) && checkCollision(x + i*50, y + j*50, 50, tiles, collisionTiles)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkCollision(int tileX, int tileY, int tileSize, int[][] tiles, int[] collisionTiles) {
        try {
            int tileindex = tiles[tileX / tileSize][tileY / tileSize];
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

    public void display(int camX, int camY)
    {
        KonQuestGame.sketch.noStroke();
        KonQuestGame.sketch.fill(0, 255, 0);
        KonQuestGame.sketch.rect(x - camX, y - camY, width, height);
    }
}
