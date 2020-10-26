package realestateempire.game;

import realestateempire.graphics.Button;
import realestateempire.graphics.CenteredText;
import realestateempire.graphics.Model;
import realestateempire.graphics.Text;

import java.util.ArrayList;

import static realestateempire.graphics.Button.ButtonState.DISABLED;
import static realestateempire.graphics.Button.ButtonState.ENABLED;

class Trade {

    private final Game game;
    private final Model background;
    private final Button trade;
    private final Button cancel;
    private final TradePlayer player1;
    private final TradePlayer player2;
    private boolean visible;

    Trade(Game game) {
        this.game = game;
        background = new Model("Prompts/Trade.png", 0, 0);
        player1 = new TradePlayer(0);
        player2 = new TradePlayer(1);

        String[] tradeTextures = { "Buttons/Trade.png", "Buttons/Trade M.png", "Buttons/Trade D.png" };
        trade = new Button(tradeTextures, 341, 1061);

        String[] cancelTextures = { "Buttons/Cancel.png", "Buttons/Cancel M.png" };
        cancel = new Button(cancelTextures, 794, 1061);
        visible = false;
    }

    void setPlayer1(Player player) {
        player1.player = player;
    }

    void setPlayer2(Player player) {
        player2.player = player;
    }

    void setPlayer1Name(String name) {
        player1.playerText.updateText(name);
    }

    void setPlayer2Name(String name) {
        player2.playerText.updateText(name);
    }

    void setVisible(boolean visible) {
        this.visible = visible;
    }

    boolean isVisible() {
        return visible;
    }

    void render() {
        if (visible) {
            background.render();
            player1.render();
            player2.render();
            trade.render();
            cancel.render();
        }
    }

    void cursorMoved(double xCursor, double yCursor) {
        player1.cursorMoved(xCursor, yCursor);
        player2.cursorMoved(xCursor, yCursor);
        trade.isMouseover(xCursor, yCursor);
        cancel.isMouseover(xCursor, yCursor);
    }

    void buttonPressed(double xCursor, double yCursor) {
        player1.buttonPressed(xCursor, yCursor);
        player2.buttonPressed(xCursor, yCursor);
        if (trade.isMouseover(xCursor, yCursor)) {
            int moneyDelta = player1.money - player2.money;
            player1.player.updateMoney(-1 * moneyDelta);
            player2.player.updateMoney(moneyDelta);

            for (Property p : player1.properties) {
                p.setOwner(player1.player);
            }
            for (Property p : player2.properties) {
                p.setOwner(player2.player);
            }
            player1.clearItems();
            player2.clearItems();
            visible = false;
        }
        if (cancel.isMouseover(xCursor, yCursor)) {
            trade.setButtonState(ENABLED);
            cancel.setButtonState(ENABLED);
            player1.clearItems();
            player2.clearItems();
            visible = false;
        }
    }

    void addProperty(Property property) {
        if (property.getOwner() == player1.player) {
            player1.addProperty(property);
        } else if (property.getOwner() == player2.player) {
            player2.addProperty(property);
        }
    }

    private void comparePlayerValues() {
        if (player2.value > player1.value) {
            trade.setButtonState(DISABLED);
        } else {
            trade.setButtonState(ENABLED);
        }
    }

    private class TradePlayer {

        private final Text playerText;
        private final Text valueText;
        private final Text moneyText;
        private final CenteredText[] propertiesText;
        private final Button addMoney;
        private final Button subtractMoney;
        private Player player;
        private ArrayList<Property> properties;
        private int value;
        private int money;

        TradePlayer(int playerId) {
            int xOffset = playerId == 0 ? 0 : 454;
            playerText = new CenteredText(" ", 276 + xOffset, 384, 434, 0.74);
            valueText = new CenteredText("Value $0", 276 + xOffset, 484, 434, 0.74);
            moneyText = new CenteredText("$0", 276 + xOffset, 548, 434, 0.74);
            propertiesText = new CenteredText[5];
            for (int i = 0; i < 5; i++) {
                propertiesText[i] = new CenteredText(" ", 276 + xOffset, 611 + (62 * i), 434, 0.74);
            }
            String[] addMoneyTextures = { "Buttons/Add Money.png", "Buttons/Add Money M.png" };
            addMoney = new Button(addMoneyTextures, 547 + xOffset, 929);

            String[] subtractMoneyTextures = { "Buttons/Subtract Money.png", "Buttons/Subtract Money M.png" };
            subtractMoney = new Button(subtractMoneyTextures, 381 + xOffset, 929);

            properties = new ArrayList<>(5);
            value = 0;
            money = 0;
        }

        private void render() {
            playerText.render();
            valueText.render();
            moneyText.render();
            addMoney.render();
            subtractMoney.render();
            for (CenteredText property : propertiesText) {
                property.render();
            }
        }

        void cursorMoved(double xCursor, double yCursor) {
            addMoney.isMouseover(xCursor, yCursor);
            subtractMoney.isMouseover(xCursor, yCursor);
        }

        void buttonPressed(double xCursor, double yCursor) {
            if (subtractMoney.isMouseover(xCursor, yCursor) && money > 0) {
                money -= 50;
                value -= 50;
                valueText.updateText("Value $" + value);
                moneyText.updateText("$" + money);
                comparePlayerValues();
            }
            if (addMoney.isMouseover(xCursor, yCursor)
                    && game.getUser().getMoney() - money >= 50) {
                money += 50;
                value += 50;
                valueText.updateText("Value $" + value);
                moneyText.updateText("$" + money);
                comparePlayerValues();
            }
        }

        void addProperty(Property property) {
            if (properties.size() < 5 && !properties.contains(property)) {
                propertiesText[properties.size()].updateText(property.toString());
                properties.add(property);
                value += property.getPrice();
                valueText.updateText("Value $" + value);
                comparePlayerValues();
            }
        }

        private void clearItems() {
            valueText.updateText("Value $0");
            moneyText.updateText("$0");
            for (Text item : propertiesText) {
                item.updateText(" ");
            }
            properties.clear();
            money = 0;
            value = 0;
            visible = false;
        }
    }
}
