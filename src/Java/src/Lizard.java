import java.util.ArrayList;

public class Lizard extends Enemy{
    boolean direction; //true is right, false is left
    public Lizard(int x, int y) {
        super(x, y);
        height = 50;
        width = 100;
        this.x = x;
        this.y = y;
    }

    public void ai(int[][] tiles, Player p1, int[] collisionTiles, ArrayList<Enemy> enemies)
    {
        boolean distance = Math.abs(p1.x - x) < 300 && Math.abs(p1.y - y) < 300;
        if (distance) {
            if (p1.x > x) {
                direction = true;
            } else if (p1.x < x) {
                direction = false;
            }
            super.ai(tiles, p1, collisionTiles, enemies);
        } else {
            if (direction && (!collideX(x, y, tiles, collisionTiles))) {
                move(2, 'r', false);
                if (collideX(x + 1, y, tiles, collisionTiles) || x + 150 >= 50 * tiles.length) {
                    direction = false;
                }
            } else if (!direction && (!collideX(x, y, tiles, collisionTiles))) {
                move(2, 'l', false);
                if (collideX(x - 1, y, tiles, collisionTiles) || x <= 0) {
                    direction = true;
                }
            }
        }
    }
}
