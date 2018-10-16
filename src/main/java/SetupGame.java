import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class SetupGame implements Screen {

    private GL2DObject background;
    private GLButton mainMenu;
    private double lastTime;
    private double moveLeftMaxTime;
    private double moveRightMaxTime;
    private double moveUpMaxTime;
    private double moveDownMaxTime;

    public SetupGame() {
        float[] backgroundVertices = new float[] {
                -6.68f, 12.68f, 0f,
                1.0f, 12.68f, 0f,
                1.0f, -1.0f, 0f,

                1.0f, -1.0f, 0f,
                -6.68f, -1.0f, 0f,
                -6.68f, 12.68f, 0f,
        };
        background = new GL2DObject(backgroundVertices, "Background.png");

        float[] mainMenuVertices = new float[] {
                -0.9f, -0.70f, 0f,
                -0.5f, -0.70f, 0f,
                -0.5f, -0.82f, 0f,

                -0.5f, -0.82f, 0f,
                -0.9f, -0.82f, 0f,
                -0.9f, -0.70f, 0f,
        };
        mainMenu = new GLButton(mainMenuVertices, "Main Menu.png", 0.05, 0.25, 0.85, 0.91);

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

        Vector3f moveLeft = new Vector3f(-0.09469f, 0, 0);
        Vector3f moveRight = new Vector3f(0.09469f, 0, 0);
        Vector3f moveUp = new Vector3f(0, 0.19469f, 0);
        Vector3f moveDown = new Vector3f(0, -0.19469f, 0);

        if (currentTime < moveRightMaxTime) {
            moveRight.mul((float)deltaTime);
            background.movePosition(moveRight);
        }
        else if (currentTime >= moveRightMaxTime && currentTime < moveDownMaxTime) {
            moveDown.mul((float)deltaTime);
            background.movePosition(moveDown);
        }
        else if (currentTime >= moveDownMaxTime && currentTime < moveLeftMaxTime) {
            moveLeft.mul((float)deltaTime);
            background.movePosition(moveLeft);
        }
        else if (currentTime >= moveLeftMaxTime && currentTime < moveUpMaxTime) {
            moveUp.mul((float)deltaTime);
            background.movePosition(moveUp);
        }
        else {
            moveRightMaxTime += 240;
            moveDownMaxTime += 240;
            moveLeftMaxTime += 240;
            moveUpMaxTime += 240;
        }

        background.render();
        mainMenu.render();
    }

    public ScreenOption buttonPressed(double cursorXPos, double cursorYPos) {
        if (mainMenu.isCursorInRange(cursorXPos, cursorYPos)) {
            return ScreenOption.MAIN_MENU;
        }

        return ScreenOption.SETUP_GAME;
    }
}
