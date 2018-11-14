import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GL2DObject {

    protected Model model;
    protected Shader shader;
    protected Texture[] texture;
    protected Matrix4f position;

    public GL2DObject() { }

    public GL2DObject(String textureFilename, int leftX, int topY, int width, int height) {
        model = new Model(createVertices(leftX, topY, width, height));
        shader = new Shader();
        texture = new Texture[1];
        texture[0] = new Texture(textureFilename);
        position = new Matrix4f().ortho2D(-1, 1, -1, 1);
    }

    public GL2DObject(String[] textureFilenames, int leftX, int topY, int width, int height) {
        model = new Model(createVertices(leftX, topY, width, height));
        shader = new Shader();
        texture = new Texture[textureFilenames.length];

        for (int i = 0; i < textureFilenames.length; i++) {
            texture[i] = new Texture(textureFilenames[i]);
        }
        position = new Matrix4f().ortho2D(-1, 1, -1, 1);
    }

    public void setPosition(Matrix4f position) {
        this.position = position;
    }

    public Matrix4f getPosition() {
        return position;
    }

    public void movePosition(Vector3f direction) {
        position.translate(direction);
    }

    private float[] createVertices(int leftX, int topY, int width, int height) {
        int halfResWidth = 1280;
        int halfResHeight = 720;

        float leftXf = (float)(leftX - halfResWidth) / halfResWidth;
        float rightXf = (float)(leftX + width - halfResWidth) / halfResWidth;
        float topYf = (float)(halfResHeight - topY) / halfResHeight;
        float bottomYf = (float)(halfResHeight - topY - height) / halfResHeight;

        float[] vertices = new float[] {
                leftXf, topYf, 0f,
                rightXf, topYf, 0f,
                rightXf, bottomYf, 0f,

                rightXf, bottomYf, 0f,
                leftXf, bottomYf, 0f,
                leftXf, topYf, 0f,
        };
        return vertices;
    }

    public void render() {
        shader.use();
        shader.setTransform(position);
        texture[0].bind();
        model.render();
    }

    public void render(int textureVal) {
        shader.use();
        shader.setTransform(position);
        texture[textureVal].bind();
        model.render();
    }
}
