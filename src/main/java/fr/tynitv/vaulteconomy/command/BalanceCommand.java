package fr.tynitv.vaulteconomy.command;

import fr.tynitv.vaulteconomy.VaultEconomy;
import fr.tynitv.vaulteconomy.economy.EconomyManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BalanceCommand implements CommandExecutor {

    private final VaultEconomy plugin;
    private final EconomyManager economyManager;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public BalanceCommand(VaultEconomy plugin, EconomyManager economyManager) {
        this.plugin = plugin;
        this.economyManager = economyManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Commande reservée aux joueurs.");
            return true;
        }

        double bal = economyManager.getBalance(player.getUniqueId());
        String symbol = plugin.getConfig().getString("currency-symbol", "$");
        String prefix = plugin.getConfig().getString("messages.prefix", "");
        String msg = plugin.getConfig().getString("messages.balance", "")
                .replace("<prefix>", prefix)
                .replace("{balance}", String.format("%.2f %s", bal, symbol));

        player.sendMessage(mm.deserialize(msg));
        return true;
    }
}
