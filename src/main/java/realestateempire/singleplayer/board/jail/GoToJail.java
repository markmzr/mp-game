package realestateempire.singleplayer.board.jail;

import realestateempire.singleplayer.Game;
import realestateempire.singleplayer.board.Location;
import realestateempire.singleplayer.player.Player;

import static realestateempire.singleplayer.TurnAction.MOVE_TOKEN;
import static realestateempire.singleplayer.player.PlayerState.IN_JAIL;

public class GoToJail implements Location {

    @Override
    public void landPlayer(Player player, Game game) {
        player.setPlayerState(IN_JAIL);
        int moveLocations = (50 - player.getLocation()) % 40;
        game.setTurnAction(MOVE_TOKEN);
        game.setMoveCount(moveLocations);
    }
}
