public interface Screen {

    void render();
    void cursorMoved(double cursorXCoord, double cursorYCoord);
    void buttonPressed(GameState gameState, double cursorXCoord, double cursorYCoord);
}
