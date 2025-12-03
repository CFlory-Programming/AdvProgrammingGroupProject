public class HUD {
    public static boolean enabled = true;

    public static void display(Player player) {
        if (enabled) {
            // Draw the player health and stamina bars
            KonQuestGame.sketch.fill(0);
            KonQuestGame.sketch.rect(10, 10, 200, 20);
            KonQuestGame.sketch.fill(255, 0, 0);
            int healthWidth = (int) ((player.health / (double) player.maxHealth) * 200);
            KonQuestGame.sketch.rect(10, 10, healthWidth, 20);

            KonQuestGame.sketch.fill(0);
            KonQuestGame.sketch.rect(10, 40, 200, 20);
            KonQuestGame.sketch.fill(0, 0, 255);
            int staminaWidth = Math.max(0, (int) ((Math.min(player.stamina, player.maxStamina) / (double) player.maxStamina) * 200));
            KonQuestGame.sketch.rect(10, 40, staminaWidth, 20);

            // Draw the player score and lives
            KonQuestGame.sketch.fill(0);
            KonQuestGame.sketch.textSize(16);
            KonQuestGame.sketch.text("Score: " + player.score, 200, 70);
            KonQuestGame.sketch.text("Lives: " + player.lives, 200, 90);
        }
    }
}
