package realestateempire.multiplayer;

import realestateempire.graphics.button.ButtonAction;
import realestateempire.multiplayer.player.PlayerEvent;
import realestateempire.multiplayer.server.GameSession;
import realestateempire.singleplayer.QuitMenu;

public class MultiplayerQuitMenu extends QuitMenu {

    private final GameSession gameSession;

    public MultiplayerQuitMenu(ButtonAction toMenu, ButtonAction quitGame,
                               GameSession gameSession) {
        super(toMenu, quitGame);
        this.gameSession = gameSession;
        mainMenu.addAction(this::quitGame);
        desktop.addAction(this::quitGame);
    }

    private void quitGame() {
        gameSession.send("quit-game", new PlayerEvent());
    }
}
