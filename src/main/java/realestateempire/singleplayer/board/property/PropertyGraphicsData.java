package realestateempire.singleplayer.board.property;

public class PropertyGraphicsData {

    private final String mortgageTexture;
    private final int location;
    private final int xOwnerIcon;
    private final int yOwnerIcon;
    private final int xMortgage;
    private final int yMortgage;

    public PropertyGraphicsData(String[] propertyGraphicsData) {
        location = Integer.parseInt(propertyGraphicsData[0]);
        xOwnerIcon = Integer.parseInt(propertyGraphicsData[1]);
        yOwnerIcon = Integer.parseInt(propertyGraphicsData[2]);
        mortgageTexture = propertyGraphicsData[3];
        xMortgage = Integer.parseInt(propertyGraphicsData[4]);
        yMortgage = Integer.parseInt(propertyGraphicsData[5]);
    }

    public String getMortgageTexture() {
        return mortgageTexture;
    }

    public int getLocation() {
        return location;
    }

    public int getXOwnerIcon() {
        return xOwnerIcon;
    }

    public int getYOwnerIcon() {
        return yOwnerIcon;
    }

    public int getXMortgage() {
        return xMortgage;
    }

    public int getYMortgage() {
        return yMortgage;
    }
}
