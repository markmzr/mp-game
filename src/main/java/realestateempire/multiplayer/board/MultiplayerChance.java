package realestateempire.multiplayer.board;

import java.lang.reflect.Type;

import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;

import realestateempire.multiplayer.server.GameSession;
import realestateempire.singleplayer.Game;
import realestateempire.singleplayer.board.chance.Chance;
import realestateempire.singleplayer.board.chance.ChanceGraphics;
import realestateempire.singleplayer.player.Player;

import static realestateempire.singleplayer.TurnAction.END_TURN;
import static realestateempire.singleplayer.TurnAction.NONE;

public class MultiplayerChance extends Chance implements StompFrameHandler {

    private final GameSession gameSession;
    private Player player;
    private Game game;

    public MultiplayerChance(Player[] players, ChanceGraphics chanceGraphics,
                             GameSession gameSession) {
        super(players, chanceGraphics);
        this.gameSession = gameSession;
        String destination = "/topic/game/" + gameSession.getGameId() + "/chance";
        gameSession.subscribe(destination, this);
    }

    @Override
    public void landPlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
        game.setTurnAction(NONE);
        gameSession.send("chance", new CardEvent());
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
        chanceGraphics.enableCard(card);
    }
}
