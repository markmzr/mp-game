package realestateempire.graphics;

public class CenteredText extends Text {

    private final int xLeft;
    private final int maxWidth;

    public CenteredText(String text, int xLeft, int y, int maxWidth, double scale) {
        this.text = text;
        //this.x = x;
        this.xLeft = xLeft;
        this.y = y;
        this.maxWidth = maxWidth;
        this.scale = scale;
        updateText(text);
        visible = true;
    }

    @Override
    public void updateText(String text) {
        this.text = text;
        characters = new Model[text.length()];
        characters[0] = new Model("Text/" + (int)text.charAt(0) + ".png", xLeft, y, scale);
        int width = (int)((characters[0].getWidth() + 5) * scale);

        for (int i = 1; i < text.length(); i++) {
            int xNew = xLeft + width;
            characters[i] = new Model("Text/" + (int)text.charAt(i) + ".png", xNew, y, scale);
            width += (int)((characters[i].getWidth() + 5) * scale);
        }

        int xMove = (maxWidth - width) / 2;
        for (Model character : characters) {
            int xNew = character.getX() + xMove;
            character.setPosition(xNew, y);
        }
    }
}
