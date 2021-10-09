package realestateempire.screens.multiplayersetup;

import realestateempire.graphics.button.Button;
import realestateempire.graphics.button.ButtonAction;
import realestateempire.graphics.model.Model;
import realestateempire.graphics.text.CenteredText;

class QueueGraphics {

    private final Model background;
    private final Button findGame;
    private final Button stopSearch;
    private final Button startGame;
    private final CenteredText queueState;
    private boolean enabled;

    QueueGraphics(Button findGame, Button stopSearch, ButtonAction toMultiplayerGame) {
        this.findGame = findGame;
        this.stopSearch = stopSearch;
        findGame.addAction(this::queue);
        stopSearch.setVisible(false);
        stopSearch.setEnabled(false);
        stopSearch.addAction(this::dequeue);
        background = new Model("Prompts/Prompt Background.png", 771, 552);

        String[] startGameTextures = {"Buttons/Start Game.png",
                "Buttons/Start Game M.png", "Buttons/Start Game D.png"};
        startGame = new Button(startGameTextures, 1070, 708);
        startGame.setVisible(false);
        startGame.setEnabled(false);
        startGame.addAction(toMultiplayerGame);
        queueState = new CenteredText("Searching For Game...", 1280, 610);
    }

    public void render() {
        findGame.render();
        stopSearch.render();
        if (enabled) {
            background.render();
            startGame.render();
            queueState.render();
        }
    }

    public void cursorMoved(double xCursor, double yCursor) {
        findGame.cursorMoved(xCursor, yCursor);
        stopSearch.cursorMoved(xCursor, yCursor);
        startGame.cursorMoved(xCursor, yCursor);
    }

    public void buttonPressed(double xCursor, double yCursor) {
        if (findGame.isVisible()) {
            findGame.buttonPressed(xCursor, yCursor);
        } else {
            stopSearch.buttonPressed(xCursor, yCursor);
        }
        startGame.buttonPressed(xCursor, yCursor);
    }

    private void queue() {
        findGame.setVisible(false);
        findGame.setEnabled(false);
        stopSearch.setVisible(true);
        stopSearch.setEnabled(true);
        queueState.updateText("Searching for game...");
        enabled = true;
    }

    void dequeue() {
        findGame.setEnabled(true);
        findGame.setVisible(true);
        stopSearch.setEnabled(false);
        stopSearch.setVisible(false);
        enabled = false;
    }

    void receivedGame(int userId) {
        queueState.updateText("Game found. You are Player " + (userId + 1) + ".");
        stopSearch.setVisible(false);
        stopSearch.setEnabled(false);
        startGame.setVisible(true);
        startGame.setEnabled(true);
    }
}
