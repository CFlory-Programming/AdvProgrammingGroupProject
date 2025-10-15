public class Level {
    int[][] tiles;
    int tileSize;
    int width;
    int height;

    public Level(int[][] tiles, int tileSize) {
        this.tiles = tiles;
        this.tileSize = tileSize;
        this.width = tiles.length * tileSize;
        this.height = tiles[0].length * tileSize;
    }

    public int[][] getTiles() {
        return tiles;
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
                java.io.InputStream is = Level.class.getResourceAsStream("/" + filename);
                if (is == null) {
                    // try without leading slash
                    is = Level.class.getResourceAsStream(filename);
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

            for (int y = 0; y < heightRows; y++) {
                String ln = lines.get(y);
                for (int x = 0; x < widthCols; x++) {
                    if (x < ln.length()) {
                        char c = ln.charAt(x);
                        if (Character.isDigit(c)) {
                            tiles[x][y] = c - '0';
                        } else {
                            tiles[x][y] = 0;
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
