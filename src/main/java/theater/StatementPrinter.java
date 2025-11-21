package theater;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * This class generates a statement for a given invoice of performances.
 */
public class StatementPrinter {

    private final Invoice invoice;
    private final Map<String, Play> plays;

    public StatementPrinter(Invoice invoice, Map<String, Play> plays) {
        this.invoice = invoice;
        this.plays = plays;
    }

    /**
     * Returns a formatted statement of the invoice associated with this printer.
     * @return the formatted statement
     * @throws RuntimeException if one of the play types is not known
     */
    public String statement() {
        final StatementData data = createStatementData();
        return renderPlainText(data);
    }

    private StatementData createStatementData() {
        final List<PerformanceData> performanceDataList = new ArrayList<>();

        for (Performance performance: invoice.getPerformances()) {
            final Play play = getPlay(performance);
            final int amount = getAmount(performance);

            performanceDataList.add(new PerformanceData(
                    play.getName(),
                    play.getType(),
                    performance.getAudience(),
                    amount
            ));
        }

        final int totalAmount = getTotalAmount();
        final int totalVolumnCredits = getTotalVolumeCredits();

        return new StatementData(
                invoice.getCustomer(),
                performanceDataList,
                totalAmount,
                totalVolumnCredits
        );
    }

    private String renderPlainText(StatementData data) {
        final StringBuilder result = new StringBuilder(
                "Statement for " + data.getCustomer() + System.lineSeparator()
        );

        for (PerformanceData perf : data.getPerformances()) {
            result.append(String.format("  %s: %s (%s seats)%n",
                    perf.getPlayName(),
                    usd(perf.getAmount()),
                    perf.getAudience()));
        }

        result.append(String.format("Amount owed is %s%n",
                usd(data.getTotalAmount())));

        result.append(String.format("You earned %s credits%n",
                data.getTotalVolumeCredits()));

        return result.toString();
    }

    private int getTotalAmount() {
        int totalAmount = 0;
        for (Performance p : invoice.getPerformances()) {
            totalAmount += getAmount(p);
        }
        return totalAmount;
    }

    private int getTotalVolumeCredits() {
        int volumeCredits = 0;
        for (Performance p : invoice.getPerformances()) {
            volumeCredits += getVolumeCredits(p);
        }
        return volumeCredits;
    }

    private static String usd(int totalAmount) {
        final NumberFormat frmt = NumberFormat.getCurrencyInstance(Locale.US);
        return frmt.format(totalAmount / Constants.PERCENT_FACTOR);
    }

    private int getVolumeCredits(Performance performance) {
        int result = 0;
        result += Math.max(performance.getAudience() - Constants.BASE_VOLUME_CREDIT_THRESHOLD, 0);
        // add extra credit for every five comedy attendees
        if ("comedy".equals(getPlay(performance).getType())) {
            result += performance.getAudience() / Constants.COMEDY_EXTRA_VOLUME_FACTOR;
        }
        return result;
    }

    private Play getPlay(Performance performance) {
        return plays.get(performance.getPlayID());
    }

    private int getAmount(Performance performance) {
        int result = 0;
        switch (getPlay(performance).getType()) {
            case "tragedy":
                result = Constants.TRAGEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.TRAGEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.TRAGEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience() - Constants.TRAGEDY_AUDIENCE_THRESHOLD);
                }
                break;
            case "comedy":
                result = Constants.COMEDY_BASE_AMOUNT;
                if (performance.getAudience() > Constants.COMEDY_AUDIENCE_THRESHOLD) {
                    result += Constants.COMEDY_OVER_BASE_CAPACITY_AMOUNT
                            + (Constants.COMEDY_OVER_BASE_CAPACITY_PER_PERSON
                            * (performance.getAudience() - Constants.COMEDY_AUDIENCE_THRESHOLD));
                }
                result += Constants.COMEDY_AMOUNT_PER_AUDIENCE * performance.getAudience();
                break;
            default:
                throw new RuntimeException(String.format("unknown type: %s", getPlay(performance).getType()));
        }
        return result;
    }
}
