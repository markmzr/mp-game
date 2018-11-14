public class Board {

    private GameState gameState;
    private BoardLocation[] boardLocations;
    private EventCard eventCard;

    public Board(GameState gameState) {
        this.gameState = gameState;

        boardLocations = new BoardLocation[40];
        boardLocations[0] = this::go;
        boardLocations[1] = new Property(gameState, "1 Ave", 1, 100, 100, true);
        boardLocations[2] = this::communityChest;
        boardLocations[3] = new Property(gameState, "3 Ave", 3, 100, 100, true);
        boardLocations[4] = this::tax;
        boardLocations[5] = new Property(gameState, "5 Ave", 5, 100, 100, false);
        boardLocations[6] = new Property(gameState, "6 Ave", 6, 100, 100, true);
        boardLocations[7] = this::chance;
        boardLocations[8] = new Property(gameState, "8 Ave", 8, 100, 100, true);
        boardLocations[9] = new Property(gameState, "9 Ave", 9, 100, 100, true);
        boardLocations[10] = this::visitingJail;
        boardLocations[11] = new Property(gameState, "11 Ave", 11, 100, 100, true);
        boardLocations[12] = new Property(gameState, "12 Ave", 12, 100, 100, false);
        boardLocations[13] = new Property(gameState, "13 Ave", 13, 100, 100, true);
        boardLocations[14] = new Property(gameState, "14 Ave", 14, 100, 100, true);
        boardLocations[15] = new Property(gameState, "15 Ave", 15, 100, 100, false);
        boardLocations[16] = new Property(gameState, "16 Ave", 16, 100, 100, true);
        boardLocations[17] = this::communityChest;
        boardLocations[18] = new Property(gameState, "18 Ave", 18, 100, 100, true);
        boardLocations[19] = new Property(gameState, "19 Ave", 19, 100, 100, true);
        boardLocations[20] = this::freeParking;
        boardLocations[21] = new Property(gameState, "21 Ave", 21, 100, 100, true);
        boardLocations[22] = this::chance;
        boardLocations[23] = new Property(gameState, "23 Ave", 23, 100, 100, true);
        boardLocations[24] = new Property(gameState, "24 Ave", 24, 100, 100, true);
        boardLocations[25] = new Property(gameState, "25 Ave", 25, 100, 100, false);
        boardLocations[26] = new Property(gameState, "26 Ave", 26, 100, 100, true);
        boardLocations[27] = new Property(gameState, "27 Ave", 27, 100, 100, true);
        boardLocations[28] = new Property(gameState, "28 Ave", 28, 100, 100, false);
        boardLocations[29] = new Property(gameState, "29 Ave", 29, 100, 100, true);
        boardLocations[30] = this::goToJail;
        boardLocations[31] = new Property(gameState, "31 Ave", 31, 100, 100, true);
        boardLocations[32] = new Property(gameState, "32 Ave", 32, 100, 100, true);
        boardLocations[33] = this::communityChest;
        boardLocations[34] = new Property(gameState, "34 Ave", 34, 100, 100, true);
        boardLocations[35] = new Property(gameState, "35 Ave", 35, 100, 100, false);
        boardLocations[36] = this::chance;
        boardLocations[37] = new Property(gameState, "37 Ave", 37, 100, 100, true);
        boardLocations[38] = this::tax;
        boardLocations[39] = new Property(gameState, "39 Ave", 39, 100, 100, true);

        eventCard = new EventCard(gameState);
    }

    public void playerLanded() {
        boardLocations[gameState.getCurrentPlayer().getCurrentPosition()].playerLanded();
    }

    public void buyProperty(Player player) {
        ((Property)boardLocations[player.getCurrentPosition()]).buyProperty(player);
    }

    private void communityChest() {
        System.out.println(gameState.getCurrentPlayer().getName() + " landed on Community Chest.");
        eventCard.communityChest();
    }

    private void chance() {
        System.out.println(gameState.getCurrentPlayer().getName() + " landed on Chance.");
        eventCard.chance();
    }

    private void go() {
        Player player = gameState.getCurrentPlayer();
        System.out.println(player.getName() + " landed on Go.");
        player.updateMoney(400);
        System.out.println(player.getName() + " received $400 for landing on Go.");
        gameState.turnCompleted();
    }

    private void tax() {
        Player player = gameState.getCurrentPlayer();
        System.out.println(player.getName() + " landed on Tax.");
        player.updateMoney(-100);
        gameState.turnCompleted();
    }

    private void visitingJail() {
        Player player = gameState.getCurrentPlayer();
        if (player.getInJail()) {
            System.out.println(player.getName() + " is in jail.");
        } else {
            System.out.println(player.getName() + " landed on Visiting Jail.");
        }
        gameState.turnCompleted();
    }

    private void freeParking() {
        Player player = gameState.getCurrentPlayer();
        System.out.println(player.getName() + " landed on Free Parking.");
        gameState.turnCompleted();
    }

    private void goToJail() {
        Player player = gameState.getCurrentPlayer();
        System.out.println(player.getName() + " landed on Go to Jail.");
        player.setInJail(true);
        int currentPosition = player.getCurrentPosition();
        int move = currentPosition <= 10 ? 10 - currentPosition : 50 - currentPosition;
        player.updatePosition(move);
    }
}
