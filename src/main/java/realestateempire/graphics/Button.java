package realestateempire.graphics;

import static realestateempire.graphics.Button.ButtonState.*;

public class Button extends Model {

    public enum ButtonState {

        ENABLED (0), MOUSEOVER (1), DISABLED (2);

        private final int texture;

        ButtonState(int texture) {
            this.texture = texture;
        }
    }

    private final double xLeft;
    private final double xRight;
    private final double yTop;
    private final double yBottom;
    private ButtonState buttonState;

    public Button(String[] textureFiles, int x, int y) {
        super(textureFiles, x, y);
        xLeft = x / 2560.0;
        xRight = (x + getWidth()) / 2560.0;
        yTop = y / 1440.0;
        yBottom = (y + getHeight()) / 1440.0;
        buttonState = ENABLED;
    }

    public void setButtonState(ButtonState buttonState) {
        this.buttonState = buttonState;
        setTexture(buttonState.texture);
    }

    public boolean isMouseover(double xCursor, double yCursor) {
        if (buttonState == DISABLED) {
            return false;
        }
        if (xCursor >= xLeft && xCursor <= xRight
                && yCursor >= yTop && yCursor <= yBottom) {
            if (buttonState == ENABLED) {
                setButtonState(MOUSEOVER);
            }
            return true;
        } else if (buttonState == MOUSEOVER) {
            setButtonState(ENABLED);
        }
        return false;
    }
}
