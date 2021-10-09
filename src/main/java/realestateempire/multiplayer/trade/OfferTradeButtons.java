package realestateempire.multiplayer.trade;

import realestateempire.graphics.Interactive;
import realestateempire.graphics.button.Button;
import realestateempire.graphics.button.ButtonAction;
import realestateempire.singleplayer.trade.TradePlayer;

class OfferTradeButtons implements Interactive {

    private final Button offerTrade;
    private final Button cancel;

    public OfferTradeButtons(ButtonAction offerTradeAction, ButtonAction cancelTrade) {
        String[] offerTradeTextures = {"Buttons/Offer Trade.png",
                "Buttons/Offer Trade M.png", "Buttons/Offer Trade D.png"};
        offerTrade = new Button(offerTradeTextures, 363, 1061);
        offerTrade.addAction(() -> {
            offerTradeAction.executeAction();
            offerTrade.setEnabled(true);
        });
        offerTrade.setEnabled(false);

        String[] cancelTextures = {"Buttons/Cancel.png", "Buttons/Cancel M.png",
                "Buttons/Cancel D.png"};
        cancel = new Button(cancelTextures, 817, 1061);
        cancel.addAction(() -> {
            cancelTrade.executeAction();
            cancel.setEnabled(true);
        });
    }

    @Override
    public void setEnabled(boolean enabled) {
        offerTrade.setEnabled(false);
        cancel.setEnabled(false);
    }

    @Override
    public void render() {
        offerTrade.render();
        cancel.render();
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        offerTrade.cursorMoved(xCursor, yCursor);
        cancel.cursorMoved(xCursor, yCursor);
    }

    @Override
    public boolean buttonPressed(double xCursor, double yCursor) {
        return offerTrade.buttonPressed(xCursor, yCursor)
                || cancel.buttonPressed(xCursor, yCursor);
    }

    void update(TradePlayer user, TradePlayer player) {
        offerTrade.setEnabled(user.getValue() > 0 || player.getValue() > 0);
    }
}
