package realestateempire.game;

import static realestateempire.game.Player.PlayerState.IN_JAIL;

public class EventCard {

    private interface Card {

        void receivedCard();
    }

    private final Game game;
    private final Card[] communityChest;
    private final Card[] chance;
    private int card;

    EventCard(Game game) {
        this.game = game;
        communityChest = new Card[16];
        for (int i = 0; i < 11; i++) {
            communityChest[i] = this::communityChestMoney;
        }
        communityChest[11] = this::collectFromPlayers;
        communityChest[12] = this::collectFromPlayers;
        communityChest[13] = this::advanceToGo;
        communityChest[14] = this::repairBuildings;
        communityChest[15] = this::goToJail;

        chance = new Card[14];
        for (int i = 0; i < 4; i++) {
            chance[i] = this::chanceMoney;
        }
        chance[4] = this::advanceToGo;
        for (int i = 5; i < 9; i++) {
            chance[i] = this::moveToProperty;
        }
        chance[9] = this::moveToRailroad;
        chance[10] = this::moveToUtility;
        chance[11] = this::payPlayers;
        chance[12] = this::repairBuildings;
        chance[13] = this::goToJail;
    }

    public void communityChest(int card) {
        this.card = card;
        communityChest[card].receivedCard();
        game.getPrompt().setCommunityChest(card);
    }

    public void chance(int card) {
        this.card = card;
        chance[card].receivedCard();
        game.getPrompt().setChance(card);
    }

    private void communityChestMoney() {
        int[] money = { 10, 20, 25, 50, 100, 100, 100, 200, -50, -50, -50};
        game.getCurrentPlayer().updateMoney(money[card]);
    }

    private void chanceMoney() {
        int[] money = { 50, 100, 150, -15 };
        game.getCurrentPlayer().updateMoney(money[card]);
    }

    private void collectFromPlayers() {
        int collectMoney = 50;
        Player currentPlayer = game.getCurrentPlayer();
        Player[] players = game.getPlayers();

        for (Player player : players) {
            if (player != currentPlayer) {
                player.updateMoney(-1 * collectMoney);
                currentPlayer.updateMoney(collectMoney);
            }
        }
    }

    private void payPlayers() {
        int payMoney = 50;
        Player currentPlayer = game.getCurrentPlayer();
        Player[] players = game.getPlayers();

        for (Player player : players) {
            if (player != currentPlayer) {
                currentPlayer.updateMoney(-1 * payMoney);
                player.updateMoney(payMoney);
            }
        }
    }

    private void advanceToGo() {
        Player player = game.getCurrentPlayer();
        int moveLocations = 40 - player.getCurrentLocation();
        player.updateLocation(moveLocations);
    }

    private void moveToProperty() {
        int[] propertyLocations = { 5, 11, 24, 39 };
        Player player = game.getCurrentPlayer();
        int currentLocation = player.getCurrentLocation();
        int newLocation = propertyLocations[card - 5];

        if (currentLocation < newLocation) {
            player.updateLocation(newLocation - currentLocation);
        } else {
            player.updateLocation(40 - currentLocation + newLocation);
        }
    }

    private void moveToRailroad() {
        Player player = game.getCurrentPlayer();
        int location = player.getCurrentLocation();
        if (location == 7) {
            player.updateLocation(8);
        } else if (location == 22) {
            player.updateLocation(3);
        } else {
            player.updateLocation(9);
        }
    }

    private void moveToUtility() {
        Player player = game.getCurrentPlayer();
        int location = player.getCurrentLocation();
        if (location == 7) {
            player.updateLocation(5);
        } else if (location == 22) {
            player.updateLocation(6);
        } else {
            player.updateLocation(16);
        }
    }

    private void repairBuildings() {
        Player player = game.getCurrentPlayer();
        Property[] properties = game.getBoard().getProperties();
        int repairCost = 0;

        for (Property property : properties) {
            if (property.getOwner() == player) {
                if (property.isHotelBuilt()) {
                    repairCost += 100;
                } else {
                    repairCost += property.getHouseCount() * 20;
                }
            }
        }
        player.updateMoney(-1 * repairCost);
    }

    private void goToJail() {
        Player player = game.getCurrentPlayer();
        player.setPlayerState(IN_JAIL);
        int location = player.getCurrentLocation();
        int moveLocations = location <= 10 ? 10 - location : 50 - location;
        player.updateLocation(moveLocations);
    }
}
