package realestateempire.singleplayer.deed;

import realestateempire.graphics.text.CenteredText;
import realestateempire.graphics.text.Font;
import realestateempire.singleplayer.board.property.Property;
import realestateempire.singleplayer.player.Player;

public abstract class Deed<T extends Property> {

    private final CenteredText propertyName;
    final Player player;

    Deed(Player player) {
        this.player = player;
        propertyName = new CenteredText(" ", Font.BOLD, 720, 386, 0.74);
    }

    abstract void cursorMoved(double xCursor, double yCursor);
    abstract boolean buttonPressed(double xCursor, double yCursor);
    public abstract void updateProperty(T property);

    void render() {
        propertyName.render();
    }

    void updateText(Property property) {
        propertyName.updateText(property.getName());
    }
}
