package realestateempire.multiplayer;

public class MultiplayerId {

    private int gameId;
    private int playerId;

    public MultiplayerId() { }

    MultiplayerId(int gameId, int playerId) {
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public int getGameId() {
        return gameId;
    }

    public int getPlayerId() {
        return playerId;
    }
}
