package realestateempire.multiplayer.board.street;

import realestateempire.multiplayer.server.GameSession;
import realestateempire.singleplayer.board.property.PropertyGraphics;
import realestateempire.singleplayer.board.street.Street;
import realestateempire.singleplayer.board.street.StreetGraphics;

import static realestateempire.multiplayer.board.street.StreetAction.*;

public class MultiplayerStreet extends Street {

    private final GameSession gameSession;
    private final int location;

    public MultiplayerStreet(String[] propertyData,
                             PropertyGraphics propertyGraphics,
                             StreetGraphics streetGraphics,
                             GameSession gameSession) {
        super(propertyData, propertyGraphics, streetGraphics);
        this.gameSession = gameSession;
        location = Integer.parseInt(propertyData[0]);
    }

    @Override
    public void buyProperty() {
        super.buyProperty();
        gameSession.sendPlayerEvent(owner, "street-event",
                new StreetEvent(BUY_PROPERTY, location));
    }

    @Override
    public void sellProperty() {
        super.sellProperty();
        gameSession.sendPlayerEvent(owner, "street-event",
                new StreetEvent(SELL_PROPERTY, location));
    }

    @Override
    public void buyHouse() {
        super.buyHouse();
        gameSession.sendPlayerEvent(owner, "street-event",
                new StreetEvent(BUY_HOUSE, location));
    }

    @Override
    public void sellHouse() {
        super.sellHouse();
        gameSession.sendPlayerEvent(owner, "street-event",
                new StreetEvent(SELL_HOUSE, location));
    }

    @Override
    public void mortgage() {
        super.mortgage();
        gameSession.sendPlayerEvent(owner, "street-event",
                new StreetEvent(MORTGAGE, location));
    }


    @Override
    public void payMortgage() {
        super.payMortgage();
        gameSession.sendPlayerEvent(owner, "street-event",
                new StreetEvent(PAY_MORTGAGE, location));
    }
}
