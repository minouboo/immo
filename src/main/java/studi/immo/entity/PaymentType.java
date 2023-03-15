package studi.immo.entity;

public enum PaymentType {

    RENT ("Loyer"),
    DEPOSIT ("Caution"),
    OTHER ("Autre");

    private final String displayValue;

    private PaymentType(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
