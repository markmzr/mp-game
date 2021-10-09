package realestateempire.singleplayer.player;

import java.awt.*;
import java.util.ArrayList;

import realestateempire.GameFileReader;
import realestateempire.graphics.Interactive;
import realestateempire.graphics.button.Button;
import realestateempire.graphics.model.Model;
import realestateempire.graphics.text.Font;
import realestateempire.graphics.text.Text;

import static realestateempire.singleplayer.player.PlayerState.IN_JAIL;

public class PlayerGraphics implements Interactive {

    private static final Point[] TOKEN_XY = initTokenXY();
    private final Model avatar;
    private final Model token;
    private final Button tradePlayer;
    private final Text name;
    private final Text money;
    private final Text moneyDelta;

    public PlayerGraphics(int id, String name, Token token, Button tradePlayer) {
        this.tradePlayer = tradePlayer;
        int yDelta = 165 * id;
        this.name = new Text(name, Font.BOLD, 1628, 84 + yDelta);
        avatar = new Model(token.getLargeTexture(), 1527, 87 + yDelta);

        String[] tokenTextures = new String[]{token.getLargeTexture(),
                token.getSmallTexture()};
        this.token = new Model(tokenTextures, 1310, 1321);
        money = new Text("$1500", Font.BOLD, 2243, 84 + yDelta);
        moneyDelta = new Text("+$0", Font.BOLD, 1920, 84 + yDelta);
    }

    @Override
    public void setEnabled(boolean enabled) {
        tradePlayer.setEnabled(false);
    }

    @Override
    public void render() {
        avatar.render();
        token.render();
        name.render();
        money.render();
        moneyDelta.render();
        tradePlayer.render();
    }

    @Override
    public void cursorMoved(double xCursor, double yCursor) {
        tradePlayer.cursorMoved(xCursor, yCursor);
    }

    @Override
    public boolean buttonPressed(double xCursor, double yCursor) {
        return tradePlayer.buttonPressed(xCursor, yCursor);
    }

    public void updateMoney(int money) {
        this.money.updateText("$" + money);
    }

    public void updateMoneyDelta(int moneyAmount) {
        String moneyDelta = moneyAmount >= 0 ? "+$" + moneyAmount
                : "-$" + -moneyAmount;
        this.moneyDelta.updateText(moneyDelta);
    }

    public void moveToken(int location, PlayerState playerState) {
        if (location == 10 && playerState == IN_JAIL) {
            token.movePosition(-125, 55);
        } else {
            token.movePosition(TOKEN_XY[location].x, TOKEN_XY[location].y);
        }
    }

    public void payJailFine() {
        token.movePosition(-84, -96);
    }

    public void declareBankruptcy() {
        token.setVisible(false);
        moneyDelta.setVisible(false);
        money.updateText("Bankrupt");
        money.movePosition(-73, 0);
        tradePlayer.setEnabled(false);
    }

    private static Point[] initTokenXY() {
        ArrayList<String[]> points = GameFileReader.readFile("Player.csv");
        Point[] tokenXY = new Point[points.size()];
        for (int i = 0; i < points.size(); i++) {
            String[] point = points.get(i);
            int x = Integer.parseInt(point[0]);
            int y = Integer.parseInt(point[1]);
            tokenXY[i] = new Point(x, y);
        }
        return tokenXY;
    }
}
