int score, lives, level, camX, camY, tileSize;
int[][] tiles = new int[100][100];
Player p1;

void setup() {
  score = 0;
  lives = 3;
  level = 1;
  camX = 0;
  camY = 0;
  tileSize = 50;
  frameRate(60);
  fullScreen();
  noStroke();
  background(0);
  fill(#FFFFFF);
  p1 = new Player(2*tileSize, tileSize, 0, 0, score, lives);

    for (int i = 0; i < tiles.length; i++) {
        for (int j = 0; j < tiles[i].length; j++) {
            //randomly assign 1 or 0 to each tile
            if (random(1) < 0.1) {
                tiles[i][j] = 1;
            } else {
                tiles[i][j] = 0;
            }
        }
    }
}

void draw() {
  background(0);
  fill(#FFFFFF);
    
    // Draw tiles
    for (int i = 0; i < tiles.length; i++) {
        for (int j = 0; j < tiles[i].length; j++) {
        if (tiles[i][j] == 1) {
            rect(i * tileSize - camX, j * tileSize - camY, tileSize, tileSize);
        }
        }
    }

    // Display Player
    fill(#FF0000);
    p1.display(camX, camY);

    if (keyPressed) {
        if ((key == 'a' || key == 'A')) {
            p1.speedX = -5;
        } else if ((key == 'd' || key == 'D')) {
            p1.speedX = 5;
        } else {
            p1.speedX = 0;
        }
        if ((key == 'w' || key == 'W')) {
            if (!p1.inAir) {
                p1.speedY = -10;
                p1.inAir = true;
            }
        }
    }

    // Move player with WASD keys
    // if (keyPressed) {
    //     if (key == 'a' || key == 'A') {
    //         p1.x -= 5;
    //     } else if (key == 'd' || key == 'D') {
    //         p1.x += 5;
    //     } else if (key == 'w' || key == 'W') {
    //         p1.y -= 5;
    //     } else if (key == 's' || key == 'S') {
    //         p1.y += 5;
    //     }
    // }

    

    // Move Camera with arrow keys
    // if (keyPressed) {
    //     if (keyCode == LEFT) {
    //         camX -= 5;
    //     } else if (keyCode == RIGHT) {
    //         camX += 5;
    //     } else if (keyCode == UP) {
    //         camY -= 5;
    //     } else if (keyCode == DOWN) {
    //         camY += 5;
    //     }
    // }

    // Camera slowly follows player
    if (p1.x < width / 2) {
        camX += -camX * 0.05;
    } else if (p1.x > tiles.length * tileSize - width / 2) {
        camX += (tiles.length * tileSize - camX - width) * 0.05;
    } else {
        camX += (p1.x - camX - width / 2) * 0.05;
    }
    if (p1.y < height / 2) {
        camY += -camY * 0.05;
    } else if (p1.y > tiles[0].length * tileSize - height / 2) {
        camY += (tiles[0].length * tileSize - camY - height) * 0.05;
    } else {
        camY += (p1.y - camY - height / 2) * 0.05;
    }

}