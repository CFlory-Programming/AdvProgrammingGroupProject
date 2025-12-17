import processing.core.PImage;
import processing.core.PConstants;

public class GameUI {
    private KonQuestGame game;

    // Main menu / options state
    public boolean mainMenu = true;
    private boolean optionsMenu = false;
    public boolean levelSelector = false;
    public boolean pauseMenu = false;

    // Menu buttons
    private Button playButton;
    private Button optionsButton;
    private Button exitButton;
    private Button backButton;
    private Button resumeButton;
    private Button mainMenuButton;
    private Button[] levelButtons = new Button[4];

    // Main menu image
    private PImage mainMenuImg;
    private PImage playerImg;
    private PImage mapImg;

    // Scrolling state for main menu background
    // menuOffset is the x position of the left-most copy. We draw two copies
    // (offset and offset + width) so it wraps seamlessly.
    private float menuOffset = 0.0f;
    // Scrolling speed in pixels per frame. Increase for faster scroll.
    private float menuScrollSpeed = 2.0f;

    // Keybinding UI state
    private String[] keyBindings;
    private String[] defaultBindings;
    private int selectedBinding = -1;
    private boolean waitingForKey = false;
    private String bindingError = "";
    private int errorTimer = 0;

    public GameUI(KonQuestGame game) {
        this.game = game;
        // initialize bindings using Processing constants via the game reference
        this.keyBindings = new String[]{"a", "d", "w", String.valueOf(PConstants.SHIFT), " ", "e"};
        this.defaultBindings = new String[]{"a", "d", "w", String.valueOf(PConstants.SHIFT), " ", "e"};
    }

