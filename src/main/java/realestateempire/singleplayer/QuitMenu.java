package realestateempire.singleplayer;

import realestateempire.graphics.Interactive;
import realestateempire.graphics.button.Button;
import realestateempire.graphics.button.ButtonAction;
import realestateempire.graphics.model.Model;

public class QuitMenu implements Interactive {

    private final Model background;
    private final Button cancel;
    protected final Button mainMenu;
    protected final Button desktop;
    boolean enabled;

    public QuitMenu(ButtonAction selectMainMenu, ButtonAction quitGame) {
        background = new Model("Prompts/Quit Menu Background.png", 0, 0);

        String[] mainMenuTextures = { "Prompts/Main Menu Small.png",
                "Prompts/Main Menu Small M.png", "Prompts/Main Menu Small.png" };
        mainMenu = new Button(mainMenuTextures, 1128, 594);
        mainMenu.addAction(selectMainMenu);
        mainMenu.addAction(() -> enabled = false);

        String[] desktopTextures = { "Prompts/Desktop.png",
                "Prompts/Desktop M.png", "Prompts/Desktop.png" };
        desktop = new Button(desktopTextures,  1128, 715);
        desktop.addAction(quitGame);

        String[] cancelTextures = { "Prompts/Cancel.png",
                "Prompts/Cancel M.png", "Prompts/Cancel.png" };
        cancel = new Button(cancelTextures,  1128, 836);
        cancel.addAction(() -> enabled = false);
        enabled = false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void render() {
        if (enabled) {
            background.render();
            mainMenu.render();
            desktop.render();
            cancel.render();
        }
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        if (enabled) {
            mainMenu.cursorMoved(xCursor, yCursor);
            desktop.cursorMoved(xCursor, yCursor);
            cancel.cursorMoved(xCursor, yCursor);
        }
    }

    @Override
    public boolean buttonPressed(double xCursor, double yCursor) {
        if (enabled) {
            return mainMenu.buttonPressed(xCursor, yCursor)
                    || desktop.buttonPressed(xCursor, yCursor)
                    || cancel.buttonPressed(xCursor, yCursor);
        }
        return false;
    }
}
