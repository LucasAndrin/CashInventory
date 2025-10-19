package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CashInventory {
    private final Map<Denomination, Integer> inventory = new TreeMap<>(Comparator.comparingDouble(Denomination::getValue).reversed());

    /**
     * Add a quantity of denominations to the inventory
     * @param denomination Denomination to be added
     * @param quantity Quantity of the denomination
     */
    public CashInventory add(Denomination denomination, int quantity) {
        inventory.put(denomination, inventory.getOrDefault(denomination, 0) + quantity);
        return this;
    }

    /**
     * Remove a quantity of denominations to the inventory
     * @param denomination Denomination to be removed
     * @param quantity Quantity of the denomination
     */
    public CashInventory remove(Denomination denomination, int quantity) {
        int current = inventory.getOrDefault(denomination, 0);
        if (quantity > current)
            throw new IllegalArgumentException("There's insuficient notes of " + denomination);
        inventory.put(denomination, current - quantity);
        return this;
    }

    /**
     * Return the total available value in the inventory
     * @return double of total value
     */
    public double getTotalValue() {
        return inventory.entrySet().stream()
                .mapToDouble(e -> e.getKey().getValue() * e.getValue())
                .sum();
    }

    /**
     * Prints the inventory
     */
    public void printInventory() {
        System.out.println("=== Cash Inventory ===");
        inventory.forEach((denomination, qtd) ->
            System.out.printf("%s: %d units ($%.2f)%n", denomination.getLabel(), qtd, denomination.getValue() * qtd)
        );
        System.out.printf("Total: $%.2f%n", getTotalValue());
        System.out.println("============================");
    }

    /**
     * Return unmodifiable map of the inventory
     * @return Unmodifiable map of the inventory
     */
    public Map<Denomination, Integer> getInventorySnapshot() {
        return Collections.unmodifiableMap(inventory);
    }

    public List<Double> getAvailableValues() {
        List<Double> values = new ArrayList<>();
        for (Denomination d : inventory.keySet()) {
            if (inventory.get(d) > 0)
                values.add(d.getValue());
        }
        values.sort(Collections.reverseOrder());
        return values;
    }
}
