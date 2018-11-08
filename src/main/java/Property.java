import java.util.Scanner;

public class Property implements BoardLocation {

    private String name;
    private int location;
    private int cost;
    private int rent;
    private boolean improvable;
    private boolean owned;
    private Player owner;

    public Property(String name, int location, int cost, int rent, boolean improvable) {
        this.name = name;
        this.location = location;
        this.cost = cost;
        this.rent = rent;
        this.improvable = improvable;
        owned = false;
        owner = null;
    }

    public void playerLanded(GameState gameState) {
        Player player = gameState.getCurrentPlayer();
        System.out.println(player.getName() + " landed on " + name + ".");

        if (!owned) {
            if (player.getMoney() >= cost) {
                if (player.getId() == 0) {
                    System.out.println(name + " costs $" + rent + ". Buy " + name + "? y / n");
                    Scanner scanner = new Scanner(System.in);
                    String input = scanner.nextLine();

                    if (input.equals("y") || input.equals("Y")) {
                        buyProperty(player);
                    }
                    else {
                        System.out.println(player.getName() + " did not buy " + name + ".");
                    }
                }
                else {
                    buyProperty(player);
                }
            }
            else {
                System.out.println(player.getName() + " cannot afford " + name + ". $" + cost + ", $" + player.getMoney());
            }
        }
        else if (player == owner) {
            System.out.println(player.getName() + " owns " + name + ".");
        }
        else {
            player.updateMoney(-1 * rent);
            owner.updateMoney(rent);
            System.out.println(owner.getName() + " owns " + name + ". "
                    + player.getName() + " paid " + owner.getName() + " $" + rent + " for rent.");
        }

        gameState.setTurnCompleted(true);
    }

    public void buyProperty(Player player) {
        owned = true;
        owner = player;
        player.updateMoney(-1 * cost);
        player.addOwnerIcon(location);
        System.out.println(player.getName() + " bought " + name + ".");
    }
}
