package realestateempire.screens;

import java.util.ArrayList;

import realestateempire.game.GameState;
import realestateempire.game.Player;
import realestateempire.graphics.Button;
import realestateempire.graphics.Model;
import realestateempire.graphics.Texture;

public class GameScreen implements Screen {

    private final Model boardBackground;
    private final Button rollDice;
    private final Button endTurn;
    private final Button quit;
    private final GameState gameState;

    public GameScreen() {
        boardBackground = new Model("Background.png", 0, 0);
        String[] rollDiceTextures = { "Buttons/Roll Dice.png",
                "Buttons/Roll Dice Highlighted.png", "Buttons/Roll Dice Disabled.png" };
        rollDice = new Button(rollDiceTextures, 1491, 1303);

        String[] endTurnTextures = { "Buttons/End Turn.png",
                "Buttons/End Turn Highlighted.png", "Buttons/End Turn Disabled.png" };
        endTurn = new Button(endTurnTextures, 1847, 1303);
        endTurn.setEnabled(false);

        String[] quitTextures = { "Buttons/Quit.png", "Buttons/Quit Highlighted.png" };
        quit = new Button(quitTextures, 2203, 1303);
        gameState = new GameState(this);
    }

    @Override
    public void render() {
        boardBackground.render();
        rollDice.render();
        endTurn.render();
        quit.render();
        gameState.render();
    }

    @Override
    public void cursorMoved(double cursorXCoord, double cursorYCoord) {
        rollDice.isCursorInRange(cursorXCoord, cursorYCoord);
        endTurn.isCursorInRange(cursorXCoord, cursorYCoord);
        quit.isCursorInRange(cursorXCoord, cursorYCoord);
        gameState.cursorMoved(cursorXCoord, cursorYCoord);
    }

    @Override
    public void buttonPressed(ScreenState screenState, double cursorXCoord, double cursorYCoord) {
        if (rollDice.isCursorInRange(cursorXCoord, cursorYCoord)) {
            rollDice.setHighlighted(false);
            rollDice.setEnabled(false);
            gameState.rollDice();
        }
        if (endTurn.isCursorInRange(cursorXCoord, cursorYCoord)) {
            endTurn.setHighlighted(false);
            endTurn.setEnabled(false);
            gameState.setNextTurn();
        }
        if (quit.isCursorInRange(cursorXCoord, cursorYCoord)) {
            screenState.quitGame();
        }
        gameState.buttonPressed(cursorXCoord, cursorYCoord);
    }

    public void enableRollDice() {
        rollDice.setHighlighted(false);
        rollDice.setEnabled(true);
    }

    public void disableRollDice() {
        rollDice.setHighlighted(false);
        rollDice.setEnabled(false);
    }

    public void enableEndTurn() {
        endTurn.setHighlighted(false);
        endTurn.setEnabled(true);
    }

    public void disableEndTurn() {
        endTurn.setHighlighted(false);
        endTurn.setEnabled(false);
    }

    public void setPlayerTokens(int tokenVal) {
        ArrayList<String[]> textures = new ArrayList<>();
        textures.add(new String[] { "Tokens/Hat.png", "Tokens/Hat Small.png" });
        textures.add(new String[] { "Tokens/Red Token.png", "Tokens/Red Token Small.png"});
        textures.add(new String[] { "Tokens/Blue Token.png", "Tokens/Blue Token Small.png"});
        textures.add(new String[] { "Tokens/Green Token.png", "Tokens/Blue Token Small.png"});

        Texture playerTexture = new Texture(textures.get(tokenVal)[0]);
        Texture playerTextureSmall = new Texture(textures.get(tokenVal)[1]);
        Player[] players = gameState.getPlayers();

        players[0].getAvatar().setTexture(playerTexture);
        players[0].getToken().setTexture(playerTexture);
        players[0].getToken().setTexture(1, playerTextureSmall);
        textures.remove(tokenVal);

        for (int i = 1; i < players.length; i++) {
            Texture tokenTexture = new Texture(textures.get(i - 1)[0]);
            Texture tokenTextureSmall = new Texture(textures.get(i - 1)[1]);
            players[i].getAvatar().setTexture(tokenTexture);
            players[i].getToken().setTexture(tokenTexture);
            players[i].getToken().setTexture(1, tokenTextureSmall);
        }
    }
}
