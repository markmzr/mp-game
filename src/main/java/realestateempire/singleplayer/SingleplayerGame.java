package realestateempire.singleplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import realestateempire.graphics.button.Button;
import realestateempire.screens.GameScreen;
import realestateempire.singleplayer.board.Board.BoardBuilder;
import realestateempire.singleplayer.deed.ViewDeed;
import realestateempire.singleplayer.player.*;
import realestateempire.singleplayer.player.User.UserGraphics;
import realestateempire.singleplayer.trade.SingleplayerTrade;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static realestateempire.singleplayer.TurnAction.MOVE_TOKEN;

public class SingleplayerGame extends Game {

    private SingleplayerGame(SingleplayerGameBuilder spGameBuilder) {
        super(spGameBuilder);
        beginTurn();
    }

    @Override
    protected void rollDice() {
        Random random = new Random();
        int dieLeft = random.nextInt(6);
        int dieRight = random.nextInt(6);
        gameGraphics.enableDice(dieLeft, dieRight);
        diceTotal = dieLeft + dieRight + 2;
        moveCount = diceTotal;
        turnAction = MOVE_TOKEN;
        actionTime = glfwGetTime() + 1;
    }

    public static class SingleplayerGameBuilder extends GameBuilder {

        private final GameBuilderGraphics gameBuilderGraphics;

        public SingleplayerGameBuilder(Token userToken,
                                       GameBuilderGraphics gameBuilderGraphics,
                                       GameScreen gameScreen) {
            this.gameGraphics = gameBuilderGraphics.gameGraphics;
            this.gameBuilderGraphics = gameBuilderGraphics;
            user = initUser(userToken, gameBuilderGraphics, gameScreen);
            ViewDeed viewDeed = new ViewDeed();
            SingleplayerTrade spTrade = new SingleplayerTrade(user);
            players = initPlayers(userToken, spTrade, viewDeed, gameScreen);
            board = new BoardBuilder(players, spTrade, viewDeed,
                    gameBuilderGraphics.boardGraphics).build();
            gameScreen.add(viewDeed, 1);
            gameScreen.add(spTrade, 1);
        }

        private User initUser(Token userToken,
                              GameBuilderGraphics gameBuilderGraphics,
                              GameScreen gameScreen) {
            String[] tradePlayerTextures = { "Buttons/Trade Player.png",
                    "Buttons/Trade Player M.png", "Buttons/Trade Player.png" };
            Button tradePlayer = new Button(tradePlayerTextures, 1481, 41);
            tradePlayer.setEnabled(false);
            PlayerGraphics playerGraphics = new PlayerGraphics(0, "Player",
                    userToken, tradePlayer);
            User user = new User("Player", userToken, playerGraphics);
            UserGraphics userGraphics = user.new UserGraphics(
                    gameBuilderGraphics.rollDice, gameBuilderGraphics.endTurn);
            gameScreen.add(playerGraphics);
            gameScreen.add(userGraphics);
            return user;
        }

        private Player[] initPlayers(Token userToken, SingleplayerTrade spTrade,
                                     ViewDeed viewDeed, GameScreen gameScreen) {
            Player[] players = new Player[4];
            players[0] = user;
            ArrayList<Token> tokens = new ArrayList<>(Arrays.asList(Token.values()));
            tokens.remove(userToken.ordinal());
            String[] tradePlayerTextures = { "Buttons/Trade Player.png",
                    "Buttons/Trade Player M.png", "Buttons/Trade Player.png" };
            for (int i = 1; i < players.length; i++) {
                String name = "AI " + i;
                Token token = tokens.get(i - 1);
                Button tradePlayer = new Button(tradePlayerTextures, 1481, 41 + (i * 165));
                PlayerGraphics playerGraphics = new PlayerGraphics(i, name, token, tradePlayer);
                players[i] = new AIPlayer(name, token, playerGraphics);
                addTradeButtonAction(tradePlayer, players[i], spTrade, viewDeed);
                gameScreen.add(playerGraphics);
            }
            return players;
        }

        private void addTradeButtonAction(Button tradePlayer, Player player,
                                          SingleplayerTrade spTrade, ViewDeed viewDeed) {
            tradePlayer.addAction(() -> {
                spTrade.enable(player);
                viewDeed.setEnabled(false);
            });
        }

        private void addButtonActions(SingleplayerGame spGame) {
            gameBuilderGraphics.rollDice.addAction(() -> {
                spGame.rollDice();
                gameBuilderGraphics.rollDice.setEnabled(false);
            });
            gameBuilderGraphics.endTurn.addAction(() -> {
                gameGraphics.disableDice();
                spGame.setNextTurn();
                gameBuilderGraphics.endTurn.setEnabled(false);
            });
        }

        public SingleplayerGame build() {
            SingleplayerGame spGame = new SingleplayerGame(this);
            addButtonActions(spGame);
            return spGame;
        }
    }
}
