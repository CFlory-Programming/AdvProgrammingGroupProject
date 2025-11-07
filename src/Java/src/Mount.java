import processing.core.PImage;

public class Mount {
    public PImage sprite;
    public int x, y, width, height;
    public boolean mounted;
    private double speedX, speedY;
    private boolean inAir;

    public Mount(PImage sprite, int x, int y, int width, int height) {
        this.sprite = sprite;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.mounted = false;
        this.speedX = 0;
        this.speedY = 0;
        this.inAir = true;
    }

    public void display(int camX, int camY) {
        KonQuestGame.sketch.image(sprite, x - camX, y - camY, width, height);
    }

    public boolean collidesWith(Player player) {
        if (player.x + player.width > x && player.x < x + width &&
            player.y + player.height > y && player.y < y + height) {
            return true;
        }
        return false;
    }

    private boolean wasSprintPressed = false;

    public void mount(Player player) {
        // Check for sprint key press/release cycle
        if (KonQuestGame.keys[3] && !wasSprintPressed) {
            if (!mounted && collidesWith(player)) {
                mounted = true;
                // Adjust player position to be on the mount
                player.x = x + (width - player.width) / 2;
                player.y = y + (height - player.height) / 2;
            } else if (mounted) {
                unmount(player);
            }
        }
        wasSprintPressed = KonQuestGame.keys[3];
    }

    public void unmount(Player player) {
        mounted = false;
        // Move the player slightly to the right when dismounting to prevent immediate remounting
        //player.x += width;
    }

    private boolean checkCollision(int tileX, int tileY, int tileSize, int[][] tiles, int[] collisionTiles) {
        try {
            int tileindex = tiles[tileX / tileSize][tileY / tileSize];
            for (int ct : collisionTiles) {
                if (tileindex == ct) {
                    return true;
                }
            }
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    public void update(Player player) {
        if (mounted) {
            // Transfer player's horizontal movement to mount's speed
            if (player.speedX != 0) {
                speedX = player.speedX;
            }
            
            // Allow the player to make the mount jump
            if (!inAir && player.speedY < 0) {
                speedY = player.speedY;
            }

            // Apply gravity
            speedY += 0.5;
            if (speedY > 15) {
                speedY = 15; // Terminal velocity
            }

            // Apply friction when not receiving player input
            if (player.speedX == 0) {
                speedX *= 0.85;
                if (Math.abs(speedX) < 0.1) {
                    speedX = 0;
                }
            }

            // Update position with collision detection
            int[][] tiles = KonQuestGame.tiles;
            int[] collisionTiles = KonQuestGame.collisionTiles;
            int tileSize = KonQuestGame.tileSize;

            // X-axis collision
            x += speedX;
            boolean collideX = checkCollision(x, y + height/2, tileSize, tiles, collisionTiles) || 
                             checkCollision(x, y, tileSize, tiles, collisionTiles) || 
                             checkCollision(x, y + height, tileSize, tiles, collisionTiles) || 
                             checkCollision(x + width, y + height/2, tileSize, tiles, collisionTiles) || 
                             checkCollision(x + width, y, tileSize, tiles, collisionTiles) || 
                             checkCollision(x + width, y + height, tileSize, tiles, collisionTiles);
            if (collideX) {
                if (speedX > 0) {
                    x = tileSize * (x/tileSize);
                } else if (speedX < 0) {
                    x = tileSize * (x/tileSize) + tileSize;
                }
                speedX = 0;
            }

            // Y-axis collision
            y += speedY;
            boolean collideY = checkCollision(x, y + height, tileSize, tiles, collisionTiles) || 
                             checkCollision(x + width, y + height, tileSize, tiles, collisionTiles);
            
            // Only check ceiling collision if we're moving upward
            if (speedY < 0) {
                collideY = collideY || checkCollision(x, y, tileSize, tiles, collisionTiles) || 
                                     checkCollision(x + width, y, tileSize, tiles, collisionTiles);
            }
            
            if (collideY) {
                if (speedY >= 0) {
                    // Snap to grid when landing
                    y = tileSize * ((y + height) / tileSize) - height;
                    inAir = false;
                    speedY = 0;  // Stop vertical movement immediately when landing
                } else if (speedY < 0) {
                    // Hit ceiling
                    y = tileSize * (y / tileSize) + tileSize;
                    speedY = 0;
                }
            } else {
                inAir = true;
            }

            // Sync player position with mount
            player.x = x + (width - player.width) / 2;
            player.y = y - player.height + 10; // Add a small offset to position player better on mount
            
            // Sync player's air state with mount
            player.inAir = inAir;
        }
    }
}
