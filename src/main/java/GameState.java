import java.util.Random;

public class GameState {

    private Board board;
    private Player[] players;
    private Player currentPlayer;
    private int firstDieVal;
    private int secondDieVal;
    private boolean playerMoving;
    private boolean turnCompleted;

    public GameState() {
        board = new Board();
        players = new Player[4];
        players[0] = new Player("Player", 0, "Hat.png");
        players[1] = new Player("AI 1", 1, "AI 1 Token.png");
        players[2] = new Player("AI 2", 2, "AI 2 Token.png");
        players[3] = new Player("AI 3", 3, "AI 3 Token.png");
        currentPlayer = players[0];

        firstDieVal = 0;
        secondDieVal = 0;
        playerMoving = false;
        turnCompleted = false;
    }

    public Player getPlayer(int id) {
        return players[id];
    }

    public int getFirstDieVal() {
        return firstDieVal;
    }

    public int getSecondDieVal() {
        return secondDieVal;
    }

    public void setTurnCompleted(boolean turnCompleted) {
        this.turnCompleted = turnCompleted;
    }

    public void render(GameScreen gameScreen) {
        for (Player player : players) {
            player.render();
        }

        if (playerMoving && currentPlayer.getCurrentPosition() == currentPlayer.getNewPosition()) {
            board.playerLanded(this, currentPlayer);
            playerMoving = false;
        }
        if (turnCompleted) {
            if (currentPlayer.getId() == 0) {
                gameScreen.enableEndTurn();
            }
            else if (currentPlayer.getId() < 3) {
                currentPlayer = players[currentPlayer.getId() + 1];
                rollDice();
            }
            else {
                currentPlayer = players[0];
                gameScreen.enableRollDice();
            }

            turnCompleted = false;
        }
    }

    public void rollDice() {
        Random random = new Random();
        firstDieVal = random.nextInt(6);
        secondDieVal = random.nextInt(6);

        int diceRoll = firstDieVal + secondDieVal + 2;
        currentPlayer.updatePosition(diceRoll);
        playerMoving = true;
    }

    public void setAiTurn() {
        currentPlayer = players[1];
        rollDice();
    }
}
