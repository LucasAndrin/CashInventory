package model;

public class Money {
    private final Denomination denomination;
    private int quantity;

    public Money(Denomination denomination, int quantity) {
        this.denomination = denomination;
        this.quantity = quantity;
    }

    public Denomination getDenomination() {
        return denomination;
    }

    public int getQuantity() {
        return quantity;
    }

    public void addQuantity(int amount) {
        quantity += amount;
    }

    public double getTotalValue() {
        return denomination.getValue() * quantity;
    }

    @Override
    public String toString() {
        return String.format("%s x%d (total: R$%.2f)", denomination.getLabel(), quantity, getTotalValue());
    }
}
