package realestateempire.screens;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import realestateempire.graphics.Model;
import realestateempire.graphics.Button;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class MenuScreen implements Screen {

    private final Model background;
    private final Model logo;
    private final Button newGame;
    private final Button quit;
    private double prevTime;
    private double moveLeftMaxTime;
    private double moveRightMaxTime;
    private double moveUpMaxTime;
    private double moveDownMaxTime;

    public MenuScreen() {
        background = new Model("Background Large.png", -8014, -9134);
        logo = new Model("Logo.png", 256, 100);

        String[] newGameTextures = { "Buttons/New Game.png", "Buttons/New Game Highlighted.png" };
        newGame = new Button(newGameTextures, 1070, 660);

        String[] quitTextures = { "Buttons/Quit Large.png", "Buttons/Quit Large Highlighted.png" };
        quit = new Button(quitTextures, 1070, 820);

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
        logo.render();
        newGame.render();
        quit.render();
    }

    @Override
    public void cursorMoved(double cursorXCoord, double cursorYCoord) {
        newGame.isCursorInRange(cursorXCoord, cursorYCoord);
        quit.isCursorInRange(cursorXCoord, cursorYCoord);
    }

    @Override
    public void buttonPressed(ScreenState screenState, double cursorXCoord, double cursorYCoord) {
        if (newGame.isCursorInRange(cursorXCoord, cursorYCoord)) {
            newGame.setHighlighted(false);
            screenState.setToSetupScreen(background.getPosition(), prevTime);
        }
        if (quit.isCursorInRange(cursorXCoord, cursorYCoord)) {
            screenState.quitGame();
        }
    }
}
