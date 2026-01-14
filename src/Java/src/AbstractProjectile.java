abstract class AbstractProjectile {
    float x, y, speedX, speedY, direction;
    int width, height, distance;
    boolean exists, outOfBounds;

    public AbstractProjectile(float x, float y, float direction, float speed, int width, int height) {
        this.x = x;
        this.y = y;
        this.speedX = (float)(Math.cos(direction) * speed);
        this.speedY = (float)(Math.sin(direction) * speed);
        this.direction = direction;
        this.width = width;
        this.height = height;
        this.exists = true;
        this.distance = 0;
    }

    public float[][] getCorners() {
        float[][] corners = new float[4][2];

        corners[0] = new float[]{(float) (x - width/2*Math.cos(direction) + height/2*Math.sin(direction)), (float) (y - width/2*Math.sin(direction) - height/2*Math.cos(direction))};
        corners[1] = new float[]{(float) (x + width/2*Math.cos(direction) + height/2*Math.sin(direction)), (float) (y + width/2*Math.sin(direction) - height/2*Math.cos(direction))};
        corners[2] = new float[]{(float) (x - width/2*Math.cos(direction) - height/2*Math.sin(direction)), (float) (y - width/2*Math.sin(direction) + height/2*Math.cos(direction))};
        corners[3] = new float[]{(float) (x + width/2*Math.cos(direction) - height/2*Math.sin(direction)), (float) (y + width/2*Math.sin(direction) + height/2*Math.cos(direction))};

        return corners;
    }

    public float[][][] getBoundBox() {
        float[][] corners = getCorners();

        // ax is max x, ay is max y, ix is min x, iy is min y

        float minX = corners[0][0], maxX = corners[0][0];
        float minY = corners[0][1], maxY = corners[0][1];
        for (int i = 1; i < 4; i++) {
            if (corners[i][0] < minX) minX = corners[i][0];
            if (corners[i][0] > maxX) maxX = corners[i][0];
            if (corners[i][1] < minY) minY = corners[i][1];
            if (corners[i][1] > maxY) maxY = corners[i][1];
        }

        float[][][] result = new float[2][2][2];
        // result[0][0] = bottom-left, result[0][1] = bottom-right
        // result[1][0] = top-left,    result[1][1] = top-right
        result[0][0] = new float[]{minX, minY};
        result[0][1] = new float[]{maxX, minY};
        result[1][0] = new float[]{minX, maxY};
        result[1][1] = new float[]{maxX, maxY};
        return result;
    }

    public boolean checkHit(int[][] tiles, int[] collisionTiles) {
        float[][][] boundBox = getBoundBox();
        float[] bbl = boundBox[0][0];
        float[] bbr = boundBox[0][1];
        float[] btl = boundBox[1][0];
        float[] btr = boundBox[1][1];
        float[][] corners = getCorners();
        float[] bl = corners[0];
        float[] br = corners[1];
        float[] tl = corners[2];
        float[] tr = corners[3];
        int minTileX = (int) Math.floor(bbl[0] / 50.0f);
        int maxTileX = (int) Math.floor(bbr[0] / 50.0f);
        int minTileY = (int) Math.floor(bbl[1] / 50.0f);
        int maxTileY = (int) Math.floor(btr[1] / 50.0f);
        for (int i = minTileX; i <= maxTileX; i++) {
            for (int j = minTileY; j <= maxTileY; j++) {
                // Check if the rectangle and tile overlap
                for (int ct : collisionTiles) {
                    try {
                        if (tiles[i][j] == ct) {
                            boolean coll = checkCollision(
                                    tl, tr, bl, br,
                                    new float[]{i * 50, (j + 1) * 50}, new float[]{(i + 1) * 50, (j + 1) * 50},
                                    new float[]{i * 50, j * 50}, new float[]{(i + 1) * 50, j * 50}
                            );
                            if (coll) {
                                return true;
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        // Ignore out of bounds
                    }
                }
            }
        }
        return false;
    }

    public boolean checkCollision(float[] tl1, float[] tr1, float[] bl1, float[] br1,
                                   float[] tl2, float[] tr2, float[] bl2, float[] br2) {
        // tl = top left, tr = top right, bl = bottom left, br = bottom right
        // all are float[2] arrays and are vertices of a rectangle
        // First consider axis formed by rectangle 1

        float[] xAxis1 = new float[]{br1[0] - bl1[0], br1[1] - bl1[1]};
        float[] yAxis1 = new float[]{tl1[0] - bl1[0], tl1[1] - bl1[1]};

        float[] tl2To1 = convertToCoordinates(xAxis1, yAxis1, new float[]{tl2[0] - bl1[0], tl2[1] - bl1[1]});
        float[] tr2To1 = convertToCoordinates(xAxis1, yAxis1, new float[]{tr2[0] - bl1[0], tr2[1] - bl1[1]});
        float[] bl2To1 = convertToCoordinates(xAxis1, yAxis1, new float[]{bl2[0] - bl1[0], bl2[1] - bl1[1]});
        float[] br2To1 = convertToCoordinates(xAxis1, yAxis1, new float[]{br2[0] - bl1[0], br2[1] - bl1[1]});

        if ((tl2To1[0] < 0 && bl2To1[0] < 0 && tr2To1[0] < 0 && br2To1[0] < 0) || (tl2To1[0] > 1 && bl2To1[0] > 1 && tr2To1[0] > 1 && br2To1[0] > 1)) {
            return false;
        }
        if ((tl2To1[1] < 0 && tr2To1[1] < 0 && bl2To1[1] < 0 && br2To1[1] < 0) || (tl2To1[1] > 1 && tr2To1[1] > 1 && bl2To1[1] > 1 && br2To1[1] > 1)) {
            return false;
        }

        // Now consider axis formed by rectangle 2

        float[] xAxis2 = new float[]{br2[0] - bl2[0], br2[1] - bl2[1]};
        float[] yAxis2 = new float[]{tl2[0] - bl2[0], tl2[1] - bl2[1]};

        float[] tl1To2 = convertToCoordinates(xAxis2, yAxis2, new float[]{tl1[0] - bl2[0], tl1[1] - bl2[1]});
        float[] tr1To2 = convertToCoordinates(xAxis2, yAxis2, new float[]{tr1[0] - bl2[0], tr1[1] - bl2[1]});
        float[] bl1To2 = convertToCoordinates(xAxis2, yAxis2, new float[]{bl1[0] - bl2[0], bl1[1] - bl2[1]});
        float[] br1To2 = convertToCoordinates(xAxis2, yAxis2, new float[]{br1[0] - bl2[0], br1[1] - bl2[1]});

        if ((tl1To2[0] < 0 && bl1To2[0] < 0 && tr1To2[0] < 0 && br1To2[0] < 0) || (tl1To2[0] > 1 && bl1To2[0] > 1 && tr1To2[0] > 1 && br1To2[0] > 1)) {
            return false;
        }
        if ((tl1To2[1] < 0 && tr1To2[1] < 0 && bl1To2[1] < 0 && br1To2[1] < 0) || (tl1To2[1] > 1 && tr1To2[1] > 1 && bl1To2[1] > 1 && br1To2[1] > 1)) {
            return false;
        }

        return true;
    }

    public static float[] convertToCoordinates(float[] xAxis, float[] yAxis, float[] point) {
        // xAxis[0]*x+yAxis[0]*y=point[0];
        // xAxis[1]*x+yAxis[1]*y=point[1];
        return new float[]{(point[0]*yAxis[1]-point[1]*yAxis[0])/(xAxis[0]*yAxis[1]-xAxis[1]*yAxis[0]),
                           (point[1]*xAxis[0]-point[0]*xAxis[1])/(xAxis[0]*yAxis[1]-xAxis[1]*yAxis[0])};
    }

    public abstract void update();
    public abstract void physics();
    public abstract void display(int camX, int camY);
}