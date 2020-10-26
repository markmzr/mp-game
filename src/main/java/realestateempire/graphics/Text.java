package realestateempire.graphics;

public class Text {

    double scale;
    Model[] characters;
    String text;
    int x;
    int y;
    boolean visible;

    public Text() { }

    public Text(String text, int x, int y, double scale) {
        this.text = text;
        this.x = x;
        this.y = y;
        this.scale = scale;
        updateText(text);
        visible = true;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void updateText(String text) {
        this.text = text;
        characters = new Model[text.length()];
        characters[0] = new Model("Text/" + (int)text.charAt(0) + ".png", x, y, scale);
        int width = (int)((characters[0].getWidth() + 5) * scale);

        for (int i = 1; i < text.length(); i++) {
            int xNew = x + width;
            characters[i] = new Model("Text/" + (int)text.charAt(i) + ".png", xNew, y, scale);
            width += (int)((characters[i].getWidth() + 5) * scale);
        }
    }

    public void render() {
        if (visible) {
            for (Model c : characters) {
                c.render();
            }
        }
    }
}
