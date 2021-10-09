package realestateempire.screens.multiplayersetup;

import realestateempire.graphics.button.Button;
import realestateempire.graphics.button.ButtonAction;
import realestateempire.graphics.model.Model;
import realestateempire.multiplayer.server.GameSession;
import realestateempire.screens.Background;
import realestateempire.screens.Screen;

public class MultiplayerSetup implements Screen {

    private final Background background;
    private final Model multiplayer;
    private final Button mainMenu;
    private final QueueGraphics queueGraphics;
    private final MultiplayerQueue mpQueue;

    public MultiplayerSetup(Background background, ButtonAction toMainMenu,
                            ButtonAction toMultiplayerGame) {
        this.background = background;
        multiplayer = new Model("Multiplayer.png", 746, 159);

        String[] mainMenuTextures = { "Buttons/Main Menu.png",
                "Buttons/Main Menu M.png" };
        mainMenu = new Button(mainMenuTextures, 100, 1248);
        mainMenu.addAction(toMainMenu);
        mainMenu.addAction(this::dequeue);

        String[] findGameTextures = {"Buttons/Find Game.png",
                "Buttons/Find Game M.png", "Buttons/Find Game D.png"};
        Button findGame = new Button(findGameTextures, 1070, 954);

        String[] stopSearchTextures = {"Buttons/Stop Search.png",
                "Buttons/Stop Search M.png", "Buttons/Stop Search D.png"};
        Button stopSearch = new Button(stopSearchTextures, 1070, 954);
        queueGraphics = new QueueGraphics(findGame, stopSearch, toMultiplayerGame);

        mpQueue = new MultiplayerQueue(queueGraphics);
        findGame.addAction(mpQueue::queueForGame);
        stopSearch.addAction(mpQueue::dequeue);
    }

    public GameSession getGameSession() {
        return mpQueue.getGameSession();
    }

    @Override
    public void render() {
        background.render();
        multiplayer.render();
        mainMenu.render();
        queueGraphics.render();
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        mainMenu.cursorMoved(xCursor, yCursor);
        queueGraphics.cursorMoved(xCursor, yCursor);
    }

    @Override
    public void buttonPressed(double xCursor, double yCursor) {
        mainMenu.buttonPressed(xCursor, yCursor);
        queueGraphics.buttonPressed(xCursor, yCursor);
    }

    private void dequeue() {
        mainMenu.setEnabled(true);
        mpQueue.dequeue();
        queueGraphics.dequeue();
    }
}
