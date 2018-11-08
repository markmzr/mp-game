import org.joml.Vector3f;

import java.util.Random;

public class GameState {

    private GameScreen gameScreen;
    private Board board;
    private Player[] players;
    private Player currentPlayer;
    private GL2DObject playerHighlight;
    private int firstDieVal;
    private int secondDieVal;
    private int playerTurn;
    private boolean turnCompleted;

    public GameState(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        board = new Board();

        players = new Player[4];
        players[0] = new Player("Player", 0, "Tokens/Hat.png");
        players[1] = new Player("AI 1", 1, "Tokens/AI 1 Token.png");
        players[2] = new Player("AI 2", 2, "Tokens/AI 2 Token.png");
        players[3] = new Player("AI 3", 3, "Tokens/AI 3 Token.png");
        currentPlayer = players[0];

        playerHighlight = new GL2DObject("Player Highlight.png", 1481, 41, 1038, 134);
        firstDieVal = 0;
        secondDieVal = 0;
        playerTurn = 0;
        turnCompleted = false;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getFirstDieVal() {
        return firstDieVal;
    }

    public int getSecondDieVal() {
        return secondDieVal;
    }

    public void setTurnCompleted(boolean turnCompleted) {
        this.turnCompleted = turnCompleted;

        if (turnCompleted) {
            if (playerTurn == 0) {
                gameScreen.enableEndTurn();
            }
            else {
                setNextTurn();
            }
        }
    }

    public void render() {
        if (currentPlayer.getTokenMoving()
                && currentPlayer.getCurrentPosition() == currentPlayer.getNewPosition()) {
            board.playerLanded(this);
            currentPlayer.setTokenMoving(false);
        }

        for (Player player : players) {
            player.render();
        }
        playerHighlight.render();
    }

    public void rollDice() {
        Random random = new Random();
        firstDieVal = random.nextInt(6);
        secondDieVal = random.nextInt(6);
        int diceRoll = firstDieVal + secondDieVal + 2;

        currentPlayer.updatePosition(diceRoll);
        currentPlayer.updateMoneyDifference("+$0");
    }

    public void setNextTurn() {
        if (playerTurn < players.length - 1) {
            playerTurn++;
            currentPlayer = players[playerTurn];
            playerHighlight.movePosition(new Vector3f(0f, -0.22916f, 0f));
            rollDice();
        }
        else {
            playerTurn = 0;
            currentPlayer = players[0];
            playerHighlight.movePosition(new Vector3f(0f, 0.6875f, 0f));
            gameScreen.enableRollDice();
        }

        turnCompleted = false;
    }
}
