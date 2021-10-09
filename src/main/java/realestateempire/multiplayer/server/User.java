package realestateempire.multiplayer.server;

import realestateempire.singleplayer.TurnAction;

import static realestateempire.singleplayer.TurnAction.NONE;

class User {

    private final String username;
    private TurnAction turnAction;
    private boolean requestedCard;
    private boolean active;

    User(String username) {
        this.username = username;
        turnAction = NONE;
        requestedCard = false;
        active = true;
    }

    void setTurnAction(TurnAction turnAction) {
        this.turnAction = turnAction;
    }

    void setRequestedCard(boolean requestedCard) {
        this.requestedCard = requestedCard;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    String getUsername() {
        return username;
    }

    TurnAction getTurnAction() {
        return turnAction;
    }

    boolean getRequestedCard() {
        return requestedCard;
    }

    public boolean isActive() {
        return active;
    }
}
