public class Money {

    private final long amountInPaise;
    private final String currency;

    // Constructor
    public Money(long amountInPaise, String currency) {

        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Invalid currency");
        }

        this.amountInPaise = amountInPaise;
        this.currency = currency;
    }

    // Add two Money objects
    public Money add(Money other) {

        checkCurrency(other);

        return new Money(
            Math.addExact(this.amountInPaise, other.amountInPaise),
            this.currency
        );
    }

    // Subtract two Money objects
    public Money subtract(Money other) {

        checkCurrency(other);

        return new Money(
            Math.subtractExact(this.amountInPaise, other.amountInPaise),
            this.currency
        );
    }

    // Check whether amount is positive
    public boolean isPositive() {
        return amountInPaise > 0;
    }

    // Check currency compatibility
    private void checkCurrency(Money other) {

        if (other == null || !this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }
    }

    // Getter methods
    public long getAmountInPaise() {
        return amountInPaise;
    }

    public String getCurrency() {
        return currency;
    }

    // Display money
    @Override
    public String toString() {
        return currency + " " +
               String.format("%.2f", amountInPaise / 100.0);
    }
}