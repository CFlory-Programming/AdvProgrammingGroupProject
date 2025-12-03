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
    double speedMultiplier;
    int frame;
    int animSpeed;
    int timer;
    String state;
    boolean visible;
    boolean launching;
    boolean launched;
    boolean attacked;
    boolean dead;
    boolean outOfBounds;
    boolean immune;

    Mount mount;

    PImage idle;
    PImage walk[];

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
        visible = true;
        dead = false;
        outOfBounds = false;
        launching = false;
        timer = 0;
        immune = true;
        mount = null;
        speedMultiplier = 1.0;
    }

    public void loadSprites() {
        idle = KonQuestGame.sketch.loadImage("Idle0.png");
        walk = new PImage[] {
            KonQuestGame.sketch.loadImage("Walk0.png"),
            KonQuestGame.sketch.loadImage("Walk1.png")
        };
    }
    
    public void jump()
    {
        launched = false;
        speedY = -11;
    }

    public void jump(int power)
    {
        launched = false;
        speedY = -power;
    }
    
    public void walk(char direction)
    {
        isMoving = true;
        launched = false;
        if (!inAir) {
            if (speedX == 0 && direction == 'r') {
                speedX = 1 * speedMultiplier;
            } else if (speedX == 0 && direction == 'l') {
                speedX = -1 * speedMultiplier;
            }
            if (direction == 'r' && speedX<=4.5 * speedMultiplier) {
                speedX += 0.5 * speedMultiplier;
            } else if (direction == 'l' && speedX>=-4.5 * speedMultiplier) {
                speedX -= 0.5 * speedMultiplier;
            } else if (direction == 'r') {
                speedX = 5 * speedMultiplier;
            } else if (direction == 'l') {
                speedX = -5 * speedMultiplier;
            }
        } else {
            if (speedX == 0 && direction == 'r') {
                speedX = 1 * speedMultiplier;
            } else if (speedX == 0 && direction == 'l') {
                speedX = -1 * speedMultiplier;
            }
            if (direction == 'r' && speedX<=5.875 * speedMultiplier) {
                speedX += 0.25 * speedMultiplier;
            } else if (direction == 'l' && speedX>=-5.875 * speedMultiplier) {
                speedX -= 0.25 * speedMultiplier;
            } else if (direction == 'r') {
                speedX = 5 * speedMultiplier;
            } else if (direction == 'l') {
                speedX = -5 * speedMultiplier;
            }
        }
    }
    
    public void run(char direction)
    {
        launched = false;
        isMoving = true;
        if (stamina > 5) {  // Require some minimum stamina to start running
            staminaRechargeCooldown = 30;  // 1 second at 60 FPS
            stamina = Math.max(0, stamina - 3);  // Prevent negative stamina
            if (!inAir) {
                if (speedX == 0 && direction == 'r') {
                    speedX = 1 * speedMultiplier;
                } else if (speedX == 0 && direction == 'l') {
                    speedX = -1 * speedMultiplier;
                }
                if (direction == 'r' && speedX<=7.2 * speedMultiplier) {
                    speedX += 0.8 * speedMultiplier;
                } else if (direction == 'l' && speedX>=-7.2 * speedMultiplier) {
                    speedX -= 0.8 * speedMultiplier;
                } else if (direction == 'r') {
                    speedX = 8 * speedMultiplier;
                } else if (direction == 'l') {
                    speedX = -8 * speedMultiplier;
                }
            } else {
                if (speedX == 0 && direction == 'r') {
                    speedX = 1 * speedMultiplier;
                } else if (speedX == 0 && direction == 'l') {
                    speedX = -1 * speedMultiplier;
                }
                if (direction == 'r' && speedX<=7.6 * speedMultiplier) {
                    speedX += 0.4 * speedMultiplier;
                } else if (direction == 'l' && speedX>=-7.6 * speedMultiplier) {
                    speedX -= 0.4 * speedMultiplier;
                } else if (direction == 'r') {
                    speedX = 8 * speedMultiplier;
                } else if (direction == 'l') {
                    speedX = -8 * speedMultiplier;
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
        immune = true;
        timer = 0;

        if (mount != null) {
            mount.dismount(this);
            mount = null;
        }
    }
    
    public void display(int camX, int camY)
    {
        if (!visible || timer % 10 >= 5) {
            return;
        }

        if (!KonQuestGame.pauseMenu) frame++;

        if (Math.abs(speedX) > 0.5) {
            state = "Walk";
        } else {
            state = "Idle";
        }

        // KonQuestGame.sketch.noStroke();
        // KonQuestGame.sketch.fill(255,0,0);
        // KonQuestGame.sketch.rect(x - camX, y - camY, width, height);

        // PImage sprite = KonQuestGame.sketch.loadImage(state + frame + ".png");
        // sprite.resize(width, height);
        // KonQuestGame.sketch.image(sprite, x - camX, y - camY);

        if (state.equals("Idle")) {
            PImage sprite = idle;
            sprite.resize(width, height);
            KonQuestGame.sketch.image(sprite, x - camX, y - camY);
        } else if (state.equals("Walk")) {
            PImage sprite = walk[frame/animSpeed % walk.length];
            sprite.resize(width, height);
            KonQuestGame.sketch.image(sprite, x - camX, y - camY);
        }

        if (frame == 60) {
            frame = 0;
        } 
           
        
        // Health bar in top right corner of screen
        // KonQuestGame.sketch.fill(0);
        // KonQuestGame.sketch.rect(10, 10, 200, 20);
        // KonQuestGame.sketch.fill(255, 0, 0);
        // int healthWidth = (int) ((health / (double) maxHealth) * 200);
        // KonQuestGame.sketch.rect(10, 10, healthWidth, 20);

        // Stamina bar in top right corner of screen, below health bar
        // KonQuestGame.sketch.fill(0);
        // KonQuestGame.sketch.rect(10, 40, 200, 20);
        // KonQuestGame.sketch.fill(0, 0, 255);
        // int staminaWidth = Math.max(0, (int) ((Math.min(stamina, maxStamina) / (double) maxStamina) * 200));
        // KonQuestGame.sketch.rect(10, 40, staminaWidth, 20);
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
            else if (!launched) speedX *= 0.9; //Air resistance
            if ((int) speedX == 0){
                speedX = 0;
            }
        }

        // Collision detection for x direction
        x += speedX;
        boolean collideX = checkCollision(x, y + height/2, 50, tiles, collisionTiles) || checkCollision(x, y, 50, tiles, collisionTiles) || (checkCollision(x, y + height, 50, tiles, collisionTiles) && y%50!=0) || checkCollision(x + width, y + height/2, 50, tiles, collisionTiles) || checkCollision(x + width, y, 50, tiles, collisionTiles) || (checkCollision(x + width, y + height, 50, tiles, collisionTiles) && y%50!=0);
        if (collideX) {
            launched = false;
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
            launched = false;
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

        if (timer == 60) {
            immune = false;
            timer = 0;
        }

        if (immune) {
            timer++;
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

    public void checkEnemyCollision(Projectile projectile) {
        boolean touchingEnemy = false;
        if ((x + width > projectile.x && x < projectile.x + projectile.width && y + height > projectile.y && y < projectile.y + projectile.height)) {
            touchingEnemy = true;
        }
        if (!touchingEnemy) {
            attacked = false; // Reset player's attacked status when not colliding with any enemy
        }
    }

    public boolean checkCollision(int tileX, int tileY, int tileSize, int[][] tiles, int[] collisionTiles) {
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
