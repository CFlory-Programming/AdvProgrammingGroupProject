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
        inAir = false;
    }
    {
        height = 0;
        width = 0;
        x = 0;
        y = 0;
        score = 0;
        lives = 3;
    }
    
    public void jump()
    {
            
    }
    
    public void walk()
    {
        
    }
    
    public void run()
    {
        
    }
    
    public void interact()
    {
        
    }

    public void display(int camX, int camY)
    {
        // Update player position based on speed
        speedY += 0.5; // Gravity

        // Collision detection for x direction
        x += speedX;
        boolean collideX = checkCollision(x, y + height/2, 50) || checkCollision(x, y, 50) || (checkCollision(x, y + height, 50) && y%50!=0) || checkCollision(x + width, y + height/2, 50) || checkCollision(x + width, y, 50) || (checkCollision(x + width, y + height, 50) && y%50!=0);
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
        boolean collideY = checkCollision(x, y + height, 50) || (checkCollision(x + width, y + height, 50) && x%50!=0) || checkCollision(x, y, 50) || (checkCollision(x + width, y, 50) && x%50!=0);
        if (collideY) {
            if(speedY>0){
              y = 50*(y/50);
            } else if(speedY<0){
              y = 50*(y/50)+50;
            }
            speedY = 0;
            inAir = false;
        } else {
            inAir = true;
        }

        // Draw player as a rectangle for now
        fill(#FF0000);
        rect(x - camX, y - camY, width, height);
    }

    public boolean checkCollision(int tileX, int tileY, int tileSize)
    {
        return tiles[tileX / tileSize][tileY / tileSize] == 1;
    }
}