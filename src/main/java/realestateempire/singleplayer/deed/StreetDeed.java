package realestateempire.singleplayer.deed;

import realestateempire.graphics.button.Button;
import realestateempire.graphics.button.ButtonAction;
import realestateempire.graphics.model.Model;
import realestateempire.graphics.text.Font;
import realestateempire.graphics.text.Text;
import realestateempire.singleplayer.board.street.Street;
import realestateempire.singleplayer.player.Player;

public class StreetDeed extends Deed<Street> {

    private final Model background;
    private final Button buyHouse;
    private final Button sellHouse;
    private final Button mortgage;
    private final Button payMortgage;
    private final Button close;
    private final Text rentHotel;
    private final Text mortgageValue;
    private final Text buildingPrice;
    private final Text[] rentHouses;
    private Street street;

    public StreetDeed(Player player, ButtonAction closeDeed) {
        super(player);
        background = new Model("Prompts/View Street.png", 0, 0);

        String[] buyHouseTextures = { "Buttons/Buy House.png",
                "Buttons/Buy House M.png", "Buttons/Buy House D.png" };
        buyHouse = new Button(buyHouseTextures, 303, 1060);
        buyHouse.addAction(this::buyHouse);

        String[] sellHouseTextures = { "Buttons/Sell House.png",
                "Buttons/Sell House M.png", "Buttons/Sell House D.png" };
        sellHouse = new Button(sellHouseTextures, 590, 1060);
        sellHouse.addAction(this::sellHouse);

        String [] mortgageTextures = { "Buttons/Mortgage.png",
                "Buttons/Mortgage M.png", "Buttons/Mortgage D.png" };
        mortgage = new Button(mortgageTextures, 446, 942);
        mortgage.addAction(this::mortgage);

        String [] payMortgageTextures = { "Buttons/Pay Mortgage.png",
                "Buttons/Pay Mortgage M.png", "Buttons/Pay Mortgage D.png" };
        payMortgage = new Button(payMortgageTextures, 734, 942);
        payMortgage.addAction(this::payMortgage);

        String[] closeTextures = { "Buttons/Close.png", "Buttons/Close M.png",
                "Buttons/Close.png" };
        close = new Button(closeTextures, 877, 1060);
        close.addAction(closeDeed);

        rentHotel = new Text(" ", Font.BOLD, 760, 721, 0.57);
        mortgageValue = new Text(" ", Font.BOLD, 576, 765, 0.57);
        buildingPrice = new Text(" ", Font.BOLD, 1057, 765, 0.57);
        rentHouses = new Text[5];
        rentHouses[0] = new Text(" ", Font.BOLD, 727, 478, 0.57);
        for (int i = 1; i < rentHouses.length; i++) {
            rentHouses[i] = new Text(" ", Font.BOLD, 889, 535 + (i - 1) * 43, 0.57);
        }
    }

    @Override
    void render() {
        background.render();
        super.render();
        buyHouse.render();
        sellHouse.render();
        mortgage.render();
        payMortgage.render();
        close.render();
        rentHotel.render();
        mortgageValue.render();
        buildingPrice.render();
        for (Text text : rentHouses) {
            text.render();
        }
    }

    @Override
    void cursorMoved(double xCursor, double yCursor) {
        buyHouse.cursorMoved(xCursor, yCursor);
        sellHouse.cursorMoved(xCursor, yCursor);
        mortgage.cursorMoved(xCursor, yCursor);
        payMortgage.cursorMoved(xCursor, yCursor);
        close.cursorMoved(xCursor, yCursor);
    }

    @Override
    boolean buttonPressed(double xCursor, double yCursor) {
        return buyHouse.buttonPressed(xCursor, yCursor)
                || sellHouse.buttonPressed(xCursor,yCursor)
                || mortgage.buttonPressed(xCursor, yCursor)
                || payMortgage.buttonPressed(xCursor, yCursor)
                || close.buttonPressed(xCursor, yCursor);
    }

    @Override
    public void updateProperty(Street street) {
        this.street = street;
        updateText();
        updateButtons();
    }

    void updateText() {
        super.updateText(street);
        int[] buildingRent = street.getBuildingRents();
        for (int i = 0; i < buildingRent.length - 1; i++) {
            rentHouses[i].updateText("$" + buildingRent[i] + ".");
        }
        rentHotel.updateText("$" + buildingRent[5] + ".");
        mortgageValue.updateText("$" + street.getMortgageValue() + ".");
        buildingPrice.updateText("$" + street.getBuildingPrice() + ".");
    }

    private void updateButtons() {
        buyHouse.setEnabled(street.canBuyHouse(player));
        sellHouse.setEnabled(street.canSellHouse(player));
        mortgage.setEnabled(street.canMortgage(player));
        payMortgage.setEnabled(street.canPayMortgage(player));
    }

    private void buyHouse() {
        street.buyHouse();
        updateButtons();
    }

    private void sellHouse() {
        street.sellHouse();
        updateButtons();
    }

    private void mortgage() {
        street.mortgage();
        updateButtons();
    }

    private void payMortgage() {
        street.payMortgage();
        updateButtons();
    }
}
