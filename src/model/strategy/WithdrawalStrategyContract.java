package model.strategy;

import java.util.Map;

import exceptions.InsufficientFundsException;
import model.Denomination;

public interface WithdrawalStrategyContract {
    /**
     * Attempts to compose the requested amount using the available inventory.
     * @param amount the desired withdrawal value
     * @param inventorySnapshot the current cash inventory
     * @return a Map<Denomination, Integer> representing how many bills of each denomination are used
     * @throws InsufficientFundsException
     * @throws IllegalArgumentException if it's impossible to compose the exact value
     */
    Map<Denomination, Integer> compose(double amount, Map<Denomination, Integer> inventorySnapshot) throws InsufficientFundsException;
}
