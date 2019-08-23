package realestateempire.multiplayer;

import java.util.HashMap;
import java.util.Random;

import org.joml.Vector3f;
import realestateempire.game.Game;
import realestateempire.game.Player;
import realestateempire.game.Player.PlayerState;
import realestateempire.game.Property;
import realestateempire.graphics.Model;
import realestateempire.multiplayer.MultiplayerEvent.GameEvent;
import realestateempire.screens.GameScreen;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static realestateempire.game.Game.TimedEvent.*;
import static realestateempire.game.Player.PlayerState.*;
import static realestateempire.graphics.Button.ButtonState.DISABLED;
import static realestateempire.graphics.Button.ButtonState.ENABLED;
import static realestateempire.multiplayer.MultiplayerEvent.GameEvent.*;

public class MultiplayerGame extends Game {

    private final MultiplayerSession mpSession;
    private final MultiplayerEventHandler mpEventHandler;
    private final int userId;

    public MultiplayerGame(GameScreen gameScreen, MultiplayerSession mpSession) {
        super(gameScreen);
        this.mpSession = mpSession;
        mpEventHandler = new MultiplayerEventHandler();
        userId = mpSession.getUserId();

        String[] player1Textures = { "Tokens/Hat.png", "Tokens/Hat Small.png" };
        players[0] = new Player(this, true, 0, "Player/Player 1.png", player1Textures);

        String[] player2Textures = { "Tokens/Red Token.png", "Tokens/Red Token Small.png"};
        players[1] = new Player(this, true, 1, "Player/Player 2.png", player2Textures);

        String[] ai1Textures = { "Tokens/Blue Token.png", "Tokens/Blue Token Small.png"};
        players[2] = new Player(this, false, 2, "Player/AI 1.png", ai1Textures);

        String[] ai2Textures = { "Tokens/Green Token.png", "Tokens/Blue Token Small.png"};
        players[3] = new Player(this, false, 3, "Player/AI 2.png", ai2Textures);
        currentPlayer = players[0];
    }

    @Override
    public Player getUser() {
        return players[mpSession.getUserId()];
    }

    @Override
    public void setNextTurn() {
        if (playerTurn == userId) {
            sendEvent(new MultiplayerEvent(END_TURN));
        }
        playerTurn = playerTurn < players.length - 1 ? ++playerTurn : 0;
        currentPlayer = players[playerTurn];
        currentPlayer.updateMoneyDelta("+$0");
        playerBorder.setPosition(1481, 41 + (165 * playerTurn));
        timedEvent = NONE;

        PlayerState playerState = currentPlayer.getPlayerState();
        if (playerTurn == userId) {
            if (playerState == IN_JAIL) {
                prompt.setInJail();
            } else {
                gameScreen.setRollDiceState(ENABLED);
            }
        } else if (!currentPlayer.isUser()) {
            if (userId == 0) {
                if (playerState == BANKRUPT) {
                    setNextTurn();
                } else {
                    if (playerState == IN_JAIL) {
                        payJailFine();
                    }
                    timedEvent = ROLL_DICE;
                    eventTime = glfwGetTime() + 2;
                }
            }
            dieLeft.setVisible(false);
            dieRight.setVisible(false);
        }
    }

    @Override
    public void rollDice() {
        Random random = new Random();
        int dieLeftVal = random.nextInt(6);
        int dieRightVal = random.nextInt(6);
        dieLeft.setTexture(dieLeftVal);
        dieRight.setTexture(dieRightVal);
        dieLeft.setVisible(true);
        dieRight.setVisible(true);
        
        diceTotal = dieLeftVal + dieRightVal + 2;
        currentPlayer.updateLocation(diceTotal);
        timedEvent = NONE;
        sendEvent(new MultiplayerEvent(DICE_ROLL, dieLeftVal, dieRightVal));
    }

    @Override
    public void turnCompleted() {
        if (playerTurn == userId) {
            gameScreen.setEndTurnState(ENABLED);
        } else if (playerTurn >= 2){
            setNextTurn();
        }
    }

    @Override
    public void buyProperty() {
        int location = currentPlayer.getCurrentLocation();
        Property property = (Property) board.getLocation(location);
        property.buyProperty();
        sendEvent(new MultiplayerEvent(BUY_PROPERTY, location));
    }

    @Override
    public void sellProperty() {
        int location = currentPlayer.getCurrentLocation();
        Property property = (Property) board.getLocation(location);
        property.sellProperty();
        sendEvent(new MultiplayerEvent(SELL_PROPERTY, location));
    }

    @Override
    public void buyHouse() {
        int location = currentPlayer.getCurrentLocation();
        Property property = (Property) board.getLocation(location);
        property.buyHouse();
        sendEvent(new MultiplayerEvent(BUY_HOUSE, location));
    }

