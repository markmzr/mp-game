package realestateempire.game;

import realestateempire.graphics.Button;
import realestateempire.graphics.Model;
import realestateempire.graphics.Texture;

public class Property implements BoardLocation {

    private final GameState gameState;
    private final Model ownerIcon;
    private final Model[] houses;
    private final Button property;
    private final int location;
    private final int cost;
    private final boolean railroad;
    private final boolean utility;
    private Model hotel;
    private Player owner;
    private int rent;
    private int houseCount;
    private boolean hotelBuilt;
    private boolean owned;

    public Property(GameState gameState, int location, int cost, int rent) {
        this.gameState = gameState;
        houses = new Model[4];
        this.location = location;
        this.cost = cost;
        this.rent = rent;
        houseCount = 0;
        railroad = ((location - 5) % 10) == 0;
        utility = (location == 12 || location == 28);
        owned = false;
        owner = null;
        ownerIcon = initOwnerIcon();
        property = initButton();
    }

    public Player getOwner() {
        return owner;
    }

    public int getHouseCount() {
        return houseCount;
    }

    public boolean getHotelBuilt() {
        return hotelBuilt;
    }

    public void render() {
        if (hotelBuilt) {
            hotel.render();
        } else {
            for (int i = 0; i < houseCount; i++) {
                houses[i].render();
            }
        }
        property.render();
        ownerIcon.render();
    }

    public void cursorMoved(double cursorXCoord, double cursorYCoord) {
        property.isCursorInRange(cursorXCoord, cursorYCoord);
    }

    public void buttonPressed(double cursorXCoord, double cursorYCoord) {
        if (property.isCursorInRange(cursorXCoord, cursorYCoord)) {
            gameState.getGamePrompt().setViewProperty(this);
        }
    }

    public boolean playerOwns() {
        return owned && owner.getId() == 0;
    }

    public boolean canSellProperty() {
        return playerOwns() && houseCount == 0 && !hotelBuilt;
    }

    public boolean canBuildHouse() {
        return playerOwns() && owner.getMoney() >= 100
                && !railroad && !utility && houseCount <= 4 && !hotelBuilt;
    }

    public boolean canSellHouse() {
        return playerOwns() && houseCount > 0;
    }

    public void playerLanded() {
        Player player = gameState.getCurrentPlayer();
        player.setTokenMoving(false);

        if (!owned) {
            if (player.getMoney() >= cost) {
                if (player.getId() == 0) {
                    gameState.getGamePrompt().setBuyProperty(this);
                } else {
                    buyProperty();
                }
            } else {
                gameState.turnCompleted();
            }
        } else if (player != owner) {
            payRent();
        } else {
            gameState.turnCompleted();
        }
    }

    public void buyProperty() {
        owned = true;
        owner = gameState.getCurrentPlayer();
        ownerIcon.setTexture(owner.getToken().getTexture());
        owner.updateMoney(-1 * cost);

        if (railroad) {
            int railroadsOwned = owner.getRailroadsOwned() + 1;
            owner.setRailroadsOwned(railroadsOwned);

            if (railroadsOwned <= 2) {
                rent = railroadsOwned * 25;
            } else if (railroadsOwned == 3) {
                rent = 100;
            } else {
                rent = 200;
            }
        } else if (utility) {
            owner.setUtilitiesOwned(owner.getUtilitiesOwned() + 1);
        }
        gameState.turnCompleted();
    }

    public void sellProperty() {
        owner.updateMoney((int)(0.5 * cost));
        owned = false;
        owner = null;
        ownerIcon.setTexture(new Texture("Tokens/Blank Token.png"));
    }

    private void payRent() {
        Player player = gameState.getCurrentPlayer();
        if (utility) {
            int diceRoll = gameState.getDiceRoll();
            if (player.getUtilitiesOwned() == 1) {
                rent = diceRoll * 4;
            } else {
                rent = diceRoll * 10;
            }
        }
        player.updateMoney(-1 * rent);
        owner.updateMoney(rent);
        gameState.turnCompleted();
    }

    public void buyHouse() {
        if (houseCount < 4) {
            if (location < 10) {
                int x = 1217 - (117 * (location - 1)) - (27 * houseCount);
                houses[houseCount] = new Model("House.png", x, 1253);
            } else if (location < 20) {
                int y = 1217 - (117 * (location - 11)) - (27 * houseCount);
                houses[houseCount] = new Model("House Side.png", 152, y);
            } else if (location < 30) {
                int x = 200 + (117 * (location - 21)) + (27 * houseCount);
                houses[houseCount] = new Model("House.png", x, 152);
            } else {
                int y = 200 + (117 * (location - 31)) + (27 * houseCount);
                houses[houseCount] = new Model("House Side.png", 1253, y);
            }
            houseCount++;
            rent += 100;
            owner.updateMoney(-100);
        } else {
            buyHotel();
        }
    }

    private void buyHotel() {
        if (location < 10) {
            int x = 1163 - (117 * (location - 1));
            hotel = new Model("Hotel.png", x, 1253);
        } else if (location < 20) {
            int y = 1163 - (117 * (location - 11));
            hotel = new Model("Hotel Side.png", 152, y);
        } else if (location < 30) {
            int x = 1163 - (117 * (29 - location));
            hotel = new Model("Hotel.png", x, 152);
        } else {
            int y = 1163 - (117 * (39 - location));
            hotel = new Model("Hotel Side.png", 1253, y);
        }
        hotelBuilt = true;
        rent += 100;
        owner.updateMoney(-100);
    }

    public void sellHouse() {
        if (hotelBuilt) {
            hotelBuilt = false;
        } else {
            houseCount--;
        }
        rent -= 100;
        owner.updateMoney(50);
    }

    private Model initOwnerIcon() {
        Model ownerIcon;
        if (location < 10) {
            int x = 1168 - (117 * (location - 1));
            ownerIcon = new Model("Tokens/Blank Token Small.png", x, 1208);
        } else if (location < 20) {
            int y = 1175 - (117 * (location - 11));
            ownerIcon = new Model("Tokens/Blank Token Small.png", 206, y);
        } else if (location < 30) {
            int x = 1168 - (117 * (29 - location));
            ownerIcon = new Model("Tokens/Blank Token Small.png", x, 206);
        } else {
            int y = 1175 - (117 * (39 - location));
            ownerIcon = new Model("Tokens/Blank Token Small.png", 1194, y);
        }
        return ownerIcon;
    }

    private Button initButton() {
        Button property;
        if (location < 10) {
            String[] propertyTextures = { "Property Empty.png", "Property Highlight.png" };
            int x = 1124 - (117 * (location - 1));
            property = new Button(propertyTextures, x, 1241);
        } else if (location < 20) {
            String[] propertyTextures = { "Property Side Empty.png", "Property Side Highlight.png" };
            int y = 1124 - (117 * (location - 11));
            property = new Button(propertyTextures, -3, y);
        } else if (location < 30) {
            String[] propertyTextures = { "Property Empty.png", "Property Highlight.png" };
            int x = 187 + (117 * (location - 21));
            property = new Button(propertyTextures, x, -3);
        } else {
            String[] propertyTextures = { "Property Side Empty.png", "Property Side Highlight.png" };
            int y = 188 + (117 * (location - 31));
            property = new Button(propertyTextures, 1241, y);
        }
        return property;
    }
}
