package model.strategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import exceptions.InsufficientFundsException;
import model.Denomination;

/**
 * Strategy class that preserve higher notes from the cash inventory
 */
public class PreserveHigherNotesStrategy implements WithdrawalStrategyContract {

    @Override
    public Map<Denomination, Integer> compose(double amount, Map<Denomination, Integer> inventorySnapshot) throws InsufficientFundsException {
        /**
         * Available denominations sorted by asc
         */
        List<Denomination> availableDenominations = new ArrayList<>(inventorySnapshot.keySet());
        availableDenominations.sort(Comparator.comparingDouble(Denomination::getValue));

        NoteComposeStrategy composeStrategy = new NoteComposeStrategy(inventorySnapshot);
        return composeStrategy.composeNotes(amount, availableDenominations);
    }
}
