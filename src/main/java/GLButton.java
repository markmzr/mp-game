public class GLButton extends GL2DObject {

    private double leftXCoord;
    private double rightXCoord;
    private double topYCoord;
    private double bottomYCoord;
    private boolean highlighted;
    private boolean enabled;

    public GLButton(float[] vertices, String[] textureFilenames,
                    double leftXCoord, double rightXCoord,
                    double topYCoord, double bottomYCoord) {
        super(vertices, textureFilenames);

        this.leftXCoord = leftXCoord;
        this.rightXCoord = rightXCoord;
        this.topYCoord = topYCoord;
        this.bottomYCoord = bottomYCoord;

        highlighted = false;
        enabled = true;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean getHighlighted() {
        return highlighted;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public boolean isCursorInRange(double cursorXPos, double cursorYPos) {
        if (cursorXPos >= leftXCoord && cursorXPos <= rightXCoord
                && cursorYPos >= topYCoord && cursorYPos <= bottomYCoord) {
            return true;
        }

        return false;
    }

    public void render() {
        shader.use();
        shader.setTransform(position);

        if (highlighted) {
            texture[1].bind();
        }
        else if (enabled) {
            texture[0].bind();
        }
        else {
            texture[2].bind();
        }

        model.render();
    }
}
