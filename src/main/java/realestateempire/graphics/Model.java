package realestateempire.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Model {

    private final VertexSet vertexSet;
    private final Shader shader;
    private final Texture[] textures;
    private Matrix4f position;
    private int texture;
    private int x;
    private int y;
    private boolean visible;

    public Model(String textureFiles, int x, int y) {
        this.x = x;
        this.y = y;
        textures = new Texture[1];
        textures[0] = new Texture(textureFiles);
        vertexSet = new VertexSet(x, y, getWidth(), getHeight());
        shader = new Shader();
        position = new Matrix4f().ortho2D(-1, 1, -1, 1);
        texture = 0;
        visible = true;
    }

    public Model(String textureFiles, int x, int y, double scale) {
        this.x = x;
        this.y = y;
        textures = new Texture[1];
        textures[0] = new Texture(textureFiles);
        int scaledWidth = (int)(getWidth() * scale);
        int scaledHeight = (int)(getHeight() * scale);
        vertexSet = new VertexSet(x, y, scaledWidth, scaledHeight);
        shader = new Shader();
        position = new Matrix4f().ortho2D(-1, 1, -1, 1);
        texture = 0;
        visible = true;
    }

    public Model(String[] textureFiles, int x, int y) {
        this.x = x;
        this.y = y;
        textures = new Texture[textureFiles.length];
        for (int i = 0; i < textureFiles.length; i++) {
            textures[i] = new Texture(textureFiles[i]);
        }
        vertexSet = new VertexSet(x, y, getWidth(), getHeight());
        shader = new Shader();
        position = new Matrix4f().ortho2D(-1, 1, -1, 1);
        texture = 0;
        visible = true;
    }

    public Model(String textureFiles, int x, int y, boolean visible) {
        this(textureFiles, x, y);
        this.visible = visible;
    }

    public void setTexture(int texture) {
        this.texture = texture;
    }

    public void setTextures(int textureVal, Texture texture) {
        this.textures[textureVal] = texture;
    }

    public void setPosition(Matrix4f position) {
        this.position = position;
    }

    public void setPosition(int x, int y) {
        int xDelta = x - this.x;
        int yDelta = this.y - y;
        Vector3f direction = new Vector3f(xToCoord(xDelta), yToCoord(yDelta), 0f);
        position.translate(direction);
        this.x = x;
        this.y = y;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Texture getTexture() {
        return textures[0];
    }

    public Matrix4f getPosition() {
        return position;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    int getWidth() {
        return textures[0].getWidth();
    }

    int getHeight() {
        return textures[0].getHeight();
    }

    public void movePosition(Vector3f direction) {
        position.translate(direction);
    }

    public boolean isVisible() {
        return visible;
    }

    public void render() {
        if (visible) {
            shader.use(position);
            textures[texture].bind();
            vertexSet.render();
        }
    }

    public static float xToCoord(double pixelCount) {
        return (float)pixelCount / 1280;
    }

    public static float yToCoord(double pixelCount) {
        return (float)pixelCount / 720;
    }
}
