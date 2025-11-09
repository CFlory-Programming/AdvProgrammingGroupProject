import java.util.*;

public class Thrower extends Enemy{
    int timer;
    public Thrower(int x, int y) {
        super(x, y);
        height = 100;
        width = 50;
        timer = 0;
    }

    public void throwed(Player p1, ArrayList<Enemy> enemies) {
        Projectile projectile = new Projectile(x + width/2, y + height/2, 10, 10, 20, false, p1);
        enemies.add(projectile);
    }

    public void ai(int[][] tiles, Player p1, int[] collisionTiles, ArrayList<Enemy> enemies) {
        if(timer == 30) {
            timer = 0;
            throwed(p1, enemies);
        }
        super.ai(tiles, p1, collisionTiles, enemies);
    }

    public void update(int[][] tiles, Player p1, int[] collisionTiles, ArrayList<Enemy> enemies) {
        super.update(tiles, p1, collisionTiles, enemies);
        timer++;
    }

    public void display(int camX, int camY) {
        KonQuestGame.sketch.noStroke();
        KonQuestGame.sketch.fill(0, 255, 0);
        KonQuestGame.sketch.rect(x - camX, y - camY, width, height);
    }
}
