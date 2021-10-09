package realestateempire.multiplayer;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import realestateempire.graphics.button.Button;
import realestateempire.multiplayer.board.MultiplayerBoard.MultiplayerBoardBuilder;
import realestateempire.multiplayer.board.MultiplayerBoardGraphics;
import realestateempire.multiplayer.player.MultiplayerPlayer;
import realestateempire.multiplayer.player.MultiplayerUser;
import realestateempire.multiplayer.player.PlayerEvent;
import realestateempire.multiplayer.server.GameSession;
import realestateempire.multiplayer.trade.MultiplayerTrade;
import realestateempire.screens.GameScreen;
import realestateempire.singleplayer.Game;
import realestateempire.singleplayer.deed.ViewDeed;
import realestateempire.singleplayer.player.Player;
import realestateempire.singleplayer.player.PlayerGraphics;
import realestateempire.singleplayer.player.Token;
import realestateempire.singleplayer.player.User;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static realestateempire.singleplayer.TurnAction.*;

public class MultiplayerGame extends Game implements StompFrameHandler {

    private TurnEvent turnEvent;

    private MultiplayerGame(MultiplayerGameBuilder mpGameBuilder) {
        super(mpGameBuilder);
        String destination = "/topic/game/" + mpGameBuilder.gameSession.getGameId()
                + "/turn-event";
        mpGameBuilder.gameSession.subscribe(destination, this);
        mpGameBuilder.gameSession.subscribe("/user/queue/pay-jail-fine", this);
        mpGameBuilder.gameSession.subscribe("/user/queue/quit-game", this);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        String destination = headers.getDestination();
        if (destination.endsWith("turn-event")) {
            return TurnEvent.class;
        } else {
            return PlayerEvent.class;
        }
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        String destination = headers.getDestination();
        if (destination.endsWith("turn-event")) {
            turnEvent = (TurnEvent) payload;
            turnEvent.getTurnAction().takeAction(this);
        } else if (destination.endsWith("pay-jail-fine")) {
            PlayerEvent playerEvent = (PlayerEvent) payload;
            players[playerEvent.getPlayerId()].payJailFine();
        } else {
            PlayerEvent playerEvent = (PlayerEvent) payload;
            players[playerEvent.getPlayerId()].declareBankruptcy();
            continueGame();
        }
    }

    @Override
    protected void rollDice() {
        int dieLeft = turnEvent.getDieLeft();
        int dieRight = turnEvent.getDieRight();
        gameGraphics.enableDice(dieLeft, dieRight);
        diceTotal = dieLeft + dieRight + 2;
        moveCount = diceTotal;
        turnAction = MOVE_TOKEN;
        actionTime = glfwGetTime() + 1;
    }

    public static class MultiplayerGameBuilder extends GameBuilder {

        private final GameBuilderGraphics gameBuilderGraphics;
        private final GameSession gameSession;

        public MultiplayerGameBuilder(GameBuilderGraphics gameBuilderGraphics,
                                      GameScreen gameScreen, GameSession gameSession) {
            this.gameGraphics = gameBuilderGraphics.gameGraphics;
            this.gameBuilderGraphics = gameBuilderGraphics;
            this.gameSession = gameSession;
            user = initUser(gameBuilderGraphics, gameScreen);
            MultiplayerTrade mpTrade = new MultiplayerTrade(user, gameSession);
            ViewDeed viewDeed = new ViewDeed();
            players = initPlayers(mpTrade, viewDeed, gameScreen);
            MultiplayerBoardGraphics boardGraphics = new MultiplayerBoardGraphics();
            board = new MultiplayerBoardBuilder(players, mpTrade, viewDeed,
                    boardGraphics, gameSession).build();
            gameBuilderGraphics.rollDice.addAction(this::rollDiceAction);
            gameBuilderGraphics.endTurn.addAction(this::endTurnAction);
            gameScreen.add(viewDeed, 1);
            gameScreen.add(mpTrade, 2);
            gameScreen.add(boardGraphics);
        }

        private User initUser(GameBuilderGraphics gameBuilderGraphics, GameScreen gameScreen) {
            int userId = gameSession.getUserId();
            String name = "Player " + (userId + 1);
            Token token = Token.values()[userId];
            String[] tradePlayerTextures = { "Buttons/Trade Player.png",
                    "Buttons/Trade Player M.png", "Buttons/Trade Player.png" };
            Button tradePlayer = new Button(tradePlayerTextures, 1481,
                    41 + (userId * 165));
            tradePlayer.setEnabled(false);
            PlayerGraphics playerGraphics = new PlayerGraphics(userId, name, token, tradePlayer);
            MultiplayerUser mpUser = new MultiplayerUser(name, token, playerGraphics, gameSession);
            User.UserGraphics userGraphics = mpUser.new UserGraphics(
                    gameBuilderGraphics.rollDice, gameBuilderGraphics.endTurn);
            gameSession.setUser(mpUser);
            gameScreen.add(playerGraphics);
            gameScreen.add(userGraphics);
            return mpUser;
        }

        private Player[] initPlayers(MultiplayerTrade mpTrade, ViewDeed viewDeed,
                                     GameScreen gameScreen) {
            Player[] players = new Player[4];
            int userId = gameSession.getUserId();
            players[userId] = user;
            String[] tradePlayerTextures = { "Buttons/Trade Player.png",
                    "Buttons/Trade Player M.png", "Buttons/Trade Player.png" };
            for (int i = 0; i < players.length; i++) {
                if (i != userId) {
                    String name = "Player " + (i + 1);
                    Token token = Token.values()[i];
                    Button tradePlayer = new Button(tradePlayerTextures, 1481, 41 + (i * 165));
                    PlayerGraphics playerGraphics = new PlayerGraphics(i, name, token, tradePlayer);
                    players[i] = new MultiplayerPlayer(name, token, playerGraphics, gameSession);
                    addTradeButtonAction(tradePlayer, players[i], i, mpTrade, viewDeed);
                    gameScreen.add(playerGraphics);
                }
            }
            return players;
        }

        private void addTradeButtonAction(Button tradePlayer, Player player,
                                          int playerId, MultiplayerTrade mpTrade,
                                          ViewDeed viewDeed) {
            tradePlayer.addAction(() -> {
                mpTrade.enable(player, playerId);
                viewDeed.setEnabled(false);
            });
        }

        private void rollDiceAction() {
            gameBuilderGraphics.rollDice.setEnabled(false);
            gameSession.send("turn-action", ROLL_DICE);
        }

        private void endTurnAction() {
            gameBuilderGraphics.endTurn.setEnabled(false);
            gameSession.send("turn-action", END_TURN);
        }

        public MultiplayerGame build() {
            return new MultiplayerGame(this);
        }
    }
}
