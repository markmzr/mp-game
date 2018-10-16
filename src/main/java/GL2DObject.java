import org.joml.Matrix4f;
import org.joml.Vector3f;

public class GL2DObject {

    private Model model;
    private Shader shader;
    private Texture texture;
    private Matrix4f position;

    public GL2DObject(float[] vertices, String textureFilename) {
        model = new Model(vertices);
        shader = new Shader();
        texture = new Texture(textureFilename);
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
        texture.bind();
        model.render();
    }
}
