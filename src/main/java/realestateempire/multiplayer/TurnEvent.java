package realestateempire.multiplayer;

import realestateempire.singleplayer.TurnAction;


public class TurnEvent {

    private TurnAction turnAction;
    private int dieLeft;
    private int dieRight;

    public TurnEvent() { }

    public TurnEvent(TurnAction turnAction) {
        this.turnAction = turnAction;
    }

    public TurnEvent(TurnAction turnAction, int dieLeft, int dieRight) {
        this.turnAction = turnAction;
        this.dieLeft = dieLeft;
        this.dieRight = dieRight;
    }

    public TurnAction getTurnAction() {
        return turnAction;
    }

    public int getDieLeft() {
        return dieLeft;
    }

    public int getDieRight() {
        return dieRight;
    }
}
