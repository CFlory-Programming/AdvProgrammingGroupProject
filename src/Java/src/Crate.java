import processing.core.PImage;

public class Crate extends LevelObject
{
    public String loot;
    public boolean broken;

    public Crate(PImage sprite, int x, int y, int height, int width, String loot)
    {
        super(sprite, x, y, height, width);
        broken = false;
        this.loot = loot;
    }
    
    public boolean isBroken()
    {
        return broken;
    }

    public void update(Player player)
    {
        if (collidesWith(player) && !broken && player.speedY > 0) {
            broken = true;
            player.jump(15);
            // Determine loot here
            switch (loot) {
                case "Health":
                    player.health += 20;
                    if (player.health > player.maxHealth) {
                        player.health = player.maxHealth;
                    }
                    break;
                case "Stamina":
                    player.stamina += 100;
                    if (player.stamina > player.maxStamina) {
                        player.stamina = player.maxStamina;
                    }
                    break;
                case "Score":
                    player.score += 100;
                    break;
                default:
                    // No loot
                    break;
            }
        }
    }
}
