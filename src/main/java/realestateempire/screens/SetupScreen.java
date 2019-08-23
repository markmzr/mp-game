package realestateempire.screens;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import realestateempire.graphics.Button;
import realestateempire.graphics.Model;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static realestateempire.graphics.Button.ButtonState.ENABLED;

public class SetupScreen implements Screen {

    private final Model background;
    private final Model chooseToken;
    private final Model tokenBorder;
    private final Button hatToken;
    private final Button redToken;
    private final Button blueToken;
    private final Button greenToken;
    private final Button mainMenu;
    private final Button startGame;
    private int userToken;
    private double prevTime;

    SetupScreen() {
        background = new Model("Background Large.png", -8014, -9134);
        chooseToken = new Model("Choose Token.png", 746, 159);
        tokenBorder = new Model("Tokens/Token Border.png", 885, 462);

        String[] hatTextures = { "Tokens/Hat Large.png", "Tokens/Hat Large.png"};
        hatToken = new Button(hatTextures, 905, 482);

        String[] redTokenTextures = { "Tokens/Red Token Large.png",
                "Tokens/Red Token Large.png" };
        redToken = new Button(redTokenTextures, 1343, 482);

        String[] blueTokenTextures = { "Tokens/Blue Token Large.png",
                "Tokens/Blue Token Large.png" };
        blueToken = new Button(blueTokenTextures, 905, 854);

        String[] greenTokenTextures = { "Tokens/Green Token Large.png",
                "Tokens/Green Token Large.png" };
        greenToken = new Button(greenTokenTextures, 1343, 854);

        String[] mainMenuTextures = { "Buttons/Main Menu.png",
                "Buttons/Main Menu M.png" };
        mainMenu = new Button(mainMenuTextures, 100, 1248);

        String[] startGameTextures = { "Buttons/Start Game.png",
                "Buttons/Start Game M.png" };
        startGame = new Button(startGameTextures, 2040, 1248);
        userToken = 0;
        prevTime = 0;
    }

    void setBackgroundPosition(Matrix4f position) {
        background.setPosition(position);
    }

    void setPrevTime(double prevTime) {
        this.prevTime = prevTime;
    }

    @Override
    public void render() {
        moveBackground();
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
        mainMenu.isMouseover(xCursor, yCursor);
        startGame.isMouseover(xCursor, yCursor);
    }

    @Override
    public void buttonPressed(ScreenState screenState, double xCursor, double yCursor) {
        if (mainMenu.isMouseover(xCursor, yCursor)) {
            mainMenu.setButtonState(ENABLED);
            screenState.setToMenuScreen(background.getPosition(), prevTime);
        }
        if (startGame.isMouseover(xCursor, yCursor)) {
            startGame.setButtonState(ENABLED);
            screenState.setToGameScreen(userToken);
        }
        if (hatToken.isMouseover(xCursor, yCursor)) {
            tokenBorder.setPosition(885, 462);
            userToken = 0;
        }
        if (redToken.isMouseover(xCursor, yCursor)) {
            tokenBorder.setPosition(1323, 462);
            userToken = 1;
        }
        if (blueToken.isMouseover(xCursor, yCursor)) {
            tokenBorder.setPosition(885, 834);
            userToken = 2;
        }
        if (greenToken.isMouseover(xCursor, yCursor)) {
            tokenBorder.setPosition(1323, 834);
            userToken = 3;
        }
    }

    private void moveBackground() {
        double time = glfwGetTime() % 240;
        prevTime = prevTime >= time ? 0 : prevTime;
        double deltaTime = time - prevTime;
        prevTime = time;

        Vector3f direction = new Vector3f(0, 0, 0);
        if (time < 60) {
            direction.x = Model.xToCoord(133.56);
        } else if (time < 120) {
            direction.y = Model.yToCoord(-152.23);
        } else if (time < 180) {
            direction.x = Model.xToCoord(-133.56);
        } else if (time < 240) {
            direction.y = Model.yToCoord(152.23);
        }
        direction.mul((float) deltaTime);
        background.movePosition(direction);
    }
}
