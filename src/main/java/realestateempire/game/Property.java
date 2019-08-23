package realestateempire.game;

import realestateempire.graphics.Button;
import realestateempire.graphics.Button.ButtonState;
import realestateempire.graphics.Model;

import static realestateempire.game.Property.PropertyType.*;
import static realestateempire.graphics.Button.ButtonState.*;

public class Property implements Location {

    public enum PropertyType {

        STREET, RAILROAD, UTILITY
    }

    private final Game game;
    private final Model ownerToken;
    private final Model hotel;
    private final Model[] houses;
    private final Button property;
    private final PropertyType propertyType;
    private final int location;
    private final int price;
    private final int rent;
    private Player owner;
    private int houseCount;
    private boolean owned;
    private boolean hotelBuilt;

    Property(Game game, String[] propertyData) {
        this.game = game;
        location = Integer.parseInt(propertyData[0]);
        price = Integer.parseInt(propertyData[1]);
        rent = Integer.parseInt(propertyData[2]);
        propertyType = PropertyType.valueOf(propertyData[3]);

        int xOwnerIcon = Integer.parseInt(propertyData[4]);
        int yOwnerIcon = Integer.parseInt(propertyData[5]);
        ownerToken = new Model("Tokens/Blank Token Small.png", xOwnerIcon, yOwnerIcon, false);

        String[] propertyTextures = { propertyData[6], propertyData[7] };
        int xProperty = Integer.parseInt(propertyData[8]);
        int yProperty = Integer.parseInt(propertyData[9]);
        property = new Button(propertyTextures, xProperty, yProperty);

        int xHotel = Integer.parseInt(propertyData[11]);
        int yHotel = Integer.parseInt(propertyData[12]);
        hotel = new Model(propertyData[10], xHotel, yHotel, false);

        int xHouse = Integer.parseInt(propertyData[14]);
        int yHouse = Integer.parseInt(propertyData[15]);
        houses = initHouses(xHouse, yHouse);
    }

    Button getButton() {
        return property;
    }

    int getLocation() {
        return location;
    }

    Player getOwner() {
        return owner;
    }

    int getHouseCount() {
        return houseCount;
    }

    boolean isHotelBuilt() {
        return hotelBuilt;
    }

    public void render() {
        for (int i = 0; i < houseCount; i++) {
            houses[i].render();
        }
        ownerToken.render();
        property.render();
        hotel.render();
    }

    public void cursorMoved(double xCursor, double yCursor) {
        if (!game.getPrompt().isVisible()) {
            property.isMouseover(xCursor, yCursor);
        }
    }

    public void buttonPressed(double xCursor, double yCursor) {
        if (property.isMouseover(xCursor, yCursor)) {
            property.setButtonState(MOUSEOVER);
            game.getPrompt().setViewProperty(this);
        }
    }

    private boolean userIsOwner() {
        return owned && owner == game.getUser();
    }

    ButtonState canSellProperty() {
        if (userIsOwner() && houseCount == 0) {
            return ENABLED;
        }
        return DISABLED;
    }

    ButtonState canBuyHouse() {
        if (userIsOwner() && propertyType == STREET
                && owner.getMoney() >= 100 && !hotelBuilt) {
            return ENABLED;
        }
        return DISABLED;
    }

    ButtonState canSellHouse() {
        if (userIsOwner() && houseCount > 0) {
            return ENABLED;
        }
        return DISABLED;
    }

    public void playerLanded() {
        Player player = game.getCurrentPlayer();
        if (owned) {
            if (player != owner) {
                payRent();
            }
        } else {
            if (player.getMoney() >= price) {
                if (player.isUser()) {
                    if (player == game.getUser()) {
                        game.getPrompt().setBuyProperty();
                    }
                } else {
                    buyProperty();
                }
            }
        }
    }

    public void buyProperty() {
        owned = true;
        owner = game.getCurrentPlayer();
        owner.updateMoney(-1 * price);
        ownerToken.setTextures(0, owner.getToken().getTexture());
        ownerToken.setVisible(true);
        if (propertyType == RAILROAD) {
            owner.setRailroadsOwned(owner.getRailroadsOwned() + 1);
        } else if (propertyType == UTILITY) {
            owner.setUtilitiesOwned(owner.getUtilitiesOwned() + 1);
        }
    }

    public void sellProperty() {
        if (propertyType == RAILROAD) {
            owner.setRailroadsOwned(owner.getRailroadsOwned() - 1);
        } else if (propertyType == UTILITY) {
            owner.setUtilitiesOwned(owner.getUtilitiesOwned() - 1);
        }
        int sellPrice = (int) Math.round(0.5 * price);
        owner.updateMoney(sellPrice);
        owned = false;
        ownerToken.setVisible(false);
    }

    private void payRent() {
        int rentTotal = rent;
        if (propertyType == STREET) {
            rentTotal += hotelBuilt ? 500 : 100 * houseCount;
        } else if (propertyType == RAILROAD) {
            int railroadsOwned = owner.getRailroadsOwned();
            if (railroadsOwned <= 2) {
                rentTotal = railroadsOwned * 25;
            } else if (railroadsOwned == 3) {
                rentTotal = 100;
            } else {
                rentTotal = 200;
            }
        } else {
            int diceTotal = game.getDiceTotal();
            int multiplier = owner.getUtilitiesOwned() == 1 ? 4 : 10;
            rentTotal = diceTotal * multiplier;
        }
        game.getCurrentPlayer().updateMoney(-1 * rentTotal);
        owner.updateMoney(rentTotal);
    }

    public void buyHouse() {
        if (houseCount < 4) {
            houseCount++;
        } else {
            hotel.setVisible(true);
            hotelBuilt = true;
            houseCount = 0;
        }
        owner.updateMoney(-100);
    }

    public void sellHouse() {
        if (hotelBuilt) {
            hotel.setVisible(false);
            hotelBuilt = false;
            houseCount = 4;
        } else {
            houseCount--;
        }
        owner.updateMoney(50);
    }

    private Model[] initHouses(int x, int y) {
        Model[] houses = new Model[4];
        if (location < 10) {
            for (int i = 0; i < houses.length; i++) {
                houses[i] = new Model("Tokens/House.png", x - (27 * i), y);
            }
        } else if (location < 20) {
            for (int i = 0; i < houses.length; i++) {
                houses[i] = new Model("Tokens/House Side.png", 152, y - (27 * i));
            }
        } else if (location < 30) {
            for (int i = 0; i < houses.length; i++) {
                houses[i] = new Model("Tokens/House.png", x + (27 * i), 152);
            }
        } else {
            for (int i = 0; i < houses.length; i++) {
                houses[i] = new Model("Tokens/House Side.png", 1253, y + (27 * i));
            }
        }
        return houses;
    }
}
