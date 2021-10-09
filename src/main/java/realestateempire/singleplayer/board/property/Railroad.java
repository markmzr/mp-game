package realestateempire.singleplayer.board.property;

import realestateempire.singleplayer.player.Player;

import static realestateempire.singleplayer.board.property.PropertyState.MORTGAGED;
import static realestateempire.singleplayer.board.property.PropertyState.OWNED;

public class Railroad extends Property {

    public Railroad(String[] propertyData, PropertyGraphics propertyGraphics) {
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
        owner.getRailroads().add(this);
    }

    @Override
    public void sellProperty() {
        super.sellProperty();
        owner.getRailroads().remove(this);
    }

    @Override
    protected void payRent(Player player) {
        int rentTotal;
        int railroadsOwned = owner.getRailroads().size();
        if (railroadsOwned <= 2) {
            rentTotal = railroadsOwned * 25;
        } else if (railroadsOwned == 3) {
            rentTotal = 100;
        } else {
            rentTotal = 200;
        }
        player.updateMoney(-1 * rentTotal);
        owner.updateMoney(rentTotal);
    }

    @Override
    public void setOwner(Player player) {
        super.setOwner(player);
        owner.getRailroads().add(this);
    }
}
