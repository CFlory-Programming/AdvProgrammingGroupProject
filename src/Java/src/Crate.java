import processing.core.PImage;

public class Crate extends LevelObject
{
    public String loot;
    public boolean broken;

    public Crate(PImage sprite, int x, int y, int height, int width)
    {
        super(sprite, x, y, height, width);
        broken = false;
        //loot = new String("");
    }
    
    public boolean isBroken()
    {
        return broken;
    }
}
