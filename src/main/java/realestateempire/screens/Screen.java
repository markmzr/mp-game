package realestateempire.screens;

interface Screen {

    void render();
    void cursorMoved(double xCursor, double yCursor);
    void buttonPressed(ScreenState screenState, double xCursor, double yCursor);
}
