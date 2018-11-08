import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class MenuScreen implements Screen {

    private GL2DObject background;
    private GL2DObject logo;
    private GLButton newGame;
    private GLButton quit;
    private double lastTime;
    private double moveLeftMaxTime;
    private double moveRightMaxTime;
    private double moveUpMaxTime;
    private double moveDownMaxTime;

    public MenuScreen() {
        background = new GL2DObject("Background Large.png", -7271, -8392, 9831, 9831);
        logo = new GL2DObject("Logo.png", 256, 100, 2048, 288);

        String[] newGameTextures = { "Buttons/New Game.png", "Buttons/New Game Highlighted.png" };
        newGame = new GLButton(newGameTextures, 1024, 677, 512, 86);

        String[] quitTextures = { "Buttons/Quit Large.png", "Buttons/Quit Large Highlighted.png" };
        quit = new GLButton(quitTextures, 1024, 852, 512, 86);

        lastTime = 0;
        moveRightMaxTime = 60;
        moveDownMaxTime = 120;
        moveLeftMaxTime = 180;
        moveUpMaxTime = 240;
    }

    public void setBackgroundPosition(Matrix4f position) {
        background.setPosition(position);
    }

    public Matrix4f getBackgroundPosition() {
        return background.getPosition();
    }

    public void setLastTime(double lastTime) {
        this.lastTime = lastTime;
    }

    public double getLastTime() {
        return lastTime;
    }

    public void render() {
        double currentTime = glfwGetTime();
        double deltaTime = currentTime - lastTime;
        lastTime = currentTime;
        Vector3f moveDirection = new Vector3f(0, 0, 0);

        if (currentTime < moveRightMaxTime) {
            moveDirection.x = 0.09469f;
        }
        else if (currentTime < moveDownMaxTime) {
            moveDirection.y = -0.19469f;
        }
        else if (currentTime < moveLeftMaxTime) {
            moveDirection.x = -0.09469f;
        }
        else if (currentTime < moveUpMaxTime) {
            moveDirection.y = 0.19469f;
        }
        else {
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

    public void cursorMoved(double cursorXCoord, double cursorYCoord) {
        if (newGame.isCursorInRange(cursorXCoord, cursorYCoord)) {
            newGame.setHighlighted(true);
        }
        else {
            newGame.setHighlighted(false);
        }

        if (quit.isCursorInRange(cursorXCoord, cursorYCoord)) {
            quit.setHighlighted(true);
        }
        else {
            quit.setHighlighted(false);
        }
    }

    public void buttonPressed(ScreenState screenState, double cursorXCoord, double cursorYCoord) {
        if (newGame.isCursorInRange(cursorXCoord, cursorYCoord)) {
            newGame.setHighlighted(false);
            screenState.setToSetupScreen();
        }

        if (quit.isCursorInRange(cursorXCoord, cursorYCoord)) {
            screenState.quitGame();
        }
    }
}
