package realestateempire.game;

import java.util.Random;

public class Board {

    private final GameState gameState;
    private final BoardLocation[] boardLocations;

    public Board(GameState gameState) {
        this.gameState = gameState;
        EventCard eventCard = new EventCard();

        boardLocations = new BoardLocation[40];
        boardLocations[0] = this::go;
        boardLocations[1] = new Property(gameState, 1, 60, 2);
        boardLocations[2] = eventCard::communityChest;
        boardLocations[3] = new Property(gameState, 3, 60, 4);
        boardLocations[4] = this::tax;
        boardLocations[5] = new Property(gameState, 5, 200, 6);
        boardLocations[6] = new Property(gameState, 6, 100, 6);
        boardLocations[7] = eventCard::chance;
        boardLocations[8] = new Property(gameState, 8, 100, 6);
        boardLocations[9] = new Property(gameState, 9, 120, 8);
        boardLocations[10] = this::neutralLocation;
        boardLocations[11] = new Property(gameState, 11, 140, 10);
        boardLocations[12] = new Property(gameState, 12, 150, 6);
        boardLocations[13] = new Property(gameState, 13, 140, 10);
        boardLocations[14] = new Property(gameState, 14, 160, 12);
        boardLocations[15] = new Property(gameState, 15, 200, 6);
        boardLocations[16] = new Property(gameState, 16, 180, 14);
        boardLocations[17] = eventCard::communityChest;
        boardLocations[18] = new Property(gameState, 18, 180, 14);
        boardLocations[19] = new Property(gameState, 19, 200, 16);
        boardLocations[20] = this::neutralLocation;
        boardLocations[21] = new Property(gameState, 21, 220, 18);
        boardLocations[22] = eventCard::chance;
        boardLocations[23] = new Property(gameState, 23, 220, 18);
        boardLocations[24] = new Property(gameState, 24, 240, 20);
        boardLocations[25] = new Property(gameState, 25, 200, 6);
        boardLocations[26] = new Property(gameState, 26, 260, 22);
        boardLocations[27] = new Property(gameState, 27, 260, 22);
        boardLocations[28] = new Property(gameState, 28, 150, 6);
        boardLocations[29] = new Property(gameState, 29, 280, 24);
        boardLocations[30] = this::goToJail;
        boardLocations[31] = new Property(gameState, 31, 300, 26);
        boardLocations[32] = new Property(gameState, 32, 300, 26);
        boardLocations[33] = eventCard::communityChest;
        boardLocations[34] = new Property(gameState, 34, 320, 28);
        boardLocations[35] = new Property(gameState, 35, 200, 6);
        boardLocations[36] = eventCard::chance;
        boardLocations[37] = new Property(gameState, 37, 350, 35);
        boardLocations[38] = this::tax;
        boardLocations[39] = new Property(gameState, 39, 400, 50);
    }

    public void render() {
        for (BoardLocation location :  boardLocations) {
            if (location instanceof Property) {
                ((Property)location).render();
            }
        }
    }

    public void cursorMoved(double cursorXCoord, double cursorYCoord) {
        for (BoardLocation location :  boardLocations) {
            if (location instanceof Property) {
                ((Property)location).cursorMoved(cursorXCoord, cursorYCoord);
            }
        }
    }

    public void buttonPressed(double cursorXCoord, double cursorYCoord) {
        for (BoardLocation location :  boardLocations) {
            if (location instanceof Property) {
                ((Property)location).buttonPressed(cursorXCoord, cursorYCoord);
            }
        }
    }

    public void playerLanded() {
        int playerLocation = gameState.getCurrentPlayer().getCurrentLocation();
        boardLocations[playerLocation].playerLanded();
    }

    private void go() {
        gameState.getCurrentPlayer().updateMoney(400);
        gameState.turnCompleted();
    }

    private void tax() {
        gameState.getCurrentPlayer().updateMoney(-100);
        gameState.turnCompleted();
    }

    private void neutralLocation() {
        gameState.turnCompleted();
    }

    private void goToJail() {
        Player player = gameState.getCurrentPlayer();
        int currentPosition = player.getCurrentLocation();
        int move = currentPosition <= 10 ? 10 - currentPosition : 50 - currentPosition;
        player.updateLocation(move);
        player.setInJail(true);
    }

