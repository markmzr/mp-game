package realestateempire.singleplayer;

import realestateempire.graphics.Interactive;
import realestateempire.graphics.button.Button;
import realestateempire.singleplayer.board.Board;
import realestateempire.singleplayer.board.SingleplayerBoardGraphics;
import realestateempire.singleplayer.player.Player;
import realestateempire.singleplayer.player.User;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static realestateempire.singleplayer.TurnAction.END_TURN;
import static realestateempire.singleplayer.TurnAction.NONE;
import static realestateempire.singleplayer.player.PlayerState.BANKRUPT;

public abstract class Game {

    private final Board board;
    private final Player user;
    protected final Player[] players;
    protected final GameGraphics gameGraphics;
    protected TurnAction turnAction;
    protected double actionTime;
    protected int playerTurn;
    protected int diceTotal;
    protected int moveCount;

    protected Game(GameBuilder gameBuilder) {
        this.board = gameBuilder.board;
        this.players = gameBuilder.players;
        this.user = gameBuilder.user;
        this.gameGraphics = gameBuilder.gameGraphics;
        turnAction = NONE;
        actionTime = 0;
        playerTurn = 0;
        diceTotal = 0;
        moveCount = 0;
    }

    public void setTurnAction(TurnAction turnAction) {
        this.turnAction = turnAction;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    protected abstract void rollDice();

    public void render() {
        if (turnAction != NONE && glfwGetTime() >= actionTime) {
            turnAction.takeAction(this);
        }
    }

    void beginTurn() {
        turnAction = players[playerTurn].beginTurn();
        actionTime = glfwGetTime() + 1;
    }

    void moveToken() {
        board.movePlayer(players[playerTurn], moveCount);
        moveCount--;
        if (moveCount > 0) {
            actionTime = glfwGetTime() + 1;
        } else {
            turnAction = END_TURN;
            board.landPlayer(players[playerTurn], this);
            actionTime = glfwGetTime() + 2;
        }
    }

    void endTurn() {
        turnAction = continueGame() ? players[playerTurn].endTurn() : NONE;
    }

    protected void setNextTurn() {
        playerTurn = ++playerTurn % players.length;
        if (players[playerTurn].getPlayerState() == BANKRUPT) {
            setNextTurn();
        } else {
            gameGraphics.movePlayerBorder(playerTurn);
            gameGraphics.disableDice();
            beginTurn();
        }
    }

    protected boolean continueGame() {
        if (user.getPlayerState() == BANKRUPT) {
            gameGraphics.endGame(false);
            turnAction = NONE;
            return false;
        }
        int activePlayers = 0;
        for (Player player : players) {
            if (player.getPlayerState() != BANKRUPT) {
                activePlayers++;
            }
        }
        if (activePlayers == 1) {
            gameGraphics.endGame(true);
            turnAction = NONE;
            return false;
        }
        return true;
    }

    public static abstract class GameBuilder {

        protected Board board;
        protected Player[] players;
        protected User user;
        protected GameGraphics gameGraphics;

    }

    public static class GameBuilderGraphics implements Interactive {

        public final GameGraphics gameGraphics;
        public final Button rollDice;
        public final Button endTurn;
        public final SingleplayerBoardGraphics boardGraphics;

        public GameBuilderGraphics(GameGraphics gameGraphics) {
            this.gameGraphics = gameGraphics;
            String[] rollDiceTextures = {"Buttons/Roll Dice.png",
                    "Buttons/Roll Dice M.png", "Buttons/Roll Dice D.png"};
            rollDice = new Button(rollDiceTextures, 1491, 1303);
            rollDice.setEnabled(false);

            String[] endTurnTextures = {"Buttons/End Turn.png",
                    "Buttons/End Turn M.png", "Buttons/End Turn D.png"};
            endTurn = new Button(endTurnTextures, 1847, 1303);
            endTurn.setEnabled(false);
            boardGraphics = new SingleplayerBoardGraphics();
        }

        @Override
        public void setEnabled(boolean enabled) {
            rollDice.setEnabled(false);
            endTurn.setEnabled(false);
            boardGraphics.setEnabled(false);
        }

        @Override
        public void render() {
            gameGraphics.render();
            rollDice.render();
            endTurn.render();
            boardGraphics.render();
        }

        @Override
        public void cursorMoved(double xCursor, double yCursor) {
            rollDice.cursorMoved(xCursor, yCursor);
            endTurn.cursorMoved(xCursor, yCursor);
            boardGraphics.cursorMoved(xCursor, yCursor);
        }

        @Override
        public boolean buttonPressed(double xCursor, double yCursor) {
            return rollDice.buttonPressed(xCursor, yCursor)
                    || endTurn.buttonPressed(xCursor, yCursor)
                    || boardGraphics.buttonPressed(xCursor, yCursor);
        }
    }
}
