package realestateempire.multiplayer.board.street;

import java.lang.reflect.Type;
import java.util.HashMap;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import realestateempire.multiplayer.server.GameSession;
import realestateempire.singleplayer.board.street.Street;

public class StreetEventHandler implements StompFrameHandler {

    private final HashMap<Integer, Street> locationToStreet;

    public StreetEventHandler(HashMap<Integer, Street> locationToStreet,
                              GameSession gameSession) {
        this.locationToStreet = locationToStreet;
        gameSession.subscribe("/user/queue/street-events", this);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return StreetEvent.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        StreetEvent streetEvent = (StreetEvent) payload;
        StreetAction streetAction = streetEvent.getStreetAction();
        Street street = locationToStreet.get(streetEvent.getLocation());
        streetAction.takeAction(street);
    }
}
