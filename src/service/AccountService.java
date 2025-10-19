package service;

import model.Account;
import java.util.HashMap;
import java.util.Map;

public class AccountService {

    private final Map<String, Account> accounts = new HashMap<>();

    public void createAccount(String id, double initialBalance) {
        if (accounts.containsKey(id))
            throw new IllegalArgumentException("Account already exists.");
        accounts.put(id, new Account(id, initialBalance));
    }

    public Account authenticate(String id) {
        Account acc = accounts.get(id);
        if (acc == null)
            throw new IllegalArgumentException("Invalid account ID.");
        return acc;
    }

    public void showAccounts() {
        accounts.values().forEach(System.out::println);
    }
}
