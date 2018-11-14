public class Property implements BoardLocation {

    private GameState gameState;
    private String name;
    private int location;
    private int cost;
    private int rent;
    private boolean improvable;
    private boolean railroad;
    private boolean utility;
    private boolean owned;
    private Player owner;

    public Property(GameState gameState, String name, int location, int cost, int rent, boolean improvable) {
        this.gameState = gameState;
        this.name = name;
        this.location = location;
        this.cost = cost;
        this.rent = rent;
        this.improvable = improvable;
        railroad = ((location - 5) % 10) == 0;
        utility = (location == 12 || location == 28);
        owned = false;
        owner = null;
    }

    public void playerLanded() {
        Player player = gameState.getCurrentPlayer();
        System.out.println(player.getName() + " landed on " + name + ".");

        if (!owned) {
            if (player.getMoney() >= cost) {
                if (player.getId() == 0) {
                    gameState.getGameScreen().setPromptState(PromptState.BUY_PROPERTY);
                } else {
                    buyProperty(player);
                }
            } else {
                System.out.println(player.getName() + " cannot afford " + name + "."
                        + "$" + cost + ", $" + player.getMoney());
            }
        } else if (player == owner) {
            System.out.println(player.getName() + " owns " + name + ".");
        } else {
            payRent(gameState, player);
        }
        if (gameState.getGameScreen().getPromptState() == PromptState.NONE) {
            gameState.turnCompleted();
        }
    }

    public void buyProperty(Player player) {
        owned = true;
        owner = player;
        player.updateMoney(-1 * cost);
        player.addOwnerIcon(location);

        if (railroad) {
            int railroadsOwned = player.getRailroadsOwned() + 1;
            player.setRailroadsOwned(railroadsOwned);

            if (railroadsOwned <= 2) {
                rent = railroadsOwned * 25;
            } else if (railroadsOwned == 3) {
                rent = 100;
            } else {
                rent = 200;
            }
        }
        if (utility) {
            player.setUtilitiesOwned(player.getUtilitiesOwned() + 1);
        }
        System.out.println(player.getName() + " bought " + name + ".");
    }

    private void payRent(GameState gameState, Player player) {
        if (utility) {
            int diceRoll = gameState.getDie1Val() + gameState.getDie2Val() + 2;
            if (player.getUtilitiesOwned() == 1) {
                rent = diceRoll * 4;
            } else {
                rent = diceRoll * 10;
            }
        }
        player.updateMoney(-1 * rent);
        owner.updateMoney(rent);
        System.out.println(owner.getName() + " owns " + name + ". "
                + player.getName() + " paid " + owner.getName() + " $" + rent + " for rent.");
    }
}
