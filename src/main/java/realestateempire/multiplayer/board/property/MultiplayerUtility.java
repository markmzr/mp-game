package realestateempire.multiplayer.board.property;

import realestateempire.multiplayer.server.GameSession;
import realestateempire.singleplayer.board.property.PropertyGraphics;
import realestateempire.singleplayer.board.property.Utility;

import static realestateempire.multiplayer.board.property.PropertyAction.*;

public class MultiplayerUtility extends Utility {

    private final GameSession gameSession;
    private final int location;

    public MultiplayerUtility(String[] propertyData,
                              PropertyGraphics propertyGraphics,
                              GameSession gameSession) {
        super(propertyData, propertyGraphics);
        this. gameSession = gameSession;
        location = Integer.parseInt(propertyData[0]);
    }

    @Override
    public void buyProperty() {
        super.buyProperty();
        gameSession.sendPlayerEvent(owner, "property-event",
                new PropertyEvent(BUY_PROPERTY, location));
    }

    @Override
    public void sellProperty() {
        super.sellProperty();
        gameSession.sendPlayerEvent(owner, "property-event",
                new PropertyEvent(SELL_PROPERTY, location));
    }

    @Override
    public void mortgage() {
        super.mortgage();
        gameSession.sendPlayerEvent(owner, "property-event",
                new PropertyEvent(MORTGAGE, location));
    }

    @Override
    public void payMortgage() {
        super.payMortgage();
        gameSession.sendPlayerEvent(owner, "property-event",
                new PropertyEvent(PAY_MORTGAGE, location));
    }
}
