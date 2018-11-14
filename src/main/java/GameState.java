import java.util.Random;

public class GameState {

    private GameScreen gameScreen;
    private Board board;
    private Player[] players;
    private Player currentPlayer;
    private int die1Val;
    private int die2Val;
    private int playerTurn;
    private int eventCard;

    public GameState(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        board = new Board(this);

        players = new Player[4];
        players[0] = new Player(board, "Player", 0, "Tokens/Hat.png");
        players[1] = new Player(board, "AI 1", 1, "Tokens/AI 1 Token.png");
        players[2] = new Player(board, "AI 2", 2, "Tokens/AI 2 Token.png");
        players[3] = new Player(board, "AI 3", 3, "Tokens/AI 3 Token.png");
        currentPlayer = players[0];

        die1Val = 0;
        die2Val = 0;
        playerTurn = 0;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void setEventCard(int eventCard) {
        this.eventCard = eventCard;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getDie1Val() {
        return die1Val;
    }

    public int getDie2Val() {
        return die2Val;
    }

    public int getEventCard() {
        return eventCard;
    }

    public void render() {
        for (Player player : players) {
            player.render();
        }
    }

    public void rollDice() {
        Random random = new Random();
        die1Val = random.nextInt(6);
        die2Val = random.nextInt(6);
        int diceRoll = die1Val + die2Val + 2;

        currentPlayer.updatePosition(diceRoll);
        currentPlayer.updateMoneyDifference("+$0");
    }

    public void setNextTurn() {
        if (playerTurn < players.length - 1) {
            playerTurn++;
            currentPlayer = players[playerTurn];
            gameScreen.setPlayerHighlight(playerTurn);

            if (currentPlayer.getInJail()) {
                payFine();
            }
            rollDice();
        } else {
            playerTurn = 0;
            currentPlayer = players[0];
            gameScreen.setPlayerHighlight(playerTurn);

            if (players[0].getInJail()) {
                gameScreen.setPromptState(PromptState.JAIL);
            } else {
                gameScreen.enableRollDice();
            }
        }
    }

    public void turnCompleted() {
        if (playerTurn == 0) {
            gameScreen.enableEndTurn();
        } else {
            setNextTurn();
        }
    }

    public void buyProperty(boolean response) {
        if (response) {
            board.buyProperty(currentPlayer);
        }
        turnCompleted();
    }

    public void payFine() {
        currentPlayer.updateMoney(-50);
        currentPlayer.setInJail(false);
        if (playerTurn == 0) {
            gameScreen.enableRollDice();
        }
    }
}
