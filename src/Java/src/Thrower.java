import java.util.*;

public class Thrower extends Enemy{
    ArrayList<Projectile> projectiles = new ArrayList<>();
    int timer;
    public Thrower(int x, int y) {
        super(x, y);
        height = 100;
        width = 50;
        timer = 0;
    }

    public void throwed(Player p1) {
        Projectile projectile = new Projectile(x + width/2, y + height/2, 10, 10, 20, false, p1);
        projectiles.add(projectile);
    }

    public void ai(int[][] tiles, Player p1, int[] collisionTiles) {
        if(timer == 20) {
            timer = 0;
            throwed(p1);
        }
        super.ai(tiles, p1, collisionTiles);
    }

    public void update(int[][] tiles, Player p1, int[] collisionTiles, ArrayList<Enemy> enemies) {
        super.update(tiles, p1, collisionTiles, enemies);
        for(int i = 0; i<projectiles.size(); i++) {
            if(!projectiles.get(i).exists) {
                projectiles.remove(i);
                i--;
            }
        }
        for(Projectile projectile : projectiles) {
            projectile.update(p1, tiles, collisionTiles);
        }
        timer++;
    }

    public void display(int camX, int camY) {
        for(Projectile projectile : projectiles) {
            projectile.display(camX, camY);
        }
        KonQuestGame.sketch.noStroke();
        KonQuestGame.sketch.fill(0, 255, 0);
        KonQuestGame.sketch.rect(x - camX, y - camY, width, height);
    }
}
