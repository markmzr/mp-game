package realestateempire.multiplayer.board;

import java.util.ArrayList;
import java.util.HashMap;

import realestateempire.GameFileReader;
import realestateempire.graphics.button.Button;
import realestateempire.graphics.button.ButtonAction;
import realestateempire.multiplayer.trade.MultiplayerTrade;
import realestateempire.singleplayer.board.BoardGraphics;
import realestateempire.singleplayer.board.property.Property;
import realestateempire.singleplayer.board.property.PropertyButtonData;
import realestateempire.singleplayer.deed.*;
import realestateempire.singleplayer.player.Player;

public class MultiplayerBoardGraphics extends BoardGraphics {

    public void initPropertyButtons(Player user,
                                    HashMap<Integer, Property> locationToProperty,
                                    MultiplayerTrade mpTrade,
                                    ViewDeed viewDeed) {
        ArrayList<String[]> propertyButtonsData = GameFileReader.readFile("Property Buttons.csv");
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
                    propertyButton.addAction(() -> propertyButtonAction(mpTrade,
                            viewDeed, property, railroadDeed));
                    break;
                case "UTILITY":
                    propertyButton.addAction(() -> propertyButtonAction(mpTrade,
                            viewDeed, property, utilityDeed));
                    break;
                default:
                    propertyButton.addAction(() -> propertyButtonAction(mpTrade,
                            viewDeed, property, streetDeed));
            }
            interactives.add(propertyButton);
        }
    }

    private void propertyButtonAction(MultiplayerTrade mpTrade,
                                      ViewDeed viewDeed, Property property,
                                      Deed<Property> deed) {
        if (mpTrade.isEnabled()) {
            mpTrade.selectProperty(property);
        } else {
            deed.updateProperty(property);
            viewDeed.setDeed(deed);
            viewDeed.setEnabled(true);
        }
    }
}
