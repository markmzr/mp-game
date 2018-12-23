package realestateempire.game;

import realestateempire.graphics.Button;
import realestateempire.graphics.Model;

public class GamePrompt {

    private interface Prompt {
        void render();
        void cursorMoved(double cursorXCoord, double cursorYCoord);
        void buttonPressed(double cursorXCoord, double cursorYCoord);
    }

    private final GameState gameState;
    private final ViewProperty viewProperty;
    private final BuyProperty buyProperty;
    private final CommunityChest communityChest;
    private final Chance chance;
    private final InJail inJail;
    private Prompt currentPrompt;
    private boolean enabled;

    public GamePrompt(GameState gameState) {
        this.gameState = gameState;
        viewProperty = new ViewProperty();
        buyProperty = new BuyProperty();
        communityChest = new CommunityChest();
        chance = new Chance();
        inJail = new InJail();
        currentPrompt = viewProperty;
        enabled = false;
    }

    public void render() {
        if (enabled) {
            currentPrompt.render();
        }
    }

    public void cursorMoved(double cursorXCoord, double cursorYCoord) {
        if (enabled) {
            currentPrompt.cursorMoved(cursorXCoord, cursorYCoord);
        }
    }

    public void buttonPressed(double cursorXCoord, double cursorYCoord) {
        if (enabled) {
            currentPrompt.buttonPressed(cursorXCoord, cursorYCoord);
        }
    }

    public void setViewProperty(Property property) {
        viewProperty.sellProperty.setEnabled(property.canSellProperty());
        viewProperty.buyHouse.setEnabled(property.canBuildHouse());
        viewProperty.sellHouse.setEnabled(property.canSellHouse());
        viewProperty.property = property;
        currentPrompt = viewProperty;
        enabled = true;
    }

    public void setBuyProperty(Property property) {
        buyProperty.property = property;
        currentPrompt = buyProperty;
        enabled = true;
    }

    public void setCommunityChest(int card) {
        communityChest.card = card;
        currentPrompt = communityChest;
        enabled = true;
    }

    public void setChance(int card) {
        chance.card = card;
        currentPrompt = chance;
        enabled = true;
    }

    public void setInJail() {
        currentPrompt = inJail;
        enabled = true;
    }

    private class ViewProperty implements Prompt {

        private final Model viewProperty;
        private final Button sellProperty;
        private final Button buyHouse;
        private final Button sellHouse;
        private final Button close;
        private Property property;

        public ViewProperty() {
            viewProperty = new Model("Property Deed.png", 364, 255);
            String[] sellPropertyTextures = { "Buttons/Sell Property.png", "Buttons/Sell Property Highlighted.png", "Buttons/Sell Property Disabled.png" };
            sellProperty = new Button(sellPropertyTextures, 342, 990);

            String[] buyHouseTextures = { "Buttons/Buy House.png", "Buttons/Buy House Highlighted.png", "Buttons/Buy House Disabled.png" };
            buyHouse = new Button(buyHouseTextures, 793, 990);

            String[] sellHouseTextures = { "Buttons/Sell House.png", "Buttons/Sell House Highlighted.png", "Buttons/Sell House Disabled.png" };
            sellHouse = new Button(sellHouseTextures, 342, 1099);

            String[] closeTextures = { "Buttons/Close.png", "Buttons/Close Highlighted.png"};
            close = new Button(closeTextures, 793, 1099);
            property = null;
        }

        @Override
        public void render() {
            viewProperty.render();
            sellProperty.render();
            buyHouse.render();
            sellHouse.render();
            close.render();
        }

        @Override
        public void cursorMoved(double cursorXCoord, double cursorYCoord) {
            sellProperty.isCursorInRange(cursorXCoord, cursorYCoord);
            buyHouse.isCursorInRange(cursorXCoord, cursorYCoord);
            sellHouse.isCursorInRange(cursorXCoord, cursorYCoord);
            close.isCursorInRange(cursorXCoord, cursorYCoord);
        }

        @Override
        public void buttonPressed(double cursorXCoord, double cursorYCoord) {
            if (close.isCursorInRange(cursorXCoord, cursorYCoord)) {
                close.setHighlighted(false);
                enabled = false;
            }
            if (sellProperty.isCursorInRange(cursorXCoord, cursorYCoord)) {
                sellProperty.setEnabled(false);
                sellProperty.setHighlighted(false);
                buyHouse.setEnabled(false);
                sellHouse.setEnabled(false);
                property.sellProperty();
            }
            if (buyHouse.isCursorInRange(cursorXCoord, cursorYCoord)) {
                property.buyHouse();
                buyHouse.setEnabled(property.canBuildHouse());
                buyHouse.setHighlighted(property.canBuildHouse());
                sellProperty.setEnabled(false);
                sellHouse.setEnabled(true);
            }
            if (sellHouse.isCursorInRange(cursorXCoord, cursorYCoord)) {
                property.sellHouse();
                sellHouse.setEnabled(property.canSellHouse());
                sellHouse.setHighlighted(property.canSellHouse());
                sellProperty.setEnabled(property.canSellProperty());
                buyHouse.setEnabled(property.canBuildHouse());
            }
        }
    }

