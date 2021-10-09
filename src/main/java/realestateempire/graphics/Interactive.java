package realestateempire.graphics;

public interface Interactive extends Graphic {

    void setEnabled(boolean enabled);
    void cursorMoved(double xCursor, double yCursor);
    boolean buttonPressed(double xCursor, double yCursor);
}
