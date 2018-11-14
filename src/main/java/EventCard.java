import java.util.Random;

public class EventCard {

    private GameState gameState;
    private Card[] communityChest;
    private Card[] chance;
    private int card;

    public EventCard(GameState gameState) {
        this.gameState = gameState;

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

    public void communityChest() {
        card = new Random().nextInt(communityChest.length);
        communityChest[card].receivedCard();

        if (gameState.getCurrentPlayer().getId() == 0) {
            gameState.getGameScreen().setPromptState(PromptState.COMMUNITY_CHEST);
            gameState.setEventCard(card);
        } else {
            gameState.turnCompleted();
        }
    }

    public void chance() {
        card = new Random().nextInt(chance.length);
        chance[card].receivedCard();

        if (gameState.getCurrentPlayer().getId() == 0) {
            gameState.getGameScreen().setPromptState(PromptState.CHANCE);
            gameState.setEventCard(card);
        } else {
            gameState.turnCompleted();
        }
    }

    private void communityChestMoney() {
        int[] moneyAmount = { 10, 20, 25, 50, 100, 100, 100, 200, -50, -50, -50};
        gameState.getCurrentPlayer().updateMoney(moneyAmount[card]);
    }

    private void chanceMoney() {
        int[] moneyAmount = { 50, 100, 150, -15 };
        gameState.getCurrentPlayer().updateMoney(moneyAmount[card]);
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
    }

    private void advanceToGo() {
        Player currentPlayer = gameState.getCurrentPlayer();
        int movePositions = 40 - currentPlayer.getCurrentPosition();
        currentPlayer.updatePosition(movePositions);
    }

    private void movePlayer() {
        int[] locations = { 0, 5, 11, 24, 39 };
        card -= 4;
        Player currentPlayer = gameState.getCurrentPlayer();
        int currentPosition = currentPlayer.getCurrentPosition();

        if (card <= 4) {
            currentPlayer.updatePosition(locations[card]);
        } else if (card == 5) {
            if (currentPosition == 7) {
                currentPlayer.updatePosition(15);
            } else if (currentPosition == 22) {
                currentPlayer.updatePosition(25);
            } else {
                currentPlayer.updatePosition(5);
            }
        }
        else {
            if (currentPosition == 22) {
                currentPlayer.updatePosition(28);
            } else {
                currentPlayer.updatePosition(12);
            }
        }
    }

    private void repairBuildings() {

    }

    private void goToJail() {
        Player player = gameState.getCurrentPlayer();
        System.out.println(player.getName() + " landed on Go to Jail.");
        player.setInJail(true);
        int currentPosition = player.getCurrentPosition();
        int move = currentPosition <= 10 ? 10 - currentPosition : 50 - currentPosition;
        player.updatePosition(move);
    }
}
