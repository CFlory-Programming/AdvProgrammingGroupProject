import processing.core.PImage;
import java.util.ArrayList;

public class Player
{

    int height;
    int width;
    float x;
    float y;
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
    boolean canShoot;
    double speedMultiplier;
    int frame;
    int animSpeed;
    int timer;
    int arrowTimer;
    String state;
    boolean visible;
    boolean launching;
    boolean launched;
    boolean attacked;
    boolean dead;
    boolean outOfBounds;
    boolean immune;
    boolean hit;
    ArrayList<Arrow> arrows;
    boolean direction; // true is right, false is left

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
        hit = false;
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
        canShoot = false;
        arrows = new ArrayList<Arrow>();
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
        deleteArrows();
        for (LevelObject lo : KonQuestGame.levelObjects) {
            if (lo instanceof PowerUp && ((PowerUp) lo).isActive) {
                ((PowerUp) lo).removeEffect(this);
            }
        }
    }
    
    public void display(int camX, int camY)
    {
        for (int i = 0; i < arrows.size(); i++) {
            arrows.get(i).display(camX, camY);
        }
        if (!visible || timer % 10 >= 5) {
            return;
        }

        if (!KonQuestGame.pauseMenu) frame++;

        if (Math.abs(speedX) > 0.5) {
            state = "Walk";
        } else {
            state = "Idle";
        }

        PImage sprite = null;

        if (state.equals("Idle")) {
            sprite = idle;
        } else if (state.equals("Walk")) {
            sprite = walk[frame/animSpeed % walk.length];
        }

            sprite.resize(width, height);
            KonQuestGame.sketch.image(sprite, x - camX, y - camY);

        if (frame == 60) {
            frame = 0;
        } 

        if (canShoot) {
            KonQuestGame.sketch.fill(150, 75, 0);
            KonQuestGame.sketch.rect(x - camX - 5 + width/2, y - camY - 20 + height/2, 10, 40);
            KonQuestGame.sketch.stroke(150, 75, 0);
            KonQuestGame.sketch.strokeWeight(10);
            KonQuestGame.sketch.noFill();
            KonQuestGame.sketch.arc(x - camX + width/2, y - camY + height/2, 40, 40, - (float) Math.PI/2, (float) Math.PI/2);
            KonQuestGame.sketch.strokeWeight(4);
        }
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
                x = (float)(50 * Math.floor(x / 50.0));;
            } else if(speedX<0) {
                x = (float)(50 * (Math.floor(x / 50.0) + 1));
            }
            speedX = 0;
        }

        // Collision detection for y direction
        y += speedY;
        boolean collideY = checkCollision(x, y + height, 50, tiles, collisionTiles) || (checkCollision(x + width, y + height, 50, tiles, collisionTiles) && x%50!=0) || checkCollision(x, y, 50, tiles, collisionTiles) || (checkCollision(x + width, y, 50, tiles, collisionTiles) && x%50!=0);
        if (collideY) {
            launched = false;
            if(speedY>=0){
                y = (float)(50 * Math.floor(y / 50.0));
                inAir = false;
            } else if(speedY<0){
                y = (float)(50 * (Math.floor(y / 50.0) + 1));
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

        if (speedX > 0) {
            direction = true;
        } else if (speedX < 0) {
            direction = false;
        }

        for (int i = 0; i < arrows.size(); i++) {
            arrows.get(i).update();
        }

        for (int i = 0; i < arrows.size(); i++) {
            arrows.get(i).update();
            if(!arrows.get(i).exists) {
                arrows.remove(i);
                i--;
            }
        }

        arrowTimer = Math.max(0, arrowTimer - 1);        
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
            hit = false;
        }
    }

    public void deleteArrows() {
        arrows.clear();
    }

    public void shoot(float direction) {
        if (!canShoot) {
            return;
        }
        if (arrowTimer > 0) {
            return;
        }
        arrowTimer = 10;
        Arrow arrow = new Arrow(x + width / 2, y + height / 2, direction, 10, 40, 5);
        arrows.add(arrow);
    }

    public void checkEnemyCollision(Projectile projectile) {
        boolean touchingEnemy = false;
        if ((x + width > projectile.x && x < projectile.x + projectile.width && y + height > projectile.y && y < projectile.y + projectile.height)) {
            touchingEnemy = true;
        }
        if (!touchingEnemy) {
            attacked = false; // Reset player's attacked status when not colliding with any enemy
            hit = false;
        }
    }

    public boolean checkCollision(float tileX, float tileY, int tileSize, int[][] tiles, int[] collisionTiles) {
        try {
            int tileindex = tiles[(int)tileX / tileSize][(int)tileY / tileSize];
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
