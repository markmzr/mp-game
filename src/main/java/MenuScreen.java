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
        float[] backgroundVertices = new float[] {
                -6.68f, 12.68f, 0f,
                1.0f, 12.68f, 0f,
                1.0f, -1.0f, 0f,

                1.0f, -1.0f, 0f,
                -6.68f, -1.0f, 0f,
                -6.68f, 12.68f, 0f,
        };
        background = new GL2DObject(backgroundVertices, "Background.png");

        float[] logoVertices = new float[] {
                -0.8f, 0.86f, 0f,
                0.8f, 0.86f, 0f,
                0.8f, 0.46f, 0f,

                0.8f, 0.46f, 0f,
                -0.8f, 0.46f, 0f,
                -0.8f, 0.86f, 0f,
        };
        logo = new GL2DObject(logoVertices, "Logo.png");

        float[] newGameVertices = new float[] {
                -0.2f, 0.06f, 0f,
                0.2f, 0.06f, 0f,
                0.2f, -0.06f, 0f,

                0.2f, -0.06f, 0f,
                -0.2f, -0.06f, 0f,
                -0.2f, 0.06f, 0f,
        };
        String[] newGameTextures = { "New Game.png", "New Game Highlighted.png" };
        newGame = new GLButton(newGameVertices, newGameTextures, 0.4, 0.6, 0.47, 0.53);

        float[] quitVertices = new float[] {
                -0.2f, -0.18f, 0f,
                0.2f, -0.18f, 0f,
                0.2f, -0.30f, 0f,

                0.2f, -0.30f, 0f,
                -0.2f, -0.30f, 0f,
                -0.2f, -0.18f, 0f,
        };
        String[] quitTextures = { "Quit.png", "Quit Highlighted.png" };
        quit = new GLButton(quitVertices, quitTextures, 0.4, 0.6, 0.59, 0.65);

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

    public void buttonPressed(GameState gameState, double cursorXCoord, double cursorYCoord) {
        if (newGame.isCursorInRange(cursorXCoord, cursorYCoord)) {
            newGame.setHighlighted(false);
            gameState.setToSetupScreen();
        }

        if (quit.isCursorInRange(cursorXCoord, cursorYCoord)) {
            quit.setHighlighted(false);
            gameState.quitGame();
        }
    }
}
