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
        if (checkCollision(x, y + height, 50)) {
            while (checkCollision(x, y + height, 50)) {
                x -= speedX/abs(speedX);
            }
            speedX = 0;
            
        }

        // Collision detection for y direction
        y += speedY;
        if (checkCollision(x, y + height, 50)) {
            while (checkCollision(x, y + height, 50)) {
                y -= speedY/abs(speedY);
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
        // Simple AABB collision detection
        if (x < tileX + tileSize && x + width > tileX && y < tileY + tileSize && y + height > tileY) {
            return true;
        }
        return false;
    }
}
