public class GameScreen implements Screen {

    private GL2DObject boardBackground;
    private GL2DObject firstDie;
    private GL2DObject secondDie;
    private GLButton rollDice;
    private GLButton endTurn;
    private GLButton quit;
    private GameState gameState;

    public GameScreen() {
        boardBackground = new GL2DObject("Background.png", 0, 0, 2560, 1440);

        String[] dieTextures = new String[6];
        for (int i = 0; i < 6; i++) {
            dieTextures[i] = "Dice/Dice" + (i + 1) + ".png";
        }
        firstDie = new GL2DObject(dieTextures, 658, 694, 51, 51);
        secondDie = new GL2DObject(dieTextures, 730, 694, 51, 51);

        String[] rollDiceTextures = {"Buttons/Roll Dice.png",
                "Buttons/Roll Dice Highlighted.png", "Buttons/Roll Dice Disabled.png" };
        rollDice = new GLButton(rollDiceTextures, 1491, 1303, 305, 86);

        String[] endTurnTextures = { "Buttons/End Turn.png",
                "Buttons/End Turn Highlighted.png", "Buttons/End Turn Disabled.png"};
        endTurn = new GLButton(endTurnTextures, 1847, 1303, 305, 86);
        endTurn.setEnabled(false);

        String[] quitTextures = {"Buttons/Quit.png", "Buttons/Quit Highlighted.png"};
        quit = new GLButton(quitTextures, 2203, 1303, 305, 86);

        gameState = new GameState(this);
    }

    public void enableRollDice() {
        rollDice.setHighlighted(false);
        rollDice.setEnabled(true);
    }

    public void enableEndTurn() {
        endTurn.setHighlighted(false);
        endTurn.setEnabled(true);
    }

    public void render() {
        boardBackground.render();
        firstDie.render(gameState.getFirstDieVal());
        secondDie.render(gameState.getSecondDieVal());

        rollDice.render();
        endTurn.render();
        quit.render();

        gameState.render();
    }

    public void cursorMoved(double cursorXCoord, double cursorYCoord) {
        if (rollDice.isCursorInRange(cursorXCoord, cursorYCoord) && rollDice.getEnabled()) {
            rollDice.setHighlighted(true);
        }
        else {
            rollDice.setHighlighted(false);
        }

        if (endTurn.isCursorInRange(cursorXCoord, cursorYCoord) && endTurn.getEnabled()) {
            endTurn.setHighlighted(true);
        }
        else {
            endTurn.setHighlighted(false);
        }

        if (quit.isCursorInRange(cursorXCoord, cursorYCoord)) {
            quit.setHighlighted(true);
        }
        else {
            quit.setHighlighted(false);
        }
    }

    public void buttonPressed(ScreenState screenState, double cursorXCoord, double cursorYCoord) {
        if (rollDice.getEnabled() && rollDice.isCursorInRange(cursorXCoord, cursorYCoord)) {
            gameState.rollDice();
            rollDice.setHighlighted(false);
            rollDice.setEnabled(false);
        }

        if (endTurn.getEnabled() && endTurn.isCursorInRange(cursorXCoord, cursorYCoord)) {
            gameState.setNextTurn();
            endTurn.setHighlighted(false);
            endTurn.setEnabled(false);
        }

        if (quit.isCursorInRange(cursorXCoord, cursorYCoord)) {
            screenState.quitGame();
        }
    }
}
