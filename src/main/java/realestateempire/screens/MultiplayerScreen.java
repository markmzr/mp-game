package realestateempire.screens;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import realestateempire.graphics.Button;
import realestateempire.graphics.Model;
import realestateempire.multiplayer.MultiplayerEvent;
import realestateempire.multiplayer.MultiplayerSession;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static realestateempire.graphics.Button.ButtonState.*;
import static realestateempire.multiplayer.MultiplayerEvent.GameEvent.PLAYER_START;

public class MultiplayerScreen implements Screen {

    private final Model background;
    private final Model multiplayer;
    private final Model gameStatus;
    private final Model promptBackground;
    private final Button mainMenu;
    private final Button startGame;
    private final Button findGame;
    private final Button stopSearch;
    private final MultiplayerSession mpSession;
    private double prevTime;

    MultiplayerScreen() {
        background = new Model("Background Large.png", -8014, -9134);
        multiplayer = new Model("Multiplayer.png", 746, 159);

        String[] gameStatusTextures = { "Prompts/Searching For Game.png",
                "Prompts/Game Found 1.png", "Prompts/Game Found 2.png" };
        gameStatus = new Model(gameStatusTextures, 791, 572);
        gameStatus.setVisible(false);

        String[] mainMenuTextures = { "Buttons/Main Menu.png",
                "Buttons/Main Menu M.png" };
        mainMenu = new Button(mainMenuTextures, 100, 1248);

        String[] startGameTextures = { "Buttons/Start Game.png",
                "Buttons/Start Game M.png", "Buttons/Start Game D.png" };
        startGame = new Button(startGameTextures, 1070, 708);
        startGame.setButtonState(DISABLED);
        startGame.setVisible(false);

        String[] findGameTextures = { "Buttons/Find Game.png",
                "Buttons/Find Game M.png", "Buttons/Find Game D.png" };
        findGame = new Button(findGameTextures, 1070, 954);

        String[] stopSearchTextures = { "Buttons/Stop Search.png",
                "Buttons/Stop Search M.png", "Buttons/Stop Search D.png" };
        stopSearch = new Button(stopSearchTextures, 1070, 954);

        promptBackground = new Model("Prompts/Prompt Background.png", 771, 552);
        promptBackground.setVisible(false);

        stopSearch.setVisible(false);
        stopSearch.setButtonState(DISABLED);
        mpSession = new MultiplayerSession(this);
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
        promptBackground.render();
        multiplayer.render();
        gameStatus.render();
        findGame.render();
        stopSearch.render();
        mainMenu.render();
        startGame.render();
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        mainMenu.isMouseover(xCursor, yCursor);
        startGame.isMouseover(xCursor, yCursor);
        findGame.isMouseover(xCursor, yCursor);
        stopSearch.isMouseover(xCursor, yCursor);
    }

    @Override
    public void buttonPressed(ScreenState screenState, double xCursor, double yCursor) {
        if (mainMenu.isMouseover(xCursor, yCursor)) {
            mainMenu.setButtonState(ENABLED);
            findGame.setButtonState(ENABLED);
            findGame.setVisible(true);
            stopSearch.setButtonState(DISABLED);
            stopSearch.setVisible(false);
            gameStatus.setVisible(false);
            mpSession.stopFindingGame();
            screenState.setToMenuScreen(background.getPosition(), prevTime);
        }
        if (startGame.isMouseover(xCursor, yCursor)) {
            mpSession.sendEvent(new MultiplayerEvent(PLAYER_START));
            screenState.setToGameScreen(mpSession);
        }
        if (findGame.isVisible() && findGame.isMouseover(xCursor, yCursor)) {
            findGame.setVisible(false);
            findGame.setButtonState(DISABLED);
            promptBackground.setVisible(true);
            stopSearch.setButtonState(MOUSEOVER);
            stopSearch.setVisible(true);
            gameStatus.setTexture(0);
            gameStatus.setVisible(true);
            mpSession.findGame();
        } else if (stopSearch.isMouseover(xCursor, yCursor)) {
            promptBackground.setVisible(false);
            findGame.setButtonState(ENABLED);
            findGame.setVisible(true);
            stopSearch.setButtonState(DISABLED);
            stopSearch.setVisible(false);
            gameStatus.setVisible(false);
            mpSession.stopFindingGame();
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

    public void foundGame() {
        gameStatus.setTexture(mpSession.getUserId() + 1);
        stopSearch.setButtonState(DISABLED);
        startGame.setButtonState(ENABLED);
        startGame.setVisible(true);
    }
}
