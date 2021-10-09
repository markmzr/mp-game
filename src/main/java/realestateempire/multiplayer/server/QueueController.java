package realestateempire.multiplayer.server;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import realestateempire.multiplayer.player.PlayerEvent;

@Controller
public class QueueController {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private ArrayList<GamePrincipal> principals;

    @Autowired
    private ArrayList<GameState> gameStates;

    private int gameId;

    @SubscribeMapping("/topic/game-queue")
    public void gameQueue(GamePrincipal principal) {
        principal.setGameId(gameId);
        principals.add(principal);
        if (principals.size() == 4) {
            setupGame();
        }
    }

    private void setupGame() {
        User[] users = new User[principals.size()];
        for (int i = 0; i < users.length; i++) {
            GamePrincipal userPrincipal = principals.get(i);
            userPrincipal.setUserId(i);
            String name = userPrincipal.getName();
            template.convertAndSendToUser(name, "/topic/game-queue", userPrincipal);
            users[i] = new User(name);
        }
        gameStates.add(gameId, new GameState(template, gameId, users));
        gameId++;
        principals.clear();
    }

    @EventListener
    public void handleUnsubscribeEvent(SessionUnsubscribeEvent event) {
        GamePrincipal principal = (GamePrincipal) event.getUser();
        principals.remove(principal);
    }

    @EventListener
    public void handleDisconnectEvent(SessionDisconnectEvent event) {
        GamePrincipal principal = (GamePrincipal) event.getUser();
        if (!principals.remove(principal)) {
            int userGameId = principal.getGameId();
            gameStates.get(userGameId).handleQuitGame(principal, new PlayerEvent());
        }
    }
}
