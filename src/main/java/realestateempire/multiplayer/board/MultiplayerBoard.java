package realestateempire.multiplayer.board;

import java.util.ArrayList;
import java.util.HashMap;

import realestateempire.GameFileReader;
import realestateempire.multiplayer.board.property.MultiplayerRailroad;
import realestateempire.multiplayer.board.property.MultiplayerUtility;
import realestateempire.multiplayer.board.property.PropertyEventHandler;
import realestateempire.multiplayer.board.street.MultiplayerStreet;
import realestateempire.multiplayer.board.street.StreetEventHandler;
import realestateempire.multiplayer.server.GameSession;
import realestateempire.multiplayer.trade.MultiplayerTrade;
import realestateempire.multiplayer.trade.TradeOfferHandler;
import realestateempire.multiplayer.trade.TradeResponseHandler;
import realestateempire.singleplayer.SingleplayerGame;
import realestateempire.singleplayer.board.*;
import realestateempire.singleplayer.board.jail.GoToJail;
import realestateempire.singleplayer.board.property.Property;
import realestateempire.singleplayer.board.street.Street;
import realestateempire.singleplayer.deed.ViewDeed;
import realestateempire.singleplayer.player.Player;

public class MultiplayerBoard extends Board {

    private final TradeOfferHandler tradeOfferHandler;
    private final TradeResponseHandler tradeResponseHandler;
    private final PropertyEventHandler propertyEventHandler;
    private final StreetEventHandler streetEventHandler;

    private MultiplayerBoard(MultiplayerBoardBuilder mpBoardBuilder) {
        super(mpBoardBuilder);
        tradeOfferHandler = mpBoardBuilder.tradeOfferHandler;
        tradeResponseHandler = mpBoardBuilder.tradeResponseHandler;
        propertyEventHandler = mpBoardBuilder.propertyEventHandler;
        streetEventHandler = mpBoardBuilder.streetEventHandler;
    }

    public void landPlayer(Player player, SingleplayerGame singleplayerGame) {
        locations[player.getLocation()].landPlayer(player, singleplayerGame);
    }

    public static class MultiplayerBoardBuilder extends BoardBuilder {

        private final TradeOfferHandler tradeOfferHandler;
        private final TradeResponseHandler tradeResponseHandler;
        private final PropertyEventHandler propertyEventHandler;
        private final StreetEventHandler streetEventHandler;

        public MultiplayerBoardBuilder(Player[] mpPlayers,
                                       MultiplayerTrade mpTrade,
                                       ViewDeed viewDeed,
                                       MultiplayerBoardGraphics boardGraphics,
                                       GameSession gameSession) {
            super(new Location[40]);
            initLocations(mpPlayers, boardGraphics, gameSession);
            HashMap<Integer, Street> locationToStreet = new HashMap<>();
            HashMap<Integer, Property> locationToProperty =
                    initProperties(locationToStreet, boardGraphics, gameSession);
            boardGraphics.initPropertyButtons(mpPlayers[gameSession.getUserId()],
                    locationToProperty, mpTrade, viewDeed);
            tradeOfferHandler = new TradeOfferHandler(mpPlayers,
                    locationToProperty, mpTrade, gameSession);
            tradeResponseHandler = new TradeResponseHandler(mpPlayers,
                    locationToProperty, mpTrade, gameSession);
            propertyEventHandler = new PropertyEventHandler(locationToProperty,
                    gameSession);
            streetEventHandler = new StreetEventHandler(locationToStreet,
                    gameSession);
        }

        private void initLocations(Player[] players,
                                   MultiplayerBoardGraphics boardGraphics,
                                   GameSession gameSession) {
            MultiplayerCommunityChest mpCommunityChest =
                    new MultiplayerCommunityChest(players,
                    boardGraphics.communityChestGraphics, gameSession);
            MultiplayerChance mpChance = new MultiplayerChance(players,
                    boardGraphics.chanceGraphics, gameSession);
            Tax tax = new Tax();
            NeutralLocation neutralLocation = new NeutralLocation();
            locations[0] = new Go();
            locations[2] = mpCommunityChest;
            locations[4] = tax;
            locations[7] = mpChance;
            locations[10] = neutralLocation;
            locations[17] = mpCommunityChest;
            locations[20] = neutralLocation;
            locations[22] = mpChance;
            locations[30] = new GoToJail();
            locations[33] = mpCommunityChest;
            locations[36] = mpChance;
            locations[38] = tax;
        }

        private HashMap<Integer, Property> initProperties(HashMap<Integer, Street> locationToStreet,
                                                          MultiplayerBoardGraphics boardGraphics,
                                                          GameSession gameSession) {
            HashMap<Integer, Property> locationToProperty = new HashMap<>();
            ArrayList<String[]> propertyData = GameFileReader.readFile("Properties.csv");
            for (String[] data : propertyData) {
                int location = Integer.parseInt(data[0]);
                String propertyType = data[1];
                Property property;
                switch (propertyType) {
                    case "RAILROAD":
                        property = new MultiplayerRailroad(data,
                                boardGraphics.locationToPropertyGraphics.get(location),
                                gameSession);
                        break;
                    case "UTILITY":
                        property = new MultiplayerUtility(data,
                                boardGraphics.locationToPropertyGraphics.get(location),
                                gameSession);
                        break;
                    default:
                        MultiplayerStreet mpStreet = new MultiplayerStreet(data,
                                boardGraphics.locationToPropertyGraphics.get(location),
                                boardGraphics.locationToStreetGraphics.get(location),
                                gameSession);
                        locationToStreet.put(location, mpStreet);
                        property = mpStreet;
                }
                locations[location] = property;
                locationToProperty.put(location, property);
            }
            return locationToProperty;
        }

        public MultiplayerBoard build() {
            return new MultiplayerBoard(this);
        }
    }
}
