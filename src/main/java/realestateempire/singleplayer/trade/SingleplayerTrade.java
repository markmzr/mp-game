package realestateempire.singleplayer.trade;

import realestateempire.graphics.button.Button;
import realestateempire.singleplayer.player.Player;

public class SingleplayerTrade extends Trade {

    private final Button trade;
    private final Button cancel;

    public SingleplayerTrade(Player user) {
        super(user);
        String[] tradeTextures = { "Buttons/Trade.png", "Buttons/Trade M.png",
                "Buttons/Trade D.png" };
        trade = new Button(tradeTextures, 363, 1061);
        trade.setEnabled(false);
        trade.addAction(this::makeTrade);

        String[] cancelTextures = { "Buttons/Cancel.png",
                "Buttons/Cancel M.png", "Buttons/Cancel.png" };
        cancel = new Button(cancelTextures, 817, 1061);
        cancel.addAction(() -> enabled = false);
    }

    @Override
    public void render() {
        if (enabled) {
            background.render();
            userGraphics.render();
            playerGraphics.render();
            trade.render();
            cancel.render();
        }
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        if (enabled) {
            userGraphics.cursorMoved(xCursor, yCursor);
            playerGraphics.cursorMoved(xCursor, yCursor);
            trade.cursorMoved(xCursor, yCursor);
            cancel.cursorMoved(xCursor, yCursor);
        }
    }

    @Override
    public boolean buttonPressed(double xCursor, double yCursor) {
        if (enabled) {
            return userGraphics.buttonPressed(xCursor, yCursor)
                    || playerGraphics.buttonPressed(xCursor, yCursor)
                    || trade.buttonPressed(xCursor, yCursor)
                    || cancel.buttonPressed(xCursor, yCursor);
        }
        return false;
    }

    @Override
    public void enable(Player player) {
        super.enable(player);
        trade.setEnabled(false);
        cancel.setEnabled(true);
    }

    @Override
    protected void updateButtons() {
        trade.setEnabled(user.getValue() > 0
                && user.getValue() >= player.getValue());
    }
}
