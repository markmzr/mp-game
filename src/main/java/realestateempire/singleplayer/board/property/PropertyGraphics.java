package realestateempire.singleplayer.board.property;

import realestateempire.graphics.Graphic;
import realestateempire.graphics.model.Model;
import realestateempire.singleplayer.player.Token;

import static realestateempire.singleplayer.player.Token.*;

public class PropertyGraphics implements Graphic {

    private final Model mortgage;
    private final Model ownerToken;

    public PropertyGraphics(PropertyGraphicsData pgData) {
        mortgage = new Model(pgData.getMortgageTexture(), pgData.getXMortgage(),
                pgData.getYMortgage());
        mortgage.setVisible(false);

        String[] tokenTextures = { HAT.getSmallTexture(), RED.getSmallTexture(),
                BLUE.getSmallTexture(), GREEN.getSmallTexture() };
        ownerToken = new Model(tokenTextures, pgData.getXOwnerIcon(),
                pgData.getYOwnerIcon());
        ownerToken.setVisible(false);
    }

    @Override
    public void render() {
        ownerToken.render();
        mortgage.render();
    }

    public void updateToken(Token token) {
        ownerToken.setTexture(token.ordinal());
        ownerToken.setVisible(true);
    }

    public void disableToken() {
        ownerToken.setVisible(false);
    }

    public void enableMortgage() {
        mortgage.setVisible(true);
    }

    public void disableMortgage() {
        mortgage.setVisible(false);
    }
}
