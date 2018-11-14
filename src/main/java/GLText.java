import org.joml.Vector3f;

public class GLText extends GL2DObject {

    private GL2DObject[] characters;
    private String text;

    public GLText(String text, int leftX, int topY, int width, int height) {
        characters = new GL2DObject[6];

        String[] textures = new String[13];
        for (int i = 0; i < 10; i++) {
            textures[i] = "Text/" + i + ".png";
        }
        textures[10] = "Text/$.png";
        textures[11] = "Text/+.png";
        textures[12] = "Text/-.png";

        for (int i = 0; i < 6; i++) {
            characters[i] = new GL2DObject(textures, leftX, topY, width, height);
            Vector3f direction = new Vector3f(0.035f * i, 0f, 0f);
            characters[i].movePosition(direction);
        }
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void movePosition(Vector3f direction) {
        for (GL2DObject character : characters) {
            character.movePosition(direction);
        }
    }

    public void render() {
        for (int i = 0; i < text.length(); i++) {
            int textureVal;
            if (text.charAt(i) == '$') {
                textureVal = 10;
            } else if (text.charAt(i) == '+') {
                textureVal = 11;
            } else if (text.charAt(i) == '-') {
                textureVal = 12;
            } else {
                textureVal = text.charAt(i) - 48;
            }
            characters[i].render(textureVal);
        }
    }
}
