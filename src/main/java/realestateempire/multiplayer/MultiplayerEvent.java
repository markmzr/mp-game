package realestateempire.multiplayer;

public class MultiplayerEvent {

    public enum GameEvent {

        DICE_ROLL, END_TURN, BUY_PROPERTY, SELL_PROPERTY, BUY_HOUSE, SELL_HOUSE,
        COMMUNITY_CHEST, CHANCE, PAY_JAIL_FINE, PLAYER_START, PLAYER_DISCONNECT
    }

    private GameEvent gameEvent;
    private int dieLeft;
    private int dieRight;
    private int propertyLocation;
    private int eventCard;

    public MultiplayerEvent() { }

    public MultiplayerEvent(GameEvent gameEvent) {
        this.gameEvent = gameEvent;
    }

    public MultiplayerEvent(GameEvent gameEvent, int val) {
        this.gameEvent = gameEvent;
        switch (gameEvent) {
            case COMMUNITY_CHEST :
            case CHANCE :
                eventCard = val;
                break;
            default :
                propertyLocation = val;
        }
    }

    public MultiplayerEvent(GameEvent gameEvent, int dieLeft, int dieRight) {
        this.gameEvent = gameEvent;
        this.dieLeft = dieLeft;
        this.dieRight = dieRight;
    }

    public GameEvent getGameEvent() {
        return gameEvent;
    }

    public int getDieLeft() {
        return dieLeft;
    }

    public int getDieRight() {
        return dieRight;
    }

    public int getPropertyLocation() {
        return propertyLocation;
    }

    public int getEventCard() {
        return eventCard;
    }
}
