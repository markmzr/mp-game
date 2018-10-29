public class GameScreen implements Screen {

    private GL2DObject boardBackground;
    private GLButton rollDice;
    private GLButton endTurn;
    private GLButton quit;

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

        float[] rollDiceVertices = new float[] {
                0.16484f, -0.80972f, 0f,
                0.40313f, -0.80972f, 0f,
                0.40313f, -0.92916f, 0f,

                0.40313f, -0.92916f, 0f,
                0.16484f, -0.92916f, 0f,
                0.16484f, -0.80972f, 0f,
        };
        rollDice = new GLButton(rollDiceVertices, "Roll Dice.png",
                "Roll Dice Highlighted.png", 0.58242, 0.70156, 0.90486, 0.96458);

        float[] endTurnVertices = new float[] {
                0.44297f, -0.80972f, 0f,
                0.68125f, -0.80972f, 0f,
                0.68125f, -0.92916f, 0f,

                0.68125f, -0.92916f, 0f,
                0.44297f, -0.92916f, 0f,
                0.44297f, -0.80972f, 0f,
        };
        endTurn = new GLButton(endTurnVertices, "End Turn.png",
                "End Turn Highlighted.png", 0.72109, 0.84063, 0.90486, 0.96458);

        float[] quitVertices = new float[] {
                0.72109f, -0.80972f, 0f,
                0.96016f, -0.80972f, 0f,
                0.96016f, -0.92916f, 0f,

                0.96016f, -0.92916f, 0f,
                0.72109f, -0.92916f, 0f,
                0.72109f, -0.80972f, 0f,
        };
        quit = new GLButton(quitVertices, "Quit Game Screen.png",
                "Quit Game Screen Highlighted.png", 0.86055, 0.98008, 0.90486, 0.96458);
    }

    public void render() {
        boardBackground.render();
        rollDice.render();
        endTurn.render();
        quit.render();
    }

    public void cursorMoved(double cursorXCoord, double cursorYCoord) {
        if (rollDice.isCursorInRange(cursorXCoord, cursorYCoord)) {
            rollDice.setHighlighted(true);
        }
        else {
            rollDice.setHighlighted(false);
        }

        if (endTurn.isCursorInRange(cursorXCoord, cursorYCoord)) {
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

    public void buttonPressed(GameState gameState, double cursorXCoord, double cursorYCoord) {
        if (quit.isCursorInRange(cursorXCoord, cursorYCoord)) {
            quit.setHighlighted(false);
            gameState.quitGame();
        }
    }
}
