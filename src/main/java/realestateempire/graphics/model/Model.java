package realestateempire.graphics.model;

import org.joml.Matrix4f;

import realestateempire.graphics.Graphic;

public class Model implements Graphic {

    private static final Shader shader = new Shader();
    private final VertexSet vertexSet;
    private final Texture[] textures;
    private final Matrix4f position;
    private float x;
    private float y;
    private int texture;
    private boolean visible;

    public Model(String textureFiles, int x, int y) {
        this(new String[]{ textureFiles }, x, y, 1);
    }

    public Model(String[] textureFiles, int x, int y) {
        this(textureFiles, x, y, 1);
    }

    public Model(String textureFile, int x, int y, double scale) {
        this(new String[]{ textureFile }, x, y, scale);
    }

    public Model(String[] textureFiles, int x, int y, double scale) {
        this.x = x;
        this.y = y;
        textures = new Texture[textureFiles.length];
        for (int i = 0; i < textureFiles.length; i++) {
            textures[i] = new Texture(textureFiles[i]);
        }
        int scaledWidth = (int) (getWidth() * scale);
        int scaledHeight = (int) (getHeight() * scale);
        vertexSet = new VertexSet(x, y, scaledWidth, scaledHeight);
        position = new Matrix4f().ortho2D(-1, 1, -1, 1);
        texture = 0;
        visible = true;
    }

    public void setTexture(int texture) {
        this.texture = texture;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getWidth() {
        return textures[0].getWidth();
    }

    public int getHeight() {
        return textures[0].getHeight();
    }

    @Override
    public void render() {
        if (visible) {
            shader.use(position);
            textures[texture].bind();
            vertexSet.render();
        }
    }

    public void setPosition(float x, float y) {
        float xCoord = (x - this.x) / 1280f;
        float yCoord = (this.y - y) / 720f;
        position.translate(xCoord, yCoord, 0);
        this.x = x;
        this.y = y;
    }

    public void movePosition(float x, float y) {
        float xCoord = x / 1280f;
        float yCoord = y / 720;
        position.translate(xCoord, yCoord, 0);
        this.x += x;
        this.y += y;
    }
}
