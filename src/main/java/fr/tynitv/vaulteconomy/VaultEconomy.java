package fr.tynitv.vaulteconomy;

import fr.tynitv.vaulteconomy.command.BalanceCommand;
import fr.tynitv.vaulteconomy.command.PayCommand;
import fr.tynitv.vaulteconomy.economy.EconomyManager;
import org.bukkit.plugin.java.JavaPlugin;

public class VaultEconomy extends JavaPlugin {

    private static VaultEconomy instance;
    private EconomyManager economyManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();

        this.economyManager = new EconomyManager(this);

        if (getCommand("balance") != null) {
            getCommand("balance").setExecutor(new BalanceCommand(this, economyManager));
        }

        if (getCommand("pay") != null) {
            getCommand("pay").setExecutor(new PayCommand(this, economyManager));
        }

        getLogger().info("VaultEconomy v1.0.0 enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("VaultEconomy disabled!");
    }

    public static VaultEconomy getInstance() {
        return instance;
    }
}
