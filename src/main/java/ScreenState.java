import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class ScreenState {

    private long glWindow;
    private MenuScreen menuScreen;
    private SetupScreen setupScreen;
    private GameScreen gameScreen;
    private Screen currentScreen;

    public ScreenState(long glWindow) {
        this.glWindow = glWindow;
        menuScreen = new MenuScreen();
        setupScreen = new SetupScreen();
        gameScreen = new GameScreen();
        currentScreen = menuScreen;
    }

    public void setToMenuScreen() {
        menuScreen.setBackgroundPosition(setupScreen.getBackgroundPosition());
        menuScreen.setLastTime(setupScreen.getLastTime());
        currentScreen = menuScreen;
    }

    public void setToSetupScreen() {
        setupScreen.setBackgroundPosition(menuScreen.getBackgroundPosition());
        setupScreen.setLastTime(menuScreen.getLastTime());
        currentScreen = setupScreen;
    }

    public void setToGameScreen() {
        currentScreen = gameScreen;
    }

    public void quitGame() {
        glfwSetWindowShouldClose(glWindow, true);
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
}