    private class BuyProperty implements Prompt {

        private final Model buyProperty;
        private final Button yes;
        private final Button no;
        private Property property;

        public BuyProperty() {
            buyProperty = new Model("Buy Property.png", 425, 959);
            String[] yesTextures = { "Buttons/Yes.png", "Buttons/Yes Highlighted.png" };
            yes = new Button(yesTextures, 342, 1096);

            String[] noTextures = { "Buttons/No.png", "Buttons/No Highlighted.png" };
            no = new Button(noTextures, 793, 1096);
            property = null;
        }

        @Override
        public void render() {
            buyProperty.render();
            yes.render();
            no.render();
        }

        @Override
        public void cursorMoved(double cursorXCoord, double cursorYCoord) {
            yes.isCursorInRange(cursorXCoord, cursorYCoord);
            no.isCursorInRange(cursorXCoord, cursorYCoord);
        }

        @Override
        public void buttonPressed(double cursorXCoord, double cursorYCoord) {
            if (yes.isCursorInRange(cursorXCoord, cursorYCoord)) {
                yes.setHighlighted(false);
                enabled = false;
                property.buyProperty();
            }
            if (no.isCursorInRange(cursorXCoord, cursorYCoord)) {
                no.setHighlighted(false);
                enabled = false;
                gameState.turnCompleted();
            }
        }
    }

    private class CommunityChest implements Prompt {

        private final Model communityChestCard;
        private final Button close;
        private int card;

        public CommunityChest() {
            String[] communityChestTextures = new String[16];
            for (int i = 0; i < 16; i++) {
                communityChestTextures[i] = "Community Chest/Community Chest " + (i + 1) + ".png";
            }
            communityChestCard = new Model(communityChestTextures, 256, 483);

            String[] closeTextures = { "Buttons/Close Large.png", "Buttons/Close Large Highlighted.png" };
            close = new Button(closeTextures, 568, 1021);
            card = 0;
        }

        @Override
        public void render() {
            communityChestCard.render(card);
            close.render();
        }

        @Override
        public void cursorMoved(double cursorXCoord, double cursorYCoord) {
            close.isCursorInRange(cursorXCoord, cursorYCoord);
        }

        @Override
        public void buttonPressed(double cursorXCoord, double cursorYCoord) {
            if (close.isCursorInRange(cursorXCoord, cursorYCoord)) {
                close.setHighlighted(false);
                enabled = false;
            }
        }
    }

    private class Chance implements Prompt {

        private final Model chanceCard;
        private final Button close;
        private int card;

        public Chance() {
            String[] chanceTextures = new String[14];
            for (int i = 0; i < 14; i++) {
                chanceTextures[i] = "Chance/Chance " + (i + 1) + ".png";
            }
            chanceCard = new Model(chanceTextures, 256, 483);

            String[] closeTextures = { "Buttons/Close Large.png", "Buttons/Close Large Highlighted.png" };
            close = new Button(closeTextures, 568, 1024);
            card = 0;
        }

        @Override
        public void render() {
            chanceCard.render(card);
            close.render();
        }

        @Override
        public void cursorMoved(double cursorXCoord, double cursorYCoord) {
            close.isCursorInRange(cursorXCoord, cursorYCoord);
        }

        @Override
        public void buttonPressed(double cursorXCoord, double cursorYCoord) {
            if (close.isCursorInRange(cursorXCoord, cursorYCoord)) {
                close.setHighlighted(false);
                enabled = false;
            }
        }
    }

    private class InJail implements Prompt {

        private final Model inJail;
        private final Button payFine;

        public InJail() {
            inJail = new Model("In Jail.png", 425, 959);
            String[] payFineTextures = { "Buttons/Pay Fine.png", "Buttons/Pay Fine Highlighted.png" };
            payFine = new Button(payFineTextures, 568, 1096);
        }

        @Override
        public void render() {
            inJail.render();
            payFine.render();
        }

        @Override
        public void cursorMoved(double cursorXCoord, double cursorYCoord) {
            payFine.isCursorInRange(cursorXCoord, cursorYCoord);
        }

        @Override
        public void buttonPressed(double cursorXCoord, double cursorYCoord) {
            if (payFine.isCursorInRange(cursorXCoord, cursorYCoord)) {
                payFine.setHighlighted(false);
                enabled = false;
                gameState.payJailFine();
            }
        }
    }
}
