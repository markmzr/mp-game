package realestateempire.singleplayer.board.property;

import realestateempire.singleplayer.Game;
import realestateempire.singleplayer.board.Location;
import realestateempire.singleplayer.player.Player;

import static realestateempire.singleplayer.board.property.PropertyState.*;
import static realestateempire.singleplayer.player.PlayerState.BANKRUPT;

public abstract class Property implements Location {

    private final PropertyGraphics propertyGraphics;
    private final String name;
    private final int location;
    private final int price;
    private Player landedPlayer;
    protected PropertyState propertyState;
    protected Player owner;

    public Property(String[] propertyData, PropertyGraphics propertyGraphics) {
        this.propertyGraphics = propertyGraphics;
        location = Integer.parseInt(propertyData[0]);
        name = propertyData[2];
        price = Integer.parseInt(propertyData[3]);
        propertyState = NOT_OWNED;
    }

    public String getName() {
        return name;
    }

    public int getLocation() {
        return location;
    }

    public int getPrice() {
        return price;
    }

    public PropertyState getPropertyState() {
        return propertyState;
    }

    public int getMortgageValue() {
        return (int) Math.round(price * 0.5);
    }

    public abstract boolean canTradeProperty(Player player);
    public abstract boolean canMortgage(Player player);
    protected abstract void payRent(Player player);

    public boolean canPayMortgage(Player player) {
        int mortgagePrice = (int) Math.round(1.1 * getMortgageValue());
        return propertyState == MORTGAGED
                && owner == player
                && owner.getMoney() > mortgagePrice;
    }

    public void landPlayer(Player player, Game game) {
        if (propertyState == NOT_OWNED && player.getMoney() > price) {
            landedPlayer = player;
            player.canBuyProperty(this);
        } else if (propertyState == OWNED && owner != player) {
            payRent(player);
        }
    }

    public void buyProperty() {
        propertyState = OWNED;
        owner = landedPlayer;
        owner.updateMoney(-1 * price);
        owner.updateNetworth((int) Math.round(price * 0.5));
        propertyGraphics.updateToken(owner.getToken());
    }

    public void sellProperty() {
        propertyState = NOT_OWNED;
        if (owner.getPlayerState() != BANKRUPT) {
            int sellPrice = (int) Math.round(0.5 * price);
            owner.updateMoney(sellPrice);
            owner.updateNetworth(-1 * sellPrice);
            propertyGraphics.disableToken();
        }
    }

    public void mortgage() {
        propertyState = MORTGAGED;
        owner.updateMoney(getMortgageValue());
        owner.updateNetworth(-1 * getMortgageValue());
        propertyGraphics.enableMortgage();
    }

    public void payMortgage() {
        propertyState = OWNED;
        owner.updateMoney((int) Math.round(-1.1 * getMortgageValue()));
        owner.updateNetworth(getMortgageValue());
        propertyGraphics.disableMortgage();
    }

    public void setOwner(Player player) {
        if (propertyState == NOT_OWNED) {
            propertyState = OWNED;
        }
        owner = player;
        owner.updateNetworth((int) Math.round(price * 0.5));
        propertyGraphics.updateToken(owner.getToken());
    }
}
