package realestateempire.singleplayer.player;

import realestateempire.singleplayer.board.property.Property;
import realestateempire.singleplayer.board.property.Railroad;
import realestateempire.singleplayer.board.property.Utility;
import realestateempire.singleplayer.board.street.Street;

import realestateempire.singleplayer.TurnAction;
import static realestateempire.singleplayer.TurnAction.ROLL_DICE;
import static realestateempire.singleplayer.TurnAction.SET_NEXT_TURN;
import static realestateempire.singleplayer.player.PlayerState.IN_JAIL;

public class AIPlayer extends Player {

    public AIPlayer(String name, Token token, PlayerGraphics playerGraphics) {
        super(name, token, playerGraphics);
    }

    @Override
    public void updateMoney(int moneyDelta) {
        super.updateMoney(moneyDelta);
        if (money < 0) {
            if (networth < -money) {
                declareBankruptcy();
            } else {
                payDebt();
            }
        }
    }

    @Override
    public TurnAction beginTurn() {
        playerGraphics.updateMoneyDelta(0);
        if (playerState == IN_JAIL) {
            payJailFine();
        }
        return ROLL_DICE;
    }

    @Override
    public TurnAction endTurn() {
        return SET_NEXT_TURN;
    }

    @Override
    public void canBuyProperty(Property property) {
        property.buyProperty();
    }

    private void payDebt() {
        for (Utility utility : utilities) {
            if (money >= 0) {
                break;
            }
            utility.mortgage();
        }
        for (Railroad railroad : railroads) {
            if (money >= 0) {
                break;
            }
            railroad.mortgage();
        }
        for (Street street : streets) {
            if (money >= 0) {
                break;
            }
            street.mortgage();
        }
    }
}
