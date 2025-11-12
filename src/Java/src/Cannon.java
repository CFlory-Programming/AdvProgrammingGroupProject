import java.util.ArrayList;

public class Cannon extends Thrower{
    boolean direct;
    public Cannon(int x, int y) {
        super(x, y);
        height = 50;
        width = 50;
    }

    public void throwed(Player p1, ArrayList<Enemy> enemies) {
        Bullet bullet;
        if(direct) {
            bullet = new Bullet(x + width/2, y + height/2, 5, 0, false, p1);
        } else {
            bullet = new Bullet(x + width/2, y + height/2, 5, (float) Math.PI, false, p1);
        }
        enemies.add(bullet);
    }

    public void ai(int[][] tiles, Player p1, int[] collisionTiles, ArrayList<Enemy> enemies) {
        if(timer == 120) {
            timer = 0;
            throwed(p1, enemies);
        }
    }

    public void physics() {
        // No gravity/friction
    }

    public void leftCollide(Player p1) {
        // Ignore
    }

    public void rightCollide(Player p1) {
        // Ignore
    }
    
    public void topCollide(Player p1) {
        // Ignore
    }

    public void bottomCollide(Player p1) {
        // Ignore
    }
}
