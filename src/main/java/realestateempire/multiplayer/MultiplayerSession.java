package realestateempire.multiplayer;

import java.lang.reflect.Type;

import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSession.Subscription;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import realestateempire.screens.MultiplayerScreen;

public class MultiplayerSession extends StompSessionHandlerAdapter {

    private final MultiplayerScreen mpScreen;
    private MultiplayerGame mpGame;
    private StompSession session;
    private Subscription findGame;
    private String eventDestination;
    private int userId;
    private boolean connected;

    public MultiplayerSession(MultiplayerScreen mpScreen) {
        this.mpScreen = mpScreen;
        connected = false;
    }

    public void setMultiplayerGame(MultiplayerGame mpGame) {
        this.mpGame = mpGame;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public void afterConnected(StompSession session, StompHeaders headers) {
        this.session = session;
        connected = true;
        findGame();
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        String destination = headers.getDestination();
        String findGameDest = findGame.getSubscriptionHeaders().getDestination();
        if (destination.equals(findGameDest)) {
            return MultiplayerId.class;
        } else {
            return MultiplayerEvent.class;
        }
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        String destination = headers.getDestination();
        String findGameDest = findGame.getSubscriptionHeaders().getDestination();
        if (destination.equals(findGameDest)) {
            handleFindGame((MultiplayerId) payload);
        } else {
            mpGame.handleEvent((MultiplayerEvent) payload);
        }
    }

    private void handleFindGame(MultiplayerId multiplayerId) {
        int gameId = multiplayerId.getGameId();
        userId = multiplayerId.getPlayerId();
        eventDestination = "/app/game/" + gameId + "/" + userId;

        StompHeaders headers = new StompHeaders();
        headers.setDestination("");
        findGame.unsubscribe(headers);

        int subscriptionId = userId == 0 ? 1 : 0;
        session.subscribe("/topic/game/" + gameId + "/" + subscriptionId, this);
        mpScreen.foundGame();
    }

    public void findGame() {
        if (connected) {
            findGame = session.subscribe("/user/topic/find-game", this);
        } else {
            WebSocketClient client = new StandardWebSocketClient();
            WebSocketStompClient stompClient = new WebSocketStompClient(client);
            stompClient.setMessageConverter(new MappingJackson2MessageConverter());
            stompClient.connect("ws://mp-game-app.herokuapp.com/real-estate-empire", this);
        }
    }

    public void stopFindingGame() {
        if (connected) {
            StompHeaders headers = new StompHeaders();
            headers.setDestination("/topic/find-game");
            findGame.unsubscribe(headers);
        }
    }

    public void sendEvent(MultiplayerEvent mpEvent) {
        session.send(eventDestination, mpEvent);
    }
}