    private interface Card {

        void receivedCard();
    }

    private class EventCard {

        private final Card[] communityChest;
        private final Card[] chance;
        private int card;

        private EventCard() {
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
            for (int i = 4; i < 11; i++) {
                chance[i] = this::movePlayer;
            }
            chance[11] = this::payPlayers;
            chance[12] = this::repairBuildings;
            chance[13] = this::goToJail;
        }

        private void communityChest() {
            card = new Random().nextInt(communityChest.length);
            communityChest[card].receivedCard();
            gameState.getGamePrompt().setCommunityChest(card);
        }

        private void chance() {
            card = new Random().nextInt(chance.length);
            chance[card].receivedCard();
            gameState.getGamePrompt().setChance(card);
        }

        private void communityChestMoney() {
            int[] moneyAmount = { 10, 20, 25, 50, 100, 100, 100, 200, -50, -50, -50};
            gameState.getCurrentPlayer().updateMoney(moneyAmount[card]);
            gameState.turnCompleted();
        }

        private void chanceMoney() {
            int[] moneyAmount = { 50, 100, 150, -15 };
            gameState.getCurrentPlayer().updateMoney(moneyAmount[card]);
            gameState.turnCompleted();
        }

        private void collectFromPlayers() {
            int collectAmount = 50;
            Player[] players = gameState.getPlayers();
            Player currentPlayer = gameState.getCurrentPlayer();
            int currentPlayerId = currentPlayer.getId();

            for (Player player : players) {
                if (player.getId() != currentPlayerId) {
                    player.updateMoney(-1 * collectAmount);
                    currentPlayer.updateMoney(collectAmount);
                }
            }
            gameState.turnCompleted();
        }

        private void payPlayers() {
            int payAmount = 50;
            Player[] players = gameState.getPlayers();
            Player currentPlayer = gameState.getCurrentPlayer();
            int currentPlayerId = currentPlayer.getId();

            for (Player player : players) {
                if (player.getId() != currentPlayerId) {
                    currentPlayer.updateMoney(-1 * payAmount);
                    player.updateMoney(payAmount);
                }
            }
            gameState.turnCompleted();
        }

        private void advanceToGo() {
            Player currentPlayer = gameState.getCurrentPlayer();
            int movePositions = 40 - currentPlayer.getCurrentLocation();
            currentPlayer.updateLocation(movePositions);
        }

        private void movePlayer() {
            int[] locations = { 0, 5, 11, 24, 39 };
            Player currentPlayer = gameState.getCurrentPlayer();
            int currentPosition = currentPlayer.getCurrentLocation();

            if (card <= 8) {
                int newPosition = locations[card - 4];
                if (currentPosition < newPosition) {
                    currentPlayer.updateLocation(newPosition - currentPosition);
                } else {
                    currentPlayer.updateLocation(40 - currentPosition + newPosition);
                }
            } else if (card == 9) {
                if (currentPosition == 7) {
                    currentPlayer.updateLocation(8);
                } else if (currentPosition == 22) {
                    currentPlayer.updateLocation(3);
                } else {
                    currentPlayer.updateLocation(9);
                }
            } else {
                if (currentPosition == 7) {
                    currentPlayer.updateLocation(5);
                } else if (currentPosition == 22) {
                    currentPlayer.updateLocation(6);
                } else {
                    currentPlayer.updateLocation(16);
                }
            }
        }

        private void repairBuildings() {
            int repairCost = 0;
            Player currentPlayer = gameState.getCurrentPlayer();

            for (BoardLocation location : boardLocations) {
                if (location instanceof Property && ((Property)location).getOwner() == currentPlayer) {
                    repairCost += ((Property)location).getHouseCount() * 20;
                    if (((Property)location).getHotelBuilt()) {
                        repairCost += 100;
                    }
                }
            }
            currentPlayer.updateMoney(-1 * repairCost);
            gameState.turnCompleted();
        }

        private void goToJail() {
            Player player = gameState.getCurrentPlayer();
            player.setInJail(true);
            int currentLocation = player.getCurrentLocation();
            int move = currentLocation <= 10 ? 10 - currentLocation : 50 - currentLocation;
            player.updateLocation(move);
        }
    }
}
