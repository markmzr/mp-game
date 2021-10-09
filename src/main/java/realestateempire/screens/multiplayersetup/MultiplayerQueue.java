package realestateempire.screens.multiplayersetup;

import java.lang.reflect.Type;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import realestateempire.multiplayer.server.GamePrincipal;
import realestateempire.multiplayer.server.GameSession;

class MultiplayerQueue extends StompSessionHandlerAdapter {

    private final QueueGraphics queueGraphics;
    private GameSession gameSession;
    private Subscription gameQueue;
    private boolean connected;

    MultiplayerQueue(QueueGraphics queueGraphics) {
        this.queueGraphics = queueGraphics;
        connected = false;
    }

    GameSession getGameSession() {
        return gameSession;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders headers) {
        gameSession = new GameSession(session);
        connected = true;
        queueForGame();
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return GamePrincipal.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        GamePrincipal principal = (GamePrincipal) payload;
        receivedGame(principal);
    }

    void queueForGame() {
        if (connected) {
            gameQueue = gameSession.subscribe("/user/topic/game-queue", this);
        } else {
            WebSocketClient client = new StandardWebSocketClient();
            WebSocketStompClient stompClient = new WebSocketStompClient(client);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());
            stompClient.connect("ws://localhost:8080/real-estate-empire", this);
        }
    }

    void dequeue() {
        if (connected) {
            gameQueue.unsubscribe();
        }
    }

    private void receivedGame(GamePrincipal principal) {
        int gameId = principal.getGameId();
        gameSession.setGameId(gameId);
        int userId = principal.getUserId();
        gameSession.setUserId(userId);
        queueGraphics.receivedGame(userId);
        gameQueue.unsubscribe();
    }
}
