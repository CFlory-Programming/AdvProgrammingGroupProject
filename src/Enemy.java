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
    
    public void move(int distance, char dir, boolean isRunning)
    {
        if (isRunning) {
            if (dir == 'r') {
                x += distance*2;
            } else if (dir == 'l') {
                x -= distance*2;
            }
        } else {
            if (dir == 'r') {
                x += distance;
            } else if (dir == 'l') {
                x -= distance;
            }
        }
    }
    
    public boolean isHit()
    {
        return true;
    }
    
    public void display()
    {
        
    }
}
