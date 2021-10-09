package realestateempire.singleplayer.board.chance;

import java.util.Random;

import realestateempire.singleplayer.Game;
import realestateempire.singleplayer.board.Card;
import realestateempire.singleplayer.board.Location;
import realestateempire.singleplayer.board.street.Street;
import realestateempire.singleplayer.player.Player;

import static realestateempire.singleplayer.TurnAction.MOVE_TOKEN;
import static realestateempire.singleplayer.board.street.BuildingState.HOTEL;
import static realestateempire.singleplayer.player.PlayerState.IN_JAIL;

public class Chance implements Location {

    private final Player[] players;
    protected final Card[] cards;
    protected final ChanceGraphics chanceGraphics;
    protected int card;

    public Chance(Player[] players, ChanceGraphics chanceGraphics) {
        this.players = players;
        this.chanceGraphics = chanceGraphics;
        cards = new Card[14];
        for (int i = 0; i < 4; i++) {
            cards[i] = this::receiveMoney;
        }
        cards[4] = this::moveToGo;
        for (int i = 5; i < 9; i++) {
            cards[i] = this::moveToProperty;
        }
        cards[9] = this::moveToRailroad;
        cards[10] = this::moveToUtility;
        cards[11] = this::payPlayers;
        cards[12] = this::repairBuildings;
        cards[13] = this::goToJail;
    }

    @Override
    public void landPlayer(Player player, Game game) {
        card = new Random().nextInt(cards.length);
        cards[card].getCard(player, game);
        chanceGraphics.enableCard(card);
    }

    private void receiveMoney(Player player, Game game) {
        int[] money = { 50, 100, 150, -15 };
        player.updateMoney(money[card]);
    }

    private void payPlayers(Player player, Game game) {
        for (Player p : players) {
            if (p != player) {
                player.updateMoney(-50);
                p.updateMoney(50);
            }
        }
    }

    private void moveToGo(Player player, Game game) {
        int moveCount = 40 - player.getLocation();
        game.setMoveCount(moveCount);
        game.setTurnAction(MOVE_TOKEN);
    }

    private void moveToProperty(Player player, Game game) {
        int[] propertyLocations = { 5, 11, 24, 39 };
        int newLocation = propertyLocations[card % 5];
        int moveCount = (40 + newLocation - player.getLocation()) % 40;
        game.setMoveCount(moveCount);
        game.setTurnAction(MOVE_TOKEN);
    }

    private void moveToRailroad(Player player, Game game) {
        int location = player.getLocation();
        int moveCount;
        if (location == 7) {
            moveCount = 8;
        } else if (location == 22) {
            moveCount = 3;
        } else {
            moveCount = 9;
        }
        game.setMoveCount(moveCount);
        game.setTurnAction(MOVE_TOKEN);
    }

    private void moveToUtility(Player player, Game game) {
        int location = player.getLocation();
        int moveCount;
        if (location == 7) {
            moveCount = 5;
        } else if (location == 22) {
            moveCount = 6;
        } else {
            moveCount = 16;
        }
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
