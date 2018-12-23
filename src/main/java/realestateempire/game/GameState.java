package realestateempire.game;

import java.util.Random;

import org.joml.Vector3f;

import realestateempire.graphics.Model;
import realestateempire.screens.GameScreen;

public class GameState {

    private final GameScreen gameScreen;
    private final GamePrompt gamePrompt;
    private final Board board;
    private final Model die1;
    private final Model die2;
    private final Model playerHighlight;
    private final Player[] players;
    private Model gameOverSign;
    private Player currentPlayer;
    private int die1Val;
    private int die2Val;
    private int playerTurn;
    private int bankruptcyCount;
    private boolean gameOver;

    public GameState(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        gamePrompt = new GamePrompt(this);

        String[] dieTextures = new String[6];
        for (int i = 0; i < 6; i++) {
            dieTextures[i] = "Dice/Dice" + (i + 1) + ".png";
        }
        die1 = new Model(dieTextures, 657, 846);
        die2 = new Model(dieTextures, 732, 846);

        board = new Board(this);
        playerHighlight = new Model("Player Highlight.png", 1481, 41);
        players = new Player[4];
        players[0] = new Player(this, 0);
        players[1] = new Player(this, 1);
        players[2] = new Player(this, 2);
        players[3] = new Player(this, 3);
        currentPlayer = players[0];
        die1Val = 0;
        die2Val = 0;
        playerTurn = 0;
        bankruptcyCount = 0;
        gameOver = false;
    }

    public GamePrompt getGamePrompt() {
        return gamePrompt;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getDiceRoll() {
        return die1Val + die2Val + 2;
    }

    public void render() {
        board.render();
        die1.render(die1Val);
        die2.render(die2Val);
        gamePrompt.render();
        playerHighlight.render();
        for (Player player : players) {
            player.render();
        }
        if (gameOver) {
            gameOverSign.render();
        }
    }

    public void cursorMoved(double cursorXCoord, double cursorYCoord) {
        gamePrompt.cursorMoved(cursorXCoord, cursorYCoord);
        board.cursorMoved(cursorXCoord, cursorYCoord);
    }

    public void buttonPressed(double cursorXCoord, double cursorYCoord) {
        gamePrompt.buttonPressed(cursorXCoord, cursorYCoord);
        board.buttonPressed(cursorXCoord, cursorYCoord);
    }

    public void setNextTurn() {
        playerTurn = playerTurn < players.length - 1 ? playerTurn + 1 : 0;
        currentPlayer = players[playerTurn];
        if (playerTurn == 0) {
            if (currentPlayer.getBankrupt()) {
                endGame();
            } else if (currentPlayer.getInJail()) {
                gamePrompt.setInJail();
            } else {
                gameScreen.enableRollDice();
            }
        } else {
            if (currentPlayer.getBankrupt()) {
                setNextTurn();
            } else if (currentPlayer.getInJail()) {
                payJailFine();
                rollDice();
            } else {
                rollDice();
            }
        }
        currentPlayer.updateMoneyDelta("+$0");
        Vector3f direction = new Vector3f(0f, 0f, 0f);
        direction.y = playerTurn == 0 ? playerHighlight.pixelYToCoord(495) : playerHighlight.pixelYToCoord(-165);
        playerHighlight.movePosition(direction);
    }

    public void rollDice() {
        Random random = new Random();
        die1Val = random.nextInt(6);
        die2Val = random.nextInt(6);
        int diceRoll = die1Val + die2Val + 2;
        currentPlayer.updateLocation(diceRoll);
    }

    public void playerLanded() {
        board.playerLanded();
    }

    public void turnCompleted() {
        currentPlayer.setTokenMoving(false);
        if (playerTurn == 0) {
            gameScreen.enableEndTurn();
        } else {
            setNextTurn();
        }
    }

    public void payJailFine() {
        currentPlayer.updateMoney(-50);
        currentPlayer.setInJail(false);
        if (playerTurn == 0) {
            gameScreen.enableRollDice();
        }
    }

    public void declareBankruptcy(Player player) {
        player.setBankrupt(true);
        if (player.getId() == 0) {
            endGame();
        } else {
            bankruptcyCount++;
        }
        if (bankruptcyCount == players.length - 1) {
            endGame();
        }
    }

    private void endGame() {
        if (players[0].getBankrupt()) {
            gameOverSign = new Model("Lose Sign.png", 746, 621);
        } else {
            gameOverSign = new Model("Win Sign.png", 746, 621);
        }
        gameOver = true;
        gameScreen.disableRollDice();
        gameScreen.disableEndTurn();
    }
}
