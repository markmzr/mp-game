package realestateempire.singleplayer.trade;

import realestateempire.graphics.button.Button;
import realestateempire.graphics.text.CenteredText;
import realestateempire.graphics.text.Font;
import realestateempire.singleplayer.board.property.Property;
import realestateempire.singleplayer.player.Player;

public class  TradePlayerGraphics {

    private final CenteredText money;
    private final CenteredText value;
    private final CenteredText playerName;
    private final CenteredText[] propertyNames;
    private final Button increaseMoney;
    private final Button decreaseMoney;

    TradePlayerGraphics(int id, Button increaseMoney, Button decreaseMoney) {
        this.increaseMoney = increaseMoney;
        this.decreaseMoney = decreaseMoney;
        int xDelta = id == 0 ? 0 : 454;
        money = new CenteredText("$0", 493 + xDelta, 548, 0.64);
        value = new CenteredText("Value $0", 493 + xDelta, 484, 0.64);
        playerName = new CenteredText(" ", Font.BOLD, 493 + xDelta, 384, 0.74);
        propertyNames = new CenteredText[5];
        for (int i = 0; i < propertyNames.length; i++) {
            int x = 493 + xDelta;
            int y = 611 + (57 * i);
            propertyNames[i] = new CenteredText(" ", x, y, 0.64);
        }
    }

    public void render() {
        money.render();
        value.render();
        playerName.render();
        for (CenteredText text : propertyNames) {
            text.render();
        }
        increaseMoney.render();
        decreaseMoney.render();
    }

    public void cursorMoved(double xCursor, double yCursor) {
        increaseMoney.cursorMoved(xCursor, yCursor);
        decreaseMoney.cursorMoved(xCursor, yCursor);
    }

    public boolean buttonPressed(double xCursor, double yCursor) {
        return increaseMoney.buttonPressed(xCursor, yCursor)
                || decreaseMoney.buttonPressed(xCursor, yCursor);
    }

    void updateMoney(int money) {
        this.money.updateText("$" + money);
    }

    void updateValue(int value) {
        this.value.updateText("Value $" + value);
    }

    void updatePlayerName(Player player) {
        playerName.updateText(player.getName());
    }

    void updateButtons(Player player, int tradeMoney) {
        increaseMoney.setEnabled(player.getMoney() - tradeMoney > 50);
        decreaseMoney.setEnabled(tradeMoney >= 50);
    }

    void addProperty(Property property, int propertyIndex) {
        propertyNames[propertyIndex].updateText(property.getName());
    }

    void clearTrade() {
        updateMoney(0);
        updateValue(0);
        increaseMoney.setEnabled(false);
        decreaseMoney.setEnabled(false);
        for (CenteredText propertyName : propertyNames) {
            propertyName.updateText(" ");
        }
    }
}
