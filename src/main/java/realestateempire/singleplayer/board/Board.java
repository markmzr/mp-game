package realestateempire.singleplayer.board;

import java.util.ArrayList;
import java.util.HashMap;

import realestateempire.GameFileReader;
import realestateempire.singleplayer.Game;
import realestateempire.singleplayer.board.chance.Chance;
import realestateempire.singleplayer.board.communitychest.CommunityChest;
import realestateempire.singleplayer.board.jail.GoToJail;
import realestateempire.singleplayer.board.property.Property;
import realestateempire.singleplayer.board.property.Railroad;
import realestateempire.singleplayer.board.property.Utility;
import realestateempire.singleplayer.board.street.Street;
import realestateempire.singleplayer.deed.ViewDeed;
import realestateempire.singleplayer.player.Player;
import realestateempire.singleplayer.trade.SingleplayerTrade;

import static realestateempire.singleplayer.player.PlayerState.ACTIVE;

public class Board {

    protected final Location[] locations;

    protected Board(BoardBuilder boardBuilder) {
        locations = boardBuilder.locations;
    }

    public void movePlayer(Player player, int moveCount) {
        int newLocation = (player.getLocation() + 1) % locations.length;
        player.updateLocation(newLocation);
        if (newLocation == 0 && moveCount > 1 && player.getPlayerState() == ACTIVE) {
            passGo(player);
        }
    }

    public void landPlayer(Player player, Game game) {
        locations[player.getLocation()].landPlayer(player, game);
    }

    private void passGo(Player player) {
        player.updateMoney(200);
    }

    public static class BoardBuilder {

        protected final Location[] locations;

        public BoardBuilder(Location[] locations) {
            this.locations = locations;
        }

        public BoardBuilder(Player[] players, SingleplayerTrade spTrade,
                            ViewDeed viewDeed, SingleplayerBoardGraphics boardGraphics) {
            locations = new Location[40];
            initLocations(players, boardGraphics);
            HashMap<Integer, Property> locationToProperty = initProperties(boardGraphics);
            boardGraphics.initPropertyButtons(players[0], locationToProperty, spTrade, viewDeed);
        }

        private void initLocations(Player[] players, SingleplayerBoardGraphics boardGraphics) {
            CommunityChest communityChest = new CommunityChest(players,
                    boardGraphics.communityChestGraphics);
            Chance chance = new Chance(players, boardGraphics.chanceGraphics);
            Tax tax = new Tax();
            NeutralLocation neutralLocation = new NeutralLocation();
            locations[0] = new Go();
            locations[2] = communityChest;
            locations[4] = tax;
            locations[7] = chance;
            locations[10] = neutralLocation;
            locations[17] = communityChest;
            locations[20] = neutralLocation;
            locations[22] = chance;
            locations[30] = new GoToJail();
            locations[33] = communityChest;
            locations[36] = chance;
            locations[38] = tax;
        }

        private HashMap<Integer, Property> initProperties(SingleplayerBoardGraphics boardGraphics) {
            HashMap<Integer, Property> locationToProperty = new HashMap<>();
            ArrayList<String[]> propertyData = GameFileReader.readFile("Properties.csv");
            for (String[] data : propertyData) {
                int location = Integer.parseInt(data[0]);
                String propertyType = data[1];
                Property property;
                switch (propertyType) {
                    case "RAILROAD":
                        property = new Railroad(data,
                                boardGraphics.locationToPropertyGraphics.get(location));
                        break;
                    case "UTILITY":
                        property = new Utility(data,
                                boardGraphics.locationToPropertyGraphics.get(location));
                        break;
                    default:
                        property = new Street(data,
                                boardGraphics.locationToPropertyGraphics.get(location),
                                boardGraphics.locationToStreetGraphics.get(location));
                }
                locations[location] = property;
                locationToProperty.put(location, property);
            }
            return locationToProperty;
        }

        public Board build() {
            return new Board(this);
        }
    }
}
