package realestateempire.multiplayer.board.property;


import realestateempire.singleplayer.board.property.Property;

public enum PropertyAction {

    BUY_PROPERTY {
        @Override
        void takeAction(Property property) {
            property.buyProperty();
        }
    },
    SELL_PROPERTY {
        @Override
        void takeAction(Property property) {
            property.sellProperty();
        }
    },
    MORTGAGE {
        @Override
        void takeAction(Property property) {
            property.mortgage();
        }
    },
    PAY_MORTGAGE {
        @Override
        void takeAction(Property property) {
            property.payMortgage();
        }
    };

    abstract void takeAction(Property property);
}
