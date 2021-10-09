package realestateempire.multiplayer.board.street;

import realestateempire.singleplayer.board.street.Street;

public enum StreetAction {

    BUY_PROPERTY {
        @Override
        void takeAction(Street street) {
            street.buyProperty();
        }
    },
    SELL_PROPERTY {
        @Override
        void takeAction(Street street) {
            street.sellProperty();
        }
    },
    BUY_HOUSE {
        @Override
        void takeAction(Street street) {
            street.buyHouse();
        }
    },
    SELL_HOUSE {
        @Override
        void takeAction(Street street) {
            street.sellHouse();
        }
    },
    MORTGAGE {
        @Override
        void takeAction(Street street) {
            street.mortgage();
        }
    },
    PAY_MORTGAGE {
        @Override
        void takeAction(Street street) {
            street.payMortgage();
        }
    };

    abstract void takeAction(Street street);
}
