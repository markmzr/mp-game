package realestateempire.graphics;

import org.joml.Vector3f;

public class Text {

    private final Model[] characters;
    private String text;

    public Text(String text, int x, int y) {
        this.text = text;
        characters = new Model[6];

        String[] textures = new String[13];
        for (int i = 0; i < 10; i++) {
            textures[i] = "Text/" + i + ".png";
        }
        textures[10] = "Text/$.png";
        textures[11] = "Text/+.png";
        textures[12] = "Text/-.png";

        for (int i = 0; i < characters.length; i++) {
            characters[i] = new Model(textures, x + (i * 41), y);
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public void movePosition(Vector3f direction) {
        for (Model character : characters) {
            character.movePosition(direction);
        }
    }

    public void render() {
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '$') {
                characters[i].render(10);
            } else if (text.charAt(i) == '+') {
                characters[i].render(11);
            } else if (text.charAt(i) == '-') {
                characters[i].render(12);
            } else {
                characters[i].render(text.charAt(i) - 48);
            }
        }
    }
}
