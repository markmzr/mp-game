package realestateempire.game;

import org.joml.Vector3f;

import realestateempire.graphics.Model;
import realestateempire.graphics.Text;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Player {

    private final GameState gameState;
    private final Model avatar;
    private final Model token;
    private final Model bankruptSign;
    private final Text moneyText;
    private final Text moneyDelta;
    private final int id;
    private int money;
    private int currentLocation;
    private int newLocation;
    private int railroadsOwned;
    private int utilitiesOwned;
    private boolean tokenMoving;
    private boolean inJail;
    private boolean bankrupt;
    private double deltaTime;
    private double prevTime;

    public Player(GameState gameState, int id) {
        this.gameState = gameState;
        avatar = new Model("Tokens/Blank Token.png", 1527, 87);
        String[] tokenTextures = { "Tokens/Blank Token.png", "Tokens/Blank Token Small.png" };
        token = new Model(tokenTextures, 1335, 1344);
        bankruptSign = new Model("Bankrupt.png", 2198, 87);
        moneyDelta = new Text("+$0", 1900, 82);

        this.id = id;
        money = 1500;
        currentLocation = 0;
        newLocation = 0;
        railroadsOwned = 0;
        utilitiesOwned = 0;
        tokenMoving = false;
        inJail = false;
        bankrupt = false;
        prevTime = 0;

        moneyText = new Text("$" + Integer.toString(money), 2243, 82);
        Vector3f direction = new Vector3f(0f, avatar.pixelYToCoord(-165) * id, 0f);
        avatar.movePosition(direction);
        bankruptSign.movePosition(direction);
        moneyText.movePosition(direction);
        moneyDelta.movePosition(direction);
    }

    public void setRailroadsOwned(int railroadsOwned) {
        this.railroadsOwned = railroadsOwned;
    }

    public void setUtilitiesOwned(int utilitiesOwned) {
        this.utilitiesOwned = utilitiesOwned;
    }

    public void setTokenMoving(boolean tokenMoving) {
        this.tokenMoving = tokenMoving;
    }

    public void setInJail(boolean inJail) {
        this.inJail = inJail;
    }

    public void setBankrupt(boolean bankrupt) {
        this.bankrupt = bankrupt;
    }

    public Model getAvatar() {
        return avatar;
    }

    public Model getToken() {
        return token;
    }

    public int getId() {
        return id;
    }

    public int getMoney() {
        return money;
    }

    public int getCurrentLocation() {
        return currentLocation;
    }

    public int getRailroadsOwned() {
        return railroadsOwned;
    }

    public int getUtilitiesOwned() {
        return utilitiesOwned;
    }

    public boolean getInJail() {
        return inJail;
    }

    public boolean getBankrupt() {
        return bankrupt;
    }

    public void updateMoney(int amount) {
        money += amount;
        if (money < 0) {
            gameState.declareBankruptcy(this);
        } else {
            if (amount >= 0) {
                moneyDelta.setText("+$" + Integer.toString(amount));
            } else {
                moneyDelta.setText("-$" + Integer.toString(-1 * amount));
            }
            moneyText.setText("$" + Integer.toString(money));
        }
    }

    public void updateMoneyDelta(String text) {
        moneyDelta.setText(text);
    }

    public void updateLocation(int diceRoll) {
        newLocation = currentLocation + diceRoll;
        newLocation = newLocation >= 40 ? newLocation - 40 : newLocation;
        tokenMoving = true;
        deltaTime = 0;
        prevTime = glfwGetTime();
    }

    public void render() {
        double currentTime = glfwGetTime();
        deltaTime += currentTime - prevTime;
        prevTime = currentTime;

        if (tokenMoving) {
            if (currentLocation == newLocation) {
                gameState.playerLanded();
            } else if (deltaTime >= 0.5) {
                moveToken();
                deltaTime = 0;
            }
        }
        if (bankrupt) {
            bankruptSign.render();
        } else {
            token.render();
            moneyText.render();
            moneyDelta.render();
        }
        avatar.render();
    }

    private void moveToken() {
        Vector3f direction = new Vector3f(0f, 0f, 0f);
        if (currentLocation < 10) {
            if (currentLocation == 0 || currentLocation == 9) {
                direction.x = token.pixelXToCoord(-179);
            } else {
                direction.x = token.pixelXToCoord(-117);
            }
        } else if (currentLocation < 20) {
            if (currentLocation == 10 || currentLocation == 19) {
                direction.y = token.pixelYToCoord(177);
            } else {
                direction.y = token.pixelYToCoord(117);
            }
        } else if (currentLocation < 30) {
            if (currentLocation == 20 || currentLocation == 29) {
                direction.x = token.pixelXToCoord(179);
            } else {
                direction.x = token.pixelXToCoord(117);
            }
        } else {
            if (currentLocation == 30 || currentLocation == 39) {
                direction.y = token.pixelYToCoord(-179);
            } else {
                direction.y = token.pixelYToCoord(-117);
            }
        }
        token.movePosition(direction);
        currentLocation++;
        if (currentLocation >= 40) {
            currentLocation = 0;
            if (newLocation > 0 && !inJail) {
                updateMoney(200);
            }
        }
    }
}
