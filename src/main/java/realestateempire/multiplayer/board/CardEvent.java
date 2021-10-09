package realestateempire.multiplayer.board;

public class CardEvent {

    public int card;

    public CardEvent() {
        card = 0;
    }

    public CardEvent(int card) {
        this.card = card;
    }

    public void setCard(int card) {
        this.card = card;
    }

    public int getCard() {
        return card;
    }
}
