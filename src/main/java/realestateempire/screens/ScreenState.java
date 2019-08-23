package realestateempire.screens;

import org.joml.Matrix4f;

import realestateempire.multiplayer.MultiplayerSession;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class ScreenState {

    private final long window;
    private final MenuScreen menuScreen;
    private final SetupScreen setupScreen;
    private final MultiplayerScreen mpScreen;
    private final GameScreen gameScreen;
    private Screen screen;

    public ScreenState(long window) {
        this.window = window;
        menuScreen = new MenuScreen();
        setupScreen = new SetupScreen();
        mpScreen = new MultiplayerScreen();
        gameScreen = new GameScreen();
        screen = menuScreen;
    }

    public void render() {
        screen.render();
    }

    public void cursorMoved(double xCursor, double yCursor) {
        screen.cursorMoved(xCursor, yCursor);
    }

    public void buttonPressed(double xCursor, double yCursor) {
        screen.buttonPressed(this, xCursor, yCursor);
    }

    public void setToMenuScreen() {
        menuScreen.setPrevTime(glfwGetTime());
        screen = menuScreen;
    }

    void setToMenuScreen(Matrix4f backgroundPosition, double prevTime) {
        menuScreen.setBackgroundPosition(backgroundPosition);
        menuScreen.setPrevTime(prevTime);
        screen = menuScreen;
    }

    void setToSetupScreen(Matrix4f backgroundPosition, double prevTime) {
        setupScreen.setBackgroundPosition(backgroundPosition);
        setupScreen.setPrevTime(prevTime);
        screen = setupScreen;
    }

    void setToMultiplayerScreen(Matrix4f backgroundPosition, double prevTime) {
        mpScreen.setBackgroundPosition(backgroundPosition);
        mpScreen.setPrevTime(prevTime);
        screen = mpScreen;
    }

    void setToGameScreen(int userToken) {
        gameScreen.initSingleplayerGame(userToken);
        screen = gameScreen;
    }

    void setToGameScreen(MultiplayerSession mpSession) {
        gameScreen.initMultiplayerGame(mpSession);
        screen = gameScreen;
    }

    public void quitGame() {
        glfwSetWindowShouldClose(window, true);
    }
}
