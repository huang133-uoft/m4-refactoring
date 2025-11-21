package theater;

/**
 *  Enriched performance data used when rendering statements.
 */
public class PerformanceData {

    private final String playName;
    private final String playType;
    private final int audience;
    private final int amount;

    public PerformanceData(String playName, String playType, int audience, int amount) {
        this.playName = playName;
        this.playType = playType;
        this.audience = audience;
        this.amount = amount;
    }

    public String getPlayName() {
        return playName;
    }

    public String getPlayType() {
        return playType;
    }

    public int getAudience() {
        return audience;
    }

    public int getAmount() {
        return amount;
    }
}
