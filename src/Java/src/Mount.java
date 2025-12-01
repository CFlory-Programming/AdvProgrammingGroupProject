import processing.core.PImage;

public class Mount extends Player {

    PImage sprite;
    boolean mounted;

    public Mount (PImage sprite, int x, int y, int height, int width) {
        super(height, width, x, y, 0, 0);
        this.sprite = sprite;
        mounted = false;
    }

    public void mount(Player player) {
        if (!mounted && collidesWithPlayer(player)) {
            mounted = true;
            //player.x = this.x;
            //player.y = this.y - this.height - player.height;
            player.mount = this;
        }
    }

    public void handleMounts(Player player, boolean interact) {
        if (interact) {
            if (mounted) {
                dismount(player);
            } else {
                mount(player);
            }
        }
    }

    public void dismount(Player player) {
        if (mounted) {
            mounted = false;
            player.mount = null;
            player.speedY = speedY-5;
            player.speedX = speedX;
        }
    }

    public void update(Player player, boolean interact) {
                int[][] tiles = KonQuestGame.tiles;
                int[] collisionTiles = KonQuestGame.collisionTiles;
                int tileSize = KonQuestGame.tileSize;

        // Update player position based on speed
        speedY += 0.5; // Gravity

        if (speedY > 15) {
            speedY = 15; // Terminal velocity
        }

        
            if (!inAir) speedX *= 0.85; //Friction
            else if (!launched) speedX *= 0.9; //Air resistance
            if ((int) speedX == 0){
                speedX = 0;
            }
        

                x += speedX;
                boolean collideX = checkCollision(x, y + height/2, tileSize, tiles, collisionTiles) || checkCollision(x, y, tileSize, tiles, collisionTiles) || (checkCollision(x, y + height, tileSize, tiles, collisionTiles) && y%tileSize!=0) || checkCollision(x + width, y + height/2, tileSize, tiles, collisionTiles) || checkCollision(x + width, y, tileSize, tiles, collisionTiles) || (checkCollision(x + width, y + height, tileSize, tiles, collisionTiles) && y%tileSize!=0);
            if (collideX) {
            launched = false;
            if(speedX>0) {
                            x = tileSize*(x/tileSize);
            } else if(speedX<0) {
                            x = tileSize*(x/tileSize)+tileSize;
            }
            speedX = 0;
        }

        // Collision detection for y direction
        y += speedY;
        boolean collideY = checkCollision(x, y + height, tileSize, tiles, collisionTiles) || (checkCollision(x + width, y + height, tileSize, tiles, collisionTiles) && x%tileSize!=0) || checkCollision(x, y, tileSize, tiles, collisionTiles) || (checkCollision(x + width, y, tileSize, tiles, collisionTiles) && x%tileSize!=0);
        if (collideY) {
            launched = false;
            if(speedY>=0){
                            y = tileSize*(y/tileSize);
                inAir = false;
            } else if(speedY<0){
                            y = tileSize*(y/tileSize)+tileSize;
            }
            speedY = 0;
        } else {
            inAir = true;
        }
        
        handleMounts(player, interact);

        if (mounted) {
            player.x = this.x + this.width / 2 - player.width / 2;
            player.y = this.y - player.height;
        }
    }

    private boolean collidesWithPlayer(Player player) {
        return (player.x < this.x + this.width &&
                player.x + player.width > this.x &&
                player.y < this.y + this.height &&
                player.y + player.height > this.y);
    }

}