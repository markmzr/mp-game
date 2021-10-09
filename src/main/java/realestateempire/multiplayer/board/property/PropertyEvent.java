package realestateempire.multiplayer.board.property;


public class PropertyEvent {

    private PropertyAction propertyAction;
    private int location;

    public PropertyEvent( ) { }

    public PropertyEvent(PropertyAction propertyAction, int location) {
        this.propertyAction = propertyAction;
        this.location = location;
    }

    public PropertyAction getPropertyAction() {
        return propertyAction;
    }

    public int getLocation() {
        return location;
    }
}
