import org.joml.Vector3f;

public class GameScreen implements Screen {

    private GL2DObject boardBackground;
    private GL2DObject firstDie;
    private GL2DObject secondDie;
    private GL2DObject playerHighlight;
    private GLButton rollDice;
    private GLButton endTurn;
    private GLButton quit;
    private GamePrompt gamePrompt;
    private GameState gameState;

    public GameScreen() {
        boardBackground = new GL2DObject("Background.png", 0, 0, 2560, 1440);

        String[] dieTextures = new String[6];
        for (int i = 0; i < 6; i++) {
            dieTextures[i] = "Dice/Dice" + (i + 1) + ".png";
        }
        firstDie = new GL2DObject(dieTextures, 658, 694, 51, 51);
        secondDie = new GL2DObject(dieTextures, 730, 694, 51, 51);

        playerHighlight = new GL2DObject("Player Highlight.png", 1481, 41, 1038, 134);

        String[] rollDiceTextures = { "Buttons/Roll Dice.png",
                "Buttons/Roll Dice Highlighted.png", "Buttons/Roll Dice Disabled.png" };
        rollDice = new GLButton(rollDiceTextures, 1491, 1303, 305, 86);

        String[] endTurnTextures = { "Buttons/End Turn.png",
                "Buttons/End Turn Highlighted.png", "Buttons/End Turn Disabled.png" };
        endTurn = new GLButton(endTurnTextures, 1847, 1303, 305, 86);
        endTurn.setEnabled(false);

        String[] quitTextures = { "Buttons/Quit.png", "Buttons/Quit Highlighted.png" };
        quit = new GLButton(quitTextures, 2203, 1303, 305, 86);

        gamePrompt = new GamePrompt();
        gameState = new GameState(this);
    }

    public void setPlayerHighlight(int playerTurn) {
        Vector3f direction = new Vector3f(0f, 0f, 0f);
        direction.y = playerTurn == 0 ? 0.6875f : -0.22916f;
        playerHighlight.movePosition(direction);
    }

    public void setPromptState(PromptState promptState) {
        gamePrompt.promptState = promptState;
        if (promptState == PromptState.BUY_PROPERTY) {
            gamePrompt.currentPrompt = gamePrompt::buyingProperty;
        } else if (promptState == PromptState.COMMUNITY_CHEST) {
            gamePrompt.currentPrompt = gamePrompt::communityChest;
        } else if (promptState == PromptState.CHANCE) {
            gamePrompt.currentPrompt = gamePrompt::chance;
        } else {
            gamePrompt.currentPrompt = gamePrompt::jail;
        }
    }

    public PromptState getPromptState() {
        return gamePrompt.promptState;
    }

    public void enableRollDice() {
        rollDice.setHighlighted(false);
        rollDice.setEnabled(true);
    }

    public void enableEndTurn() {
        endTurn.setHighlighted(false);
        endTurn.setEnabled(true);
    }

    public void render() {
        boardBackground.render();
        firstDie.render(gameState.getDie1Val());
        secondDie.render(gameState.getDie2Val());
        playerHighlight.render();
        rollDice.render();
        endTurn.render();
        quit.render();
        gamePrompt.render();
        gameState.render();
    }

    public void cursorMoved(double cursorXCoord, double cursorYCoord) {
        rollDice.isCursorInRange(cursorXCoord, cursorYCoord);
        endTurn.isCursorInRange(cursorXCoord, cursorYCoord);
        quit.isCursorInRange(cursorXCoord, cursorYCoord);
        gamePrompt.cursorMoved(cursorXCoord, cursorYCoord);
    }

    public void buttonPressed(ScreenState screenState, double cursorXCoord, double cursorYCoord) {
        if (rollDice.isCursorInRange(cursorXCoord, cursorYCoord)) {
            rollDice.setHighlighted(false);
            rollDice.setEnabled(false);
            gameState.rollDice();
        }
        if (endTurn.isCursorInRange(cursorXCoord, cursorYCoord)) {
            endTurn.setHighlighted(false);
            endTurn.setEnabled(false);
            gameState.setNextTurn();
        }
        if (quit.isCursorInRange(cursorXCoord, cursorYCoord)) {
            screenState.quitGame();
        }
        gamePrompt.buttonPressed(screenState, cursorXCoord, cursorYCoord);
    }