    @Override
    public void sellHouse() {
        int location = currentPlayer.getCurrentLocation();
        Property property = (Property) board.getLocation(location);
        property.sellHouse();
        sendEvent(new MultiplayerEvent(SELL_HOUSE, location));
    }


    @Override
    public void communityChest() {
        if (playerTurn == userId || (userId == 0 && !currentPlayer.isUser())) {
            int card = new Random().nextInt(16);
            board.getEventCard().communityChest(card);
            prompt.setCommunityChest(card);
            sendEvent(new MultiplayerEvent(COMMUNITY_CHEST, card));
        }
    }

    @Override
    public void chance() {
        if (playerTurn == userId || (userId == 0 && !currentPlayer.isUser())) {
            int card = new Random().nextInt(14);
            board.getEventCard().chance(card);
            prompt.setChance(card);
            sendEvent(new MultiplayerEvent(CHANCE, card));
        }
    }

    @Override
    public void payJailFine() {
        currentPlayer.updateMoney(-50);
        currentPlayer.setPlayerState(ACTIVE);
        float x = Model.xToCoord(-84);
        float y = Model.yToCoord(-96);
        Vector3f direction = new Vector3f(x, y, 0);
        currentPlayer.getToken().movePosition(direction);
        if (playerTurn == userId || (userId == 0 && !currentPlayer.isUser())) {
            sendEvent(new MultiplayerEvent(PAY_JAIL_FINE));
            gameScreen.setRollDiceState(ENABLED);
        }
    }

    public void sendEvent(MultiplayerEvent mpEvent) {
        mpSession.sendEvent(mpEvent);
    }

    void handleEvent(MultiplayerEvent mpEvent) {
        mpEventHandler.handleEvent(mpEvent);
    }

    private interface EventHandler {

        void handleEvent();
    }

    private class MultiplayerEventHandler {

        private final HashMap<GameEvent, EventHandler> eventHandler;
        private MultiplayerEvent mpEvent;

        private MultiplayerEventHandler() {
            eventHandler = new HashMap<>();
            eventHandler.put(DICE_ROLL, this::rollDice);
            eventHandler.put(END_TURN, this::endTurn);
            eventHandler.put(BUY_PROPERTY, this::buyProperty);
            eventHandler.put(SELL_PROPERTY, this::sellProperty);
            eventHandler.put(BUY_HOUSE, this::buyHouse);
            eventHandler.put(SELL_HOUSE, this::sellHouse);
            eventHandler.put(COMMUNITY_CHEST, this::communityChest);
            eventHandler.put(CHANCE, this::chance);
            eventHandler.put(PAY_JAIL_FINE, this::jailFine);
            eventHandler.put(PLAYER_START, this::startGame);
            eventHandler.put(PLAYER_DISCONNECT, this::playerDisconnect);
        }

        private void handleEvent(MultiplayerEvent mpEvent) {
            this.mpEvent = mpEvent;
            GameEvent gameEvent = mpEvent.getGameEvent();
            eventHandler.get(gameEvent).handleEvent();
        }

        private void rollDice() {
            int dieLeftVal = mpEvent.getDieLeft();
            int dieRightVal = mpEvent.getDieRight();
            dieLeft.setTexture(dieLeftVal);
            dieRight.setTexture(dieRightVal);
            dieLeft.setVisible(true);
            dieRight.setVisible(true);

            diceTotal = dieLeftVal + dieRightVal + 2;
            currentPlayer.updateLocation(diceTotal);
            timedEvent = NONE;
        }

        private void endTurn() {
            setNextTurn();
        }

        private void buyProperty() {
            int location = mpEvent.getPropertyLocation();
            Property property = (Property) board.getLocation(location);
            property.buyProperty();
        }

        private void sellProperty() {
            int location = mpEvent.getPropertyLocation();
            Property property = (Property) board.getLocation(location);
            property.sellProperty();
        }

        private void buyHouse() {
            int location = mpEvent.getPropertyLocation();
            Property property = (Property) board.getLocation(location);
            property.buyHouse();
        }

        private void sellHouse() {
            int location = mpEvent.getPropertyLocation();
            Property property = (Property) board.getLocation(location);
            property.sellHouse();
        }

        private void communityChest() {
            int card = mpEvent.getEventCard();
            board.getEventCard().communityChest(card);
        }

        private void chance() {
            int card = mpEvent.getEventCard();
            board.getEventCard().chance(card);
        }

        private void jailFine() {
            payJailFine();
        }

        private void startGame() {
            System.out.println("Opponent started the game.");
        }

        private void playerDisconnect() {
            gameScreen.setRollDiceState(DISABLED);
            gameScreen.setEndTurnState(DISABLED);
            currentPlayer.setMoving(false);
            prompt.setPlayerDisconnect();
        }
    }
}
