import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GL2DObject {

    protected Model model;
    protected Shader shader;
    protected Texture[] texture;
    protected Matrix4f position;

    public GL2DObject(float[] vertices, String textureFilename) {
        model = new Model(vertices);
        shader = new Shader();
        texture = new Texture[1];
        texture[0] = new Texture(textureFilename);
        position = new Matrix4f().ortho2D(-1, 1, -1, 1);
    }

    public GL2DObject(float[] vertices, String[] textureFilenames) {
        model = new Model(vertices);
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

    public void render() {
        shader.use();
        shader.setTransform(position);
        texture[0].bind();
        model.render();
    }

    public void render(int textureValue) {
        shader.use();
        shader.setTransform(position);
        texture[textureValue].bind();
        model.render();
    }
}
