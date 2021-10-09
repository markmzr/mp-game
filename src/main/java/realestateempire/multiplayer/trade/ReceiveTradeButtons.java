package realestateempire.multiplayer.trade;

import realestateempire.graphics.Interactive;
import realestateempire.graphics.button.Button;
import realestateempire.graphics.button.ButtonAction;

class ReceiveTradeButtons implements Interactive {

    private final Button accept;
    private final Button refuse;

    public ReceiveTradeButtons(ButtonAction acceptTrade,
                               ButtonAction refuseTrade) {
        String[] acceptTextures = {"Buttons/Accept.png", "Buttons/Accept M.png",
                "Buttons/Accept.png"};
        accept = new Button(acceptTextures, 363, 1061);
        accept.addAction(() -> {
            acceptTrade.executeAction();
            accept.setEnabled(true);
        });

        String[] refuseTextures = {"Buttons/Refuse.png", "Buttons/Refuse M.png",
                "Buttons/Refuse.png"};
        refuse = new Button(refuseTextures, 817, 1061);
        refuse.addAction(() -> {
            refuseTrade.executeAction();
            refuse.setEnabled(true);
        });
    }

    @Override
    public void setEnabled(boolean enabled) {
        accept.setEnabled(false);
        refuse.setEnabled(false);
    }

    @Override
    public void render() {
        accept.render();
        refuse.render();
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        accept.cursorMoved(xCursor, yCursor);
        refuse.cursorMoved(xCursor, yCursor);
    }

    @Override
    public boolean buttonPressed(double xCursor, double yCursor) {
        return accept.buttonPressed(xCursor, yCursor)
                || refuse.buttonPressed(xCursor, yCursor);
    }
}
