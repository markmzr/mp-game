package realestateempire.multiplayer.trade;

import java.lang.reflect.Type;
import java.util.HashMap;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import realestateempire.multiplayer.server.GameSession;
import realestateempire.singleplayer.board.property.Property;
import realestateempire.singleplayer.player.Player;

public class TradeResponseHandler implements StompFrameHandler {

    private final Player[] players;
    private final HashMap<Integer, Property> locationToProperty;
    private final MultiplayerTrade mpTrade;
    private final int userId;

    public TradeResponseHandler(Player[] players,
                                HashMap<Integer, Property> locationToProperty,
                                MultiplayerTrade mpTrade, GameSession gameSession) {
        this.players = players;
        this.locationToProperty = locationToProperty;
        this.mpTrade = mpTrade;
        userId = gameSession.getUserId();
        gameSession.subscribe("/user/queue/trade-responses", this);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return TradeDetails.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        TradeDetails tradeDetails = (TradeDetails) payload;
        if (tradeDetails.getReceiver().getId() == userId) {
            mpTrade.receiveTradeOfferResponse(tradeDetails);
        } else {
            makeTrade(tradeDetails);
        }
    }

    private void makeTrade(TradeDetails tradeDetails) {
        TradePlayerDetails offerorDetails = tradeDetails.getOfferor();
        TradePlayerDetails receiverDetails = tradeDetails.getReceiver();
        Player offeror = players[offerorDetails.getId()];
        Player receiver = players[receiverDetails.getId()];
        int moneyDelta = offerorDetails.getMoney() - receiverDetails.getMoney();
        offeror.updateMoney(-1 * moneyDelta);
        receiver.updateMoney(moneyDelta);
        for (int propertyLocation : offerorDetails.getPropertyLocations()) {
            locationToProperty.get(propertyLocation).setOwner(receiver);
        }
        for (int propertyLocation : receiverDetails.getPropertyLocations()) {
            locationToProperty.get(propertyLocation).setOwner(offeror);
        }
    }
}
