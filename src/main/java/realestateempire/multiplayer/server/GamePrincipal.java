package realestateempire.multiplayer.server;

import java.security.Principal;

public class GamePrincipal implements Principal {

    private final String name;
    private int gameId;
    private int userId;

    public GamePrincipal() {
        name = "";
    }

    public GamePrincipal(String name) {
        this.name = name;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getGameId() {
        return gameId;
    }

    public int getUserId() {
        return userId;
    }
}
