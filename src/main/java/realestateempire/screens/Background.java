package realestateempire.screens;

import realestateempire.graphics.model.Model;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class Background {

    private final Model background;

    Background() {
        background = new Model("Background Large.png", -8014, -9134);
    }

    public void render() {
        animate();
        background.render();
    }

    private void animate() {
        float time = (float) glfwGetTime() % 240;
        if (time < 60) {
            background.setPosition(-8014 * (1 - (time / 60)), -9134);
        } else if (time < 120) {
            background.setPosition(0, -9134 * (1 - ((time - 60) / 60)));
        } else if (time < 180) {
            background.setPosition(-8014 * ((time - 120) / 60), 0);
        } else if (time < 240) {
            background.setPosition(-8014, -9134 * ((time - 180) / 60));
        }
    }
}
