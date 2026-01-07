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

    public Lizard(Lizard other) {
        super(other);
        this.direction = other.direction;
    }

    @Override
    public Lizard deepCopy(Enemy other) {
        return new Lizard((Lizard) other);
    }

    @Override
    public void ai(int[][] tiles, Player p1, int[] collisionTiles, ArrayList<Enemy> enemies)
    {
        boolean distance = Math.abs(p1.x - x) < 300 && Math.abs(p1.y - y) < 300;
        if (distance) {
            if (p1.x > x) {
                direction = true;
            } else if (p1.x < x) {
                direction = false;
            }
            advancedAi(tiles, p1, collisionTiles, enemies);
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

    public void advancedAi(int[][] tiles, Player p1, int[] collisionTiles, ArrayList<Enemy> enemies)
    {
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
        if (!inAir && ((p1.y <= y && !collideY(x+speedX, y+speedY+1, tiles, collisionTiles)) || (x%50==0 && (collideX(x+1, y, tiles, collisionTiles) || collideX(x-1, y, tiles, collisionTiles))))) {
            jump(10, 'u');
            tryJump = true;
        } else {
            tryJump = false;
        }
    }
}
