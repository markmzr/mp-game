import java.nio.*;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    private long window;
    private GameState gameState;

    public void run() {
        init();
        loop();

        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        int resolutionWidth = vidmode.width();
        int resolutionHeight = vidmode.height();

        window = glfwCreateWindow(
                (int) (resolutionWidth * 0.6),
                (int) (resolutionHeight * 0.6),
                "Real Estate Empire", NULL, NULL);
        if ( window == NULL )
            throw new RuntimeException("Failed to create the GLFW window");

        glfwSetWindowAspectRatio(window, resolutionWidth, resolutionHeight);

        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(window, pWidth, pHeight);

            glfwSetWindowPos(
                    window,
                    (resolutionWidth - pWidth.get(0)) / 2,
                    (resolutionHeight - pHeight.get(0)) / 2);
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        // Set callbacks
        glfwSetFramebufferSizeCallback(window, resizeWindow);
        glfwSetMouseButtonCallback(window, mouseButtonCallback);
    }

    private void loop() {
        GL.createCapabilities();
        gameState = new GameState();

        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            gameState.render(window);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }

    // Callback for window resizing
    private GLFWFramebufferSizeCallback resizeWindow = new GLFWFramebufferSizeCallback(){
        @Override
        public void invoke(long window, int width, int height){
            glfwSetWindowSize(window, width, height);
            glViewport(0,0, width, height);
        }
    };

    // Callback for mouse button events
    private GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int button, int action, int mods) {
            double[] cursorXPos = new double[1];
            double[] cursorYPos = new double[1];
            glfwGetCursorPos(window, cursorXPos, cursorYPos);

            int[] windowWidth = new int[1];
            int[] windowHeight = new int[1];
            glfwGetWindowSize(window, windowWidth, windowHeight);

            double cursorXCoord = cursorXPos[0] / windowWidth[0];
            double cursorYCoord = cursorYPos[0] / windowHeight[0];

            if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                gameState.buttonPressed(cursorXCoord, cursorYCoord);
            }
        }
    };
}
