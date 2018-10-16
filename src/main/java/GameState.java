import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;

public class GameState {

    private MainMenu mainMenu;
    private SetupGame setupGame;
    private ScreenOption currentScreen;

    public GameState() {
        mainMenu = new MainMenu();
        setupGame = new SetupGame();
        currentScreen = ScreenOption.MAIN_MENU;
    }

    public void render(long window) {
        switch (currentScreen) {
            case MAIN_MENU:
                mainMenu.render();
                break;
            case SETUP_GAME:
                setupGame.render();
                break;
            case QUIT:
                glfwSetWindowShouldClose(window, true);
        }
    }

    public void buttonPressed(double cursorXCoord, double cursorYCoord) {
        if (currentScreen == ScreenOption.MAIN_MENU) {
            setupGame.setBackgroundPosition(mainMenu.getBackgroundPosition());
            setupGame.setLastTime(mainMenu.getLastTime());
            currentScreen = mainMenu.buttonPressed(cursorXCoord, cursorYCoord);
        }
        else if (currentScreen == ScreenOption.SETUP_GAME) {
            mainMenu.setBackgroundPosition(setupGame.getBackgroundPosition());
            mainMenu.setLastTime(setupGame.getLastTime());
            currentScreen = setupGame.buttonPressed(cursorXCoord, cursorYCoord);
        }
    }
}
