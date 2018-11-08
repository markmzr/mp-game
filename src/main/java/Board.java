public class Board {

    private BoardLocation[] boardLocations;

    public Board() {
        boardLocations = new BoardLocation[40];
        BoardLocation go = this::go;
        BoardLocation communityChest = this::communityChest;
        BoardLocation chance = this::chance;
        BoardLocation tax = this::tax;
        BoardLocation visitingJail = this::visitingJail;
        BoardLocation freeParking = this::freeParking;
        BoardLocation goToJail = this::goToJail;

        boardLocations[0] = go;
        boardLocations[1] = new Property("1 Ave", 1, 100, 100, true);
        boardLocations[2] = communityChest;
        boardLocations[3] = new Property("3 Ave", 3, 100, 100, true);
        boardLocations[4] = tax;
        boardLocations[5] = new Property("5 Ave", 5, 100, 100, false);
        boardLocations[6] = new Property("6 Ave", 6, 100, 100, true);
        boardLocations[7] = chance;
        boardLocations[8] = new Property("8 Ave", 8, 100, 100, true);
        boardLocations[9] = new Property("9 Ave", 9, 100, 100, true);
        boardLocations[10] = visitingJail;
        boardLocations[11] = new Property("11 Ave", 11, 100, 100, true);
        boardLocations[12] = new Property("12 Ave", 12, 100, 100, false);
        boardLocations[13] = new Property("13 Ave", 13, 100, 100, true);
        boardLocations[14] = new Property("14 Ave", 14, 100, 100, true);
        boardLocations[15] = new Property("15 Ave", 15, 100, 100, false);
        boardLocations[16] = new Property("16 Ave", 16, 100, 100, true);
        boardLocations[17] = communityChest;
        boardLocations[18] = new Property("18 Ave", 18, 100, 100, true);
        boardLocations[19] = new Property("19 Ave", 19, 100, 100, true);
        boardLocations[20] = freeParking;
        boardLocations[21] = new Property("21 Ave", 21, 100, 100, true);
        boardLocations[22] = chance;
        boardLocations[23] = new Property("23 Ave", 23, 100, 100, true);
        boardLocations[24] = new Property("24 Ave", 24, 100, 100, true);
        boardLocations[25] = new Property("25 Ave", 25, 100, 100, false);
        boardLocations[26] = new Property("26 Ave", 26, 100, 100, true);
        boardLocations[27] = new Property("27 Ave", 27, 100, 100, true);
        boardLocations[28] = new Property("28 Ave", 28, 100, 100, false);
        boardLocations[29] = new Property("29 Ave", 29, 100, 100, true);
        boardLocations[30] = goToJail;
        boardLocations[31] = new Property("31 Ave", 31, 100, 100, true);
        boardLocations[32] = new Property("32 Ave", 32, 100, 100, true);
        boardLocations[33] = communityChest;
        boardLocations[34] = new Property("34 Ave", 34, 100, 100, true);
        boardLocations[35] = new Property("35 Ave", 35, 100, 100, false);
        boardLocations[36] = chance;
        boardLocations[37] = new Property("37 Ave", 37, 100, 100, true);
        boardLocations[38] = tax;
        boardLocations[39] = new Property("39 Ave", 39, 100, 100, true);
    }

    public void playerLanded(GameState gameState) {
        boardLocations[gameState.getCurrentPlayer().getCurrentPosition()].playerLanded(gameState);
    }

    public void go(GameState gameState) {
        Player player = gameState.getCurrentPlayer();

        System.out.println(player.getName() + " landed on Go.");
        player.updateMoney(400);
        System.out.println(player.getName() + " received $400 for landing on Go.");
        gameState.setTurnCompleted(true);
    }

    public void communityChest(GameState gameState) {
        Player player = gameState.getCurrentPlayer();
        System.out.println(player.getName() + " landed on Community Chest.");
        gameState.setTurnCompleted(true);
    }

    public void chance(GameState gameState) {
        Player player = gameState.getCurrentPlayer();
        System.out.println(player.getName() + " landed on Chance.");
        gameState.setTurnCompleted(true);
    }

    public void tax(GameState gameState) {
        Player player = gameState.getCurrentPlayer();
        System.out.println(player.getName() + " landed on Tax.");
        player.updateMoney(-100);
        gameState.setTurnCompleted(true);
    }

    public void visitingJail(GameState gameState) {
        Player player = gameState.getCurrentPlayer();
        System.out.println(player.getName() + " landed on Visiting Jail.");
        gameState.setTurnCompleted(true);
    }

    public void freeParking(GameState gameState) {
        Player player = gameState.getCurrentPlayer();
        System.out.println(player.getName() + " landed on Free Parking.");
        gameState.setTurnCompleted(true);
    }

    public void goToJail(GameState gameState) {
        Player player = gameState.getCurrentPlayer();
        System.out.println(player.getName() + " landed on Go to Jail.");
        gameState.setTurnCompleted(true);
    }
}
