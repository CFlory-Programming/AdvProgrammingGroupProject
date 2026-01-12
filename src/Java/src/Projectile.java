import java.util.*;

public class Projectile extends AbstractProjectile {
    public float speed;
    boolean homing;
    Player target;
    public Projectile(float x, float y, int width, int height, int speed, boolean homing, Player p1) {
        super(x, y, 0, speed, width, height); // Initial direction set to 0
        this.homing = homing;
        this.speed = speed;
        this.target = p1;
        changeDirection(p1);
        super.speedX = (float)(Math.cos(direction) * speed);
        super.speedY = (float)(Math.sin(direction) * speed);
    }
    
    public Projectile(float x, float y, int width, int height, int speed, float direction, boolean homing, Player p1) {
        super(x, y, direction, speed, width, height);
        changeDirection(p1);
        this.speed = speed;
        this.homing = homing;
        this.target = p1;
    }

    public void update()
    {
        physics();  // Add physics calculation
        if (homing) {
            changeDirection(target);
            super.speedX = (float)(Math.cos(direction) * speed);
            super.speedY = (float)(Math.sin(direction) * speed);
        }
        x += speedX;
        y += speedY;
        distance += Math.sqrt(speedX*speedX + speedY*speedY);
        float[][] corners = getCorners();
        if(target.visible && checkCollision(new float[]{(float) (target.x+target.speedX), (float) (target.y+target.speedY+target.height)},
                                            new float[]{(float) (target.x+target.speedX+target.width), (float) (target.y+target.speedY+target.height)},
                                            new float[]{(float) (target.x+target.speedX), (float) (target.y+target.speedY)},
                                            new float[]{(float) (target.x+target.speedX+target.width), (float) (target.y+target.speedY)},
                                            corners[2], corners[3], corners[0], corners[1])) {
            if (speedY < target.speedY && checkCollision(new float[]{(float) (target.x+target.speedX), (float) (target.y+target.height)},
                                                        new float[]{(float) (target.x+target.speedX+target.width), (float) (target.y+target.height)},
                                                        new float[]{(float) (target.x+target.speedX), (float) (target.y)},
                                                        new float[]{(float) (target.x+target.speedX+target.width), (float) (target.y)},
                                                        corners[2], corners[3], corners[0], corners[1])) {
                topCollide(target);
            } else if (speedY > target.speedY && checkCollision(new float[]{(float) (target.x+target.speedX), (float) (target.y+target.height)},
                                                        new float[]{(float) (target.x+target.speedX+target.width), (float) (target.y+target.height)},
                                                        new float[]{(float) (target.x+target.speedX), (float) (target.y)},
                                                        new float[]{(float) (target.x+target.speedX+target.width), (float) (target.y)},
                                                        corners[2], corners[3], corners[0], corners[1])) {
                bottomCollide(target);
            } else if (speedX < target.speedX && checkCollision(new float[]{(float) (target.x), (float) (target.y+target.speedY+target.height)}, 
                                                        new float[]{(float) (target.x+target.width), (float) (target.y+target.speedY+target.height)},
                                                        new float[]{(float) (target.x), (float) (target.y+target.speedY)},
                                                        new float[]{(float) (target.x+target.width), (float) (target.y+target.speedY)},
                                                        corners[2], corners[3], corners[0], corners[1])) {
                leftCollide(target);
            } else if (speedX > target.speedX && checkCollision(new float[]{(float) (target.x), (float) (target.y+target.speedY+target.height)}, 
                                                        new float[]{(float) (target.x+target.width), (float) (target.y+target.speedY+target.height)},
                                                        new float[]{(float) (target.x), (float) (target.y+target.speedY)},
                                                        new float[]{(float) (target.x+target.width), (float) (target.y+target.speedY)},
                                                        corners[2], corners[3], corners[0], corners[1])) {
                rightCollide(target);
            } else {
                // Knockback effect
                if (speedX < 0) {
                    leftCollide(target);
                } else if (speedX > 0) {
                    rightCollide(target);
                } else {
                    if (speedY > 0) {
                        bottomCollide(target);
                    } else {
                        topCollide(target);
                    }
                }
            }
        }
        target.checkEnemyCollision(this);
        if (distance > 1000) {
            exists = false;
        }
    }

    public void physics()
    {
        
    }

    public void topCollide(Player p1) {
        p1.speedX += speedX/speed*5;
        p1.speedY += speedY/speed*5;
        exists = false;
    }

    public void bottomCollide(Player p1) {
        if (p1.immune) {
            return;
        }
        p1.speedX += speedX/speed*5;
        p1.speedY += speedY/speed*5;
        if (!p1.launched && !p1.attacked) {
            p1.health -= 5;
        }
        exists = false;
    }

    public void leftCollide(Player p1) {
        if (p1.immune) {
            return;
        }
        p1.speedX += speedX/speed*5;
        p1.speedY += speedY/speed*5;
        if (!p1.launched && !p1.attacked) {
            p1.health -= 5;
        }
        exists = false;
    }

    public void rightCollide(Player p1) {
        if (p1.immune) {
            return;
        }
        p1.speedX += speedX/speed*5;
        p1.speedY += speedY/speed*5;
        if (!p1.launched && !p1.attacked) {
            p1.health -= 5;
        }
        exists = false;
    }

    public void changeDirection(Player p1) {
        float actualPx = (float) (p1.x + p1.width/2 + 0.5*p1.speedX);
        float actualPy = (float) (p1.y + p1.height/2 + 0.5*p1.speedY);
        float actualX = (float) (x + width/2*Math.cos(direction) - height/2*Math.sin(direction));
        float actualY = (float) (y + width/2*Math.sin(direction) + height/2*Math.cos(direction));
        direction = (float) Math.atan2(((actualPy)-actualY), ((actualPx)-actualX));
    }

    public void display(int camX, int camY)
    {
        KonQuestGame.sketch.pushMatrix();
        KonQuestGame.sketch.translate((float) (x + width/2*Math.cos(direction) - height/2*Math.sin(direction) - camX), (float) (y - camY + width/2*Math.sin(direction) + height/2*Math.cos(direction)));
        KonQuestGame.sketch.rotate(direction);
        KonQuestGame.sketch.rect(-width/2, -height/2, width, height);
        KonQuestGame.sketch.popMatrix();
    }
}
