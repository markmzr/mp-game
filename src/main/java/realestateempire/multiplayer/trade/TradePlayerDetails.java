package realestateempire.multiplayer.trade;

import java.util.ArrayList;

import realestateempire.singleplayer.board.property.Property;
import realestateempire.singleplayer.trade.TradePlayer;

public class TradePlayerDetails {

    private ArrayList<Property> properties;
    private ArrayList<Integer> propertyLocations;
    private int id;
    private int money;
    private int value;

    public TradePlayerDetails() { }

    public TradePlayerDetails(TradePlayer tradePlayer, int id) {
        this.id = id;
        this.money = tradePlayer.getMoney();
        this.value = tradePlayer.getValue();
        properties = new ArrayList<>();
        propertyLocations = new ArrayList<>();
        for (Property property : tradePlayer.getProperties()) {
            propertyLocations.add(property.getLocation());
        }
    }

    public void setProperties(ArrayList<Property> properties) {
        this.properties = properties;
    }

    public ArrayList<Property> getProperties() {
        return properties;
    }

    public ArrayList<Integer> getPropertyLocations() {
        return propertyLocations;
    }

    public int getId() {
        return id;
    }

    public int getMoney() {
        return money;
    }

    public int getValue() {
        return value;
    }
}
