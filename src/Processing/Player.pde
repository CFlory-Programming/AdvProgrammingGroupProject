public class Player
{

    int height;
    int width;
    int x;
    int y;
    int score;
    int lives;

    public Player(int height, int width, int x, int y, int score, int lives)
    {
        this.height = height;
        this.width = width;
        this.x = x;
        this.y = y;
        this.score = score;
        this.lives = lives;
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
    
    public void display()
    {
        //draw player
        rect(x, y, width, height);
    }
}