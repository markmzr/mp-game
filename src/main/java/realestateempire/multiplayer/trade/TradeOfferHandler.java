package realestateempire.multiplayer.trade;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import realestateempire.multiplayer.server.GameSession;
import realestateempire.singleplayer.board.property.Property;
import realestateempire.singleplayer.player.Player;

public class TradeOfferHandler implements StompFrameHandler {

    private final Player[] players;
    private final HashMap<Integer, Property> locationToProperty;
    private final MultiplayerTrade mpTrade;

    public TradeOfferHandler(Player[] players,
                             HashMap<Integer, Property> locationToProperty,
                             MultiplayerTrade mpTrade, GameSession gameSession) {
        this.players = players;
        this.locationToProperty = locationToProperty;
        this.mpTrade = mpTrade;
        gameSession.subscribe("/user/queue/trade-offers", this);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return TradeDetails.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        TradeDetails tradeDetails = (TradeDetails) payload;
        receiveTrade(tradeDetails);
    }

    private void receiveTrade(TradeDetails tradeDetails) {
        TradePlayerDetails offeror = tradeDetails.getOfferor();
        TradePlayerDetails receiver = tradeDetails.getReceiver();
        offeror.setProperties(getTradeProperties(offeror.getPropertyLocations()));
        receiver.setProperties(getTradeProperties(receiver.getPropertyLocations()));
        Player offeringPlayer = players[offeror.getId()];
        mpTrade.receiveTrade(offeringPlayer, tradeDetails);
    }

    private ArrayList<Property> getTradeProperties(ArrayList<Integer> propertyLocations) {
        ArrayList<Property> properties = new ArrayList<>();
        for (int location : propertyLocations) {
            properties.add(locationToProperty.get(location));
        }
        return properties;
    }
}
