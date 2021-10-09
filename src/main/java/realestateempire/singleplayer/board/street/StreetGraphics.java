package realestateempire.singleplayer.board.street;

import java.awt.*;

import realestateempire.graphics.Graphic;
import realestateempire.graphics.model.Model;

import static realestateempire.singleplayer.board.street.BuildingState.HOTEL;

public class StreetGraphics implements Graphic {

    private final Model hotel;
    private final Model[] houses;

    public StreetGraphics(StreetGraphicsData sgData) {
        Point[] xyHouses = sgData.getXYHouses();
        houses = new Model[xyHouses.length];
        for (int i = 0; i < houses.length; i++) {
            houses[i] = new Model(sgData.getHouseTexture(), xyHouses[i].x,
                    xyHouses[i].y);
            houses[i].setVisible(false);
        }
        Point xyHotel = sgData.getXYHotel();
        hotel = new Model(sgData.getHotelTexture(), xyHotel.x, xyHotel.y);
        hotel.setVisible(false);
    }

    @Override
    public void render() {
        for (Model house : houses) {
            house.render();
        }
        hotel.render();
    }

    public void enableBuildings(BuildingState buildingState) {
        for (int i = 0; i < houses.length; i++) {
            houses[i].setVisible(buildingState != HOTEL
                    && i < buildingState.getBuildingLevel());
        }
        hotel.setVisible(buildingState == HOTEL);
    }
}
