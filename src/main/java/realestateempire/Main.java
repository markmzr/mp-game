package realestateempire;

import java.nio.*;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import realestateempire.screens.ScreenState;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    private static long window;
    private ScreenState screenState;

    public static long getWindow() {
        return window;
    }

    private void run() {
        init();
        loop();
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();
        if (!glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        int resolutionWidth = vidMode.width();
        int resolutionHeight = vidMode.height();
        window = glfwCreateWindow(
                (int)(resolutionWidth * 0.6),
                (int)(resolutionHeight * 0.6),
                "Real Estate Empire", NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create GLFW window");
        }
        glfwSetWindowAspectRatio(window, resolutionWidth, resolutionHeight);

        try (MemoryStack stack = stackPush()) {
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

        glfwSetFramebufferSizeCallback(window, resizeWindow);
        glfwSetCursorPosCallback(window, cursorPosCallback);
        glfwSetMouseButtonCallback(window, mouseButtonCallback);

        IntBuffer x = BufferUtils.createIntBuffer(1);
        IntBuffer y = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer cursorData = stbi_load("./Textures/Cursor.png", x, y, channels, 4);

        GLFWImage cursor = GLFWImage.create();
        cursor.width(x.get());
        cursor.height(y.get());
        cursor.pixels(cursorData);
        long cursorId = glfwCreateCursor(cursor, 0, 0);
        glfwSetCursor(window, cursorId);
        stbi_image_free(cursorData);

        GL.createCapabilities();
        screenState = new ScreenState(window);
    }

    private void loop() {
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            screenState.render();
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }

    private GLFWFramebufferSizeCallback resizeWindow = new GLFWFramebufferSizeCallback(){
        @Override
        public void invoke(long window, int width, int height){
            glfwSetWindowSize(window, width, height);
            glViewport(0,0, width, height);
        }
    };

    private GLFWCursorPosCallback cursorPosCallback = new GLFWCursorPosCallback() {
        @Override
        public void invoke(long window, double xCursorPos, double yCursorPos) {
            int[] windowWidth = new int[1];
            int[] windowHeight = new int[1];
            glfwGetWindowSize(window, windowWidth, windowHeight);

            double xCursor = xCursorPos / windowWidth[0];
            double yCursor = yCursorPos / windowHeight[0];
            screenState.cursorMoved(xCursor, yCursor);
        }
    };

    private GLFWMouseButtonCallback mouseButtonCallback = new GLFWMouseButtonCallback() {
        @Override
        public void invoke(long window, int button, int action, int mods) {
            double[] xCursorPos = new double[1];
            double[] yCursorPos = new double[1];
            glfwGetCursorPos(window, xCursorPos, yCursorPos);

            int[] windowWidth = new int[1];
            int[] windowHeight = new int[1];
            glfwGetWindowSize(window, windowWidth, windowHeight);

            double xCursor = xCursorPos[0] / windowWidth[0];
            double yCursor = yCursorPos[0] / windowHeight[0];
            if (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS) {
                screenState.buttonPressed(xCursor, yCursor);
            }
        }
    };
}
