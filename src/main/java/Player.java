import java.util.Vector;

import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Player {

    private Board board;
    private GL2DObject token;
    private Vector<GL2DObject> ownerIcons;
    private GLText glMoney;
    private GLText moneyDifference;
    private String name;
    private int id;
    private int money;
    private int currentPosition;
    private int newPosition;
    private int railroadsOwned;
    private int utilitiesOwned;
    private boolean tokenMoving;
    private boolean inJail;
    private double deltaTime;
    private double lastTime;

    public Player(Board board, String name, int id, String tokenTexture) {
        this.board = board;
        token = new GL2DObject(tokenTexture, 1301, 1312, 64, 42);
        ownerIcons = new Vector<>();
        moneyDifference = new GLText("+$0", 1900, 82, 45, 51);

        this.name = name;
        this.id = id;
        money = 1500;
        currentPosition = 0;
        newPosition = 0;
        railroadsOwned = 0;
        utilitiesOwned = 0;
        tokenMoving = false;
        inJail = false;
        lastTime = 0;

        glMoney = new GLText("$" + Integer.toString(money), 2243, 82, 45, 51);
        Vector3f direction = new Vector3f(0f, -0.22916f * id, 0f);
        glMoney.movePosition(direction);
        moneyDifference.movePosition(direction);
    }

    public void setRailroadsOwned(int railroadsOwned) {
        this.railroadsOwned = railroadsOwned;
    }

    public void setUtilitiesOwned(int utilitiesOwned) {
        this.utilitiesOwned = utilitiesOwned;
    }

    public void setInJail(boolean inJail) {
        this.inJail = inJail;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getMoney() {
        return money;
    }

    public int getCurrentPosition() {
        return currentPosition;
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

    public void updateMoney(int amount) {
        money += amount;
        glMoney.setText("$" + Integer.toString(money));
        String amountStr = amount >= 0 ? "+$" + Integer.toString(amount) : "-$" + Integer.toString(-1 * amount);
        moneyDifference.setText(amountStr);
    }

    public void updateMoneyDifference(String text) {
        moneyDifference.setText(text);
    }

    public void updatePosition(int diceRoll) {
        newPosition = currentPosition + diceRoll;
        newPosition = newPosition >= 40 ? (newPosition - 40) : newPosition;
        tokenMoving = true;
        deltaTime = 0;
        lastTime = glfwGetTime();
    }

    public void render() {
        double currentTime = glfwGetTime();
        deltaTime += currentTime - lastTime;
        lastTime = currentTime;

        if (tokenMoving) {
            if (currentPosition == newPosition) {
                board.playerLanded();
                tokenMoving = false;
            } else if (deltaTime > 0.5) {
                token.movePosition(getTokenDirection());
                currentPosition++;

                if (currentPosition >= 40) {
                    currentPosition = 0;
                    if (newPosition > 0 && !inJail) {
                        updateMoney(200);
                    }
                }
                deltaTime = 0;
            }
        }

        for (GL2DObject icon : ownerIcons) {
            icon.render();
        }
        token.render();
        glMoney.render();
        moneyDifference.render();
    }

    private Vector3f getTokenDirection() {
        Vector3f direction = new Vector3f(0f, 0f, 0f);
        if (currentPosition < 10) {
            if (currentPosition == 0 || currentPosition == 9) {
                direction.x = -0.125f;
            } else {
                direction.x = -0.0890625f;
            }
        } else if (currentPosition < 20) {
            if (currentPosition == 10 || currentPosition == 19) {
                direction.y = 0.22222f;
            } else {
                direction.y = 0.15833f;
            }
        } else if (currentPosition < 30) {
            if (currentPosition == 20 || currentPosition == 29) {
                direction.x = 0.125f;
            } else {
                direction.x = 0.0890625f;
            }
        } else {
            if (currentPosition == 30 || currentPosition == 39) {
                direction.y = -0.22222f;
            } else {
                direction.y = -0.15833f;
            }
        }
        return direction;
    }

    public void addOwnerIcon(int location) {
        String texture = (id == 0) ? "Tokens/Hat.png" : "Tokens/AI " + id + " Token.png";
        GL2DObject ownerIcon = new GL2DObject(texture, 1154, 1193, 39, 26);
        Vector3f direction = new Vector3f(0f, 0f, 0f);

        if (location < 10) {
            direction.x = -0.08828125f * (location - 1);
        } else if (location < 20) {
            direction.x = -0.728125f;
            direction.y = 0.15833f * (location - 11) + 0.04444f;
        } else if (location < 30) {
            direction.x = -0.08828125f * (29 - location);
            direction.y = 1.34861f;
        } else {
            direction.x = 0.01953125f;
            direction.y = 0.15694f * (39 - location) + 0.04444f;
        }
        ownerIcon.movePosition(direction);
        ownerIcons.add(ownerIcon);
    }
}
