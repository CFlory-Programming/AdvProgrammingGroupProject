public class Enemy
{

    int height;
    int width;
    int x;
    float speedX;
    float speedY;
    int y;
    int health;
    boolean inAir;
    
    public Enemy()
    {
        height = 100;
        width = 50;
        x = 50;
        y = 50;
        health = 100;
        speedX = 0;
        speedY = 0;
        inAir = true;
    }
    
    public void jump(int height, char dir)
    {
        speedY=-height;
    }
    
    public void move(int distance, char dir, boolean isRunning)
    {
        if (isRunning) {
            if (dir == 'r') {
                speedX = distance*2;
            } else if (dir == 'l') {
                speedX = -distance*2;
            }
        } else {
            if (dir == 'r') {
                speedX = distance;
            } else if (dir == 'l') {
                speedX = -distance;
            }
        }
    }
    
    public boolean isHit()
    {
        return true;
    }

    public void update(int[][] tiles, Player p1, int[] collisionTiles)
    {
        

        if (p1.x > x) {
            if (p1.x-x > 3){
                move(4, 'r', false);
            } else {
                x=p1.x;
            }
        } else if (p1.x < x) {
            if (x-p1.x > 3){
                move(4, 'l', false);
            } else {
                x=p1.x;
            }
        }
        if (p1.y < y && !inAir && (x%50<=4 || x%50>=46)) {
            jump(10, 'u');
        }
        // Update enemy position based on speed
        speedY += 0.5; // Gravity

        speedX *= 0.8; //Friction
        if ((int) speedX == 0) {
            speedX = 0;
        }

        // Collision detection for x direction
        x += speedX;
        boolean collideX = checkCollision(x, y + height/2, 50, tiles, collisionTiles) || checkCollision(x, y, 50, tiles, collisionTiles) || (checkCollision(x, y + height, 50, tiles, collisionTiles) && y%50!=0) || checkCollision(x + width, y + height/2, 50, tiles, collisionTiles) || checkCollision(x + width, y, 50, tiles, collisionTiles) || (checkCollision(x + width, y + height, 50, tiles, collisionTiles) && y%50!=0);
        if (collideX) {
            if(speedX>0) {
              x = 50*(x/50);
            } else if(speedX<0) {
              x = 50*(x/50)+50;
            }
            speedX = 0;
        }

        // Collision detection for y direction
        y += speedY;
        boolean collideY = checkCollision(x, y + height, 50, tiles, collisionTiles) || (checkCollision(x + width, y + height, 50, tiles, collisionTiles) && x%50!=0) || checkCollision(x, y, 50, tiles, collisionTiles) || (checkCollision(x + width, y, 50, tiles, collisionTiles) && x%50!=0);
        if (collideY) {
            if(speedY>=0){
                y = 50*(y/50);
                inAir = false;
            } else if(speedY<0){
              y = 50*(y/50)+50;
            }
            speedY = 0;
        } else {
            inAir = true;
        }
    }

    public void display(int camX, int camY)
    {
        DonkeyKongGame.sketch.noStroke();
        DonkeyKongGame.sketch.fill(0, 255, 0);
        DonkeyKongGame.sketch.rect(x - camX, y - camY, width, height);
    }

    private boolean checkCollision(int tileX, int tileY, int tileSize, int[][] tiles, int[] collisionTiles) {
        int tileindex = tiles[tileX / tileSize][tileY / tileSize];
        for (int ct : collisionTiles) {
            if (tileindex == ct) {
                return true;
            }
        }
        return false;
    }
}
