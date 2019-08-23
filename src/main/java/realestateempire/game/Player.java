package realestateempire.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.joml.Vector3f;

import realestateempire.graphics.Model;
import realestateempire.graphics.Text;

import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static realestateempire.game.Player.PlayerState.*;

public class Player {

    public enum PlayerState {

        ACTIVE, IN_JAIL, BANKRUPT
    }

    private static final Vector3f[] tokenDirections = initTokenDirections();
    private final Game game;
    private final Model name;
    private final Model avatar;
    private final Model token;
    private final Model bankrupt;
    private final Text moneyText;
    private final Text moneyDelta;
    private final boolean user;
    private PlayerState playerState;
    private int money;
    private int currentLocation;
    private int newLocation;
    private int railroadsOwned;
    private int utilitiesOwned;
    private double prevTime;
    private double deltaTime;
    private boolean moving;

    public Player(Game game, boolean user, int id, String nameTexture, String[] tokenTextures) {
        this.game = game;
        this.user = user;
        int yDelta = 165 * id;
        name = new Model(nameTexture, 1628, 88 + yDelta);
        avatar = new Model(tokenTextures[0], 1527, 87 + yDelta);
        token = new Model(tokenTextures, 1310, 1321);
        bankrupt = new Model("Player/Bankrupt.png", 2198, 87 + yDelta);
        bankrupt.setVisible(false);

        playerState = ACTIVE;
        money = 1500;
        moneyDelta = new Text("+$0", 1900, 82 + yDelta);
        moneyText = new Text("$" + money, 2243, 82 + yDelta);
        currentLocation = 0;
        newLocation = 0;
        railroadsOwned = 0;
        utilitiesOwned = 0;
        prevTime = 0;
        deltaTime = 0;
        moving = false;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    void setRailroadsOwned(int railroadsOwned) {
        this.railroadsOwned = railroadsOwned;
    }

    void setUtilitiesOwned(int utilitiesOwned) {
        this.utilitiesOwned = utilitiesOwned;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public Model getToken() {
        return token;
    }

    public boolean isUser() {
        return user;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    int getMoney() {
        return money;
    }

    public int getCurrentLocation() {
        return currentLocation;
    }

    int getRailroadsOwned() {
        return railroadsOwned;
    }

    int getUtilitiesOwned() {
        return utilitiesOwned;
    }

    boolean isMoving() {
        return moving;
    }

    public void updateMoney(int amount) {
        money += amount;
        if (money < 0) {
            token.setVisible(false);
            moneyText.setVisible(false);
            moneyDelta.setVisible(false);
            bankrupt.setVisible(true);
            playerState = BANKRUPT;
            game.playerBankrupt(user);
        } else {
            if (amount >= 0) {
                moneyDelta.updateText("+$" + amount);
            } else {
                moneyDelta.updateText("-$" + (-1 * amount));
            }
            moneyText.updateText("$" + money);
        }
    }

    public void updateMoneyDelta(String text) {
        moneyDelta.updateText(text);
    }

    public void updateLocation(int diceTotal) {
        newLocation = currentLocation + diceTotal;
        newLocation = newLocation >= 40 ? newLocation - 40 : newLocation;
        moving = true;
        deltaTime = 0;
        prevTime = glfwGetTime();
    }

    public void render() {
        if (moving) {
            moveToken();
        }
        name.render();
        avatar.render();
        token.render();
        bankrupt.render();
        moneyText.render();
        moneyDelta.render();
    }

    private void moveToken() {
        double time = glfwGetTime();
        deltaTime += time - prevTime;
        prevTime = time;

        if (currentLocation == newLocation) {
            moving = false;
            game.playerLanded();
        } else if (deltaTime >= 0.75) {
            if (currentLocation == 9 && playerState == IN_JAIL) {
                float x = Model.xToCoord(-125);
                float y = Model.yToCoord(55);
                token.movePosition(new Vector3f(x, y, 0));
            } else {
                token.movePosition(tokenDirections[currentLocation]);
            }
            deltaTime = 0;
            currentLocation++;
            if (currentLocation >= 40) {
                currentLocation = 0;
                if (newLocation > 0 && playerState != IN_JAIL) {
                    updateMoney(200);
                }
            }
        }
    }

    private static Vector3f[] initTokenDirections() {
        Vector3f[] tokenDirections = new Vector3f[40];
        try {
            File file = new File("Player Data.csv");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.readLine();
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                String[] vectorData = line.split(",");
                float x = Float.parseFloat(vectorData[0]);
                float y = Float.parseFloat(vectorData[1]);
                tokenDirections[i] = new Vector3f(Model.xToCoord(x), Model.yToCoord(y), 0);
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return tokenDirections;
    }
}
