package realestateempire.multiplayer.trade;

public class TradeDetails {

    private TradePlayerDetails offeror;
    private TradePlayerDetails receiver;
    private boolean tradeAccepted;

    public TradeDetails() { }

    public TradeDetails(TradePlayerDetails offeror, TradePlayerDetails receiver) {
        this.offeror = offeror;
        this.receiver = receiver;
    }

    public void setTradeAccepted(boolean tradeAccepted) {
        this.tradeAccepted = tradeAccepted;
    }

    public TradePlayerDetails getOfferor() {
        return offeror;
    }

    public TradePlayerDetails getReceiver() {
        return receiver;
    }

    public boolean isTradeAccepted() {
        return tradeAccepted;
    }
}
