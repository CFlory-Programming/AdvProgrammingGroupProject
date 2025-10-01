public class Enemy
{

    int height;
    int width;
    int x;
    int speedX;
    int speedY;
    int y;
    int health;
    boolean inAir;
    
    public Enemy()
    {
        height = 100;
        width = 50;
        x = 0;
        y = 50;
        health = 100;
        speedX = 0;
        speedY = 0;
        inAir = false;
    }
    
    public void jump(int height, char dir)
    {
        if (dir == 'u')
        {
            speedY = -height/2;
        } else if (dir == 'r')
        {
            speedX = -height/2;
            speedY = -height/2;
        } else if (dir == 'l')
        {
            speedX = -height/2;
            speedY = -height/2;
        }
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

    public void update(int[][] tiles, Player p1)
    {
        // Update enemy position based on speed
        speedY += 1; // Gravity

        if (p1.x > x) {
            move(4, 'r', true);
        } else if (p1.x < x) {
            move(4, 'l', true);
        }
        if (p1.y < y & !inAir) {
            jump(24, 'u');
        }

        speedX *= 0.8; //Friction
        if ((int) speedX == 0) {
            speedX = 0;
        }

        // Collision detection for x direction
        x += speedX;
        boolean collideX = checkCollision(x, y + height/2, 50, tiles) || checkCollision(x, y, 50, tiles) || (checkCollision(x, y + height, 50, tiles) && y%50!=0) || checkCollision(x + width, y + height/2, 50, tiles) || checkCollision(x + width, y, 50, tiles) || (checkCollision(x + width, y + height, 50, tiles) && y%50!=0);
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
        boolean collideY = checkCollision(x, y + height, 50, tiles) || (checkCollision(x + width, y + height, 50, tiles) && x%50!=0) || checkCollision(x, y, 50, tiles) || (checkCollision(x + width, y, 50, tiles) && x%50!=0);
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

    private boolean checkCollision(int tileX, int tileY, int tileSize, int[][] tiles)
    {
        return tiles[tileX / tileSize][tileY / tileSize] == 1;
    }
}
