package theater;

import java.util.List;

/**
 *  Data-transfer object representing all information
 *  needed to render a statement.
 */
public class StatementData {

    private final String customer;
    private final List<PerformanceData> performances;
    private final int totalAmount;
    private final int totalVolumeCredits;

    public StatementData(String customer, List<PerformanceData> performances, int totalAmount, int totalVolumeCredits) {
        this.customer = customer;
        this.performances = performances;
        this.totalAmount = totalAmount;
        this.totalVolumeCredits = totalVolumeCredits;
    }

    public String getCustomer() {
        return customer;
    }

    public List<PerformanceData> getPerformances() {
        return performances;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public int getTotalVolumeCredits() {
        return totalVolumeCredits;
    }

}
