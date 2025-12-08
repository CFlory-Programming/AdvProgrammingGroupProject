import java.util.ArrayList;

public class TallEnemy extends Enemy
{
    public TallEnemy(int x, int y)
    {
        super(x, y);
        height = 100;
        width = 50;
    }

    public TallEnemy(TallEnemy other) {
        super(other);
    }

    public TallEnemy deepCopy(Enemy other) {
        return new TallEnemy((TallEnemy) other);
    }

    public void ai(int[][] tiles, Player p1, int[] collisionTiles, ArrayList<Enemy> enemies)
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
            tryJump = true;
            jump(10, 'u');
        } else {
            tryJump = false;
        }
    }
}
