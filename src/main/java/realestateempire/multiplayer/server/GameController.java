package realestateempire.multiplayer.server;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import realestateempire.multiplayer.board.CardEvent;
import realestateempire.multiplayer.board.property.PropertyEvent;
import realestateempire.multiplayer.board.street.StreetEvent;
import realestateempire.multiplayer.player.PlayerEvent;
import realestateempire.multiplayer.trade.TradeDetails;
import realestateempire.singleplayer.TurnAction;

@Controller
public class GameController {

    @Autowired
    private ArrayList<GameState> gameStates;

    @MessageMapping("/game/turn-action")
    public void handleTurnEvent(GamePrincipal principal,
                                TurnAction turnAction) {
        int gameId = principal.getGameId();
        gameStates.get(gameId).handleTurnEvent(principal, turnAction);
    }

    @MessageMapping("/game/community-chest")
    public void handleCommunityChestEvent(GamePrincipal principal,
                                          CardEvent cardEvent) {
        int gameId = principal.getGameId();
        gameStates.get(gameId).handleCommunityChest(principal, cardEvent);
    }

    @MessageMapping("/game/chance")
    public void handleChanceEvent(GamePrincipal principal, CardEvent cardEvent) {
        int gameId = principal.getGameId();
        gameStates.get(gameId).handleChance(principal, cardEvent);
    }

    @MessageMapping("/game/property-event")
    public void handlePropertyEvent(GamePrincipal principal,
                                    PropertyEvent propertyEvent) {
        int gameId = principal.getGameId();
        gameStates.get(gameId).handlePropertyEvent(principal, propertyEvent);
    }

    @MessageMapping("/game/street-event")
    public void handleStreetEvent(GamePrincipal principal,
                                  StreetEvent streetEvent) {
        int gameId = principal.getGameId();
        gameStates.get(gameId).handleStreetEvent(principal, streetEvent);
    }

    @MessageMapping("/game/trade-offer")
    public void handleTradeOffer(GamePrincipal principal,
                                 TradeDetails tradeDetails) {
        int gameId = principal.getGameId();
        gameStates.get(gameId).handleTradeOffer(tradeDetails);
    }

    @MessageMapping("/game/trade-response")
    public void handleTradeResponse(GamePrincipal principal,
                                    TradeDetails tradeDetails) {
        int gameId = principal.getGameId();
        gameStates.get(gameId).handleTradeResponse(principal, tradeDetails);
    }

    @MessageMapping("/game/pay-jail-fine")
    public void handlePayJailFine(GamePrincipal principal,
                                  PlayerEvent playerEvent) {
        int gameId = principal.getGameId();
        gameStates.get(gameId).handlePayJailFine(principal, playerEvent);
    }

    @MessageMapping("/game/declare-bankruptcy")
    public void handleDeclareBankruptcy(GamePrincipal principal,
                                        PlayerEvent playerEvent) {
        int gameId = principal.getGameId();
        gameStates.get(gameId).handleDeclareBankruptcy(principal, playerEvent);
    }

    @MessageMapping("/game/quit-game")
    public void handleQuitGame(GamePrincipal principal,
                               PlayerEvent playerEvent) {
        int gameId = principal.getGameId();
        gameStates.get(gameId).handleQuitGame(principal, playerEvent);
    }
}
