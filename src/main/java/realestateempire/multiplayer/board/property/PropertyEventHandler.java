package realestateempire.multiplayer.board.property;

import java.lang.reflect.Type;
import java.util.HashMap;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import realestateempire.multiplayer.server.GameSession;
import realestateempire.singleplayer.board.property.Property;

public class PropertyEventHandler implements StompFrameHandler {

    private final HashMap<Integer, Property> locationToProperty;

    public PropertyEventHandler(HashMap<Integer, Property> locationToProperty,
                                GameSession gameSession) {
        this.locationToProperty = locationToProperty;
        gameSession.subscribe("/user/queue/property-events", this);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return PropertyEvent.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        PropertyEvent propertyEvent = (PropertyEvent) payload;
        PropertyAction propertyAction = propertyEvent.getPropertyAction();
        Property property = locationToProperty.get(propertyEvent.getLocation());
        propertyAction.takeAction(property);
    }
}
