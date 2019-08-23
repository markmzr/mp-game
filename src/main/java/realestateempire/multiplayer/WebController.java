package realestateempire.multiplayer;

import java.security.Principal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import org.springframework.messaging.simp.annotation.*;

import static realestateempire.multiplayer.MultiplayerEvent.GameEvent.PLAYER_DISCONNECT;

@Controller
public class WebController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private HashMap<String, Integer> playerToGameId;

    private String player;
    private int gameId;
    private int playerCount;

    @MessageMapping("/game/{gameId}/{playerId}")
    @SendTo("/topic/game/{gameId}/{playerId}")
    public MultiplayerEvent handleMultiplayerEvent(MultiplayerEvent mpEvent) {
        return mpEvent;
    }

    @SubscribeMapping("/topic/find-game")
    public void findGame(Principal principal) {
        playerCount++;
        if (playerCount < 2) {
            player = principal.getName();
        } else {
            String player2 = principal.getName();
            playerToGameId.put(player, gameId);
            playerToGameId.put(player2, gameId);
            String destination = "/topic/find-game";
            MultiplayerId playerId = new MultiplayerId(gameId, 0);
            MultiplayerId player2Id = new MultiplayerId(gameId, 1);
            template.convertAndSendToUser(player, destination, playerId);
            template.convertAndSendToUser(player2, destination, player2Id);
            gameId++;
            playerCount = 0;
        }
    }

    @EventListener(SessionUnsubscribeEvent.class)
    public void handleUnsubscribeEvent(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = accessor.getDestination();
        if (destination.equals("/topic/find-game")) {
            playerCount--;
        }
    }

    @EventListener(SessionDisconnectEvent.class)
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        String player = event.getUser().getName();
        if (playerToGameId.containsKey(player)) {
            int playerGameId = playerToGameId.remove(player);
            String destination = "/topic/game/" + playerGameId;
            MultiplayerEvent mpEvent = new MultiplayerEvent(PLAYER_DISCONNECT);
            template.convertAndSend(destination + "/0", mpEvent);
            template.convertAndSend(destination + "/1", mpEvent);
        } else {
            playerCount--;
        }
    }
}
