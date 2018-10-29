public class GLButton extends GL2DObject {

    private Texture highlightTexture;
    private double[] topLeftXY;
    private double[] topRightXY;
    private double[] bottomLeftXY;
    private double[] bottomRightXY;
    private boolean highlighted;

    public GLButton(float[] vertices, String textureFilename, String highlightTexture,
                    double x1Coord, double x2Coord, double y1Coord, double y2Coord) {
        super(vertices, textureFilename);

        topLeftXY = new double[2];
        topLeftXY[0] = x1Coord;
        topLeftXY[1] = y1Coord;

        topRightXY = new double[2];
        topRightXY[0] = x2Coord;
        topRightXY[1] = y1Coord;

        bottomLeftXY = new double[2];
        bottomLeftXY[0] = x1Coord;
        bottomLeftXY[1] = y2Coord;

        bottomRightXY = new double[2];
        bottomRightXY[0] = x2Coord;
        bottomRightXY[1] = y2Coord;

        highlighted = false;
        this.highlightTexture = new Texture(highlightTexture);
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }

    public boolean getHighlighted() {
        return highlighted;
    }

    public boolean isCursorInRange(double cursorXPos, double cursorYPos) {
        if ((cursorXPos >= topLeftXY[0] && cursorYPos >= topLeftXY[1])
                && (cursorXPos <= topRightXY[0] && cursorYPos >= topRightXY[1])
                && (cursorXPos >= bottomLeftXY[0] && cursorYPos <= bottomLeftXY[1])
                && (cursorXPos <= bottomRightXY[0] && cursorYPos <= bottomRightXY[1])) {
            return true;
        }

        return false;
    }

    public void render() {
        shader.use();
        shader.setTransform(position);

        if (highlighted) {
            highlightTexture.bind();
        }
        else {
            texture.bind();
        }

        model.render();
    }
}
