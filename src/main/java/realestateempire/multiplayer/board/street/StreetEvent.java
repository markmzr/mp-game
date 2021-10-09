package realestateempire.multiplayer.board.street;

public class StreetEvent {

    private StreetAction streetAction;
    private int location;

    public StreetEvent( ) { }

    public StreetEvent(StreetAction streetAction, int location) {
        this.streetAction = streetAction;
        this.location = location;
    }

    public StreetAction getStreetAction() {
        return streetAction;
    }

    public int getLocation() {
        return location;
    }
}
