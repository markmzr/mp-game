package realestateempire.multiplayer.trade;

import realestateempire.graphics.Interactive;
import realestateempire.graphics.button.Button;
import realestateempire.graphics.button.ButtonAction;
import realestateempire.graphics.model.Model;
import realestateempire.graphics.text.CenteredText;

public class TradeResponseGraphics implements Interactive {

    private final Model background;
    private final Button ok;
    private final CenteredText tradeResponse;
    private boolean enabled;

    public TradeResponseGraphics(ButtonAction checkTradeList) {
        background = new Model("Prompts/Trade State.png", 0, 0);
        String[] okTextures = new String[]{ "Buttons/Trade State OK.png",
                "Buttons/Trade State OK M.png", "Buttons/Trade State OK.png" };
        ok = new Button(okTextures, 590, 721);
        ok.setVisible(false);
        ok.setEnabled(false);
        ok.addAction(this::disable);
        ok.addAction(checkTradeList);
        tradeResponse = new CenteredText(" ", 720, 650, 0.68);

        enabled = false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void render() {
        if (enabled) {
            background.render();
            ok.render();
            tradeResponse.render();
        }
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        if (enabled) {
            ok.cursorMoved(xCursor, yCursor);
        }
    }

    @Override
    public boolean buttonPressed(double xCursor, double yCursor) {
        if (enabled) {
            return ok.buttonPressed(xCursor, yCursor);
        }
        return false;
    }

    void offeredTrade() {
        tradeResponse.updateText("Trade offered. Waiting for response.");
        enabled = true;
    }

    void receiveTradeOfferResponse(boolean tradeAccepted) {
        String response = tradeAccepted ? "Your trade was accepted."
                : "Your trade was refused.";
        tradeResponse.updateText(response);
        ok.setVisible(true);
        ok.setEnabled(true);
    }

    private void disable() {
        enabled = false;
        ok.setVisible(false);
        ok.setEnabled(false);
    }
}
