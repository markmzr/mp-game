package realestateempire.singleplayer.board.street;

import java.awt.*;

public class StreetGraphicsData {

    private final Point xyHotel;
    private final Point[] xyHouses;
    private final String houseTexture;
    private final String hotelTexture;
    private final int location;

    public StreetGraphicsData(String[] streetGraphicsData) {
        location = Integer.parseInt(streetGraphicsData[0]);
        houseTexture = streetGraphicsData[1];
        xyHouses = new Point[4];
        for (int i = 0; i < 4; i++) {
            int xHouse = Integer.parseInt(streetGraphicsData[2 + (i * 2)]);
            int yHouse = Integer.parseInt(streetGraphicsData[3 + (i * 2)]);
            xyHouses[i] = new Point(xHouse, yHouse);
        }
        hotelTexture = streetGraphicsData[10];
        int xHotel = Integer.parseInt(streetGraphicsData[11]);
        int yHotel = Integer.parseInt(streetGraphicsData[12]);
        xyHotel = new Point(xHotel, yHotel);
    }

    public Point getXYHotel() {
        return xyHotel;
    }

    public Point[] getXYHouses() {
        return xyHouses;
    }

    public String getHouseTexture() {
        return houseTexture;
    }

    public String getHotelTexture() {
        return hotelTexture;
    }

    public int getLocation() {
        return location;
    }
}
