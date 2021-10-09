package realestateempire.multiplayer.board;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import realestateempire.multiplayer.server.GameSession;
import realestateempire.singleplayer.Game;
import realestateempire.singleplayer.board.communitychest.CommunityChest;
import realestateempire.singleplayer.board.communitychest.CommunityChestGraphics;
import realestateempire.singleplayer.player.Player;

import static realestateempire.singleplayer.TurnAction.END_TURN;
import static realestateempire.singleplayer.TurnAction.NONE;

public class MultiplayerCommunityChest extends CommunityChest implements StompFrameHandler {

    private final GameSession gameSession;
    private Player player;
    private Game game;

    public MultiplayerCommunityChest(Player[] players,
                                     CommunityChestGraphics communityChestGraphics,
                                     GameSession gameSession) {
        super(players, communityChestGraphics);
        this.gameSession = gameSession;
        String destination = "/topic/game/" + gameSession.getGameId()
                + "/community-chest";
        gameSession.subscribe(destination, this);
    }

    @Override
    public void landPlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
        game.setTurnAction(NONE);
        gameSession.send("community-chest", new CardEvent());
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return CardEvent.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        CardEvent cardEvent = (CardEvent) payload;
        takeCard(cardEvent.getCard());
    }

    private void takeCard(int card) {
        this.card = card;
        game.setTurnAction(END_TURN);
        cards[card].getCard(player, game);
        communityChestGraphics.enableCard(card);
    }
}
