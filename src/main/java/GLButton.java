public class GLButton extends GL2DObject {

    private double leftXCoord;
    private double rightXCoord;
    private double topYCoord;
    private double bottomYCoord;
    private boolean highlighted;
    private boolean enabled;

    public GLButton(String[] textureFilenames, int leftX, int topY, int width, int height) {
        super(textureFilenames, leftX, topY, width, height);
        createCoords(leftX, topY, width, height);
        highlighted = false;
        enabled = true;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    private void createCoords(int leftX, int topY, int width, int height) {
        int resolutionWidth = 2560;
        int resolutionHeight = 1440;

        leftXCoord = (float)leftX / resolutionWidth;
        rightXCoord = (float)(leftX + width) / resolutionWidth;
        topYCoord = (float)topY / resolutionHeight;
        bottomYCoord = (float)(topY + height) / resolutionHeight;
    }

    public void render() {
        shader.use();
        shader.setTransform(position);
        if (highlighted) {
            texture[1].bind();
        } else if (enabled) {
            texture[0].bind();
        } else {
            texture[2].bind();
        }
        model.render();
    }

    public boolean isCursorInRange(double cursorXPos, double cursorYPos) {
        if (cursorXPos >= leftXCoord && cursorXPos <= rightXCoord
                && cursorYPos >= topYCoord && cursorYPos <= bottomYCoord && enabled) {
            highlighted = true;
            return true;
        }
        highlighted = false;
        return false;
    }
}
