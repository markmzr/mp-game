package realestateempire.screens;

import realestateempire.multiplayer.server.GameSession;
import realestateempire.screens.multiplayersetup.MultiplayerSetup;

import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static realestateempire.singleplayer.TurnAction.BEGIN_TURN;

public class ScreenState {

    private final long window;
    private final MainMenu mainMenu;
    private final SingleplayerSetup spSetup;
    private final MultiplayerSetup mpSetup;
    private final GameScreen gameScreen;
    private Screen screen;

    public ScreenState(long window) {
        this.window = window;
        Background background = new Background();
        mainMenu = new MainMenu(background, this::toGameSetup,
                this::toMultiplayerSetup, this::quitGame);
        spSetup = new SingleplayerSetup(background, this::toMainMenu,
                this::toSingleplayerGame);
        mpSetup = new MultiplayerSetup(background, this::toMainMenu,
                this::toMultiplayerGame);
        gameScreen = new GameScreen(this::toMainMenu, this::quitGame);
        screen = mainMenu;
    }

    public void render() {
        screen.render();
    }

    public void cursorMoved(double xCursor, double yCursor) {
        screen.cursorMoved(xCursor, yCursor);
    }

    public void buttonPressed(double xCursor, double yCursor) {
        screen.buttonPressed(xCursor, yCursor);
    }

    private void toMainMenu() {
        screen = mainMenu;
    }

    private void toGameSetup() {
        screen = spSetup;
    }

    private void toMultiplayerSetup() {
        screen = mpSetup;
    }

    private void toSingleplayerGame() {
        gameScreen.initSingleplayerGame(spSetup.getUserToken());
        screen = gameScreen;
    }

    private void toMultiplayerGame() {
        GameSession gameSession = mpSetup.getGameSession();
        gameScreen.initMultiplayerGame(gameSession);
        screen = gameScreen;
        gameSession.send("turn-action", BEGIN_TURN);
    }

    private void quitGame() {
        glfwSetWindowShouldClose(window, true);
    }
}
