package realestateempire.singleplayer.player;

public enum Token {

    HAT("Tokens/Hat.png", "Tokens/Hat Small.png"),
    RED("Tokens/Red Token.png", "Tokens/Red Token Small.png"),
    BLUE("Tokens/Blue Token.png", "Tokens/Blue Token Small.png"),
    GREEN("Tokens/Green Token.png", "Tokens/Green Token Small.png");

    private final String largeTexture;
    private final String smallTexture;

    Token(String largeTexture, String smallTexture) {
        this.largeTexture = largeTexture;
        this.smallTexture = smallTexture;
    }

    public String getLargeTexture() {
        return largeTexture;
    }

    public String getSmallTexture() {
        return smallTexture;
    }
}

