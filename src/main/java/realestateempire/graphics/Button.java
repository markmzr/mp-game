package realestateempire.graphics;

public class Button extends Model {

    private final double leftX;
    private final double rightX;
    private final double topY;
    private final double bottomY;
    private boolean enabled;
    private boolean highlighted;

    public Button(String[] textureFilenames, int x, int y) {
        super(textureFilenames, x, y);
        leftX = x / 2560.0;
        rightX = (x + getWidth()) / 2560.0;
        topY = y / 1440.0;
        bottomY = (y + getHeight()) / 1440.0;
        highlighted = false;
        enabled = true;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public void render() {
        shader.use();
        shader.setTransform(position);
        if (highlighted) {
            textures[1].bind();
        } else if (enabled) {
            textures[0].bind();
        } else {
            textures[2].bind();
        }
        vertexSet.render();
    }

    public boolean isCursorInRange(double cursorX, double cursorY) {
        if (enabled && cursorX >= leftX && cursorX <= rightX
                && cursorY >= topY && cursorY <= bottomY) {
            highlighted = true;
            return true;
        }
        highlighted = false;
        return false;
    }
}
