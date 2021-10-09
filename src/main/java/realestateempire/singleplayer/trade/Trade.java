package realestateempire.singleplayer.trade;

import realestateempire.graphics.Interactive;
import realestateempire.graphics.button.Button;
import realestateempire.graphics.model.Model;
import realestateempire.singleplayer.board.property.Property;
import realestateempire.singleplayer.player.Player;

public abstract class Trade implements Interactive {

    protected final Model background;
    protected final TradePlayerGraphics userGraphics;
    protected final TradePlayerGraphics playerGraphics;
    protected final TradePlayer user;
    protected final TradePlayer player;
    protected boolean enabled;

    public Trade(Player user) {
        background = new Model("Prompts/Trade.png", 0, 0);
        String[] incMoneyTextures = {"Buttons/Add Money.png",
                "Buttons/Add Money M.png", "Buttons/Add Money D.png"};
        Button userIncreaseMoney = new Button(incMoneyTextures, 556, 929);

        String[] decreaseMoneyTextures = {"Buttons/Subtract Money.png",
                "Buttons/Subtract Money M.png", "Buttons/Subtract Money D.png"};
        Button userDecreaseMoney = new Button(decreaseMoneyTextures, 370, 929);

        userGraphics = new TradePlayerGraphics(0, userIncreaseMoney,
                userDecreaseMoney);
        this.user = new TradePlayer(userGraphics, userIncreaseMoney,
                userDecreaseMoney, this::updateButtons);
        this.user.updatePlayer(user);

        Button playerIncreaseMoney = new Button(incMoneyTextures, 1010, 929);
        Button playerDecreaseMoney = new Button(decreaseMoneyTextures, 824, 929);
        playerGraphics = new TradePlayerGraphics(1, playerIncreaseMoney,
                playerDecreaseMoney);
        player = new TradePlayer(playerGraphics, playerIncreaseMoney,
                playerDecreaseMoney, this::updateButtons);
        player.updatePlayer(user);
        enabled = false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    protected abstract void updateButtons();

    public void enable(Player player) {
        user.clearTrade();
        this.player.clearTrade();
        this.player.updatePlayer(player);
        enabled = true;
    }

    public boolean selectProperty(Property property) {
        if (user.canAddProperty(property)) {
            user.addProperty(property);
            updateButtons();
            return true;
        }
        if (player.canAddProperty(property)) {
            player.addProperty(property);
            updateButtons();
            return true;
        }
        return false;
    }

    protected void makeTrade() {
        int moneyDelta = user.getMoney() - player.getMoney();
        Player user = this.user.getPlayer();
        Player player = this.player.getPlayer();
        user.updateMoney(-1 * moneyDelta);
        player.updateMoney(moneyDelta);
        for (Property property : this.user.getProperties()) {
            property.setOwner(player);
        }
        for (Property property : this.player.getProperties()) {
            property.setOwner(user);
        }
        enabled = false;
    }
}
