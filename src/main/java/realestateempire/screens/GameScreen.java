package realestateempire.screens;

import java.util.ArrayList;

import realestateempire.graphics.Graphic;
import realestateempire.graphics.Interactive;
import realestateempire.graphics.button.Button;
import realestateempire.graphics.button.ButtonAction;
import realestateempire.graphics.model.Model;
import realestateempire.multiplayer.MultiplayerGame.MultiplayerGameBuilder;
import realestateempire.multiplayer.MultiplayerQuitMenu;
import realestateempire.multiplayer.server.GameSession;
import realestateempire.singleplayer.Game;
import realestateempire.singleplayer.Game.GameBuilderGraphics;
import realestateempire.singleplayer.GameGraphics;
import realestateempire.singleplayer.QuitMenu;
import realestateempire.singleplayer.SingleplayerGame.SingleplayerGameBuilder;
import realestateempire.singleplayer.player.Token;

public class GameScreen implements Screen {

    private final Model background;
    private final Button quit;
    private final ButtonAction selectMainMenu;
    private final ButtonAction quitGame;
    private final ArrayList<Graphic> graphics;
    private final ArrayList<Interactive>[] interactives;
    private Game game;

    GameScreen(ButtonAction selectMainMenu, ButtonAction quitGame) {
        this.selectMainMenu = selectMainMenu;
        this.quitGame = quitGame;
        background = new Model("Background.png", 0, 0);
        String[] quitTextures = { "Buttons/Quit.png", "Buttons/Quit M.png",
                "Buttons/Quit.png" };
        quit = new Button(quitTextures, 2203, 1303);
        graphics = new ArrayList<>();
        interactives = new ArrayList[5];
        for (int i = 0; i < interactives.length; i++) {
            interactives[i] = new ArrayList<>();
        }
    }

    @Override
    public void render() {
        background.render();
        game.render();
        quit.render();
        for (Graphic graphic : graphics) {
            graphic.render();
        }
        for (ArrayList<Interactive> interactiveList : interactives) {
            for (Interactive interactive : interactiveList) {
                interactive.render();
            }
        }
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        quit.cursorMoved(xCursor, yCursor);
        for (ArrayList<Interactive> interactiveList : interactives) {
            for (Interactive interactive : interactiveList) {
                interactive.cursorMoved(xCursor, yCursor);
            }
        }
    }

    @Override
    public void buttonPressed(double xCursor, double yCursor) {
        quit.buttonPressed(xCursor, yCursor);
        for (int i = interactives.length - 1; i >= 0; i--) {
            for (int j = 0; j < interactives[i].size(); j++) {
                if (interactives[i].get(j).buttonPressed(xCursor, yCursor)) {
                    return;
                }
            }
        }
    }

    void initSingleplayerGame(Token userToken) {
        clearGraphics();
        GameGraphics gameGraphics = new GameGraphics(interactives);
        GameBuilderGraphics gameBuilderGraphics = new GameBuilderGraphics(gameGraphics);
        game = new SingleplayerGameBuilder(userToken, gameBuilderGraphics, this).build();
        QuitMenu quitMenu = new QuitMenu(selectMainMenu, quitGame);
        addQuitAction(quitMenu);
        interactives[0].add(gameBuilderGraphics);
        interactives[4].add(quitMenu);
    }

    void initMultiplayerGame(GameSession gameSession) {
        clearGraphics();
        GameGraphics gameGraphics = new GameGraphics(interactives);
        GameBuilderGraphics gameBuilderGraphics = new GameBuilderGraphics(gameGraphics);
        game = new MultiplayerGameBuilder(gameBuilderGraphics, this, gameSession).build();
        QuitMenu quitMenu = new MultiplayerQuitMenu(selectMainMenu, quitGame, gameSession);
        addQuitAction(quitMenu);
        interactives[0].add(gameBuilderGraphics);
        interactives[4].add(quitMenu);
    }

    public void add(Interactive interactive) {
        interactives[0].add(interactive);
    }

    public void add(Interactive interactive, int index) {
        interactives[index].add(interactive);
    }

    private void clearGraphics() {
        graphics.clear();
        for (ArrayList<Interactive> interactiveList : interactives) {
            interactiveList.clear();
        }
    }

    private void addQuitAction(QuitMenu quitMenu) {
        quit.addAction(() -> quitMenu.setEnabled(true));
    }
}
