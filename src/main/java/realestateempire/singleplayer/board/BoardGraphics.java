package realestateempire.singleplayer.board;

import java.util.ArrayList;
import java.util.HashMap;

import realestateempire.GameFileReader;
import realestateempire.graphics.Interactive;
import realestateempire.graphics.Graphic;
import realestateempire.singleplayer.board.chance.ChanceGraphics;
import realestateempire.singleplayer.board.communitychest.CommunityChestGraphics;
import realestateempire.singleplayer.board.property.PropertyGraphics;
import realestateempire.singleplayer.board.property.PropertyGraphicsData;
import realestateempire.singleplayer.board.street.StreetGraphics;
import realestateempire.singleplayer.board.street.StreetGraphicsData;

public abstract class BoardGraphics implements Interactive {

    public final CommunityChestGraphics communityChestGraphics;
    public final ChanceGraphics chanceGraphics;
    public final HashMap<Integer, PropertyGraphics> locationToPropertyGraphics;
    public final HashMap<Integer, StreetGraphics> locationToStreetGraphics;
    private final ArrayList<Graphic> graphics;
    protected final ArrayList<Interactive> interactives;

    public BoardGraphics() {
        communityChestGraphics = new CommunityChestGraphics();
        chanceGraphics = new ChanceGraphics();
        graphics = new ArrayList<>();
        interactives = new ArrayList<>();
        locationToPropertyGraphics = initPropertyGraphics();
        locationToStreetGraphics = initStreetGraphics();
        interactives.add(communityChestGraphics);
        interactives.add(chanceGraphics);
    }

    @Override
    public void setEnabled(boolean enabled) {
        communityChestGraphics.setEnabled(false);
        chanceGraphics.setEnabled(false);
        for (Interactive interactive : interactives) {
            interactive.setEnabled(false);
        }
    }

    @Override
    public void render() {
        for (Graphic graphic : graphics) {
            graphic.render();
        }
        for (Interactive interactive : interactives) {
            interactive.render();
        }
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        for (Interactive interactive : interactives) {
            interactive.cursorMoved(xCursor, yCursor);
        }
    }

    @Override
    public boolean buttonPressed(double xCursor, double yCursor) {
        for (Interactive interactive : interactives) {
            if (interactive.buttonPressed(xCursor, yCursor)) {
                return true;
            }
        }
        return false;
    }

    private HashMap<Integer, PropertyGraphics> initPropertyGraphics() {
        HashMap<Integer, PropertyGraphics> locationToPropertyGraphics = new HashMap<>();
        ArrayList<String[]> propertyGraphicsData = GameFileReader.readFile(
                "Property Graphics.csv");
        for (String[] data : propertyGraphicsData) {
            PropertyGraphicsData pgData = new PropertyGraphicsData(data);
            PropertyGraphics propertyGraphics = new PropertyGraphics(pgData);
            locationToPropertyGraphics.put(pgData.getLocation(), propertyGraphics);
            graphics.add(propertyGraphics);
        }
        return locationToPropertyGraphics;
    }

    private HashMap<Integer, StreetGraphics> initStreetGraphics() {
        HashMap<Integer, StreetGraphics> locationToStreetGraphics = new HashMap<>();
        ArrayList<String[]> streetGraphicsData = GameFileReader.readFile(
                "Street Graphics.csv");
        for (String[] data : streetGraphicsData) {
            StreetGraphicsData sgData = new StreetGraphicsData(data);
            StreetGraphics streetGraphics = new StreetGraphics(sgData);
            locationToStreetGraphics.put(sgData.getLocation(), streetGraphics);
            graphics.add(streetGraphics);
        }
        return locationToStreetGraphics;
    }
}
