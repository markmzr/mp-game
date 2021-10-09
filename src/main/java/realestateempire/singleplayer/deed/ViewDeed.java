package realestateempire.singleplayer.deed;

import realestateempire.graphics.Interactive;

public class ViewDeed implements Interactive {

    private Deed deed;
    private boolean enabled;

    public void setDeed(Deed deed) {
        this.deed = deed;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void render() {
        if (enabled) {
            deed.render();
        }
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        if (enabled) {
            deed.cursorMoved(xCursor, yCursor);
        }
    }

    @Override
    public boolean buttonPressed(double xCursor, double yCursor) {
        if (enabled) {
            return deed.buttonPressed(xCursor, yCursor);
        }
        return false;
    }
}
