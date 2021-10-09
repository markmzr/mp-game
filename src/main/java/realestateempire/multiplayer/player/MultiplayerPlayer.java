package realestateempire.multiplayer.player;

import realestateempire.multiplayer.server.GameSession;
import realestateempire.singleplayer.TurnAction;
import realestateempire.singleplayer.board.property.Property;
import realestateempire.singleplayer.player.Player;
import realestateempire.singleplayer.player.PlayerGraphics;
import realestateempire.singleplayer.player.Token;

import static realestateempire.singleplayer.TurnAction.*;

public class MultiplayerPlayer extends Player {

    private final GameSession gameSession;

    public MultiplayerPlayer(String name, Token token,
                             PlayerGraphics playerGraphics,
                             GameSession gameSession) {
        super(name, token, playerGraphics);
        this. gameSession = gameSession;
    }

    @Override
    public void updateMoney(int moneyDelta) {
        super.updateMoney(moneyDelta);
        if (money < 0 && networth < -money) {
            declareBankruptcy();
        }
    }

    @Override
    public TurnAction beginTurn() {
        playerGraphics.updateMoneyDelta(0);
        gameSession.send("turn-action", ROLL_DICE);
        return NONE;
    }

    @Override
    public TurnAction endTurn() {
        gameSession.send("turn-action", END_TURN);
        return NONE;
    }

    @Override
    public void canBuyProperty(Property property) { }
}
