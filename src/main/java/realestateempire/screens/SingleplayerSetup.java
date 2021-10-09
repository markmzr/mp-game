package realestateempire.screens;

import realestateempire.graphics.button.Button;
import realestateempire.graphics.button.ButtonAction;
import realestateempire.graphics.model.Model;
import realestateempire.singleplayer.player.Token;

import static realestateempire.singleplayer.player.Token.*;

class SingleplayerSetup implements Screen {

    private final Background background;
    private final Model chooseToken;
    private final Model tokenBorder;
    private final Button hatToken;
    private final Button redToken;
    private final Button blueToken;
    private final Button greenToken;
    private final Button mainMenu;
    private final Button startGame;
    private Token userToken;

    SingleplayerSetup(Background background, ButtonAction toMainMenu,
                      ButtonAction toSingleplayerGame) {
        this.background = background;
        chooseToken = new Model("Choose Token.png", 746, 159);
        tokenBorder = new Model("Tokens/Token Border.png", 885, 462);

        String[] hatTextures = { "Tokens/Hat Large.png", "Tokens/Hat Large.png"};
        hatToken = new Button(hatTextures, 905, 482);
        hatToken.addAction(this::selectHatToken);

        String[] redTokenTextures = { "Tokens/Red Token Large.png",
                "Tokens/Red Token Large.png" };
        redToken = new Button(redTokenTextures, 1343, 482);
        redToken.addAction(this::selectRedToken);

        String[] blueTokenTextures = { "Tokens/Blue Token Large.png",
                "Tokens/Blue Token Large.png" };
        blueToken = new Button(blueTokenTextures, 905, 854);
        blueToken.addAction(this::selectBlueToken);

        String[] greenTokenTextures = { "Tokens/Green Token Large.png",
                "Tokens/Green Token Large.png" };
        greenToken = new Button(greenTokenTextures, 1343, 854);
        greenToken.addAction(this::selectGreenToken);

        String[] mainMenuTextures = { "Buttons/Main Menu.png",
                "Buttons/Main Menu M.png" };
        mainMenu = new Button(mainMenuTextures, 100, 1248);
        mainMenu.addAction(toMainMenu);

        String[] startGameTextures = { "Buttons/Start Game.png",
                "Buttons/Start Game M.png" };
        startGame = new Button(startGameTextures, 2040, 1248);
        startGame.addAction(toSingleplayerGame);
        userToken = HAT;
    }

    Token getUserToken() {
        return userToken;
    }

    @Override
    public void render() {
        background.render();
        chooseToken.render();
        hatToken.render();
        redToken.render();
        blueToken.render();
        greenToken.render();
        tokenBorder.render();
        mainMenu.render();
        startGame.render();
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        mainMenu.cursorMoved(xCursor, yCursor);
        startGame.cursorMoved(xCursor, yCursor);
    }

    @Override
    public void buttonPressed(double xCursor, double yCursor) {
        mainMenu.buttonPressed(xCursor, yCursor);
        startGame.buttonPressed(xCursor, yCursor);
        hatToken.buttonPressed(xCursor, yCursor);
        redToken.buttonPressed(xCursor, yCursor);
        blueToken.buttonPressed(xCursor, yCursor);
        greenToken.buttonPressed(xCursor, yCursor);
    }

    private void selectHatToken() {
        tokenBorder.setPosition(885, 462);
        userToken = HAT;
    }

    private void selectRedToken() {
        tokenBorder.setPosition(1323, 462);
        userToken = RED;
    }

    private void selectBlueToken() {
        tokenBorder.setPosition(885, 834);
        userToken = BLUE;
    }

    private void selectGreenToken() {
        tokenBorder.setPosition(1323, 834);
        userToken = GREEN;
    }
}
