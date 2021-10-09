package realestateempire.multiplayer.player;

public class PlayerEvent {

    private int playerId;

    public PlayerEvent() { }

    public PlayerEvent(int playerId) {
        this.playerId = playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }
}
