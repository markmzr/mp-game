package realestateempire.screens;

import realestateempire.graphics.button.Button;
import realestateempire.graphics.button.ButtonAction;
import realestateempire.graphics.model.Model;

class MainMenu implements Screen {

    private final Background background;
    private final Model logo;
    private final Button singleplayer;
    private final Button multiplayer;
    private final Button quit;

    MainMenu(Background background, ButtonAction toGameSetup,
             ButtonAction toMultiplayerSetup, ButtonAction quitGame) {
        this.background = background;
        logo = new Model("Logo.png", 256, 100);

        String[] newGameTextures = { "Buttons/Singleplayer.png",
                "Buttons/Singleplayer M.png" };
        singleplayer = new Button(newGameTextures, 1070, 560);
        singleplayer.addAction(toGameSetup);

        String[] multiplayerTextures = { "Buttons/Multiplayer.png",
                "Buttons/Multiplayer M.png" };
        multiplayer = new Button(multiplayerTextures, 1070, 720);
        multiplayer.addAction(toMultiplayerSetup);

        String[] quitTextures = { "Buttons/Quit Large.png",
                "Buttons/Quit Large M.png" };
        quit = new Button(quitTextures, 1070, 880);
        quit.addAction(quitGame);
    }

    @Override
    public void render() {
        background.render();
        logo.render();
        singleplayer.render();
        multiplayer.render();
        quit.render();
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        singleplayer.cursorMoved(xCursor, yCursor);
        multiplayer.cursorMoved(xCursor, yCursor);
        quit.cursorMoved(xCursor, yCursor);
    }

    @Override
    public void buttonPressed(double xCursor, double yCursor) {
        singleplayer.buttonPressed(xCursor, yCursor);
        multiplayer.buttonPressed(xCursor, yCursor);
        quit.buttonPressed(xCursor, yCursor);
    }
}
