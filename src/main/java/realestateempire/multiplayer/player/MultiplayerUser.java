package realestateempire.multiplayer.player;

import realestateempire.multiplayer.server.GameSession;
import realestateempire.singleplayer.player.PlayerGraphics;
import realestateempire.singleplayer.player.Token;
import realestateempire.singleplayer.player.User;

public class MultiplayerUser extends User {

    private final GameSession gameSession;

    public MultiplayerUser(String name, Token token,
                           PlayerGraphics playerGraphics,
                           GameSession gameSession) {
        super(name, token, playerGraphics);
        this.gameSession = gameSession;
    }

    @Override
    public void payJailFine() {
        super.payJailFine();
        gameSession.send("pay-jail-fine", new PlayerEvent());
    }

    @Override
    public void declareBankruptcy() {
        super.declareBankruptcy();
        gameSession.send("declare-bankruptcy", new PlayerEvent());
    }
}
