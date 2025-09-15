public class Enemy
{
    public Enemy()
    {
        int height = 0;
        int width = 0;
        int x = 0;
        int y = 0;
        int health = 100;
    }
    
    public void jump(int height, char dir)
    {
            
    }
    
    public void walk(int distance, char dir)
    {
        if (dir == 'r') {
            x += distance;
        } else if (dir == 'l') {
            x -= distance;
        }
    }
    
    public void run(int distance, char dir)
    {
        
    }
    
    public boolean isHit()
    {
        return true;
    }
    
    public void display()
    {
        
    }
}
