import java.util.ArrayList;

abstract class Enemy
{

    int height;
    int width;
    float x;
    float startX;
    float speedX;
    float speedY;
    float y;
    float startY;
    int health;
    int headHeight;
    boolean inAir;
    boolean outOfBounds;
    boolean exists;
    boolean cy;
    boolean tryJump;
    
    public Enemy(int x, int y)
    {
        height = 100;
        width = 50;
        this.x = (float)x;
        this.y = (float)y;
        startX = (float)x;
        startY = (float)y;
        health = 100;
        headHeight = 10;
        speedX = 0;
        speedY = 0;
        inAir = true;
        exists = true;
        tryJump = false;
    }

    public Enemy(Enemy other)
    {
        this.height = other.height;
        this.width = other.width;
        this.x = other.x;
        this.y = other.y;
        this.startX = other.startX;
        this.startY = other.startY;
        this.health = other.health;
        this.headHeight = other.headHeight;
        this.speedX = other.speedX;
        this.speedY = other.speedY;
        this.inAir = other.inAir;
        this.exists = other.exists;
    }

    abstract Enemy deepCopy(Enemy other);
    {
        // ABSTRACT METHOD
    }
    
    public void jump(int height, char dir)
    {
        speedY=-height;
    }
    
    public void move(float distance, char dir, boolean isRunning)
    {
        if (isRunning) {
            if (dir == 'r') {
                if (speedX == 0 && distance >= 6) {
                    speedX = 2;
                } else if (speedX <= distance - 1 && speedX != 0) {
                    speedX += 1;
                } else {
                    speedX = distance;
                }
            } else if (dir == 'l') {
                if (speedX == 0 && distance <= -6) {
                    speedX = -2;
                } else if (speedX >= -distance + 1 && speedX != 0) {
                    speedX -= 1;
                } else {
                    speedX = -distance;
                }
            }
        } else {
            if (dir == 'r') {
                if (speedX == 0 && distance >= 3) {
                    speedX = 2;
                } else if (speedX <= distance - 0.5 && speedX != 0) {
                    speedX += 0.5;
                } else {
                    speedX = distance;
                }
            } else if (dir == 'l') {
                if (speedX == 0 && distance <= -3) {
                    speedX = -2;
                } else if (speedX >= -distance + 0.5 && speedX != 0) {
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

    abstract void ai(int[][] tiles, Player p1, int[] collisionTiles, ArrayList<Enemy> enemies);
    {
        // ABSTRACT METHOD
    }

    public boolean collideX(float fx, float fy, int[][] tiles, int[] collisionTiles)
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

    public boolean collideY(float fx, float fy, int[][] tiles, int[] collisionTiles)
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
            cy = true;
        } else {
            inAir = true;
            cy = false;
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
        for (LevelObject pu : KonQuestGame.levelObjects) {
            if (pu instanceof PowerUp) {
                pu.update(p1);
            }
        }
    }

    public void physics()
    {
        // Update enemy position based on speed
        speedY += 0.5; // Gravity
        if (speedY > 15) {
            speedY = 15; // Terminal velocity
        }

        speedX *= 0.95; //Friction
        /*if (Math.abs(speedX) <= 0.5) {
            speedX = 0;
        }*/
    }

    public void handleCollideX()
    {
        if(speedX>0) {
            x = (float)(50 * Math.floor(x / 50.0));
        } else if(speedX<0) {
            x = (float)(50 * (Math.floor(x / 50.0) + 1));
        }
        speedX = 0;
    }

    public void handleCollideY()
    {
        if(speedY>0){
            y = (float)(50 * Math.floor(y / 50.0));
            inAir = false;
        } else if(speedY<=0){
            y = (float)(50 * (Math.floor(y / 50.0) + 1));
            inAir = true;
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
            p1.hit = true;
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
        p1.hit = true;
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
        p1.hit = true;
        if (!p1.launched && !p1.attacked) {
            // Player is to the right of the enemy
            p1.health -= 5;
        }
    }

    public void display(int camX, int camY)
    {
        KonQuestGame.sketch.noStroke();
        KonQuestGame.sketch.fill(0, 255, 0);
        int screenX = (int)x - camX;
        int screenY = (int)y - camY;
        
        // Only draw if on screen (with some margin)
        if (screenX > -width && screenX < KonQuestGame.sketch.width + width &&
            screenY > -height && screenY < KonQuestGame.sketch.height + height) {
            KonQuestGame.sketch.rect(screenX, screenY, width, height);
            /*for(int i = 0; i <= (width)/50; i++) {
                for(int j = 0; j <= (height)/50; j++) {
                    if((j == 0 || j == (height)/50) && (x%50 + width > 50*i) && (y%50 + height > 50*j)) {
                        KonQuestGame.sketch.noStroke();
                        KonQuestGame.sketch.fill(0, 0, 0);
                        KonQuestGame.sketch.rect(x - x%50 - camX + i*50, y - y%50 - camY + j*50, 50, 50);
                    }
                }
            }*/
        }
    }

    private boolean checkCollision(float tileX, float tileY, int tileSize, int[][] tiles, int[] collisionTiles) {
        try {
            int tileindex = tiles[(int)(tileX) / tileSize][(int)(tileY) / tileSize];
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
