import java.lang.reflect.Array;
import java.util.*;

public class Thrower extends Enemy{
    int timer;
    ArrayList<Projectile> projectiles;
    public Thrower(int x, int y) {
        super(x, y);
        height = 100;
        width = 50;
        timer = 0;
        projectiles = new ArrayList<Projectile>();
    }

    public void throwed(Player p1) {
        Projectile projectile = new Projectile(x + width/2, y + height/2, 10, 10, 20, false, p1);
        projectiles.add(projectile);
    }

    public void ai(int[][] tiles, Player p1, int[] collisionTiles, ArrayList<Enemy> enemies) {
        if(timer == 30) {
            timer = 0;
            throwed(p1);
        }
        if (p1.x > x) {
            if (p1.x-x >= 4){
                move(4, 'r', false);
            } else if (!collideX(p1.x, y, tiles, collisionTiles)) {
                move(p1.x - x, 'r', false);
            }
        } else if (p1.x < x) {
            if (x-p1.x >= 4){
                move(4, 'l', false);
            } else if (!collideX(p1.x, y, tiles, collisionTiles)) {
                move(p1.x - x, 'r', false);
            }
        }
        if (!inAir && ((p1.y <= y && !collideY((int)(x+speedX), (int)(y+1), tiles, collisionTiles)) || (x%50==0 && (collideX(x+1, y, tiles, collisionTiles) || collideX(x-1, y, tiles, collisionTiles))))) {
            jump(10, 'u');
        }
    }

    public void update(int[][] tiles, Player p1, int[] collisionTiles, ArrayList<Enemy> enemies) {
        super.update(tiles, p1, collisionTiles, enemies);
        timer++;
        for(int i = 0; i < projectiles.size(); i++) {
            projectiles.get(i).update(tiles, p1, collisionTiles, enemies);
        }
        for(int i = 0; i < projectiles.size(); i++) {
            if (!projectiles.get(i).exists) {
                projectiles.remove(i);
                i--;
            }
        }
    }
    public void display(int camX, int camY) {
        KonQuestGame.sketch.noStroke();
        KonQuestGame.sketch.fill(0, 255, 0);
        KonQuestGame.sketch.rect(x - camX, y - camY, width, height);
        for(int i = 0; i < projectiles.size(); i++) {
            projectiles.get(i).display(camX, camY);
        }
    }
}
