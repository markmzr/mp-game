package realestateempire.multiplayer.server;

import java.util.EnumMap;
import java.util.Random;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import realestateempire.multiplayer.TurnEvent;
import realestateempire.multiplayer.board.CardEvent;
import realestateempire.multiplayer.board.property.PropertyEvent;
import realestateempire.multiplayer.board.street.StreetEvent;
import realestateempire.multiplayer.player.PlayerEvent;
import realestateempire.multiplayer.trade.TradeDetails;
import realestateempire.singleplayer.TurnAction;

import static realestateempire.singleplayer.TurnAction.*;

class GameState {

    private interface TurnCommand {

        void execute();
    }

    private final SimpMessagingTemplate template;
    private final EnumMap<TurnAction, TurnCommand> turnCommands;
    private final User[] users;
    private final int gameId;
    private TurnAction turnAction;
    private int playerTurn;

    GameState(SimpMessagingTemplate template, int gameId, User[] users) {
        this.template = template;
        this.gameId = gameId;
        this.users = users;
        turnCommands = new EnumMap<>(TurnAction.class);
        turnCommands.put(BEGIN_TURN, this::beginTurn);
        turnCommands.put(ROLL_DICE, this::rollDice);
        turnCommands.put(END_TURN, this::endTurn);
        turnAction = BEGIN_TURN;
        playerTurn = 0;
    }

    private void beginTurn() {
        turnAction = ROLL_DICE;
        send(new TurnEvent(BEGIN_TURN));
    }

    private void rollDice() {
        Random random = new Random();
        int dieLeftVal = random.nextInt(6);
        int dieRightVal = random.nextInt(6);
        turnAction = END_TURN;
        send(new TurnEvent(ROLL_DICE, dieLeftVal, dieRightVal));
    }

    private void endTurn() {
        if (continueGame()) {
            playerTurn = ++playerTurn % users.length;
            if (!users[playerTurn].isActive()) {
                endTurn();
            } else {
                turnAction = ROLL_DICE;
                send(new TurnEvent(SET_NEXT_TURN));
            }
        }
    }

    private boolean continueGame() {
        int activePlayers = 0;
        for (User user : users) {
            if (user.isActive()) {
                activePlayers++;
            }
        }
        return activePlayers > 1;
    }

    void handleTurnEvent(GamePrincipal gamePrincipal, TurnAction userTurnAction) {
        users[gamePrincipal.getUserId()].setTurnAction(userTurnAction);
        boolean updateTurn = true;
        for (User user : users) {
            if (user.getTurnAction() != turnAction && user.isActive()) {
                updateTurn = false;
                break;
            }
        }
        if (updateTurn) {
            turnCommands.get(turnAction).execute();
        }
    }

    void handleCommunityChest(GamePrincipal gamePrincipal, CardEvent cardEvent) {
        users[gamePrincipal.getUserId()].setRequestedCard(true);
        boolean sendCard = true;
        for (User user : users) {
            if (!user.getRequestedCard() && user.isActive()) {
                sendCard = false;
                break;
            }
        }
        if (sendCard) {
            int card = new Random().nextInt(16);
            cardEvent.setCard(card);
            template.convertAndSend("/topic/game/" + gameId + "/community-chest", cardEvent);
            for (User user : users) {
                user.setRequestedCard(false);
            }
        }
    }

    void handleChance(GamePrincipal gamePrincipal, CardEvent cardEvent) {
        users[gamePrincipal.getUserId()].setRequestedCard(true);
        boolean sendCard = true;
        for (User user : users) {
            if (!user.getRequestedCard() && user.isActive()) {
                sendCard = false;
                break;
            }
        }
        if (sendCard) {
            int card = new Random().nextInt(14);
            cardEvent.setCard(card);
            template.convertAndSend("/topic/game/" + gameId + "/chance", cardEvent);
            for (User user : users) {
                user.setRequestedCard(false);
            }
        }
    }

    void handlePropertyEvent(GamePrincipal gamePrincipal, PropertyEvent propertyEvent) {
        send(gamePrincipal.getUserId(), "/queue/property-events", propertyEvent);
    }

    void handleStreetEvent(GamePrincipal gamePrincipal, StreetEvent streetEvent) {
        send(gamePrincipal.getUserId(), "/queue/street-events", streetEvent);
    }

    void handleTradeOffer(TradeDetails tradeDetails) {
        int receiverId = tradeDetails.getReceiver().getId();
        template.convertAndSendToUser(users[receiverId].getUsername(),
                "/queue/trade-offers", tradeDetails);
    }

    void handleTradeResponse(GamePrincipal gamePrincipal, TradeDetails tradeDetails) {
        if (tradeDetails.isTradeAccepted()) {
            send(gamePrincipal.getUserId(), "/queue/trade-responses", tradeDetails);
        } else {
            int receiverId = tradeDetails.getReceiver().getId();
            template.convertAndSendToUser(users[receiverId].getUsername(),
                    "/queue/trade-responses", tradeDetails);
        }
    }

    void handlePayJailFine(GamePrincipal gamePrincipal, PlayerEvent playerEvent) {
        int userId = gamePrincipal.getUserId();
        playerEvent.setPlayerId(userId);
        send(userId, "/queue/pay-jail-fine", playerEvent);
    }

    void handleDeclareBankruptcy(GamePrincipal gamePrincipal,
                                 PlayerEvent playerEvent) {
        int userId = gamePrincipal.getUserId();
        users[userId].setActive(false);
    }

    void handleQuitGame(GamePrincipal gamePrincipal, PlayerEvent playerEvent) {
        int userId = gamePrincipal.getUserId();
        users[userId].setActive(false);
        playerEvent.setPlayerId(userId);
        send(userId, "/queue/quit-game", playerEvent);
        if (playerTurn == userId) {
            for (User user : users) {
                if (user.isActive()) {
                    user.setTurnAction(NONE);
                }
            }
            endTurn();
        }
    }

    private void send(TurnEvent turnEvent) {
        template.convertAndSend("/topic/game/" + gameId + "/turn-event", turnEvent);
    }

    private void send(int userId, String destination, Object payload) {
        for (int i = 0; i < users.length; i++) {
            if (i != userId && users[i].isActive()) {
                template.convertAndSendToUser(users[i].getUsername(),
                        destination, payload);
            }
        }
    }
}
