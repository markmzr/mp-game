package realestateempire.singleplayer.board.communitychest;

import java.util.Random;

import realestateempire.singleplayer.Game;
import realestateempire.singleplayer.board.Card;
import realestateempire.singleplayer.board.Location;
import realestateempire.singleplayer.board.street.Street;
import realestateempire.singleplayer.player.Player;

import static realestateempire.singleplayer.TurnAction.MOVE_TOKEN;
import static realestateempire.singleplayer.board.street.BuildingState.HOTEL;
import static realestateempire.singleplayer.player.PlayerState.IN_JAIL;

public class CommunityChest implements Location {

    private final Player[] players;
    protected final Card[] cards;
    protected final CommunityChestGraphics communityChestGraphics;
    protected int card;

    public CommunityChest(Player[] players,
                          CommunityChestGraphics communityChestGraphics) {
        this.players = players;
        this.communityChestGraphics = communityChestGraphics;
        cards = new Card[16];
        for (int i = 0; i < 11; i++) {
            cards[i] = this::receiveMoney;
        }
        cards[11] = this::collectFromPlayers;
        cards[12] = this::collectFromPlayers;
        cards[13] = this::advanceToGo;
        cards[14] = this::repairBuildings;
        cards[15] = this::goToJail;
    }

    public void landPlayer(Player player, Game game) {
        card = new Random().nextInt(cards.length);
        cards[card].getCard(player, game);
        communityChestGraphics.enableCard(card);
    }

    private void receiveMoney(Player player, Game game) {
        int[] money = { 10, 20, 25, 50, 100, 100, 100, 200, -50, -50, -50};
        player.updateMoney(money[card]);
    }

    private void collectFromPlayers(Player player, Game game) {
        for (Player p : players) {
            if (p != player) {
                p.updateMoney(-50);
                player.updateMoney(50);
            }
        }
    }

    private void advanceToGo(Player player, Game game) {
        int moveCount = 40 - player.getLocation();
        game.setMoveCount(moveCount);
        game.setTurnAction(MOVE_TOKEN);
    }

    private void repairBuildings(Player player, Game game) {
        int repairCost = 0;
        for (Street street : player.getStreets()) {
            if (street.getBuildingState() == HOTEL) {
                repairCost += 100;
            } else {
                repairCost += street.getBuildingLevel() * 20;
            }
        }
        player.updateMoney(-1 * repairCost);
    }

    private void goToJail(Player player, Game game) {
        player.setPlayerState(IN_JAIL);
        int moveCount = (50 - player.getLocation()) % 40;
        game.setMoveCount(moveCount);
        game.setTurnAction(MOVE_TOKEN);
    }
}
