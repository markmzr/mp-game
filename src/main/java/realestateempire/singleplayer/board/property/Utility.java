package realestateempire.singleplayer.board.property;

import realestateempire.singleplayer.player.Player;

import static realestateempire.singleplayer.board.property.PropertyState.MORTGAGED;
import static realestateempire.singleplayer.board.property.PropertyState.OWNED;

public class Utility extends Property {

    public Utility(String[] propertyData, PropertyGraphics propertyGraphics) {
        super(propertyData, propertyGraphics);
    }

    @Override
    public boolean canTradeProperty(Player player) {
        return (propertyState == OWNED || propertyState == MORTGAGED)
                && owner == player;
    }

    @Override
    public boolean canMortgage(Player player) {
        return propertyState == OWNED && owner == player;
    }

    @Override
    public void buyProperty() {
        super.buyProperty();
        owner.getUtilities().add(this);
    }

    @Override
    public void sellProperty() {
        super.sellProperty();
        owner.getUtilities().remove(this);
    }

    @Override
    protected void payRent(Player player) {
        int multiplier = owner.getUtilities().size() == 1 ? 4 : 10;
        int rentTotal = 5 * multiplier;
        player.updateMoney(-1 * rentTotal);
        owner.updateMoney(rentTotal);
    }

    @Override
    public void setOwner(Player player) {
        super.setOwner(player);
        owner.getUtilities().add(this);
    }
}
