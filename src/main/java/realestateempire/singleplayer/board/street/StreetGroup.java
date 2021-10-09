package realestateempire.singleplayer.board.street;

import java.util.ArrayList;

import static realestateempire.singleplayer.board.property.PropertyState.MORTGAGED;

public enum StreetGroup {

    BROWN("BROWN", 2),
    GREEN("GREEN", 3),
    LIGHT_BLUE("LIGHT_BLUE", 3),
    ORANGE("ORANGE", 3),
    PURPLE("PURPLE", 3),
    RED("RED", 3),
    DARK_BLUE("DARK_BLUE", 3),
    GOLD("GOLD", 2);

    private final String color;
    private final int streetCount;

    StreetGroup(String color, int streetCount) {
        this.color = color;
        this.streetCount = streetCount;
    }

    public boolean playerHasMonopoly(ArrayList<Street> playerStreets) {
        int playerStreetCount = 0;
        for (Street street : playerStreets) {
            if (street.getStreetGroup() == this) {
                playerStreetCount++;
            }
        }
        return playerStreetCount == streetCount;
    }

    public boolean canBuyHouse(Street street, ArrayList<Street> playerStreets) {
        int playerStreetCount = 0;
        boolean canBuyHouse = true;
        for (Street s : playerStreets) {
            if (s.getStreetGroup() == this) {
                if (s.getPropertyState() == MORTGAGED
                        || s.getBuildingLevel() > street.getBuildingLevel()) {
                    canBuyHouse = false;
                    break;
                }
                playerStreetCount++;
            }
        }
        return playerStreetCount == streetCount && canBuyHouse;
    }

    public boolean canSellHouse(Street street, ArrayList<Street> playerStreets) {
        int playerStreetCount = 0;
        boolean canSellHouse = true;
        for (Street s : playerStreets) {
            if (s.getStreetGroup() == this) {
                if (street.getBuildingLevel() < s.getBuildingLevel()) {
                    canSellHouse = false;
                    break;
                }
                playerStreetCount++;
            }
        }
        return playerStreetCount == streetCount && canSellHouse;
    }

    public boolean hasBuildings(ArrayList<Street> playerStreets) {
        boolean hasBuildings = false;
        for (Street s : playerStreets) {
            if (s.getStreetGroup() == this && s.getBuildingLevel() > 0) {
                hasBuildings = true;
                break;
            }
        }
        return hasBuildings;
    }
}
