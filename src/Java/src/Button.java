import processing.core.PApplet;

public class Button
{
    private int x, y, width, height;
    private String label;
    private int normalColor, hoverColor;
    private boolean isHovered;
    private boolean isClicked;
    private boolean lastMousePressed = false; // for edge detection
    
    public Button(int x, int y, int width, int height, String label, int normalColor, int hoverColor)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.normalColor = normalColor;
        this.hoverColor = hoverColor;
        this.isHovered = false;
        this.isClicked = false;
    }
    
    public void update(int mouseX, int mouseY, boolean mousePressed)
    {
        // Check if mouse is hovering over button
        isHovered = mouseX >= x && mouseX <= x + width && 
                   mouseY >= y && mouseY <= y + height;
        
        // Update click state on rising edge (single click)
        if (isHovered && mousePressed && !lastMousePressed) {
            isClicked = true;
        } else {
            isClicked = false;
        }

        lastMousePressed = mousePressed;
    }
    
    public void display()
    {
        PApplet sketch = KonQuestGame.sketch;
        
        // Draw button background
        sketch.stroke(255);
        sketch.strokeWeight(3);
        sketch.fill(isHovered ? hoverColor : normalColor);
        sketch.rect(x, y, width, height);
        
        // Draw button text
        sketch.fill(255);
        sketch.textAlign(PApplet.CENTER, PApplet.CENTER);
        sketch.textSize(24);
        sketch.text(label, x + width/2, y + height/2);
    }
    
    public boolean isClicked()
    {
        return isClicked;
    }

    /**
     * Consume the click state and return true only once per click (edge).
     */
    public boolean consumeClick() {
        boolean c = isClicked;
        isClicked = false;
        return c;
    }

    public void setLabel(String newLabel) {
        this.label = newLabel;
    }
}
