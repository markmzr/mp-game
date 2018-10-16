public interface Screen {

    void render();
    ScreenOption buttonPressed(double cursorXPos, double cursorYPos);
}
