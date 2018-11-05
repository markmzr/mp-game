public class GameScreen implements Screen {

    private GL2DObject boardBackground;
    private GL2DObject firstDie;
    private GL2DObject secondDie;
    private GLButton rollDice;
    private GLButton endTurn;
    private GLButton quit;
    private GameState gameState;

    public GameScreen() {
        float[] backgroundVertices = new float[] {
                -1f, 1f, 0f,
                1f, 1f, 0f,
                1f, -1f, 0f,

                1f, -1f, 0f,
                -1f, -1f, 0f,
                -1f, 1f, 0f,
        };
        boardBackground = new GL2DObject(backgroundVertices, "Game Screen Background.png");

        float[] firstDieVertices = new float[] {
                -0.4859375f, 0.03611111f, 0f,
                -0.44609375f, 0.03611111f, 0f,
                -0.44609375f, -0.03472222f, 0f,

                -0.44609375f, -0.03472222f, 0f,
                -0.4859375f, -0.03472222f, 0f,
                -0.4859375f, 0.03611111f, 0f,
        };

        String[] dieTextures = new String[6];
        for (int i = 0; i < 6; i++) {
            dieTextures[i] = "Dice" + (i + 1) + ".png";
        }

        firstDie = new GL2DObject(firstDieVertices, dieTextures);

        float[] secondDieVertices = new float[] {
                -0.4296875f, 0.03611111f, 0f,
                -0.38984375f, 0.03611111f, 0f,
                -0.38984375f, -0.03472222f, 0f,

                -0.38984375f, -0.03472222f, 0f,
                -0.4296875f, -0.03472222f, 0f,
                -0.4296875f, 0.03611111f, 0f,
        };
        secondDie = new GL2DObject(secondDieVertices, dieTextures);

        float[] rollDiceVertices = new float[] {
                0.16484f, -0.80972f, 0f,
                0.40313f, -0.80972f, 0f,
                0.40313f, -0.92916f, 0f,

                0.40313f, -0.92916f, 0f,
                0.16484f, -0.92916f, 0f,
                0.16484f, -0.80972f, 0f,
        };
        String[] rollDiceTextures = {"Roll Dice.png", "Roll Dice Highlighted.png",
                "Roll Dice Disabled.png" };
        rollDice = new GLButton(rollDiceVertices, rollDiceTextures,
                0.58242, 0.70156, 0.90486, 0.96458);

        float[] endTurnVertices = new float[] {
                0.44297f, -0.80972f, 0f,
                0.68125f, -0.80972f, 0f,
                0.68125f, -0.92916f, 0f,

                0.68125f, -0.92916f, 0f,
                0.44297f, -0.92916f, 0f,
                0.44297f, -0.80972f, 0f,
        };
        String[] endTurnTextures = { "End Turn.png", "End Turn Highlighted.png",
                "End Turn Disabled.png" };
        endTurn = new GLButton(endTurnVertices, endTurnTextures,
                0.72109, 0.84063, 0.90486, 0.96458);
        endTurn.setEnabled(false);

        float[] quitVertices = new float[] {
                0.72109f, -0.80972f, 0f,
                0.96016f, -0.80972f, 0f,
                0.96016f, -0.92916f, 0f,

                0.96016f, -0.92916f, 0f,
                0.72109f, -0.92916f, 0f,
                0.72109f, -0.80972f, 0f,
        };
        String[] quitTextures = {"Quit Game Screen.png",
                "Quit Game Screen Highlighted.png"};
        quit = new GLButton(quitVertices, quitTextures, 0.86055, 0.98008, 0.90486, 0.96458);

        gameState = new GameState();
    }

    public void render() {
        boardBackground.render();
        firstDie.render(gameState.getFirstDieVal());
        secondDie.render(gameState.getSecondDieVal());

        rollDice.render();
        endTurn.render();
        quit.render();

        gameState.render(this);
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
            gameState.setAiTurn();
            endTurn.setHighlighted(false);
            endTurn.setEnabled(false);
        }

        if (quit.isCursorInRange(cursorXCoord, cursorYCoord)) {
            screenState.quitGame();
        }
    }

    public void enableRollDice() {
        rollDice.setHighlighted(false);
        rollDice.setEnabled(true);
    }

    public void enableEndTurn() {
        endTurn.setHighlighted(false);
        endTurn.setEnabled(true);
    }
}
