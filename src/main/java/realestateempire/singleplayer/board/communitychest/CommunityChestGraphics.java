package realestateempire.singleplayer.board.communitychest;

import realestateempire.graphics.Interactive;
import realestateempire.graphics.button.Button;
import realestateempire.graphics.model.Model;

public class CommunityChestGraphics implements Interactive {

    private final Model card;
    private final Button ok;
    private boolean enabled;

    public CommunityChestGraphics() {
        String[] communityChestTextures = new String[16];
        for (int i = 0; i < 16; i++) {
            communityChestTextures[i] =
                    "Community Chest/Community Chest " + (i + 1) + ".png";
        }
        card = new Model(communityChestTextures, 1589, 711);
        String[] closeTextures = { "Buttons/OK.png", "Buttons/OK M.png",
                "Buttons/OK.png" };
        ok = new Button(closeTextures, 1847, 1161);
        ok.addAction(() -> enabled = false);
        enabled = false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void render() {
        if (enabled) {
            card.render();
            ok.render();
        }
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        if (enabled) {
            ok.cursorMoved(xCursor, yCursor);
        }
    }

    @Override
    public boolean buttonPressed(double xCursor, double yCursor) {
        if (enabled) {
            return ok.buttonPressed(xCursor, yCursor);
        }
        return false;
    }

    public void enableCard(int card) {
        this.card.setTexture(card);
        enabled = true;
    }
}
