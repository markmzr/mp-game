package realestateempire.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Model {

    protected final VertexSet vertexSet;
    protected final Shader shader;
    protected final Texture[] textures;
    protected Matrix4f position;
    protected int x;
    protected int y;

    public Model(String textureFilename, int x, int y) {
        this.x = x;
        this.y = y;
        textures = new Texture[1];
        textures[0] = new Texture(textureFilename);
        vertexSet = new VertexSet(x, y, getWidth(), getHeight());
        shader = new Shader();
        position = new Matrix4f().ortho2D(-1, 1, -1, 1);
    }

    public Model(String[] textureFilenames, int x, int y) {
        this.x = x;
        this.y = y;
        textures = new Texture[textureFilenames.length];
        for (int i = 0; i < textureFilenames.length; i++) {
            textures[i] = new Texture(textureFilenames[i]);
        }
        vertexSet = new VertexSet(x, y, getWidth(), getHeight());
        shader = new Shader();
        position = new Matrix4f().ortho2D(-1, 1, -1, 1);
    }

    public void setTexture(Texture texture) {
        this.textures[0] = texture;
    }

    public void setTexture(int textureVal, Texture texture) {
        this.textures[textureVal] = texture;
    }

    public void setPosition(Matrix4f position) {
        this.position = position;
    }

    public void setPosition(int x, int y) {
        int deltaX = x - this.x;
        int deltaY = this.y - y;
        movePosition(new Vector3f(pixelXToCoord(deltaX), pixelYToCoord(deltaY), 0f));
        this.x = x;
        this.y = y;
    }

    public Texture getTexture() {
        return textures[0];
    }

    public Matrix4f getPosition() {
        return position;
    }

    public int getWidth() {
        return textures[0].getWidth();
    }

    public int getHeight() {
        return textures[0].getHeight();
    }

    public void movePosition(Vector3f direction) {
        position.translate(direction);
    }

    public void render() {
        shader.use();
        shader.setTransform(position);
        textures[0].bind();
        vertexSet.render();
    }

    public void render(int textureVal) {
        shader.use();
        shader.setTransform(position);
        textures[textureVal].bind();
        vertexSet.render();
    }

    public float pixelXToCoord(double pixelCount) {
        return (float)pixelCount / 1280;
    }

    public float pixelYToCoord(double pixelCount) {
        return (float)pixelCount / 720;
    }
}
