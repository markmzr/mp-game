package realestateempire.singleplayer.board;

import java.util.ArrayList;
import java.util.HashMap;

import realestateempire.GameFileReader;
import realestateempire.graphics.button.Button;
import realestateempire.graphics.button.ButtonAction;
import realestateempire.singleplayer.board.property.Property;
import realestateempire.singleplayer.board.property.PropertyButtonData;
import realestateempire.singleplayer.deed.*;
import realestateempire.singleplayer.player.Player;
import realestateempire.singleplayer.trade.Trade;

public class SingleplayerBoardGraphics extends BoardGraphics {

    public void initPropertyButtons(Player user,
                                    HashMap<Integer, Property> locationToProperty,
                                    Trade trade, ViewDeed viewDeed) {
        ArrayList<String[]> propertyButtonsData = GameFileReader.readFile(
                "Property Buttons.csv");
        ButtonAction closeDeed = () -> viewDeed.setEnabled(false);
        Deed railroadDeed = new RailroadDeed(user, closeDeed);
        Deed utilityDeed = new UtilityDeed(user, closeDeed);
        Deed streetDeed = new StreetDeed(user, closeDeed);
        for (String[] buttonData : propertyButtonsData) {
            PropertyButtonData pbData = new PropertyButtonData(buttonData);
            Button propertyButton = new Button(pbData.getButtonTextures(),
                    pbData.getXButton(), pbData.getYButton(), 12);
            Property property = locationToProperty.get(pbData.getLocation());
            switch (pbData.getPropertyType()) {
                case "RAILROAD":
                    propertyButton.addAction(() -> propertyButtonAction(trade,
                            viewDeed, property, railroadDeed));
                    break;
                case "UTILITY":
                    propertyButton.addAction(() -> propertyButtonAction(trade,
                            viewDeed, property, utilityDeed));
                    break;
                default:
                    propertyButton.addAction(() -> propertyButtonAction(trade,
                            viewDeed, property, streetDeed));
            }
            interactives.add(propertyButton);
        }
    }

    private void propertyButtonAction(Trade trade, ViewDeed viewDeed,
                                      Property property, Deed<Property> deed) {
        if (trade.isEnabled()) {
            trade.selectProperty(property);
        } else {
            deed.updateProperty(property);
            viewDeed.setDeed(deed);
            viewDeed.setEnabled(true);
        }
    }
}
