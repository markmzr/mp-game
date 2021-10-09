package realestateempire.singleplayer;

import java.util.ArrayList;

import realestateempire.graphics.Interactive;
import realestateempire.graphics.Graphic;
import realestateempire.graphics.model.Model;

public class GameGraphics implements Graphic {

    private final Model playerBorder;
    private final Model gameOver;
    private final Model dieLeft;
    private final Model dieRight;
    private final ArrayList<Interactive>[] interactives;

    public GameGraphics(ArrayList<Interactive>[] interactives) {
        this.interactives = interactives;
        playerBorder = new Model("Player Border.png", 1481, 41);
        String[] gameOverTextures = {"Prompts/Win.png", "Prompts/Lose.png"};
        gameOver = new Model(gameOverTextures, 746, 621);
        gameOver.setVisible(false);

        String[] dieTextures = new String[6];
        for (int i = 0; i < 6; i++) {
            dieTextures[i] = "Dice/Dice " + (i + 1) + ".png";
        }
        dieLeft = new Model(dieTextures, 657, 846);
        dieRight = new Model(dieTextures, 732, 846);
        dieLeft.setVisible(false);
        dieRight.setVisible(false);
    }

    @Override
    public void render() {
        playerBorder.render();
        gameOver.render();
        dieLeft.render();
        dieRight.render();
    }

    public void movePlayerBorder(int playerTurn) {
        playerBorder.setPosition(1481, 41 + (165 * playerTurn));
    }

    public void enableDice(int dieLeftVal, int dieRightVal) {
        dieLeft.setTexture(dieLeftVal);
        dieRight.setTexture(dieRightVal);
        dieLeft.setVisible(true);
        dieRight.setVisible(true);
    }

    public void disableDice() {
        dieLeft.setVisible(false);
        dieRight.setVisible(false);
    }

    public void endGame(boolean userWins) {
        for (ArrayList<Interactive> interactiveList : interactives) {
            for (Interactive interactive : interactiveList) {
                interactive.setEnabled(false);
            }
        }
        playerBorder.setVisible(false);
        gameOver.setTexture(userWins ? 0 : 1);
        gameOver.setVisible(true);
    }
}
