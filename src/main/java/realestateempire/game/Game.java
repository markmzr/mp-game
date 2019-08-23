package realestateempire.game;

import java.util.ArrayList;
import java.util.Random;

import org.joml.Vector3f;
import realestateempire.game.Player.PlayerState;
import realestateempire.graphics.Model;
import realestateempire.screens.GameScreen;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static realestateempire.game.Game.TimedEvent.*;
import static realestateempire.game.Player.PlayerState.*;
import static realestateempire.graphics.Button.ButtonState.DISABLED;
import static realestateempire.graphics.Button.ButtonState.ENABLED;

public class Game {

    public enum TimedEvent {

        NONE, ROLL_DICE, NEXT_TURN
    }

    protected final GameScreen gameScreen;
    protected final Prompt prompt;
    protected final Board board;
    protected final Model dieLeft;
    protected final Model dieRight;
    protected final Model playerBorder;
    protected final Model gameOver;
    protected final Player[] players;
    protected Player currentPlayer;
    protected TimedEvent timedEvent;
    protected int playerTurn;
    protected int diceTotal;
    protected double eventTime;

    public Game(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        prompt = new Prompt(this);
        board = new Board(this);
        String[] dieTextures = new String[6];
        for (int i = 0; i < 6; i++) {
            dieTextures[i] = "Dice/Dice " + (i + 1) + ".png";
        }
        dieLeft = new Model(dieTextures, 657, 846);
        dieRight = new Model(dieTextures, 732, 846);
        playerBorder = new Model("Player/Player Border.png", 1481, 41);

        String[] gameOverTextures = { "Prompts/Win.png", "Prompts/Lose.png" };
        gameOver = new Model(gameOverTextures, 746, 621);
        gameOver.setVisible(false);
        players = new Player[4];
        currentPlayer = players[0];
        timedEvent = NONE;
        diceTotal = 0;
        playerTurn = 0;
        eventTime = 0;
    }

    public Game(GameScreen gameScreen, int userToken) {
        this(gameScreen);
        ArrayList<String[]> textures = new ArrayList<>();
        textures.add(new String[] { "Tokens/Hat.png", "Tokens/Hat Small.png" });
        textures.add(new String[] { "Tokens/Red Token.png", "Tokens/Red Token Small.png"});
        textures.add(new String[] { "Tokens/Blue Token.png", "Tokens/Blue Token Small.png"});
        textures.add(new String[] { "Tokens/Green Token.png", "Tokens/Blue Token Small.png"});

        players[0] = new Player(this, true, 0, "Player/Player.png", textures.get(userToken));
        textures.remove(userToken);
        for (int i = 1; i < players.length; i++) {
            players[i] = new Player(this, false, i, "Player/AI " + i + ".png", textures.get(i - 1));
        }
        currentPlayer = players[0];
    }

    public Prompt getPrompt() {
        return prompt;
    }

    Board getBoard() {
        return board;
    }

    Player[] getPlayers() {
        return players;
    }

    public Player getUser() {
        return players[0];
    }

    Player getCurrentPlayer() {
        return currentPlayer;
    }

    int getDiceTotal() {
        return diceTotal;
    }

    public void render() {
        double time = glfwGetTime();
        if (timedEvent == ROLL_DICE && time >= eventTime) {
            rollDice();
        } else if (timedEvent == NEXT_TURN && time >= eventTime) {
            setNextTurn();
        }
        for (Player player : players) {
            player.render();
        }
        currentPlayer.render();
        board.render();
        dieLeft.render();
        dieRight.render();
        playerBorder.render();
        prompt.render();
        gameOver.render();
    }

    public void cursorMoved(double xCursor, double yCursor) {
        prompt.cursorMoved(xCursor, yCursor);
        board.cursorMoved(xCursor, yCursor);
    }

    public void buttonPressed(double xCursor, double yCursor) {
        if (prompt.isVisible()) {
            prompt.buttonPressed(xCursor, yCursor);
        } else {
            board.buttonPressed(xCursor, yCursor);
        }
    }

    public void setNextTurn() {
        playerTurn = playerTurn < players.length - 1 ? ++playerTurn : 0;
        currentPlayer = players[playerTurn];
        currentPlayer.updateMoneyDelta("+$0");
        playerBorder.setPosition(1481, 41 + (165 * playerTurn));
        timedEvent = NONE;

        PlayerState playerState = currentPlayer.getPlayerState();
        if (currentPlayer.isUser()) {
            if (playerState == IN_JAIL) {
                prompt.setInJail();
            } else {
                gameScreen.setRollDiceState(ENABLED);
            }
        } else {
            if (playerState == BANKRUPT) {
                setNextTurn();
            } else {
                if (playerState == IN_JAIL) {
                    payJailFine();
                }
                timedEvent = ROLL_DICE;
                eventTime = glfwGetTime() + 2;
                dieLeft.setVisible(false);
                dieRight.setVisible(false);
            }
        }
    }

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
    }

    void playerLanded() {
        board.playerLanded();
        if (!currentPlayer.isMoving()) {
            turnCompleted();
        }
    }

    public void turnCompleted() {
        if (currentPlayer.isUser()) {
            gameScreen.setEndTurnState(ENABLED);
        } else {
            timedEvent = NEXT_TURN;
            eventTime = glfwGetTime() + 2;
        }
    }

    public void buyProperty() {
        int location = currentPlayer.getCurrentLocation();
        Property property = (Property) board.getLocation(location);
        property.buyProperty();
    }

    public void sellProperty() {
        int location = currentPlayer.getCurrentLocation();
        Property property = (Property) board.getLocation(location);
        property.sellProperty();
    }

    public void buyHouse() {
        int location = currentPlayer.getCurrentLocation();
        Property property = (Property) board.getLocation(location);
        property.buyHouse();
    }

    public void sellHouse() {
        int location = currentPlayer.getCurrentLocation();
        Property property = (Property) board.getLocation(location);
        property.sellHouse();
    }

    public void communityChest() {
        int card = new Random().nextInt(16);
        board.getEventCard().communityChest(card);
        prompt.setCommunityChest(card);
    }

    public void chance() {
        int card = new Random().nextInt(14);
        board.getEventCard().chance(card);
        prompt.setChance(card);
    }

    public void payJailFine() {
        currentPlayer.updateMoney(-50);
        currentPlayer.setPlayerState(ACTIVE);
        float x = Model.xToCoord(-84);
        float y = Model.yToCoord(-96);
        Vector3f direction = new Vector3f(x, y, 0);
        currentPlayer.getToken().movePosition(direction);
        if (currentPlayer.isUser()) {
            gameScreen.setRollDiceState(ENABLED);
        }
    }

    void playerBankrupt(boolean user) {
        if (user) {
            endGame(false);
        } else {
            int bankruptPlayers = 0;
            for (Player player : players) {
                if (player.getPlayerState() == BANKRUPT) {
                    bankruptPlayers++;
                }
            }
            if (bankruptPlayers == players.length - 1) {
                endGame(true);
            }
        }
    }

    private void endGame(boolean userWins) {
        if (!userWins) {
            gameOver.setTexture(1);
        }
        gameOver.setVisible(true);
        gameScreen.setRollDiceState(DISABLED);
        gameScreen.setEndTurnState(DISABLED);
    }
}
