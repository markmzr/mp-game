package realestateempire.singleplayer.player;

import java.util.ArrayList;

import realestateempire.singleplayer.TurnAction;
import realestateempire.singleplayer.board.property.Property;
import realestateempire.singleplayer.board.property.Railroad;
import realestateempire.singleplayer.board.property.Utility;
import realestateempire.singleplayer.board.street.Street;

import static realestateempire.singleplayer.player.PlayerState.ACTIVE;
import static realestateempire.singleplayer.player.PlayerState.BANKRUPT;

public abstract class Player {

    private final String name;
    private final Token token;
    final ArrayList<Street> streets;
    final ArrayList<Railroad> railroads;
    final ArrayList<Utility> utilities;
    protected final PlayerGraphics playerGraphics;
    protected PlayerState playerState;
    protected int money;
    protected int networth;
    protected int location;

    public Player(String name, Token token, PlayerGraphics playerGraphics) {
        this.name = name;
        this.token = token;
        this.playerGraphics = playerGraphics;
        streets = new ArrayList<>();
        railroads = new ArrayList<>();
        utilities = new ArrayList<>();
        playerState = ACTIVE;
        money = 1500;
        location = 0;
    }

    public abstract TurnAction beginTurn();
    public abstract TurnAction endTurn();
    public abstract void canBuyProperty(Property property);

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public Token getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Street> getStreets() {
        return streets;
    }

    public ArrayList<Railroad> getRailroads() {
        return railroads;
    }

    public ArrayList<Utility> getUtilities() {
        return utilities;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public int getMoney() {
        return money;
    }

    public int getLocation() {
        return location;
    }

    public void updateMoney(int moneyDelta) {
        money += moneyDelta;
        playerGraphics.updateMoney(money);
        playerGraphics.updateMoneyDelta(moneyDelta);
    }

    public void updateNetworth(int amount) {
        networth += amount;
    }

    public void updateLocation(int location) {
        this.location = location;
        playerGraphics.moveToken(location, playerState);
    }

    public void payJailFine() {
        updateMoney(-50);
        playerState = ACTIVE;
        playerGraphics.payJailFine();
    }

    public void declareBankruptcy() {
        playerState = BANKRUPT;
        for (Street street : streets) {
            street.sellProperty();
        }
        for (Railroad railroad : railroads) {
            railroad.sellProperty();
        }
        for (Utility utility : utilities) {
            utility.sellProperty();
        }
        playerGraphics.declareBankruptcy();
    }
}
