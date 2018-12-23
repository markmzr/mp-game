package realestateempire.screens;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import realestateempire.graphics.Model;
import realestateempire.graphics.Button;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class SetupScreen implements Screen {

    private final Model background;
    private final Model chooseToken;
    private final Model tokenHighlight;
    private final Button hatToken;
    private final Button redToken;
    private final Button blueToken;
    private final Button greenToken;
    private final Button mainMenu;
    private final Button startGame;
    private int tokenVal;
    private double prevTime;
    private double moveLeftMaxTime;
    private double moveRightMaxTime;
    private double moveUpMaxTime;
    private double moveDownMaxTime;

    public SetupScreen() {
        background = new Model("Background Large.png", -8014, -9134);
        chooseToken = new Model("Choose Token.png", 746, 159);
        tokenHighlight = new Model("Token Highlight.png", 885, 462);

        String[] hatTextures = { "Tokens/Hat Large.png", "Tokens/Hat Large.png"};
        hatToken = new Button(hatTextures, 905, 482);

        String[] redTokenTextures = { "Tokens/Red Token Large.png", "Tokens/Red Token Large.png" };
        redToken = new Button(redTokenTextures, 1343, 482);

        String[] blueTokenTextures = { "Tokens/Blue Token Large.png", "Tokens/Blue Token Large.png" };
        blueToken = new Button(blueTokenTextures, 905, 854);

        String[] greenTokenTextures = { "Tokens/Green Token Large.png", "Tokens/Green Token Large.png" };
        greenToken = new Button(greenTokenTextures, 1343, 854);

        String[] mainMenuTextures = { "Buttons/Main Menu.png",
                "Buttons/Main Menu Highlighted.png" };
        mainMenu = new Button(mainMenuTextures, 100, 1240);

        String[] startGameTextures = { "Buttons/Start Game.png",
                "Buttons/Start Game Highlighted.png" };
        startGame = new Button(startGameTextures, 2040, 1240);

        tokenVal = 0;
        prevTime = 0;
        moveRightMaxTime = 60;
        moveDownMaxTime = 120;
        moveLeftMaxTime = 180;
        moveUpMaxTime = 240;
    }

    public void setBackgroundPosition(Matrix4f position) {
        background.setPosition(position);
    }

    public void setPrevTime(double prevTime) {
        this.prevTime = prevTime;
    }

    @Override
    public void render() {
        double currentTime = glfwGetTime();
        double deltaTime = currentTime - prevTime;
        prevTime = currentTime;
        Vector3f moveDirection = new Vector3f(0, 0, 0);

        if (currentTime < moveRightMaxTime) {
            moveDirection.x = background.pixelXToCoord(133.56);
        } else if (currentTime < moveDownMaxTime) {
            moveDirection.y = background.pixelYToCoord(-152.23);
        } else if (currentTime < moveLeftMaxTime) {
            moveDirection.x = background.pixelXToCoord(-133.56);
        } else if (currentTime < moveUpMaxTime) {
            moveDirection.y = background.pixelYToCoord(152.23);
        } else {
            moveRightMaxTime += 240;
            moveDownMaxTime += 240;
            moveLeftMaxTime += 240;
            moveUpMaxTime += 240;
        }
        moveDirection.mul((float)deltaTime);
        background.movePosition(moveDirection);
        background.render();
        chooseToken.render();
        hatToken.render();
        redToken.render();
        blueToken.render();
        greenToken.render();
        tokenHighlight.render();
        mainMenu.render();
        startGame.render();
    }

    @Override
    public void cursorMoved(double cursorXCoord, double cursorYCoord) {
        mainMenu.isCursorInRange(cursorXCoord, cursorYCoord);
        startGame.isCursorInRange(cursorXCoord, cursorYCoord);
    }

    @Override
    public void buttonPressed(ScreenState screenState, double cursorXCoord, double cursorYCoord) {
        if (mainMenu.isCursorInRange(cursorXCoord, cursorYCoord)) {
            mainMenu.setHighlighted(false);
            screenState.setToMenuScreen(background.getPosition(), prevTime);
        }
        if (startGame.isCursorInRange(cursorXCoord, cursorYCoord)) {
            startGame.setHighlighted(false);
            screenState.setToGameScreen(tokenVal);
        }
        if (hatToken.isCursorInRange(cursorXCoord, cursorYCoord)) {
            tokenHighlight.setPosition(885, 462);
            tokenVal = 0;
        }
        if (redToken.isCursorInRange(cursorXCoord, cursorYCoord)) {
            tokenHighlight.setPosition(1323, 462);
            tokenVal = 1;
        }
        if (blueToken.isCursorInRange(cursorXCoord, cursorYCoord)) {
            tokenHighlight.setPosition(885, 834);
            tokenVal = 2;
        }
        if (greenToken.isCursorInRange(cursorXCoord, cursorYCoord)) {
            tokenHighlight.setPosition(1323, 834);
            tokenVal = 3;
        }
    }
}
