package realestateempire.graphics.text;

public class CenteredText extends Text {

    public CenteredText(String text, int x, int y) {
        this(text, x, y, 1);
    }

    public CenteredText(String text, int x, int y, double scale) {
        super(text, x, y, scale);
        updateText(text);
    }

    public CenteredText(String text, Font font, int x, int y, double scale) {
        super(text, font, x, y, scale);
        updateText(text);
    }

    @Override
    public void updateText(String newText) {
        for (int i = 0; i < text.length(); i++) {
            charCounts[text.charAt(i)]--;
        }
        text = newText;
        int textWidth = 0;
        for (int i = 0; i < text.length(); i++) {
            int character = text.charAt(i);
            textChars[i] = charSet[character][charCounts[character]];
            textChars[i].setPosition(x + textWidth, y + getCharacterHeight(character));
            textWidth += getCharacterWidth(character);
            charCounts[character]++;
        }
        int x = -textWidth / 2;
        for (int i = 0; i < text.length(); i++) {
            textChars[i].movePosition(x, 0);
        }
    }
}
