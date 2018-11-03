import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Player {

    private GL2DObject token;
    private int currentPosition;
    private int newPosition;
    private double deltaTime;
    private double lastTime;

    public Player() {
        float[] tokenVertices = new float[] {
                0.01641f, -0.82361f, 0,
                0.06641f, -0.82361f, 0,
                0.06641f, -0.88195f, 0,

                0.06641f, -0.88195f, 0,
                0.01641f, -0.88195f, 0,
                0.01641f, -0.82361f, 0,
        };
        token = new GL2DObject(tokenVertices, "Hat.png");

        currentPosition = 0;
        newPosition = 0;
        lastTime = 0;
    }

    public void render() {
        double currentTime = glfwGetTime();
        deltaTime += currentTime - lastTime;
        lastTime = currentTime;

        if (currentPosition != newPosition && deltaTime > 0.5) {
            token.movePosition(getTokenDirection());
            currentPosition++;
            deltaTime = 0;
        }
        if (currentPosition == 40) {
            currentPosition = 0;
        }

        token.render();
    }

    public void updatePosition(int diceRoll) {
        newPosition = currentPosition + diceRoll;

        if (newPosition >= 40) {
            newPosition -= 40;
        }

        deltaTime = 0;
        lastTime = glfwGetTime();
    }

    private Vector3f getTokenDirection() {
        Vector3f direction = new Vector3f(0f, 0f, 0f);

        if (currentPosition < 10) {
            if (currentPosition == 0 || currentPosition == 9) {
                direction.x = -0.125f;
            }
            else {
                direction.x = -0.0890625f;
            }
        }
        else if (currentPosition < 20) {
            if (currentPosition == 10 || currentPosition == 19) {
                direction.y = 0.22222f;
            }
            else {
                direction.y = 0.15833f;
            }
        }
        else if (currentPosition < 30) {
            if (currentPosition == 20 || currentPosition == 29) {
                direction.x = 0.125f;
            }
            else {
                direction.x = 0.0890625f;
            }
        }
        else if (currentPosition < 40) {
            if (currentPosition == 30 || currentPosition == 39) {
                direction.y = -0.22222f;
            }
            else {
                direction.y = -0.15833f;
            }
        }

        return direction;
    }
}
