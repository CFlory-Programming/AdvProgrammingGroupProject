import processing.core.PImage;
import java.util.ArrayList;

public class Player
{

    int height;
    int width;
    int x;
    int y;
    int score;
    int lives;
    int health;
    int maxHealth;
    int stamina;
    int maxStamina;
    int staminaRechargeCooldown;
    boolean isMoving;
    double speedX;
    double speedY;
    boolean inAir;
    int frame;
    int animSpeed;
    String state;
    boolean attacked;
    boolean dead;
    boolean outOfBounds;

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
        inAir = true;
        frame = 0;
        animSpeed = 10;
        state = "Idle";
        attacked = false;
        dead = false;
        health = 100;
        maxHealth = 100;
        stamina = 500;
        maxStamina = 500;
        staminaRechargeCooldown = 0;
    }
    
    public void jump()
    {
        speedY = -11;
    }
    
    public void walk(char direction)
    {
        isMoving = true;
        if (!inAir) {
            if (speedX == 0 && direction == 'r') {
                speedX = 1;
            } else if (speedX == 0 && direction == 'l') {
                speedX = -1;
            }
            if (direction == 'r' && speedX<=4.5) {
                speedX += 0.5;
            } else if (direction == 'l' && speedX>=-4.5) {
                speedX -= 0.5;
            } else if (direction == 'r') {
                speedX = 5;
            } else if (direction == 'l') {
                speedX = -5;
            }
        } else {
            if (speedX == 0 && direction == 'r') {
                speedX = 1;
            } else if (speedX == 0 && direction == 'l') {
                speedX = -1;
            }
            if (direction == 'r' && speedX<=5.875) {
                speedX += 0.25;
            } else if (direction == 'l' && speedX>=-5.875) {
                speedX -= 0.25;
            } else if (direction == 'r') {
                speedX = 5;
            } else if (direction == 'l') {
                speedX = -5;
            }
        }
    }
    
    public void run(char direction)
    {
        isMoving = true;
        if (stamina > 5) {  // Require some minimum stamina to start running
            staminaRechargeCooldown = 30;  // 1 second at 60 FPS
            stamina = Math.max(0, stamina - 3);  // Prevent negative stamina
            if (!inAir) {
                if (speedX == 0 && direction == 'r') {
                    speedX = 1;
                } else if (speedX == 0 && direction == 'l') {
                    speedX = -1;
                }
                if (direction == 'r' && speedX<=7.2) {
                    speedX += 0.8;
                } else if (direction == 'l' && speedX>=-7.2) {
                    speedX -= 0.8;
                } else if (direction == 'r') {
                    speedX = 8;
                } else if (direction == 'l') {
                    speedX = -8;
                }
            } else {
                if (speedX == 0 && direction == 'r') {
                    speedX = 1;
                } else if (speedX == 0 && direction == 'l') {
                    speedX = -1;
                }
                if (direction == 'r' && speedX<=7.6) {
                    speedX += 0.4;
                } else if (direction == 'l' && speedX>=-7.6) {
                    speedX -= 0.4;
                } else if (direction == 'r') {
                    speedX = 8;
                } else if (direction == 'l') {
                    speedX = -8;
                }
            }
        } else {
            walk(direction);
        }
    }
    
    public void interact()
    {
        
    }

    public void die()
    {
        dead = true;
        health = maxHealth;
        stamina = maxStamina;
        lives--;
    }
    
    public void display(int camX, int camY)
    {
        KonQuestGame.sketch.noStroke();
        KonQuestGame.sketch.fill(255,0,0);
        KonQuestGame.sketch.rect(x - camX, y - camY, width, height);
        // PImage sprite = KonQuestGame.sketch.loadImage(state + frame + ".png");
        // sprite.resize(width, height);
        // KonQuestGame.sketch.image(sprite, x - camX, y - camY);

        // Health bar in top right corner of screen
        KonQuestGame.sketch.fill(0);
        KonQuestGame.sketch.rect(10, 10, 200, 20);
        KonQuestGame.sketch.fill(255, 0, 0);
        int healthWidth = (int) ((health / (double) maxHealth) * 200);
        KonQuestGame.sketch.rect(10, 10, healthWidth, 20);

        // Stamina bar in top right corner of screen, below health bar
        KonQuestGame.sketch.fill(0);
        KonQuestGame.sketch.rect(10, 40, 200, 20);
        KonQuestGame.sketch.fill(0, 0, 255);
        int staminaWidth = Math.max(0, (int) ((Math.min(stamina, maxStamina) / (double) maxStamina) * 200));
        KonQuestGame.sketch.rect(10, 40, staminaWidth, 20);
    }

    public void update(int[][] tiles, int[] collisionTiles, boolean pressed)
    {
        // Stamina regeneration logic
        if (staminaRechargeCooldown > 0) {
            staminaRechargeCooldown--;
        } else if (stamina < maxStamina) {
            // Regenerate stamina faster when standing still, slower when walking
            if (!isMoving) {
                stamina = Math.min(maxStamina, stamina + 4);  // Faster regeneration when still
            } else {
                stamina = Math.min(maxStamina, stamina + 2);  // Slower regeneration when moving
            }
        }
        
        // Reset movement flag each frame
        isMoving = false;

        // Update player position based on speed
        speedY += 0.5; // Gravity

        if (speedY > 15) {
            speedY = 15; // Terminal velocity
        }

        if (!pressed) {
            if (!inAir) speedX *= 0.85; //Friction
            else speedX *= 0.9; //Air resistance
            if ((int) speedX == 0){
                speedX = 0;
            }
        }

        // Collision detection for x direction
        x += speedX;
        boolean collideX = checkCollision(x, y + height/2, 50, tiles, collisionTiles) || checkCollision(x, y, 50, tiles, collisionTiles) || (checkCollision(x, y + height, 50, tiles, collisionTiles) && y%50!=0) || checkCollision(x + width, y + height/2, 50, tiles, collisionTiles) || checkCollision(x + width, y, 50, tiles, collisionTiles) || (checkCollision(x + width, y + height, 50, tiles, collisionTiles) && y%50!=0);
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
        boolean collideY = checkCollision(x, y + height, 50, tiles, collisionTiles) || (checkCollision(x + width, y + height, 50, tiles, collisionTiles) && x%50!=0) || checkCollision(x, y, 50, tiles, collisionTiles) || (checkCollision(x + width, y, 50, tiles, collisionTiles) && x%50!=0);
        if (collideY) {
            if(speedY>=0){
              y = 50*(y/50);
                inAir = false;
            } else if(speedY<0){
              y = 50*(y/50)+50;
            }
            speedY = 0;
        } else {
            inAir = true;
        }

        if (health <= 0 && !dead) {
            die();
        }
    }

    public void checkEnemyCollision(ArrayList<Enemy> enemies) {
        boolean touchingEnemy = false;
        for (Enemy enemy : enemies) {
            // Check if player is touching the enemy's body
            if ((x + width > enemy.x && x < enemy.x + enemy.width && y + height > enemy.y && y < enemy.y + enemy.height)) {
                touchingEnemy = true;
            }
        }
        if (!touchingEnemy) {
            attacked = false; // Reset player's attacked status when not colliding with any enemy
        }
    }

    private boolean checkCollision(int tileX, int tileY, int tileSize, int[][] tiles, int[] collisionTiles) {
        try {
            int tileindex = tiles[tileX / tileSize][tileY / tileSize];
            outOfBounds = false;
            for (int ct : collisionTiles) {
                if (tileindex == ct) {
                    return true;
                }
            }
            return false;
        } catch (ArrayIndexOutOfBoundsException e) {
            outOfBounds = true;
            return false;
        }
    }
}
