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
    
    public Enemy(int x, int y)
    {
        height = 100;
        width = 50;
        this.x = x;
        this.y = y;
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

    public void ai(int[][] tiles, Player p1, int[] collisionTiles)
    {
        if (p1.x > x) {
            if (p1.x-x >= 4){
                move(4, 'r', false);
            } else if (!collideX(x, y, tiles, collisionTiles)) {
                x = p1.x;
            }
        } else if (p1.x < x) {
            if (x-p1.x >= 4){
                move(4, 'l', false);
            } else if (!collideX(x, y, tiles, collisionTiles)) {
                x = p1.x;
            }
        }
        if (!inAir && ((p1.y <= y && x%50>=45 && x%50!=0 && p1.x>x && !checkCollision(x + width, y + height, 50, tiles, collisionTiles)) || (p1.y <= y && p1.x<x && x%50<=5 && !checkCollision(x, y + height, 50, tiles, collisionTiles)) || (x%50==0 && (collideX(x+1, y, tiles, collisionTiles) || collideX(x-1, y, tiles, collisionTiles))))) {
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

    public void update(int[][] tiles, Player p1, int[] collisionTiles)
    {
        ai(tiles, p1, collisionTiles);
        // Update enemy position based on speed
        speedY += 0.5; // Gravity
        if (speedY > 15) {
            speedY = 15; // Terminal velocity
        }

        speedX *= 0.8; //Friction
        if ((int) speedX == 0) {
            speedX = 0;
        }

        // Collision detection for x direction
        x += speedX;
        if (collideX(x, y, tiles, collisionTiles)) {
            if(speedX>0) {
              x = 50*(x/50);
            } else if(speedX<0) {
              x = 50*(x/50)+50;
            }
            speedX = 0;
        }

        // Collision detection for y direction
        y += speedY;
        if (collideY(x, y, tiles, collisionTiles)) {
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
        KonQuestGame.sketch.noStroke();
        KonQuestGame.sketch.fill(0, 255, 0);
        KonQuestGame.sketch.rect(x - camX, y - camY, width, height);
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
