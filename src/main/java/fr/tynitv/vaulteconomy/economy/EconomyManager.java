package fr.tynitv.vaulteconomy.economy;

import fr.tynitv.vaulteconomy.VaultEconomy;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EconomyManager {

    private final VaultEconomy plugin;
    private final Map<UUID, Double> balances = new HashMap<>();

    public EconomyManager(VaultEconomy plugin) {
        this.plugin = plugin;
    }

    public double getBalance(UUID uuid) {
        return balances.getOrDefault(uuid, plugin.getConfig().getDouble("starting-balance", 1000.0));
    }

    public void setBalance(UUID uuid, double amount) {
        balances.put(uuid, Math.max(0, amount));
    }

    public boolean deposit(UUID uuid, double amount) {
        if (amount <= 0) return false;
        setBalance(uuid, getBalance(uuid) + amount);
        return true;
    }

    public boolean withdraw(UUID uuid, double amount) {
        if (amount <= 0) return false;
        double current = getBalance(uuid);
        if (current < amount) return false;
        setBalance(uuid, current - amount);
        return true;
    }
}