    private class GamePrompt implements Screen {

        private GL2DObject buyProperty;
        private GL2DObject communityChestCard;
        private GL2DObject chanceCard;
        private GL2DObject inJail;
        private GLButton yes;
        private GLButton no;
        private GLButton close;
        private GLButton payFine;
        private Prompt currentPrompt;
        private PromptState promptState;

        private GamePrompt() {
            buyProperty = new GL2DObject("Buy Property.png", 430, 990, 579, 86);

            String[] communityChestTextures = new String[16];
            for (int i = 0; i < 16; i++) {
                communityChestTextures[i] = "Community Chest/Community Chest " + (i + 1) + ".png";
            }
            communityChestCard = new GL2DObject(communityChestTextures, 245, 471, 949, 498);

            String[] chanceTextures = new String[14];
            for (int i = 0; i < 14; i++) {
                chanceTextures[i] = "Chance/Chance " + (i + 1) + ".png";
            }
            chanceCard = new GL2DObject(chanceTextures, 245, 471, 949, 498);
            inJail = new GL2DObject("In Jail.png", 430, 990, 579, 86);

            String[] yesTextures = { "Buttons/Yes.png", "Buttons/Yes Highlighted.png" };
            yes = new GLButton(yesTextures, 347, 1109, 305, 86);

            String[] noTextures = { "Buttons/No.png", "Buttons/No Highlighted.png" };
            no = new GLButton(noTextures, 787, 1109, 305, 86);

            String[] closeTextures = { "Buttons/Close.png", "Buttons/Close Highlighted.png" };
            close = new GLButton(closeTextures, 567, 1109, 305, 86);

            String[] payFineTextures = { "Buttons/Pay Fine.png", "Buttons/Pay Fine Highlighted.png" };
            payFine = new GLButton(payFineTextures, 567, 1109, 305, 86);

            currentPrompt = this::buyingProperty;
            promptState = PromptState.NONE;
        }

        public void render() {
            if (promptState != PromptState.NONE) {
                currentPrompt.render();
            }
        }

        private void buyingProperty() {
            buyProperty.render();
            yes.render();
            no.render();
        }

        private void communityChest() {
            communityChestCard.render(gameState.getEventCard());
            close.render();
        }

        private void chance() {
            chanceCard.render(gameState.getEventCard());
            close.render();
        }

        private void jail() {
            inJail.render();
            payFine.render();
        }

        public void cursorMoved(double cursorXCoord, double cursorYCoord) {
            if (promptState == PromptState.BUY_PROPERTY) {
                yes.isCursorInRange(cursorXCoord, cursorYCoord);
                no.isCursorInRange(cursorXCoord, cursorYCoord);
            } else if (promptState == PromptState.COMMUNITY_CHEST
                    || promptState == PromptState.CHANCE) {
                close.isCursorInRange(cursorXCoord, cursorYCoord);
            } else if (promptState == PromptState.JAIL) {
                payFine.isCursorInRange(cursorXCoord, cursorYCoord);
            }
        }

        public void buttonPressed(ScreenState screenState, double cursorXCoord, double cursorYCoord) {
            if (promptState == PromptState.BUY_PROPERTY) {
                if (yes.isCursorInRange(cursorXCoord, cursorYCoord)) {
                    yes.setHighlighted(false);
                    promptState = PromptState.NONE;
                    gameState.buyProperty(true);
                }
                if (no.isCursorInRange(cursorXCoord, cursorYCoord)) {
                    no.setHighlighted(false);
                    promptState = PromptState.NONE;
                    gameState.buyProperty(false);
                }
            } else if (promptState == PromptState.COMMUNITY_CHEST
                    || promptState == PromptState.CHANCE
                    && close.isCursorInRange(cursorXCoord, cursorYCoord)) {
                close.setHighlighted(false);
                promptState = PromptState.NONE;
                gameState.turnCompleted();
            } else if (promptState == PromptState.JAIL
                    && payFine.isCursorInRange(cursorXCoord, cursorYCoord)) {
                payFine.setHighlighted(false);
                promptState = PromptState.NONE;
                gameState.payFine();
            }
        }
    }
}
