package realestateempire.screens;

public interface Screen {

    void render();
    void cursorMoved(double cursorXCoord, double cursorYCoord);
    void buttonPressed(ScreenState screenState, double cursorXCoord, double cursorYCoord);
}
