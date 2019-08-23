package realestateempire.screens;

import realestateempire.game.Game;
import realestateempire.graphics.Button;
import realestateempire.graphics.Button.ButtonState;
import realestateempire.graphics.Model;
import realestateempire.multiplayer.MultiplayerGame;
import realestateempire.multiplayer.MultiplayerSession;

import static realestateempire.graphics.Button.ButtonState.DISABLED;

public class GameScreen implements Screen {

    private final Model background;
    private final Button rollDice;
    private final Button endTurn;
    private final Button quit;
    private Game game;

    GameScreen() {
        background = new Model("Background.png", 0, 0);
        String[] rollDiceTextures = { "Buttons/Roll Dice.png",
                "Buttons/Roll Dice M.png", "Buttons/Roll Dice D.png" };
        rollDice = new Button(rollDiceTextures, 1491, 1303);

        String[] endTurnTextures = { "Buttons/End Turn.png",
                "Buttons/End Turn M.png", "Buttons/End Turn D.png" };
        endTurn = new Button(endTurnTextures, 1847, 1303);
        endTurn.setButtonState(DISABLED);

        String[] quitTextures = { "Buttons/Quit.png", "Buttons/Quit M.png" };
        quit = new Button(quitTextures, 2203, 1303);
    }

    @Override
    public void render() {
        background.render();
        rollDice.render();
        endTurn.render();
        quit.render();
        game.render();
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        game.cursorMoved(xCursor, yCursor);
        rollDice.isMouseover(xCursor, yCursor);
        endTurn.isMouseover(xCursor, yCursor);
        quit.isMouseover(xCursor, yCursor);
    }

    @Override
    public void buttonPressed(ScreenState screenState, double xCursor, double yCursor) {
        game.buttonPressed(xCursor, yCursor);
        if (!game.getPrompt().isVisible()) {
            if (rollDice.isMouseover(xCursor, yCursor)) {
                rollDice.setButtonState(DISABLED);
                game.rollDice();
            }
            if (endTurn.isMouseover(xCursor, yCursor)) {
                endTurn.setButtonState(DISABLED);
                game.setNextTurn();
            }
            if (quit.isMouseover(xCursor, yCursor)) {
                game.getPrompt().setQuitMenu(screenState);
            }
        }
    }

    public void setRollDiceState(ButtonState buttonState) {
        rollDice.setButtonState(buttonState);
    }

    public void setEndTurnState(ButtonState buttonState) {
        endTurn.setButtonState(buttonState);
    }

    void initSingleplayerGame(int userToken) {
        game = new Game(this, userToken);
    }

    void initMultiplayerGame(MultiplayerSession mpSession) {
        game = new MultiplayerGame(this, mpSession);
        mpSession.setMultiplayerGame((MultiplayerGame) game);
        if (mpSession.getUserId() != 0) {
            rollDice.setButtonState(DISABLED);
            endTurn.setButtonState(DISABLED);
        }
    }
}
