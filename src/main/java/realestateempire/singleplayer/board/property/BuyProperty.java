package realestateempire.singleplayer.board.property;

import realestateempire.graphics.button.Button;
import realestateempire.graphics.button.ButtonAction;
import realestateempire.graphics.model.Model;
import realestateempire.graphics.text.CenteredText;

public class BuyProperty {

    private final Model background;
    private final Button yes;
    private final Button no;
    private final CenteredText propertyName;
    private Property property;
    private boolean enabled;

    public BuyProperty(ButtonAction endTurn) {
        background = new Model("Prompts/Prompt Background.png", 1491, 830);
        String[] yesTextures = { "Buttons/Yes.png", "Buttons/Yes M.png",
                "Buttons/Yes.png" };
        yes = new Button(yesTextures, 1634, 995);
        yes.addAction(this::buyProperty);
        yes.addAction(endTurn);

        String[] noTextures = { "Buttons/No.png", "Buttons/No M.png",
                "Buttons/No.png" };
        no = new Button(noTextures, 2061, 995);
        no.addAction(() -> enabled = false);
        no.addAction(endTurn);
        propertyName = new CenteredText(" ", 2000, 893);
        enabled = false;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void render() {
        if (enabled) {
            background.render();
            yes.render();
            no.render();
            propertyName.render();
        }
    }

    public void cursorMoved(double xCursor, double yCursor) {
        if (enabled) {
            yes.cursorMoved(xCursor, yCursor);
            no.cursorMoved(xCursor, yCursor);
        }
    }
    
    public boolean buttonPressed(double xCursor, double yCursor) {
        if (enabled) {
            return yes.buttonPressed(xCursor, yCursor)
                    || no.buttonPressed(xCursor, yCursor);
        }
        return false;
    }

    public void updateProperty(Property property) {
        this.property = property;
        propertyName.updateText("Buy " + property.getName() + "?");
    }

    private void buyProperty() {
        property.buyProperty();
        enabled = false;
    }
}
