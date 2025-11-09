import java.util.*;

public class Projectile extends Enemy{
    private int distance, speed;
    float direction; //direction in radians
    boolean homing;
    public Projectile(int x, int y, int width, int height, int speed, boolean homing, Player p1) {
        super(x, y);
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.homing = homing;
        this.exists = true;
        this.distance = 0;
        changeDirection(p1);
    }

    public void update(int[][] tiles, Player p1, int[] collisionTiles, ArrayList<Enemy> enemies)
    {
        distance += speed;  // Increment distance before super.update
        super.update(tiles, p1, collisionTiles, enemies);
        if (distance > 1000) {
            exists = false;
        }
    }

    public void physics()
    {
        // No air resistance/friction
    }

    public void ai(int[][] tiles, Player p1, int[] collisionTiles, ArrayList<Enemy> enemies) {
        if(homing) {
            changeDirection(p1);
        }
        speedX = speed*(float) Math.cos(direction);
        speedY = speed*(float) Math.sin(direction);
    }

    public void handleCollideY() {
        exists = false;
    }

    public void handleCollideX() {
        exists = false;
    }

    public void topCollide(Player p1) {
        p1.speedX += speedX/speed*5;
        p1.speedY += speedY/speed*5;
        exists = false;
    }

    public void bottomCollide(Player p1) {
        p1.speedX += speedX/speed*5;
        p1.speedY += speedY/speed*5;
        if (!p1.launched && !p1.attacked) {
            p1.health -= 5;
        }
        exists = false;
    }

    public void leftCollide(Player p1) {
        p1.speedX += speedX/speed*5;
        p1.speedY += speedY/speed*5;
        if (!p1.launched && !p1.attacked) {
            p1.health -= 5;
        }
        exists = false;
    }

    public void rightCollide(Player p1) {
        p1.speedX += speedX/speed*5;
        p1.speedY += speedY/speed*5;
        if (!p1.launched && !p1.attacked) {
            p1.health -= 5;
        }
        exists = false;
    }

    public void changeDirection(Player p1) {
        direction = (float) Math.atan((double) ((p1.y+p1.height/2)-y-height/2)/((p1.x+p1.width/2)-x-width/2));
        if (p1.x - x < 0) {
            direction += Math.PI;
        }
    }

    public void display(int camX, int camY)
    {
        KonQuestGame.sketch.noStroke();
        KonQuestGame.sketch.fill(0, 255, 0);
        KonQuestGame.sketch.rect(x - camX, y - camY, width, height);
    }
}
