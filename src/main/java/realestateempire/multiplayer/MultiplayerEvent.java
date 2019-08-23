package realestateempire.multiplayer;

public class MultiplayerEvent {

    public enum GameEvent {

        DICE_ROLL, END_TURN, BUY_PROPERTY, SELL_PROPERTY, BUY_HOUSE, SELL_HOUSE,
        COMMUNITY_CHEST, CHANCE, PAY_JAIL_FINE, PLAYER_START, PLAYER_DISCONNECT
    }

    private GameEvent gameEvent;
    private int die1;
    private int die2;
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

    public MultiplayerEvent(GameEvent gameEvent, int die1, int die2) {
        this.gameEvent = gameEvent;
        this.die1 = die1;
        this.die2 = die2;
    }

    public GameEvent getGameEvent() {
        return gameEvent;
    }

    public int getDie1() {
        return die1;
    }

    public int getDie2() {
        return die2;
    }

    public int getPropertyLocation() {
        return propertyLocation;
    }

    public int getEventCard() {
        return eventCard;
    }
}
