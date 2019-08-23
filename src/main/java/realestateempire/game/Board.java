package realestateempire.game;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static realestateempire.game.Player.PlayerState.IN_JAIL;

public class Board {

    private final Game game;
    private final Property[] properties;
    private final Location[] locations;
    private final EventCard eventCard;

    Board(Game game) {
        this.game = game;
        eventCard = new EventCard(game);
        locations = new Location[40];
        properties = initProperties();
        locations[0] = this::go;
        locations[2] = game::communityChest;
        locations[4] = this::tax;
        locations[7] = game::chance;
        locations[10] = this::neutralLocation;
        locations[17] = game::communityChest;
        locations[20] = this::neutralLocation;
        locations[22] = game::chance;
        locations[30] = this::goToJail;
        locations[33] = game::communityChest;
        locations[36] = game::chance;
        locations[38] = this::tax;
    }

    Property[] getProperties() {
        return properties;
    }

    public Location getLocation(int location) {
        return locations[location];
    }

    public EventCard getEventCard() {
        return eventCard;
    }

    public void render() {
        for (Property property : properties) {
            property.render();
        }
    }

    public void cursorMoved(double xCursor, double yCursor) {
        for (Property property : properties) {
            property.cursorMoved(xCursor, yCursor);
        }
    }

    public void buttonPressed(double xCursor, double yCursor) {
        for (Property property : properties) {
            property.buttonPressed(xCursor, yCursor);
        }
    }

    void playerLanded() {
        int location = game.getCurrentPlayer().getCurrentLocation();
        locations[location].playerLanded();
    }

    private void go() {
        game.getCurrentPlayer().updateMoney(400);
    }

    private void tax() {
        game.getCurrentPlayer().updateMoney(-100);
    }

    private void neutralLocation() { }

    private void goToJail() {
        Player player = game.getCurrentPlayer();
        player.setPlayerState(IN_JAIL);
        int location = player.getCurrentLocation();
        int moveLocations = location <= 10 ? 10 - location : 50 - location;
        player.updateLocation(moveLocations);
    }

    private Property[] initProperties() {
        Property[] properties = new Property[28];
        try {
            File file = new File("Property Data.csv");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.readLine();
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                String[] propertyData = line.split(",");
                properties[i] = new Property(game, propertyData);
                locations[properties[i].getLocation()] = properties[i];
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return properties;
    }
}
