import java.util.ArrayList;

public class Bullet extends Projectile{
    boolean starting;
    public Bullet(int x, int y, int speed, boolean homing, Player p1) {
        super(x, y, 50, 50, speed, homing, p1);
        starting = true;
    }
    
    public Bullet(int x, int y, int speed, float direction, boolean homing, Player p1) {
        super(x, y, 50, 50, speed, direction, homing, p1);
        starting = true;
    }

    public void handleCollideX() {
        if (starting) {
            return;
        }
        exists = false;
    }

    public void handleCollideY() {
        if (starting) {
            return;
        }
        exists = false;
    }

    public void topCollide(Player p1) {
        exists = false;
        p1.jump(15);
    }

    public void bottomCollide(Player p1) {
        if (p1.immune) {
            return;
        }
        exists = false;
        p1.speedX += speedX*2;
        p1.speedY += speedY*2;
        if (!p1.attacked && !p1.launched) {
            p1.health -= 30;
        }
    }

    public void rightCollide(Player p1) {
        exists = false;
        p1.speedX += speedX*2;
        p1.speedY += speedY*2;
        if (!p1.attacked && !p1.launched) {
            p1.health -= 30;
        }
    }

    public void leftCollide(Player p1) {
        exists = false;
        p1.speedX += speedX*2;
        p1.speedY += speedY*2;
        if (!p1.attacked && !p1.launched) {
            p1.health -= 30;
        }
    }

    public void update(int[][] tiles, Player p1, int[] collisionTiles, ArrayList<Enemy> enemies)
    {
        if (starting && !collideX(x, y, tiles, collisionTiles) && !collideY(x, y, tiles, collisionTiles)) {
            starting = false;
        }
        super.update(tiles, p1, collisionTiles, enemies);
    }
}
