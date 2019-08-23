package realestateempire.graphics;

public class Text {

    private final Model[] characters;
    private String text;
    private boolean visible;

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
        updateText(text);
        visible = true;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void updateText(String text) {
        this.text = text;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '$') {
                characters[i].setTexture(10);
            } else if (text.charAt(i) == '+') {
                characters[i].setTexture(11);
            } else if (text.charAt(i) == '-') {
                characters[i].setTexture(12);
            } else {
                characters[i].setTexture(text.charAt(i) - 48);
            }
        }
    }

    public void render() {
        if (visible) {
            for (int i = 0; i < text.length(); i++) {
                characters[i].render();
            }
        }
    }
}
