package realestateempire.graphics.button;

import java.util.ArrayList;

import realestateempire.graphics.Interactive;
import realestateempire.graphics.model.Model;

import static realestateempire.graphics.button.Button.ButtonState.*;

public class Button implements Interactive {

    enum ButtonState {

        ENABLED (0), MOUSEOVER (1), DISABLED (2);

        private final int texture;

        ButtonState(int texture) {
            this.texture = texture;
        }
    }

    private final Model model;
    private final double xLeft;
    private final double xRight;
    private final double yTop;
    private final double yBottom;
    private final ArrayList<ButtonAction> buttonActions;
    private ButtonState buttonState;

    public Button(String[] textureFiles, int x, int y) {
        model = new Model(textureFiles, x, y);
        xLeft = x / 2560.0;
        xRight = (x + model.getWidth()) / 2560.0;
        yTop = y / 1440.0;
        yBottom = (y + model.getHeight()) / 1440.0;
        buttonActions = new ArrayList<>();
        buttonState = ENABLED;
    }

    public Button(String[] textureFiles, int x, int y, int reduceBorder) {
        model = new Model(textureFiles, x, y);
        xLeft = (x + reduceBorder) / 2560.0;
        xRight = (x + model.getWidth() - reduceBorder) / 2560.0;
        yTop = (y + reduceBorder) / 1440.0;
        yBottom = (y + model.getHeight() - reduceBorder) / 1440.0;
        buttonActions = new ArrayList<>();
        buttonState = ENABLED;
    }

    public void setVisible(boolean visible) {
        model.setVisible(visible);
    }

    public void setEnabled(boolean enabled) {
        setButtonState(enabled ? ENABLED : DISABLED);
    }

    private void setButtonState(ButtonState buttonState) {
        this.buttonState = buttonState;
        model.setTexture(buttonState.texture);
    }

    public boolean isVisible() {
        return model.isVisible();
    }

    @Override
    public void render() {
        model.render();
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        if (buttonState != DISABLED) {
            setButtonState(isMouseover(xCursor, yCursor) ? MOUSEOVER : ENABLED);
        }
    }

    @Override
    public boolean buttonPressed(double xCursor, double yCursor) {
        if (buttonState != DISABLED && isMouseover(xCursor, yCursor)) {
            for (ButtonAction buttonAction : buttonActions) {
                buttonAction.executeAction();
            }
            return true;
        }
        return false;
    }

    public void addAction(ButtonAction buttonAction) {
        buttonActions.add(buttonAction);
    }

    private boolean isMouseover(double xCursor, double yCursor) {
        return xCursor >= xLeft
                && xCursor <= xRight
                && yCursor >= yTop
                && yCursor <= yBottom;
    }
}
