package realestateempire.screens;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import realestateempire.graphics.Model;
import realestateempire.graphics.Button;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static realestateempire.graphics.Button.ButtonState.ENABLED;

public class MenuScreen implements Screen {

    private final Model background;
    private final Model logo;
    private final Button singleplayer;
    private final Button multiplayer;
    private final Button quit;
    private double prevTime;

    MenuScreen() {
        background = new Model("Background Large.png", -8014, -9134);
        logo = new Model("Logo.png", 256, 100);

        String[] newGameTextures = { "Buttons/Singleplayer.png",
                "Buttons/Singleplayer M.png" };
        singleplayer = new Button(newGameTextures, 1070, 560);

        String[] multiplayerTextures = { "Buttons/Multiplayer.png",
                "Buttons/Multiplayer M.png" };
        multiplayer = new Button(multiplayerTextures, 1070, 720);

        String[] quitTextures = { "Buttons/Quit Large.png",
                "Buttons/Quit Large M.png" };
        quit = new Button(quitTextures, 1070, 880);
        prevTime = 0;
    }

    void setBackgroundPosition(Matrix4f position) {
        background.setPosition(position);
    }

    void setPrevTime(double prevTime) {
        this.prevTime = prevTime;
    }

    @Override
    public void render() {
        moveBackground();
        background.render();
        logo.render();
        singleplayer.render();
        multiplayer.render();
        quit.render();
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        singleplayer.isMouseover(xCursor, yCursor);
        multiplayer.isMouseover(xCursor, yCursor);
        quit.isMouseover(xCursor, yCursor);
    }

    @Override
    public void buttonPressed(ScreenState screenState, double xCursor, double yCursor) {
        if (singleplayer.isMouseover(xCursor, yCursor)) {
            singleplayer.setButtonState(ENABLED);
            screenState.setToSetupScreen(background.getPosition(), prevTime);
        }
        if (multiplayer.isMouseover(xCursor, yCursor)) {
            multiplayer.setButtonState(ENABLED);
            screenState.setToMultiplayerScreen(background.getPosition(), prevTime);
        }
        if (quit.isMouseover(xCursor, yCursor)) {
            screenState.quitGame();
        }
    }

    private void moveBackground() {
        double time = glfwGetTime() % 240;
        prevTime = prevTime >= time ? 0 : prevTime;
        double deltaTime = time - prevTime;
        prevTime = time;

        Vector3f direction = new Vector3f(0, 0, 0);
        if (time < 60) {
            direction.x = Model.xToCoord(133.56);
        } else if (time < 120) {
            direction.y = Model.yToCoord(-152.23);
        } else if (time < 180) {
            direction.x = Model.xToCoord(-133.56);
        } else if (time < 240) {
            direction.y = Model.yToCoord(152.23);
        }
        direction.mul((float) deltaTime);
        background.movePosition(direction);
    }
}
