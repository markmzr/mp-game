package realestateempire.singleplayer.board.street;

import realestateempire.singleplayer.board.property.Property;
import realestateempire.singleplayer.board.property.PropertyGraphics;
import realestateempire.singleplayer.player.Player;

import static realestateempire.singleplayer.board.property.PropertyState.MORTGAGED;
import static realestateempire.singleplayer.board.property.PropertyState.OWNED;
import static realestateempire.singleplayer.board.street.BuildingState.NO_HOUSES;

public class Street extends Property {

    private final StreetGroup streetGroup;
    private final StreetGraphics streetGraphics;
    private final int buildingPrice;
    private final int[] buildingRents;
    private BuildingState buildingState;

    public Street(String[] streetData, PropertyGraphics propertyGraphics,
                  StreetGraphics streetGraphics) {
        super(streetData, propertyGraphics);
        this.streetGraphics = streetGraphics;
        streetGroup = StreetGroup.valueOf(streetData[4]);
        buildingState = NO_HOUSES;
        buildingRents = new int[6];
        for (int i = 0; i < buildingRents.length; i++) {
            buildingRents[i] = Integer.parseInt(streetData[5 + i]);
        }
        buildingPrice = Integer.parseInt(streetData[11]);
    }

    public StreetGroup getStreetGroup() {
        return streetGroup;
    }

    public int[] getBuildingRents() {
        return buildingRents;
    }

    public int getBuildingPrice() {
        return buildingPrice;
    }

    public BuildingState getBuildingState() {
        return buildingState;
    }

    public int getBuildingLevel() {
        return buildingState.getBuildingLevel();
    }

    @Override
    public boolean canTradeProperty(Player player) {
        return (propertyState == OWNED || propertyState == MORTGAGED)
                && player == owner
                && !streetGroup.hasBuildings(player.getStreets());
    }

    public boolean canBuyHouse(Player player) {
        return propertyState == OWNED
                && player == owner
                && owner.getMoney() > buildingPrice
                && buildingState != BuildingState.HOTEL
                && streetGroup.canBuyHouse(this, player.getStreets());
    }

    public boolean canSellHouse(Player player) {
        return propertyState == OWNED
                && player == owner
                && buildingState.getBuildingLevel() > 0
                && streetGroup.canSellHouse(this, player.getStreets());
    }

    @Override
    public boolean canMortgage(Player player) {
        return propertyState == OWNED
                && player == owner
                && !streetGroup.hasBuildings(player.getStreets());
    }

    protected void payRent(Player player) {
        int rent = buildingRents[buildingState.getBuildingLevel()];
        if (buildingState.getBuildingLevel() == 0
                && streetGroup.playerHasMonopoly(player.getStreets())) {
            rent *= 2;
        }
        player.updateMoney(-1 * rent);
        owner.updateMoney(rent);
    }

    @Override
    public void buyProperty() {
        super.buyProperty();
        owner.getStreets().add(this);
    }

    @Override
    public void sellProperty() {
        super.sellProperty();
        owner.getStreets().remove(this);
    }

    public void buyHouse() {
        buildingState = buildingState.getNext();
        streetGraphics.enableBuildings(buildingState);
        owner.updateMoney(-1 * buildingPrice);
        owner.updateNetworth((int) Math.round(0.5 * buildingPrice));
    }

    public void sellHouse() {
        buildingState = buildingState.getPrevious();
        streetGraphics.enableBuildings(buildingState);
        int sellPrice = (int) Math.round(0.5 * buildingPrice);
        owner.updateMoney(sellPrice);
        owner.updateNetworth(-1 * sellPrice);
    }

    @Override
    public void setOwner(Player player) {
        super.setOwner(player);
        player.getStreets().add(this);
    }
}
