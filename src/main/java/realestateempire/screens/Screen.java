package realestateempire.screens;

public interface Screen {

    void render();
    void cursorMoved(double xCursor, double yCursor);
    void buttonPressed(double xCursor, double yCursor);
}
