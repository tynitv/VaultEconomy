package fr.tynitv.vaulteconomy.command;

import fr.tynitv.vaulteconomy.VaultEconomy;
import fr.tynitv.vaulteconomy.economy.EconomyManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PayCommand implements CommandExecutor {

    private final VaultEconomy plugin;
    private final EconomyManager economyManager;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public PayCommand(VaultEconomy plugin, EconomyManager economyManager) {
        this.plugin = plugin;
        this.economyManager = economyManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Commande reservée aux joueurs.");
            return true;
        }

        if (args.length < 2) {
            player.sendMessage(mm.deserialize("<red>Usage: /pay <joueur> <montant></red>"));
            return true;
        }

        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            player.sendMessage(mm.deserialize("<red>Joueur introuvable.</red>"));
            return true;
        }

        double amount;
        try {
            amount = Double.parseDouble(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage(mm.deserialize("<red>Montant invalide.</red>"));
            return true;
        }

        String symbol = plugin.getConfig().getString("currency-symbol", "$");
        String prefix = plugin.getConfig().getString("messages.prefix", "");

        if (economyManager.withdraw(player.getUniqueId(), amount)) {
            economyManager.deposit(target.getUniqueId(), amount);

            String paidMsg = plugin.getConfig().getString("messages.paid", "")
                    .replace("<prefix>", prefix)
                    .replace("{amount}", String.format("%.2f %s", amount, symbol))
                    .replace("{player}", target.getName());
            player.sendMessage(mm.deserialize(paidMsg));

            String recMsg = plugin.getConfig().getString("messages.received", "")
                    .replace("<prefix>", prefix)
                    .replace("{amount}", String.format("%.2f %s", amount, symbol))
                    .replace("{player}", player.getName());
            target.sendMessage(mm.deserialize(recMsg));
        } else {
            String err = plugin.getConfig().getString("messages.insufficient-funds", "").replace("<prefix>", prefix);
            player.sendMessage(mm.deserialize(err));
        }

        return true;
    }
}
