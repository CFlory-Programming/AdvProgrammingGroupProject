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
        if (dir == 'u')
        {
            y += height;
        } else if (dir == 'r')
        {
            x += height;
            y += height;
        } else if (dir == 'l')
        {
            x -= height;
            y += height;
        }
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
