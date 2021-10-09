package realestateempire.singleplayer;

public enum TurnAction {

    NONE {
        @Override
        public void takeAction(Game game) {
        }
    },
    BEGIN_TURN {
        @Override
        public void takeAction(Game game) {
            game.beginTurn();
        }
    },
    ROLL_DICE {
        @Override
        public void takeAction(Game game) {
            game.rollDice();
        }
    },
    MOVE_TOKEN {
        @Override
        public void takeAction(Game game) {
            game.moveToken();
        }
    },
    END_TURN {
        @Override
        public void takeAction(Game game) {
            game.endTurn();
        }
    },
    SET_NEXT_TURN {
        @Override
        public void takeAction(Game game) {
            game.setNextTurn();
        }
    };

    public abstract void takeAction(Game game);
}
