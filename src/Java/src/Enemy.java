import java.util.ArrayList;

public class Enemy
{

    int height;
    int width;
    int x;
    int startX;
    float speedX;
    float speedY;
    int y;
    int startY;
    int health;
    int headHeight;
    boolean inAir;
    boolean outOfBounds;
    boolean exists;
    
    public Enemy(int x, int y)
    {
        height = 100;
        width = 50;
        this.x = x;
        this.y = y;
        startX = x;
        startY = y;
        health = 100;
        headHeight = 10;
        speedX = 0;
        speedY = 0;
        inAir = true;
        exists = true;
    }
    
    public void jump(int height, char dir)
    {
        speedY=-height;
    }
    
    public void move(float distance, char dir, boolean isRunning)
    {
        if (isRunning) {
            if (dir == 'r') {
                if (speedX == 0) {
                    speedX = 2;
                } else if (speedX <= distance - 1) {
                    speedX += 1;
                } else {
                    speedX = distance;
                }
            } else if (dir == 'l') {
                if (speedX == 0) {
                    speedX = 2;
                } else if (speedX >= -distance + 1) {
                    speedX -= 1;
                } else {
                    speedX = -distance;
                }
            }
        } else {
            if (dir == 'r') {
                if (speedX == 0) {
                    speedX = 1;
                } else if (speedX <= distance - 0.5) {
                    speedX += 0.5;
                } else {
                    speedX = distance;
                }
            } else if (dir == 'l') {
                if (speedX == 0) {
                    speedX = 1;
                } else if (speedX >= -distance + 0.5) {
                    speedX -= 0.5;
                } else {
                    speedX = -distance;
                }
            }
        }
    }
    
    public boolean isHit()
    {
        return true;
    }

    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
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
        if (!inAir && ((p1.y <= y && !collideY((int)(x+speedX), (int)(y+1), tiles, collisionTiles)) || (x%50==0 && (collideX(x+1, y, tiles, collisionTiles) || collideX(x-1, y, tiles, collisionTiles))))) {
            jump(10, 'u');
        }
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
        p1.checkEnemyCollision(enemies);
    }

    public void physics()
    {
        // Update enemy position based on speed
        speedY += 0.5; // Gravity
        if (speedY > 15) {
            speedY = 15; // Terminal velocity
        }

        speedX *= 0.8; //Friction
        /*if ((int) speedX == 0) {
            speedX = 0;
        }*/
    }

    public void handleCollideX()
    {
        if(speedX>0) {
            x = 50*(x/50);
        } else if(speedX<0) {
            x = 50*(x/50)+50;
        }
        speedX = 0;
    }

    public void handleCollideY()
    {
        if(speedY>=0){
            y = 50*(y/50);
            inAir = false;
        } else if(speedY<0){
            y = 50*(y/50)+50;
        }
        speedY = 0;
    }

    public void topCollide(Player p1)
    {
        // Player is above the enemy
        health -= 100; // Reduce enemy health
        p1.jump(15); // Make the player bounce up  
        p1.attacked = true; // Make the player immune for a short time
    }

    public void bottomCollide(Player p1)
    {
        if (p1.immune) {
            return;
        }
        jump(10, 'u');
        p1.speedY = 5;
        p1.speedX = 0;
        if (!p1.launched && !p1.attacked) {
            // Player is touching the enemy's body
            //p1.die(); // Player dies
            p1.health -= 5;
            //p1.attacked = true; // Make the player immune for a short time
            jump(10, 'u');
            p1.speedY = 5;
            p1.speedX = 0;
        }
    }

    public void leftCollide(Player p1)
    {
        if (p1.immune) {
            return;
        }
        p1.speedX = -10;
        p1.speedY = -5;
        speedX = 5;
        if (!p1.launched && !p1.attacked) {
            // Player is to the left of the enemy
            p1.health -= 5;
        }
    }

    public void rightCollide(Player p1)
    {
        if (p1.immune) {
            return;
        }
        p1.speedX = 10;
        p1.speedY = -5;
        speedX = -5;
        if (!p1.launched && !p1.attacked) {
            // Player is to the right of the enemy
            p1.health -= 5;
        }
    }

    public void display(int camX, int camY)
    {
        KonQuestGame.sketch.noStroke();
        KonQuestGame.sketch.fill(0, 255, 0);
        int screenX = x - camX;
        int screenY = y - camY;
        
        // Only draw if on screen (with some margin)
        if (screenX > -width && screenX < KonQuestGame.sketch.width + width &&
            screenY > -height && screenY < KonQuestGame.sketch.height + height) {
            KonQuestGame.sketch.rect(screenX, screenY, width, height);
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
}
