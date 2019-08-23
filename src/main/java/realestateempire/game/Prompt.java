package realestateempire.game;

import realestateempire.graphics.Button;
import realestateempire.graphics.Model;
import realestateempire.multiplayer.MultiplayerEvent;
import realestateempire.multiplayer.MultiplayerGame;
import realestateempire.screens.ScreenState;

import static realestateempire.graphics.Button.ButtonState.DISABLED;
import static realestateempire.graphics.Button.ButtonState.ENABLED;
import static realestateempire.multiplayer.MultiplayerEvent.GameEvent.PLAYER_DISCONNECT;

public class Prompt {

    private final Game game;
    private final ViewProperty viewProperty;
    private final BuyProperty buyProperty;
    private final CommunityChest communityChest;
    private final Chance chance;
    private final InJail inJail;
    private final QuitMenu quitMenu;
    private final PlayerDisconnect playerDisconnect;
    private GamePrompt gamePrompt;
    private boolean visible;

    Prompt(Game game) {
        this.game = game;
        viewProperty = new ViewProperty();
        buyProperty = new BuyProperty();
        communityChest = new CommunityChest();
        chance = new Chance();
        inJail = new InJail();
        quitMenu = new QuitMenu();
        playerDisconnect = new PlayerDisconnect();
        gamePrompt = buyProperty;
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void render() {
        if (visible) {
            gamePrompt.render();
        }
    }

    public void cursorMoved(double xCursor, double yCursor) {
        if (visible) {
            gamePrompt.cursorMoved(xCursor, yCursor);
        }
    }

    public void buttonPressed(double xCursor, double yCursor) {
        if (visible) {
            gamePrompt.buttonPressed(xCursor, yCursor);
        }
    }

    void setViewProperty(Property property) {
        viewProperty.sellProperty.setButtonState(property.canSellProperty());
        viewProperty.buyHouse.setButtonState(property.canBuyHouse());
        viewProperty.sellHouse.setButtonState(property.canSellHouse());
        viewProperty.property = property;
        gamePrompt = viewProperty;
        visible = true;
    }

    void setBuyProperty() {
        gamePrompt = buyProperty;
        visible = true;
    }

    public void setCommunityChest(int card) {
        communityChest.background.setTexture(card);
        gamePrompt = communityChest;
        visible = true;
    }

    public void setChance(int card) {
        chance.background.setTexture(card);
        gamePrompt = chance;
        visible = true;
    }

    public void setInJail() {
        gamePrompt = inJail;
        visible = true;
    }

    public void setQuitMenu(ScreenState screenState) {
        quitMenu.screenState = screenState;
        gamePrompt = quitMenu;
        visible = true;
    }

    public void setPlayerDisconnect() {
        gamePrompt = playerDisconnect;
        visible = true;
    }

    private abstract class GamePrompt {

        abstract void render();
        abstract void cursorMoved(double xCursor, double yCursor);
        abstract void buttonPressed(double xCursor, double yCursor);
    }

    private class ViewProperty extends GamePrompt {

        private final Model background;
        private final Button sellProperty;
        private final Button buyHouse;
        private final Button sellHouse;
        private final Button close;
        private Property property;

        ViewProperty() {
            background = new Model("Prompts/View Property.png", 1491, 711);
            String[] sellPropertyTextures = { "Buttons/Sell Property.png",
                    "Buttons/Sell Property M.png", "Buttons/Sell Property D.png" };
            sellProperty = new Button(sellPropertyTextures, 1633, 998);

            String[] buyHouseTextures = { "Buttons/Buy House.png",
                    "Buttons/Buy House M.png", "Buttons/Buy House D.png" };
            buyHouse = new Button(buyHouseTextures, 2062, 998);

            String[] sellHouseTextures = { "Buttons/Sell House.png",
                    "Buttons/Sell House M.png", "Buttons/Sell House D.png" };
            sellHouse = new Button(sellHouseTextures, 1633, 1115);

            String[] closeTextures = { "Buttons/Close.png", "Buttons/Close M.png"};
            close = new Button(closeTextures, 2062, 1115);
        }

        @Override
        void render() {
            background.render();
            sellProperty.render();
            buyHouse.render();
            sellHouse.render();
            close.render();
        }

        @Override
        void cursorMoved(double xCursor, double yCursor) {
            sellProperty.isMouseover(xCursor, yCursor);
            buyHouse.isMouseover(xCursor, yCursor);
            sellHouse.isMouseover(xCursor, yCursor);
            close.isMouseover(xCursor, yCursor);
        }

        @Override
        void buttonPressed(double xCursor, double yCursor) {
            if (close.isMouseover(xCursor, yCursor)) {
                property.getButton().setButtonState(ENABLED);
                close.setButtonState(ENABLED);
                visible = false;
            }
            if (sellProperty.isMouseover(xCursor, yCursor)) {
                game.sellProperty();
                sellProperty.setButtonState(DISABLED);
                buyHouse.setButtonState(DISABLED);
                sellHouse.setButtonState(DISABLED);
            }
            if (buyHouse.isMouseover(xCursor, yCursor)) {
                game.buyHouse();
                sellProperty.setButtonState(DISABLED);
                buyHouse.setButtonState(property.canBuyHouse());
                sellHouse.setButtonState(ENABLED);
            }
            if (sellHouse.isMouseover(xCursor, yCursor)) {
                game.sellHouse();
                sellProperty.setButtonState(property.canSellProperty());
                buyHouse.setButtonState(property.canBuyHouse());
                sellHouse.setButtonState(property.canSellHouse());
            }
        }
    }

    private class BuyProperty extends GamePrompt {

        private final Model background;
        private final Button yes;
        private final Button no;

        BuyProperty() {
            background = new Model("Prompts/Buy Property.png", 1491, 830);
            String[] yesTextures = { "Buttons/Yes.png", "Buttons/Yes M.png" };
            yes = new Button(yesTextures, 1634, 995);

            String[] noTextures = { "Buttons/No.png", "Buttons/No M.png" };
            no = new Button(noTextures, 2061, 995);
        }

        @Override
        public void render() {
            background.render();
            yes.render();
            no.render();
        }

        @Override
        public void cursorMoved(double xCursor, double yCursor) {
            yes.isMouseover(xCursor, yCursor);
            no.isMouseover(xCursor, yCursor);
        }

        @Override
        public void buttonPressed(double xCursor, double yCursor) {
            if (yes.isMouseover(xCursor, yCursor)) {
                yes.setButtonState(ENABLED);
                visible = false;
                game.buyProperty();
            }
            if (no.isMouseover(xCursor, yCursor)) {
                no.setButtonState(ENABLED);
                visible = false;
                game.turnCompleted();
            }
        }
    }

    private class CommunityChest extends GamePrompt {

        private Model background;
        private final Button ok;

        CommunityChest() {
            String[] communityChestTextures = new String[16];
            for (int i = 0; i < 16; i++) {
                communityChestTextures[i] = "Community Chest/Community Chest " + (i + 1) + ".png";
            }
            background = new Model(communityChestTextures, 1589, 711);
            String[] closeTextures = { "Buttons/OK.png", "Buttons/OK M.png" };
            ok = new Button(closeTextures, 1847, 1161);
        }

        @Override
        void render() {
            background.render();
            ok.render();
        }

        @Override
        void cursorMoved(double xCursor, double yCursor) {
            ok.isMouseover(xCursor, yCursor);
        }

        @Override
        void buttonPressed(double xCursor, double yCursor) {
            if (ok.isMouseover(xCursor, yCursor)) {
                ok.setButtonState(ENABLED);
                visible = false;
            }
        }
    }

    private class Chance extends GamePrompt {

        private Model background;
        private final Button ok;

        Chance() {
            String[] chanceTextures = new String[14];
            for (int i = 0; i < 14; i++) {
                chanceTextures[i] = "Chance/Chance " + (i + 1) + ".png";
            }
            background = new Model(chanceTextures, 1589, 711);
            String[] closeTextures = { "Buttons/OK.png", "Buttons/OK M.png" };
            ok = new Button(closeTextures, 1847, 1161);
        }

        @Override
        void render() {
            background.render();
            ok.render();
        }

        @Override
        void cursorMoved(double xCursor, double yCursor) {
            ok.isMouseover(xCursor, yCursor);
        }

        @Override
        void buttonPressed(double xCursor, double yCursor) {
            if (ok.isMouseover(xCursor, yCursor)) {
                ok.setButtonState(ENABLED);
                visible = false;
            }
        }
    }

    private class InJail extends GamePrompt {

        private final Model background;
        private final Button payFine;

        InJail() {
            background = new Model("Prompts/In Jail.png", 1491, 830);
            String[] payFineTextures = { "Buttons/Pay Fine.png", "Buttons/Pay Fine M.png" };
            payFine = new Button(payFineTextures, 1847, 995);
        }

        @Override
        void render() {
            background.render();
            payFine.render();
        }

        @Override
        void cursorMoved(double xCursor, double yCursor) {
            payFine.isMouseover(xCursor, yCursor);
        }

        @Override
        void buttonPressed(double xCursor, double yCursor) {
            if (payFine.isMouseover(xCursor, yCursor)) {
                payFine.setButtonState(ENABLED);
                visible = false;
                game.payJailFine();
            }
        }
    }

    private class PlayerDisconnect extends GamePrompt {

        private final Model background;
        private final Button ok;

        PlayerDisconnect() {
            background = new Model("Prompts/Player Disconnect Background.png", 0, 0);
            String[] closeTextures = { "Buttons/OK.png", "Buttons/OK M.png" };
            ok = new Button(closeTextures, 1128, 734);
        }

        @Override
        void render() {
            background.render();
            ok.render();
        }

        @Override
        void cursorMoved(double xCursor, double yCursor) {
            ok.isMouseover(xCursor, yCursor);
        }

        @Override
        void buttonPressed(double xCursor, double yCursor) {
            if (ok.isMouseover(xCursor, yCursor)) {
                background.setVisible(false);
                ok.setButtonState(ENABLED);
                ok.setVisible(false);
            }
        }
    }

    private class QuitMenu extends GamePrompt {

        private final Model background;
        private final Button mainMenu;
        private final Button desktop;
        private final Button cancel;
        private ScreenState screenState;

        QuitMenu() {
            background = new Model("Prompts/Quit Menu Background.png", 0, 0);

            String[] mainMenuTextures = { "Prompts/Main Menu Small.png", "Prompts/Main Menu Small M.png" };
            mainMenu = new Button(mainMenuTextures, 1128, 594);

            String[] desktopTextures = { "Prompts/Desktop.png", "Prompts/Desktop M.png" };
            desktop = new Button(desktopTextures,  1128, 715);

            String[] cancelTextures = { "Prompts/Cancel.png", "Prompts/Cancel M.png" };
            cancel = new Button(cancelTextures,  1128, 836);
        }

        @Override
        void render() {
            background.render();
            mainMenu.render();
            desktop.render();
            cancel.render();
        }

        @Override
        void cursorMoved(double xCursor, double yCursor) {
            mainMenu.isMouseover(xCursor, yCursor);
            desktop.isMouseover(xCursor, yCursor);
            cancel.isMouseover(xCursor, yCursor);
        }

        @Override
        void buttonPressed(double xCursor, double yCursor) {
            if (mainMenu.isMouseover(xCursor, yCursor)) {
                mainMenu.setButtonState(ENABLED);
                visible = false;
                screenState.setToMenuScreen();
                if (game instanceof MultiplayerGame) {
                    MultiplayerEvent mpEvent = new MultiplayerEvent(PLAYER_DISCONNECT);
                    ((MultiplayerGame) game).sendEvent(mpEvent);
                }
            }
            if (desktop.isMouseover(xCursor, yCursor)) {
                screenState.quitGame();
            }
            if (cancel.isMouseover(xCursor, yCursor)) {
                cancel.setButtonState(ENABLED);
                visible = false;
            }
        }
    }
}
