package realestateempire.singleplayer.player;

import realestateempire.graphics.Interactive;
import realestateempire.graphics.button.Button;
import realestateempire.graphics.model.Model;
import realestateempire.singleplayer.TurnAction;
import realestateempire.singleplayer.board.jail.InJail;
import realestateempire.singleplayer.board.property.BuyProperty;
import realestateempire.singleplayer.board.property.Property;

import static realestateempire.singleplayer.TurnAction.*;
import static realestateempire.singleplayer.player.PlayerState.ACTIVE;
import static realestateempire.singleplayer.player.PlayerState.IN_JAIL;

public class User extends Player {

    private TurnAction turnAction;
    private UserGraphics userGraphics;

    public User(String name, Token token, PlayerGraphics playerGraphics) {
        super(name, token, playerGraphics);
        turnAction = NONE;
    }

    @Override
    public void updateMoney(int moneyDelta) {
        super.updateMoney(moneyDelta);
        if (money < 0 && networth < -money) {
            declareBankruptcy();
        } else {
            userGraphics.updatePayDebt();
            userGraphics.updateRollDice();
            userGraphics.updateEndTurn();
        }
    }

    @Override
    public TurnAction beginTurn() {
        turnAction = BEGIN_TURN;
        playerGraphics.updateMoneyDelta(0);
        userGraphics.updateInJail();
        userGraphics.updatePayDebt();
        userGraphics.updateRollDice();
        return NONE;
    }

    @Override
    public TurnAction endTurn() {
        turnAction = END_TURN;
        userGraphics.updatePayDebt();
        userGraphics.updateEndTurn();
        return NONE;
    }

    @Override
    public void canBuyProperty(Property property) {
        userGraphics.updateBuyProperty(property);
    }

    @Override
    public void payJailFine() {
        super.payJailFine();
        userGraphics.updateInJail();
        userGraphics.updateRollDice();
    }

    public class UserGraphics implements Interactive {

        private final Model payDebt;
        private final Button rollDice;
        public final Button endTurn;
        public final BuyProperty buyProperty;
        public final InJail inJail;

        public UserGraphics(Button rollDice, Button endTurn) {
            this.rollDice = rollDice;
            this.endTurn = endTurn;
            rollDice.addAction(() -> turnAction = MOVE_TOKEN);
            endTurn.addAction(() -> turnAction = SET_NEXT_TURN);
            buyProperty = new BuyProperty(() -> endTurn.setEnabled(true));
            inJail = new InJail(User.this::payJailFine);
            payDebt = new Model("Prompts/Pay Debt.png", 1491, 830);
            payDebt.setVisible(false);
            userGraphics = this;
        }

        @Override
        public void setEnabled(boolean enabled) {
            buyProperty.setEnabled(false);
            inJail.setEnabled(false);
            payDebt.setVisible(false);
            rollDice.setEnabled(false);
            endTurn.setEnabled(false);
        }

        @Override
        public void render() {
            buyProperty.render();
            payDebt.render();
            inJail.render();
        }

        @Override
        public void cursorMoved(double xCursor, double yCursor) {
            buyProperty.cursorMoved(xCursor, yCursor);
            inJail.cursorMoved(xCursor, yCursor);
        }

        @Override
        public boolean buttonPressed(double xCursor, double yCursor) {
            return buyProperty.buttonPressed(xCursor, yCursor)
                    || inJail.buttonPressed(xCursor, yCursor);
        }

        private void updateBuyProperty(Property property) {
            buyProperty.updateProperty(property);
            buyProperty.setEnabled(true);
        }

        private void updateInJail() {
            inJail.setEnabled(playerState == IN_JAIL);
        }

        private void updatePayDebt() {
            payDebt.setVisible(turnAction != SET_NEXT_TURN && money < 0);
        }

        private void updateRollDice() {
            rollDice.setEnabled(playerState == ACTIVE
                    && turnAction == BEGIN_TURN && money >= 0);
        }

        private void updateEndTurn() {
            endTurn.setEnabled(turnAction == END_TURN && money >= 0
                    && !buyProperty.isEnabled());
        }
    }
}
