package realestateempire.multiplayer.server;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;

import realestateempire.singleplayer.player.Player;

public class GameSession {

    private final StompSession session;
    private final String destination;
    private Player user;
    private int gameId;
    private int userId;

    public GameSession(StompSession session) {
        this.session = session;
        destination = "/app/game/";
    }

    public void setUser(Player user) {
        this.user = user;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getGameId() {
        return gameId;
    }

    public int getUserId() {
        return userId;
    }

    public Subscription subscribe(String destination, StompFrameHandler handler) {
        return session.subscribe(destination, handler);
    }

    public void send(String eventType, Object event) {
        session.send(destination + eventType, event);
    }

    public void sendPlayerEvent(Player player, String eventType, Object event) {
        if (player == user) {
            session.send(destination + eventType, event);
        }
    }
}
