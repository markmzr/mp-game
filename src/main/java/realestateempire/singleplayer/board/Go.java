package realestateempire.singleplayer.board;

import realestateempire.singleplayer.Game;
import realestateempire.singleplayer.player.Player;

public class Go implements Location {

    @Override
    public void landPlayer(Player player, Game game) {
        player.updateMoney(400);
    }
}
