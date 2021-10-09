package realestateempire.singleplayer.board.property;

public class PropertyButtonData {

    private final String propertyType;
    private final String[] buttonTextures;
    private final int location;
    private final int xButton;
    private final int yButton;

    public PropertyButtonData(String[] propertyButtonData) {
        location = Integer.parseInt(propertyButtonData[0]);
        propertyType = propertyButtonData[1];
        buttonTextures = new String[]{propertyButtonData[2],
                propertyButtonData[3], propertyButtonData[2]};
        xButton = Integer.parseInt(propertyButtonData[4]);
        yButton = Integer.parseInt(propertyButtonData[5]);
    }

    public String getPropertyType() {
        return propertyType;
    }

    public String[] getButtonTextures() {
        return buttonTextures;
    }

    public int getLocation() {
        return location;
    }

    public int getXButton() {
        return xButton;
    }

    public int getYButton() {
        return yButton;
    }
}
