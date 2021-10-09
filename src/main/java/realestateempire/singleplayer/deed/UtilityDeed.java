package realestateempire.singleplayer.deed;

import realestateempire.graphics.button.Button;
import realestateempire.graphics.button.ButtonAction;
import realestateempire.graphics.model.Model;
import realestateempire.singleplayer.board.property.Utility;
import realestateempire.singleplayer.player.Player;

public class UtilityDeed extends Deed<Utility> {

    private final Model background;
    private final Button mortgage;
    private final Button payMortgage;
    private final Button close;
    private Utility utility;

    public UtilityDeed(Player player, ButtonAction closeDeed) {
        super(player);
        background = new Model("Prompts/View Utility.png", 0, 0);

        String[] mortgageTextures = { "Buttons/Mortgage.png",
                "Buttons/Mortgage M.png", "Buttons/Mortgage D.png" };
        mortgage = new Button(mortgageTextures, 446, 942);
        mortgage.addAction(this::mortgage);

        String[] payMortgageTextures = { "Buttons/Pay Mortgage.png",
                "Buttons/Pay Mortgage M.png", "Buttons/Pay Mortgage D.png" };
        payMortgage = new Button(payMortgageTextures, 734, 942);
        payMortgage.addAction(this::payMortgage);

        String[] closeTextures = { "Buttons/Close.png", "Buttons/Close M.png",
                "Buttons/Close.png" };
        close = new Button(closeTextures, 590, 1060);
        close.addAction(closeDeed);
    }

    void render() {
        background.render();
        super.render();
        mortgage.render();
        payMortgage.render();
        close.render();
    }

    void cursorMoved(double xCursor, double yCursor) {
        mortgage.cursorMoved(xCursor, yCursor);
        payMortgage.cursorMoved(xCursor, yCursor);
        close.cursorMoved(xCursor, yCursor);
    }

    boolean buttonPressed(double xCursor, double yCursor) {
        return mortgage.buttonPressed(xCursor, yCursor)
                || payMortgage.buttonPressed(xCursor, yCursor)
                || close.buttonPressed(xCursor, yCursor);
    }

    @Override
    public void updateProperty(Utility utility) {
        this.utility = utility;
        updateText(utility);
        updateButtons();
    }

    private void updateButtons() {
        mortgage.setEnabled(utility.canMortgage(player));
        payMortgage.setEnabled(utility.canPayMortgage(player));
    }

    private void mortgage() {
        utility.mortgage();
        updateButtons();
    }

    private void payMortgage() {
        utility.payMortgage();
        updateButtons();
    }
}
