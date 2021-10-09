package realestateempire.singleplayer.board.jail;

import realestateempire.graphics.button.Button;
import realestateempire.graphics.button.ButtonAction;
import realestateempire.graphics.model.Model;

public class InJail {

    private final Model background;
    private final Button payFine;
    private boolean enabled;

    public InJail(ButtonAction payJailFine) {
        background = new Model("Prompts/In Jail.png", 1491, 830);
        String[] payFineTextures = { "Buttons/Pay Fine.png",
                "Buttons/Pay Fine M.png", "Buttons/Pay Fine.png" };
        payFine = new Button(payFineTextures, 1847, 995);
        payFine.addAction(payJailFine);
        payFine.addAction(() -> enabled = false);
        enabled = false;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void render() {
        if (enabled) {
            background.render();
            payFine.render();
        }
    }

    public void cursorMoved(double xCursor, double yCursor) {
        if (enabled) {
            payFine.cursorMoved(xCursor, yCursor);
        }
    }

    public boolean buttonPressed(double xCursor, double yCursor) {
        if (enabled) {
            return payFine.buttonPressed(xCursor, yCursor);
        }
        return false;
    }
}
