package realestateempire.graphics.text;

import realestateempire.graphics.model.Model;

public class Text {

    final Model[] textChars;
    final Model[][] charSet;
    final Font font;
    final int x;
    final int y;
    final int[] charCounts;
    final double scale;
    String text;
    private boolean visible;

    public Text(String text, Font font, int x, int y) {
        this(text, font, x, y, 1);
    }

    public Text(String text, int x, int y, double scale) {
        this(text, Font.REGULAR, x, y, scale);
    }

    public Text(String text, Font font, int x, int y, double scale) {
        this.text = text;
        this.font = font;
        this.x = x;
        this.y = y;
        this.scale = scale;
        textChars = new Model[50];
        charSet = initCharSet();
        charCounts = new int[123];
        for (int i = 0; i < text.length(); i++) {
            charCounts[text.charAt(i)]++;
        }
        updateText(text);
        visible = true;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void render() {
        if (visible) {
            for (int i = 0; i < text.length(); i++) {
                textChars[i].render();
            }
        }
    }

    public void updateText(String newText) {
        for (int i = 0; i < text.length(); i++) {
            charCounts[text.charAt(i)]--;
        }
        text = newText;
        float textWidth = 0;
        for (int i = 0; i < text.length(); i++) {
            int character = text.charAt(i);
            textChars[i] = charSet[character][charCounts[character]];
            textChars[i].setPosition(x + textWidth, y + getCharacterHeight(character));
            textWidth += getCharacterWidth(character);
            charCounts[character]++;
        }
    }

    public void movePosition(float x, float y) {
        for (int i = 0; i < text.length(); i++) {
            textChars[i].movePosition(x, y);
        }
    }

    float getCharacterWidth(int character) {
        float charWidth = charSet[character][0].getWidth();
        switch (character) {
            case 65:
            case 86:
            case 89:
            case 102:
            case 114:
                return (float) (charWidth * scale);
            case 121:
                return (float) ((charWidth + 1) * scale);
            case 118:
                return (float) ((charWidth + 2) * scale);
            default:
                return (float) ((charWidth + 5) * scale);
        }
    }

    float getCharacterHeight(int character) {
        if (character == 36) {
            return (float) (-6 * scale);
        } else {
            return 0;
        }
    }

    private Model[][] initCharSet() {
        Model[][] charSet = new Model[123][5];
        String fontDir = font.getFontDir();
        for (String filename : font.getCharFilenames()) {
            String charFilename = filename.substring(0, filename.length() - 4);
            int charFilenameInt = Integer.parseInt(charFilename);
            for (int j = 0; j < charSet[charFilenameInt].length; j++) {
                charSet[charFilenameInt][j] = new Model(fontDir + filename, 0, 0, scale);
            }
        }
        return charSet;
    }
}
