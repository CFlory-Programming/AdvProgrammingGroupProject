import java.util.*;

public class Projectile{
    public int distance, speed, width, height, x, y;
    float speedX, speedY;
    boolean exists, inAir, outOfBounds;
    float direction; //direction in radians
    boolean homing;
    public Projectile(float x, float y, int width, int height, int speed, boolean homing, Player p1) {
        this.x = (int)x;
        this.y = (int)y;
        this.width = width;
        this.height = height;
        this.x -= width/2;
        this.y -= height/2;
        this.speed = speed;
        this.homing = homing;
        this.exists = true;
        this.distance = 0;
        changeDirection(p1);
    }
    
    public Projectile(float x, float y, int width, int height, int speed, float direction, boolean homing, Player p1) {
        this.x = (int)x;
        this.y = (int)y;
        this.width = width;
        this.height = height;
        this.x -= width/2;
        this.y -= height/2;
        this.speed = speed;
        this.homing = homing;
        this.direction = direction;
        this.exists = true;
        this.distance = 0;
    }

    public boolean collideX(int fx, int fy, int[][] tiles, int[] collisionTiles)
    {
        for(int i = 0; i <= (width)/50; i++) {
            for(int j = 0; j <= (height)/50; j++) {
                if ((i == 0 || i == (width)/50) && (fx%50 + width > 50*i) && (fy%50 + height > 50*j) && checkCollision(fx + i*50, fy + j*50, 50, tiles, collisionTiles)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean collideY(int fx, int fy, int[][] tiles, int[] collisionTiles)
    {
        for(int i = 0; i <= (width)/50; i++) {
            for(int j = 0; j <= (height)/50; j++) {
                if ((j == 0 || j == (height)/50) && (fx%50 + width > 50*i) && (fy%50 + height > 50*j) && checkCollision(fx + i*50, fy + j*50, 50, tiles, collisionTiles)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void update(int[][] tiles, Player p1, int[] collisionTiles, ArrayList<Enemy> enemies)
    {
        distance += speed;  // Increment distance before super.update
        /*if (collideX((int) (x+speedX), (int) (y+speedY), tiles, collisionTiles) || collideY((int) (x+speedX), (int) (y+speedY), tiles, collisionTiles)) {
            exists = false;
        }*/
        ai(tiles, p1, collisionTiles, enemies);
        physics();  // Add physics calculation

        // Collision detection for x direction
        x += speedX;
        if (collideX(x, y, tiles, collisionTiles)) {
            handleCollideX();
        }

        // Collision detection for y direction
        y += speedY;
        if (collideY(x, y, tiles, collisionTiles)) {
            handleCollideY();
        } else {
            inAir = true;
        }
        if(p1.visible && p1.x + p1.speedX + p1.width >= x + speedX && p1.x + p1.speedX <= x + speedX + width && p1.y + p1.speedY + p1.height >= y + speedY && p1.y + p1.speedY <= y + speedY + height) {
            if (p1.y + p1.height < y) {
                topCollide(p1);
            } else if (p1.y > y + height) {
                bottomCollide(p1);
            } else if (p1.x + p1.width < x) {
                leftCollide(p1);
            } else if (p1.x > x + width) {
                rightCollide(p1);
            } else {
                // Knockback effect
                if (speedX < 0) {
                    leftCollide(p1);
                } else if (speedX > 0) {
                    rightCollide(p1);
                } else {
                    if (speedY > 0) {
                        bottomCollide(p1);
                    } else {
                        topCollide(p1);
                    }
                }
            }
        }
        p1.checkEnemyCollision(this);
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
        if (p1.immune) {
            return;
        }
        p1.speedX += speedX/speed*5;
        p1.speedY += speedY/speed*5;
        if (!p1.launched && !p1.attacked) {
            p1.health -= 5;
        }
        exists = false;
    }

    public void leftCollide(Player p1) {
        if (p1.immune) {
            return;
        }
        p1.speedX += speedX/speed*5;
        p1.speedY += speedY/speed*5;
        if (!p1.launched && !p1.attacked) {
            p1.health -= 5;
        }
        exists = false;
    }

    public void rightCollide(Player p1) {
        if (p1.immune) {
            return;
        }
        p1.speedX += speedX/speed*5;
        p1.speedY += speedY/speed*5;
        if (!p1.launched && !p1.attacked) {
            p1.health -= 5;
        }
        exists = false;
    }

    public void changeDirection(Player p1) {
        if (p1.x + p1.width/2 == x + width/2) {
            if (p1.y + p1.height/2 > y + height/2) {
                direction = (float) Math.PI/2;
            } else {
                direction = -(float) Math.PI/2;
            }
            return;
        }
        direction = (float) Math.atan((double) ((p1.y+p1.height/2)-y-height/2)/((p1.x+p1.width/2)-x-width/2));
        if (p1.x + p1.width/2 - x - width/2 < 0) {
            direction += (float) Math.PI;
        }
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
