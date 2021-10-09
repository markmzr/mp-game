package realestateempire.multiplayer.trade;

import java.util.PriorityQueue;
import javafx.util.Pair;

import realestateempire.graphics.Interactive;
import realestateempire.multiplayer.server.GameSession;
import realestateempire.singleplayer.board.property.Property;
import realestateempire.singleplayer.player.Player;
import realestateempire.singleplayer.trade.Trade;

public class MultiplayerTrade extends Trade {

    private final OfferTradeButtons offerTradeButtons;
    private final ReceiveTradeButtons receiveTradeButtons;
    private final TradeResponseGraphics tradeResponseGraphics;
    private final GameSession gameSession;
    private final PriorityQueue<Pair<Player, TradeDetails>> receivedTrades;
    private Interactive tradeButtons;
    private int playerId;
    private boolean receivedTrade;

    public MultiplayerTrade(Player user, GameSession gameSession) {
        super(user);
        this.gameSession = gameSession;
        offerTradeButtons = new OfferTradeButtons(this::offerTrade, this::cancelTrade);
        receiveTradeButtons = new ReceiveTradeButtons(() -> respondToTradeOffer(true),
                () -> respondToTradeOffer(false));
        tradeResponseGraphics = new TradeResponseGraphics(this::checkTradeQueue);
        tradeButtons = offerTradeButtons;
        receivedTrade = false;
        receivedTrades = new PriorityQueue<>();
    }

    @Override
    public void render() {
        if (enabled) {
            background.render();
            userGraphics.render();
            playerGraphics.render();
            tradeButtons.render();
        }
        tradeResponseGraphics.render();
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        if (enabled) {
            userGraphics.cursorMoved(xCursor, yCursor);
            playerGraphics.cursorMoved(xCursor, yCursor);
            tradeButtons.cursorMoved(xCursor, yCursor);
        }
        tradeResponseGraphics.cursorMoved(xCursor, yCursor);
    }

    @Override
    public boolean buttonPressed(double xCursor, double yCursor) {
        if (enabled) {
            return userGraphics.buttonPressed(xCursor, yCursor)
                    || playerGraphics.buttonPressed(xCursor, yCursor)
                    || tradeButtons.buttonPressed(xCursor, yCursor);
        }
        return tradeResponseGraphics.buttonPressed(xCursor, yCursor);
    }

    public void enable(Player player, int playerId) {
        if (!enabled && !tradeResponseGraphics.isEnabled()) {
            this.playerId = playerId;
            receivedTrade = false;
            updateButtons();
            super.enable(player);
        }
    }

    @Override
    protected void updateButtons() {
        tradeButtons = offerTradeButtons;
        offerTradeButtons.update(user, player);
    }

    @Override
    public boolean selectProperty(Property property) {
        boolean propertySelected = super.selectProperty(property);
        if (propertySelected) {
            tradeButtons = offerTradeButtons;
        }
        return propertySelected;
    }

    private void offerTrade() {
        tradeResponseGraphics.offeredTrade();
        TradePlayerDetails userDetails = new TradePlayerDetails(user, gameSession.getUserId());
        TradePlayerDetails playerDetails = new TradePlayerDetails(player, playerId);
        gameSession.send("trade-offer", new TradeDetails(userDetails, playerDetails));
        enabled = false;
    }

    void receiveTrade(Player offeror, TradeDetails tradeDetails) {
        if ((!enabled && !tradeResponseGraphics.isEnabled())
                || tradeDetails.getOfferor().getId() == playerId) {
            setupTrade(offeror, tradeDetails);
        } else {
            receivedTrades.add(new Pair<>(offeror, tradeDetails));
        }
    }

    private void setupTrade(Player offeror, TradeDetails tradeDetails) {
        TradePlayerDetails offerorDetails = tradeDetails.getOfferor();
        user.updateTrade(tradeDetails.getReceiver());
        player.updatePlayer(offeror);
        player.updateTrade(offerorDetails);
        tradeResponseGraphics.setEnabled(false);
        tradeButtons = receiveTradeButtons;
        playerId = offerorDetails.getId();
        receivedTrade = true;
        enabled = true;
    }

    private void respondToTradeOffer(boolean tradeAccepted) {
        if (tradeAccepted) {
            makeTrade();
        } else {
            enabled = false;
        }
        TradePlayerDetails userDetails = new TradePlayerDetails(user, gameSession.getUserId());
        TradePlayerDetails playerDetails = new TradePlayerDetails(player, playerId);
        TradeDetails tradeDetails = new TradeDetails(userDetails, playerDetails);
        tradeDetails.setTradeAccepted(tradeAccepted);
        gameSession.send("trade-response", tradeDetails);
        checkTradeQueue();
    }

    void receiveTradeOfferResponse(TradeDetails tradeDetails) {
        boolean tradeAccepted = tradeDetails.isTradeAccepted();
        if (tradeAccepted) {
            makeTrade();
        }
        tradeResponseGraphics.receiveTradeOfferResponse(tradeAccepted);
    }

    private void cancelTrade() {
        enabled = false;
        if (receivedTrade) {
            respondToTradeOffer(false);
        } else {
            checkTradeQueue();
        }
    }

    private void checkTradeQueue() {
        if (receivedTrades.size() > 0) {
            Pair<Player, TradeDetails> receivedTrade = receivedTrades.poll();
            setupTrade(receivedTrade.getKey(), receivedTrade.getValue());
        }
    }
}
