package realestateempire.singleplayer.trade;

import java.util.ArrayList;

import realestateempire.graphics.button.Button;
import realestateempire.graphics.button.ButtonAction;
import realestateempire.multiplayer.trade.TradePlayerDetails;
import realestateempire.singleplayer.board.property.Property;
import realestateempire.singleplayer.player.Player;

public class TradePlayer {

    private final TradePlayerGraphics tradePlayerGraphics;
    private ArrayList<Property> properties;
    private Player player;
    private int money;
    private int value;

    TradePlayer(TradePlayerGraphics tradePlayerGraphics, Button increaseMoney,
                Button decreaseMoney, ButtonAction updateButtons) {
        this.tradePlayerGraphics = tradePlayerGraphics;
        increaseMoney.addAction(this::increaseMoney);
        increaseMoney.addAction(updateButtons);
        decreaseMoney.addAction(this::decreaseMoney);
        decreaseMoney.addAction(updateButtons);
        properties = new ArrayList<>(5);
        value = 0;
        money = 0;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public Player getPlayer() {
        return player;
    }

    public int getMoney() {
        return money;
    }

    public int getValue() {
        return value;
    }

    public void updatePlayer(Player player) {
        this.player = player;
        tradePlayerGraphics.updatePlayerName(player);
        tradePlayerGraphics.updateButtons(player, money);
    }

    public void updateTrade(TradePlayerDetails tradePlayerDetails) {
        money = tradePlayerDetails.getMoney();
        value = tradePlayerDetails.getValue();
        properties = tradePlayerDetails.getProperties();
        tradePlayerGraphics.updateMoney(money);
        tradePlayerGraphics.updateButtons(player, money);
        tradePlayerGraphics.updateValue(value);
        for (int i = 0; i < properties.size(); i++) {
            tradePlayerGraphics.addProperty(properties.get(i), i);
        }
    }

    boolean canAddProperty(Property property) {
        return property.canTradeProperty(player)
                && !properties.contains(property)
                && properties.size() < 5;
    }

    void addProperty(Property property) {
        properties.add(property);
        value += property.getPrice();
        tradePlayerGraphics.addProperty(property, properties.size() - 1);
        tradePlayerGraphics.updateValue(value);
    }

    public void clearTrade() {
        money = 0;
        value = 0;
        properties.clear();
        tradePlayerGraphics.clearTrade();
        tradePlayerGraphics.updateButtons(player, money);
    }

    private void increaseMoney() {
        money += 50;
        value += 50;
        tradePlayerGraphics.updateMoney(money);
        tradePlayerGraphics.updateValue(value);
        tradePlayerGraphics.updateButtons(player, money);
    }

    private void decreaseMoney() {
        money -= 50;
        value -= 50;
        tradePlayerGraphics.updateMoney(money);
        tradePlayerGraphics.updateValue(value);
        tradePlayerGraphics.updateButtons(player, money);
    }
}
