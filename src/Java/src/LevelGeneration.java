// 0 = Empty tile
// 1 = Wall tile
// 2 = Brick tile
// 3 = Gold coin (if used as tile)
// 4 = Crate
// 5 = Launch Barrel
// 6 = Lizard
// 7 = TallEnemy
// 8 = Thrower
// 9 = Cannon

public class LevelGeneration {
    public int[][] tiles;
    public int tileSize;
    public int width;
    public int height;
    
    // Store entity data: code, x, y
    public java.util.ArrayList<EntityData> entities = new java.util.ArrayList<>();
    
    public static class EntityData {
        public int code;
        public int x;
        public int y;
        
        public EntityData(int code, int x, int y) {
            this.code = code;
            this.x = x;
            this.y = y;
        }
    }

    public LevelGeneration(int[][] tiles, int tileSize) {
        this.tiles = tiles;
        this.tileSize = tileSize;
        this.width = tiles.length * tileSize;
        this.height = tiles[0].length * tileSize;
    }

    public int[][] getTiles() {
        return tiles;
    }
    
    public java.util.ArrayList<EntityData> getEntities() {
        return entities;
    }

    public void readFromFile(String filename) {
        // Read level data from a file and populate the tiles array
        java.io.BufferedReader br = null;
        try {
            java.io.File f = new java.io.File(filename);
            if (!f.exists()) {
                // common alternative when running from the project `src/Java` directory
                java.io.File alt = new java.io.File("src/" + filename);
                if (alt.exists()) {
                    f = alt;
                }
            }

            if (f.exists()) {
                br = new java.io.BufferedReader(new java.io.FileReader(f));
            } else {
                // try loading as a classpath resource (e.g., when packed or run from IDE)
                java.io.InputStream is = LevelGeneration.class.getResourceAsStream("/" + filename);
                if (is == null) {
                    // try without leading slash
                    is = LevelGeneration.class.getResourceAsStream(filename);
                }
                if (is != null) {
                    br = new java.io.BufferedReader(new java.io.InputStreamReader(is));
                } else {
                    throw new java.io.FileNotFoundException("Level file not found: " + filename + " (tried: " + filename + ", src/" + filename + ")");
                }
            }

            // now read from br
            try (java.io.BufferedReader _br = br) {
                br = _br; // ensure finally/closing handled by try-with-resources below
                java.util.List<String> lines = new java.util.ArrayList<>();
                String line;
                int maxWidth = 0;
                while ((line = br.readLine()) != null) {
                    // keep the raw line (don't split by whitespace because the file uses contiguous digits)
                    String trimmed = line.replaceAll("\\r", "");
                    lines.add(trimmed);
                    if (trimmed.length() > maxWidth) maxWidth = trimmed.length();
                }

                // Create tiles as tiles[width][height] where width == columns and height == rows
                int heightRows = lines.size();
                int widthCols = Math.max(maxWidth, 0);
                tiles = new int[widthCols][heightRows];
                
                // Clear entities list
                entities.clear();

                for (int y = 0; y < heightRows; y++) {
                    String ln = lines.get(y);
                    for (int x = 0; x < widthCols; x++) {
                        if (x < ln.length()) {
                            char c = ln.charAt(x);
                            int code = c - '0';
                            // Codes 0-3 are tiles (0=empty, 1=wall, 2=brick, 3=gold coin)
                            // Codes 4+ are entities
                            if (code < 4) {
                                tiles[x][y] = code;
                            } else {
                                // Store entity data (code, x in pixels, y in pixels)
                                entities.add(new EntityData(code, x * tileSize, y * tileSize));
                                tiles[x][y] = 0; // Leave tile as empty
                            }
                        } else {
                            tiles[x][y] = 0; // pad shorter lines with 0
                        }
                    }
                }

                width = tiles.length * tileSize;
                height = (tiles.length > 0 && tiles[0].length > 0) ? tiles[0].length * tileSize : heightRows * tileSize;
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
