package model.strategy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import exceptions.InsufficientFundsException;
import model.Denomination;

/**
 * Class responsible to compose the used denominations for an amount
 */
public class NoteComposeStrategy {

    private final Map<Denomination, Integer> inventorySnapshot;

    public NoteComposeStrategy(Map<Denomination, Integer> inventorySnapshot) {
        this.inventorySnapshot = inventorySnapshot;
    }

    public Map<Denomination, Integer> getInventorySnapshot() {
        return inventorySnapshot;
    }

    /**
     * Compose the used denominations for an amount giving priority to the first denominations in the list
     * @param amount to compose
     * @param denominations to be used
     * @return used denominations
     * @throws InsufficientFundsException when it's not possible to compose the amount using the available denominations
     */
    protected Map<Denomination, Integer> composeNotes(double amount, List<Denomination> denominations) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("It's not possible to compose notes an amount of $0.00 or less!");
        }

        double remaining = amount;
        Map<Denomination, Integer> usedDenominations = new LinkedHashMap<>();
        for (Denomination denomination : denominations) {
            int neededQtd = (int) (remaining / denomination.getValue());
            int availableQtd = getInventorySnapshot().get(denomination);
            int usedQtd = Math.min(neededQtd, availableQtd);
            if (usedQtd > 0) {
                usedDenominations.put(denomination, usedQtd);
                remaining -= usedQtd * denomination.getValue();
            }

            if (remaining == 0) {
                break;
            }
        }

        if (remaining != 0) {
            throw new InsufficientFundsException(
                String.format("Unable to compose $%.2f with the available notes in the inventory!", amount)
            );
        }
        return usedDenominations;
    }
}
