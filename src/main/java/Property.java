import java.util.Scanner;

public class Property implements BoardLocation {

    private String name;
    private int location;
    private int cost;
    private int rent;
    private boolean improvable;
    private boolean owned;
    private int ownerId;

    public Property(String name, int location, int cost, int rent, boolean improvable) {
        this.name = name;
        this.location = location;
        this.cost = cost;
        this.rent = rent;
        this.improvable = improvable;
        owned = false;
        ownerId = 0;
    }

    public void playerLanded(GameState gameState, Board board, Player player) {
        System.out.println(player.getName() + " landed on " + name + ".");

        if (!owned) {
            if (player.getMoney() >= cost) {
                if (player.getId() == 0) {
                    System.out.println(name + " costs $" + rent + ". Buy " + name + "? y / n");
                    Scanner scanner = new Scanner(System.in);
                    String input = scanner.nextLine();

                    if (input.equals("y") || input.equals("Y")) {
                        owned = true;
                        ownerId = player.getId();
                        player.updateMoney(-1 * cost);
                        System.out.println(player.getName() + " bought " + name + ", $" + player.getMoney() + " remaining.");
                    }
                    else {
                        System.out.println(player.getName() + " did not buy " + name + ".");
                    }
                }
                else {
                    owned = true;
                    ownerId = player.getId();
                    player.updateMoney(-1 * cost);
                    System.out.println(player.getName() + " bought " + name + ", $" + player.getMoney() + " remaining.");
                }
            }
            else {
                System.out.println(player.getName() + " cannot afford " + name + ". $" + cost + ", $" + player.getMoney());
            }
        }
        else if (ownerId == player.getId()) {
            System.out.println(player.getName() + " owns " + name + ".");
        }
        else {
            player.updateMoney(-1 * rent);
            Player owner = gameState.getPlayer(ownerId);
            owner.updateMoney(rent);

            System.out.println(owner.getName() + " owns " + name + ". "
                    + player.getName() + " paid " + owner.getName() + " $" + rent + " for rent.");
        }

        gameState.setTurnCompleted(true);
    }
}
