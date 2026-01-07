public class Arrow{
    float x, y, speedX, speedY, direction;
    int width, height, distance;
    boolean exists, outOfBounds;
    public Arrow(float x, float y, float direction, float speed, int width, int height) {
        this.x = x;
        this.y = y;
        this.speedX = (float)(Math.cos(direction) * speed);
        this.speedY = (float)(Math.sin(direction) * speed);
        this.direction = direction;
        this.width = width;
        this.height = height;
        this.exists = true;
    }

    public void update() {
        physics();
        if (checkHit(KonQuestGame.tiles, KonQuestGame.collisionTiles)) {
            speedX = 0;
            speedY = 0;
        }
        x += speedX;
        y += speedY;
        distance += Math.sqrt((speedX*speedX) + (speedY*speedY));
        if(distance > 1000) {
            exists = false;
        }
        if(outOfBounds) {
            exists = false;
        }
    }

    private float[][][] getBoundBox() {
        float[][] corners = new float[4][2];

        corners[0] = new float[]{(float) (x - width/2*Math.cos(direction) - height/2*Math.sin(direction)), (float) (y - height/2*Math.sin(direction) + width/2*Math.cos(direction))};
        corners[1] = new float[]{(float) (x + width/2*Math.cos(direction) - height/2*Math.sin(direction)), (float) (y - height/2*Math.sin(direction) - width/2*Math.cos(direction))};
        corners[2] = new float[]{(float) (x + width/2*Math.cos(direction) + height/2*Math.sin(direction)), (float) (y + height/2*Math.sin(direction) - width/2*Math.cos(direction))};
        corners[3] = new float[]{(float) (x - width/2*Math.cos(direction) + height/2*Math.sin(direction)), (float) (y + height/2*Math.sin(direction) + width/2*Math.cos(direction))};

        // ax is max x, ay is max y, ix is min x, iy is min y

        int ax = 0;
        for (int i = 0; i < 4; i++) {
            if (corners[i][0] > corners[ax][0]) {
                ax = i;
            }
        }

        int ay = 0;
        for (int i = 0; i < 4; i++) {
            if (corners[i][1] > corners[ay][1]) {
                ay = i;
            }
        }

        int ix = 0;
        for (int i = 0; i < 4; i++) {
            if (corners[i][0] < corners[ix][0]) {
                ix = i;
            }
        }

        int iy = 0;
        for (int i = 0; i < 4; i++) {
            if (corners[i][1] < corners[iy][1]) {
                iy = i;
            }
        }
        
        // float[0] is horizontal, float[0][0] is lesser

        float[][][] result = new float[2][2][2];
        result[0][0] = corners[ix];
        result[0][1] = corners[ax];
        result[1][0] = corners[iy];
        result[1][1] = corners[ay];
        return result;
    }

    private boolean checkHit(int[][] tiles, int[] collisionTiles) {
        /*
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (checkCollision((float) (x - width/2*Math.cos(direction) + i*Math.cos(direction) - j*Math.sin(direction)), (float) (y - height/2*Math.sin(direction) + i*Math.sin(direction) + j*Math.cos(direction)), 50, tiles, collisionTiles)) {
                    return true;
                }
            }
        }
        return false;*/
        float[][][] boundBox = getBoundBox();
        float[] minX = boundBox[0][0];
        float[] maxX = boundBox[0][1];
        float[] minY = boundBox[1][0];
        float[] maxY = boundBox[1][1];
        for (int i = (int)(minX[0]/50); i <= (int)(maxX[0]/50); i++) {
            for (int j = (int)(minY[1]/50); j <= (int)(maxY[1]/50); j++) {
                // Check if the rectangle and tile overlap
                float tileX = i * 50;
                float tileY = j * 50;
                if (maxX[0] > tileX && minX[0] < tileX + 50 && maxY[1] > tileY && minY[1] < tileY + 50) {
                    if (checkCollision(tileX, tileY, 50, tiles, collisionTiles)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void physics() {
        if (speedX == 0 && speedY == 0) {
            return;
        }
        // Gravity effect
        speedY += 0.1;
        direction = (float) Math.atan2(speedY, speedX);
        if (speedX < 0) {
            direction += Math.PI;
        }
    }

    public void display(int camX, int camY) {
        KonQuestGame.sketch.pushMatrix();
        KonQuestGame.sketch.translate(x + width / 2 - camX, y + height / 2 - camY);
        KonQuestGame.sketch.rotate(direction);
        KonQuestGame.sketch.fill(150, 75, 0);
        KonQuestGame.sketch.rectMode(KonQuestGame.sketch.CENTER);
        KonQuestGame.sketch.rect(0, 0, width, height);
        KonQuestGame.sketch.rectMode(KonQuestGame.sketch.CORNER);
        KonQuestGame.sketch.popMatrix();
    }

    private boolean checkCollision(float tileX, float tileY, int tileSize, int[][] tiles, int[] collisionTiles) {
        try {
            int tileindex = tiles[((int) tileX) / tileSize][((int) tileY) / tileSize];
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
