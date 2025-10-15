public class Player
{

    int height;
    int width;
    int x;
    int y;
    int score;
    int lives;
    float speedX;
    float speedY;
    boolean inAir;


    public Player(int height, int width, int x, int y, int score, int lives)
    {
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
        this.score = score;
        this.lives = lives;
        speedX = 0;
        speedY = 0;
        inAir = true;
    }
    
    public void jump()
    {
        speedY = -10;
    }
    
    public void walk(char direction)
    {
        if (direction == 'r') {
            speedX = 5;
        } else if (direction == 'l') {
            speedX = -5;
        }
    }
    
    public void run(char direction)
    {
        if (direction == 'r') {
            speedX = 8;
        } else if (direction == 'l') {
            speedX = -8;
        }
    }
    
    public void interact()
    {
        
    }
    
    public void display(int camX, int camY)
    {
        KonQuestGame.sketch.noStroke();
        KonQuestGame.sketch.fill(255,0,0);
        KonQuestGame.sketch.rect(x - camX, y - camY, width, height);
    }

    public void update(int[][] tiles, int[] collisionTiles)
    {
        // Update player position based on speed
        speedY += 0.5; // Gravity

        if (!inAir) speedX *= 0.85; //Friction
        else speedX *= 0.9; //Air resistance
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