    // Human-friendly name for a binding string
    private String bindingName(String s) {
        if (s == null) return "";
        // If the binding is the literal space character, display it as SPACE
        if (s.charAt(0) == ' ')
            return "SPACE";
        boolean isNumber = true;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) {
                isNumber = false;
                break;
            }
        }
        if (isNumber) {
            int v = Integer.parseInt(s);
            if (v == 32) return "SPACE";
            if (v == PConstants.CONTROL) return "CONTROL";
            if (v == PConstants.SHIFT) return "SHIFT";
            if (v == PConstants.ALT) return "ALT";
            if (v == PConstants.LEFT) return "LEFT";
            if (v == PConstants.RIGHT) return "RIGHT";
            if (v == PConstants.UP) return "UP";
            if (v == PConstants.DOWN) return "DOWN";
            if (v == PConstants.TAB) return "TAB";
            if (v == PConstants.ENTER) return "ENTER";
            if (v == PConstants.BACKSPACE) return "BACKSPACE";
            if (v == PConstants.DELETE) return "DELETE";
            return "KEY_" + v;
        }
        return s.toUpperCase();
    }

    public void setupUI() {
        // Initialize main menu buttons (positions relative to current window size)
        int buttonX = (game.width / 2) - 500;
        int startY = (game.height / 2) - 50;
        int buttonSpacing = game.height / 7;

        int MAIN_MENU_BUTTON_WIDTH = 200;
        int MAIN_MENU_BUTTON_HEIGHT = 60;

        // Level selector buttons (round)
        int selectorY = game.height / 2;
        int selectorStartX = game.width / 2 - 550;
        int selectorSpacing = 400;
        int selectorRadius = 60;
        for (int i = 0; i < 4; i++) {
            int buttonX2 = selectorStartX + i * selectorSpacing;
            levelButtons[i] = new Button(buttonX2, selectorY, selectorRadius, selectorRadius, "" + (i+1), game.color(80,180,220), game.color(120,220,255));
        }

    // Create main menu buttons with color scheme
    // Play Button: bluish (normal) -> lighter blue (hover)
    int playNormal = game.color(11, 33, 98);
    int playHover = game.color(60, 100, 200);
    playButton = new Button(buttonX-200, startY-100,
                MAIN_MENU_BUTTON_WIDTH+100, MAIN_MENU_BUTTON_HEIGHT+20, "PLAY",
                playNormal, playHover);

    // Options Button: purple/magenta (normal) -> lighter magenta (hover)
    int optionsNormal = game.color(161, 44, 95);
    int optionsHover = game.color(200, 120, 170);
    optionsButton = new Button(buttonX-150, startY-90 + MAIN_MENU_BUTTON_HEIGHT + buttonSpacing,
                   MAIN_MENU_BUTTON_WIDTH, MAIN_MENU_BUTTON_HEIGHT, "OPTIONS",
                   optionsNormal, optionsHover);

    // Exit Button: softer red/burgundy (normal) -> lighter red (hover)
    int exitNormal = game.color(140, 60, 60);
    int exitHover = game.color(190, 110, 110);
    exitButton = new Button(buttonX-150, startY-90 + (MAIN_MENU_BUTTON_HEIGHT + buttonSpacing) * 2,
                MAIN_MENU_BUTTON_WIDTH, MAIN_MENU_BUTTON_HEIGHT, "EXIT",
                exitNormal, exitHover);

    mainMenuImg = game.loadImage("Jungle.png");
    if (mainMenuImg != null)
        mainMenuImg.resize(game.width, game.height);
    playerImg = game.loadImage("MainMenu.png");
    if (playerImg != null)
        playerImg.resize(200, 200);
    mapImg = game.loadImage("map.png");
    if (mapImg != null)
        mapImg.resize(game.width, game.height);

    // Resume button for pause screen
    int resumeNormal = game.color(11, 33, 98);
    int resumeHover = game.color(60, 100, 200);
    resumeButton = new Button(buttonX-300, startY-100,
                MAIN_MENU_BUTTON_WIDTH+100, MAIN_MENU_BUTTON_HEIGHT+20, "RESUME",
                resumeNormal, resumeHover);

    // Main menu button for pause screen
    int mainMenuNormal = game.color(140, 60, 60);
    int mainMenuHover = game.color(190, 110, 110);
    mainMenuButton = new Button(buttonX-300, startY+100 + MAIN_MENU_BUTTON_HEIGHT + 40,
                MAIN_MENU_BUTTON_WIDTH+100, MAIN_MENU_BUTTON_HEIGHT+20, "MAIN MENU",
                mainMenuNormal, mainMenuHover);
    }

    public void drawMainMenu() {
        if (levelSelector) {
            drawLevelSelector();
            return;
        }
        
        game.background(0, 0, 0);
        if (mainMenuImg != null) {
            // First copy at offset, second copy immediately after it
            game.image(mainMenuImg, menuOffset, 0, game.width, game.height);
            game.image(mainMenuImg, menuOffset + game.width, 0, game.width, game.height);

            // Advance offset to scroll left. When a full image has moved off the left edge, wrap by adding width.
            menuOffset -= menuScrollSpeed;
            if (menuOffset <= -game.width) {
                menuOffset += game.width;
            }
        }

        if (playerImg != null) {
            game.image(playerImg, game.width - 400, game.height - 300);
        }

        // Draw the game title on the main menu at the top-left with a small shadow
        int titleX = 50;
        int titleY = 40;
        game.textAlign(PConstants.LEFT, PConstants.TOP);
        game.textSize(100);
        // Shadow
        game.fill(255, 150);
        game.text("KonQuest", titleX + 2, titleY + 2);
        // Main title
        game.fill(255, 199, 37);
        game.text("KonQuest", titleX, titleY);


        // If options menu not open, show main menu buttons
        if (!optionsMenu) {
            playButton.update(game.mouseX, game.mouseY, game.mousePressed);
            optionsButton.update(game.mouseX, game.mouseY, game.mousePressed);
            exitButton.update(game.mouseX, game.mouseY, game.mousePressed);

            playButton.display();
            optionsButton.display();
            exitButton.display();

            if (playButton.consumeClick()) {
                levelSelector = true;
            }
            if (optionsButton.consumeClick()) {
                optionsMenu = true;
            }
            if (exitButton.consumeClick()) {
                game.exit();
            }
        } else {
            // Options overlay
            game.fill(20, 20, 30, 200);
            game.rect(0, 0, game.width, game.height);

            game.fill(255);
            game.textAlign(PConstants.CENTER, PConstants.TOP);
            game.textSize(48);
            game.text("OPTIONS", game.width / 2, game.height / 8);

            int centerX = game.width / 2;
            int startY = game.height / 4;
            int sectionStartX = centerX - 200;
            int bindingY = startY + 200;
            int bindingSpacing = 45;
            String[] bindingLabels = {"Move Left", "Move Right", "Jump", "Sprint", "Interact", "Attack"};

            // Reset button
            int resetBtnWidth = 160;
            int resetBtnHeight = 35;
            int resetBtnX = centerX - resetBtnWidth / 2;
            int resetBtnY = bindingY - resetBtnHeight - 80;
            if (game.mousePressed && game.mouseX >= resetBtnX && game.mouseX <= resetBtnX + resetBtnWidth &&
                game.mouseY >= resetBtnY && game.mouseY <= resetBtnY + resetBtnHeight) {
                game.fill(150, 0, 0);
                for (int i = 0; i < keyBindings.length; i++) keyBindings[i] = defaultBindings[i];
            } else {
                game.fill(200, 0, 0);
            }
            game.rect(resetBtnX, resetBtnY, resetBtnWidth, resetBtnHeight);
            game.fill(255);
            game.textAlign(PConstants.CENTER, PConstants.CENTER);
            game.textSize(20);
            game.text("Reset to Default", centerX, resetBtnY + resetBtnHeight/2);

            for (int i = 0; i < keyBindings.length; i++) {
                game.textAlign(PConstants.RIGHT, PConstants.CENTER);
                game.textSize(20);
                game.text(bindingLabels[i] + ": ", sectionStartX + 150, bindingY + i * bindingSpacing);

                int keyBtnX = sectionStartX + 170;
                int keyBtnY = bindingY + i * bindingSpacing - 15;
                if (selectedBinding == i) game.fill(200, 200, 0); else game.fill(60, 60, 60);
                game.rect(keyBtnX, keyBtnY, 80, 30);

                game.fill(255);
                game.textAlign(PConstants.CENTER, PConstants.CENTER);
                if (selectedBinding == i && waitingForKey) {
                    game.text("Press Key", keyBtnX + 40, keyBtnY + 15);
                } else {
                    game.text(bindingName(keyBindings[i]), keyBtnX + 40, keyBtnY + 15);
                }

                if (game.mousePressed && game.mouseX >= keyBtnX && game.mouseX <= keyBtnX + 80 &&
                    game.mouseY >= keyBtnY && game.mouseY <= keyBtnY + 30) {
                    selectedBinding = i;
                    waitingForKey = true;
                }
            }

            if (!bindingError.isEmpty() && errorTimer > 0) {
                game.fill(200, 0, 0);
                game.textAlign(PConstants.CENTER, PConstants.CENTER);
                game.text(bindingError, centerX, bindingY + 6 * bindingSpacing);
                errorTimer--;
                if (errorTimer == 0) bindingError = "";
            }

            if (waitingForKey) {
                game.fill(0, 0, 0, 200);
                game.rect(game.width/2 - 200, game.height/2 - 50, 400, 100);
                game.fill(255);
                game.textAlign(PConstants.CENTER, PConstants.CENTER);
                game.textSize(24);
                game.text("Press any key to rebind\nESC to cancel", game.width/2, game.height/2);
            }

            if (backButton == null) {
                backButton = new Button(80, game.height - 100, 200, 60, "BACK", game.color(100,100,100), game.color(150,150,150));
            }
            backButton.update(game.mouseX, game.mouseY, game.mousePressed);
            backButton.display();
            if (backButton.consumeClick()) {
                optionsMenu = false;
            }
        }

        game.text(game.mouseX + ", " + game.mouseY, 50, 10);
    }

    // Draw level selector UI
    public void drawLevelSelector() {
        if (mapImg != null) {
            game.image(mapImg, 0, 0);
        }
        // Draw 4 round buttons
        int lineY = game.height / 2;
        for (int i = 0; i < 4; i++) {
            levelButtons[i].update(game.mouseX, game.mouseY, game.mousePressed);
            levelButtons[i].display();
            if (levelButtons[i].consumeClick()) {
                // Load level i+1
                mainMenu = false;
                levelSelector = false;
                KonQuestGame.loadLevel(i+1);
            }
        }
        // Draw text
        game.fill(60);
        game.textAlign(PConstants.CENTER, PConstants.TOP);
        game.textSize(40);
        game.text("Select Level", game.width/2, lineY- 500);
    }

    public void drawPauseMenu() {
        // Draw a semi-transparent overlay
        game.fill(0, 0, 0, 150);

        // Draw the pause title at the top-left with a shadow
        int titleX = 50;
        int titleY = 40;
        game.textAlign(PConstants.LEFT, PConstants.TOP);
        game.textSize(100);
        // Shadow
        game.fill(255, 150);
        game.text("PAUSED", titleX + 2, titleY + 2);
        // Main title
        game.fill(255, 199, 37);
        game.text("PAUSED", titleX, titleY);

        // Resume button
        resumeButton.update(game.mouseX, game.mouseY, game.mousePressed);
        resumeButton.display();

        if (resumeButton.consumeClick()) {
            pauseMenu = false;
        }

        // Return to main menu button
        mainMenuButton.update(game.mouseX, game.mouseY, game.mousePressed);
        mainMenuButton.display();

        if (mainMenuButton.consumeClick()) {
            pauseMenu = false;
            mainMenu = true;
        }
    }

    // Return true if we handled the key event and it should not be processed further
    public boolean handleKeyPressed() {
        // If rebinding in progress
        if (waitingForKey && optionsMenu) {
            if (game.key == PConstants.ESC) {
                waitingForKey = false;
                selectedBinding = -1;
            } else {
                String newKeyStr;
                if (game.key == PConstants.CODED)
                    newKeyStr = String.valueOf(game.keyCode);
                else
                    newKeyStr = String.valueOf(Character.toLowerCase(game.key));

                boolean isDuplicate = false;
                for (int i = 0; i < keyBindings.length; i++) {
                    if (i != selectedBinding && keyBindings[i].equalsIgnoreCase(newKeyStr)) {
                        isDuplicate = true;
                        bindingError = "Key '" + bindingName(newKeyStr) + "' is already in use!";
                        errorTimer = 120;
                        break;
                    }
                }
                if (!isDuplicate) {
                    keyBindings[selectedBinding] = newKeyStr.toLowerCase();
                    waitingForKey = false;
                    selectedBinding = -1;
                }
            }
            game.key = 0;
            return true;
        }

        // Toggle pause menu with ESC (only when not in main menu)
        if (!mainMenu && game.key == PConstants.ESC) {
            game.key = 0;
            pauseMenu = !pauseMenu;
            return true;
        }

        // Custom keybindings -> set game.keys[]
        String pressedName;
    if (game.key == PConstants.CODED)
        pressedName = String.valueOf(game.keyCode);
        else
            pressedName = String.valueOf(Character.toLowerCase(game.key));
        for (int i = 0; i < keyBindings.length; i++) {
            if (pressedName.equalsIgnoreCase(keyBindings[i])) {
                KonQuestGame.keys[i] = true;
            }
        }
        return false;
    }

    public void handleKeyReleased() {
    String releasedName;
    if (game.key == PConstants.CODED)
        releasedName = String.valueOf(game.keyCode);
    else
        releasedName = String.valueOf(Character.toLowerCase(game.key));
        for (int i = 0; i < keyBindings.length; i++) {
            if (releasedName.equalsIgnoreCase(keyBindings[i])) {
                KonQuestGame.keys[i] = false;
            }
        }
    }
}
