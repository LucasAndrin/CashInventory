package model;

import java.util.Objects;

public class Denomination {
    private final double value;
    private final String label;

    public Denomination(double value) {
        this.value = value;
        this.label = String.format("R$%.2f", value);
    }

    public double getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public boolean equals(Object denomination) {
        return this == denomination
            || denomination instanceof Denomination
            && Double.compare(((Denomination) denomination).value, value) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return label;
    }
}
