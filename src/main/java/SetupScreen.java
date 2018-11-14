import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class SetupScreen implements Screen {

    private GL2DObject background;
    private GLButton mainMenu;
    private GLButton startGame;
    private double lastTime;
    private double moveLeftMaxTime;
    private double moveRightMaxTime;
    private double moveUpMaxTime;
    private double moveDownMaxTime;

    public SetupScreen() {
        background = new GL2DObject("Background Large.png", -7271, -8392, 9831, 9831);

        String[] mainMenuTextures = { "Buttons/Main Menu.png",
                "Buttons/Main Menu Highlighted.png" };
        mainMenu = new GLButton(mainMenuTextures, 128, 1224, 512, 86);

        String[] startGameTextures = { "Buttons/Start Game.png",
                "Buttons/Start Game Highlighted.png" };
        startGame = new GLButton(startGameTextures, 1920, 1224, 512, 86);

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
        } else if (currentTime < moveDownMaxTime) {
            moveDirection.y = -0.19469f;
        } else if (currentTime < moveLeftMaxTime) {
            moveDirection.x = -0.09469f;
        } else if (currentTime < moveUpMaxTime) {
            moveDirection.y = 0.19469f;
        } else {
            moveRightMaxTime += 240;
            moveDownMaxTime += 240;
            moveLeftMaxTime += 240;
            moveUpMaxTime += 240;
        }

        moveDirection.mul((float)deltaTime);
        background.movePosition(moveDirection);
        background.render();
        mainMenu.render();
        startGame.render();
    }

    public void cursorMoved(double cursorXCoord, double cursorYCoord) {
        mainMenu.isCursorInRange(cursorXCoord, cursorYCoord);
        startGame.isCursorInRange(cursorXCoord, cursorYCoord);
    }

    public void buttonPressed(ScreenState screenState, double cursorXCoord, double cursorYCoord) {
        if (mainMenu.isCursorInRange(cursorXCoord, cursorYCoord)) {
            mainMenu.setHighlighted(false);
            screenState.setToMenuScreen();
        }
        if (startGame.isCursorInRange(cursorXCoord, cursorYCoord)) {
            startGame.setHighlighted(false);
            screenState.setToGameScreen();
        }
    }
}
