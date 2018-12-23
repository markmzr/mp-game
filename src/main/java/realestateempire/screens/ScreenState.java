package realestateempire.screens;

import org.joml.Matrix4f;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class ScreenState {

    private final long glWindow;
    private final MenuScreen menuScreen;
    private final SetupScreen setupScreen;
    private final GameScreen gameScreen;
    private Screen currentScreen;

    public ScreenState(long glWindow) {
        this.glWindow = glWindow;
        menuScreen = new MenuScreen();
        MenuScreen menuScreen = new MenuScreen();
        setupScreen = new SetupScreen();
        gameScreen = new GameScreen();
        currentScreen = menuScreen;
    }

    public void render() {
        currentScreen.render();
    }

    public void cursorMoved(double cursorXCoord, double cursorYCoord) {
        currentScreen.cursorMoved(cursorXCoord, cursorYCoord);
    }

    public void buttonPressed(double cursorXCoord, double cursorYCoord) {
        currentScreen.buttonPressed(this, cursorXCoord, cursorYCoord);
    }

    public void setToMenuScreen(Matrix4f backgroundPosition, double prevTime) {
        menuScreen.setBackgroundPosition(backgroundPosition);
        menuScreen.setPrevTime(prevTime);
        currentScreen = menuScreen;
    }

    public void setToSetupScreen(Matrix4f backgroundPosition, double prevTime) {
        setupScreen.setBackgroundPosition(backgroundPosition);
        setupScreen.setPrevTime(prevTime);
        currentScreen = setupScreen;
    }

    public void setToGameScreen(int tokenVal) {
        gameScreen.setPlayerTokens(tokenVal);
        currentScreen = gameScreen;
    }

    public void quitGame() {
        glfwSetWindowShouldClose(glWindow, true);
    }
}
