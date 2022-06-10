package wtf.casper.randomplayertp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RPTCommand implements CommandExecutor {

    private final RandomPlayerTP randomPlayerTP;

    public RPTCommand(RandomPlayerTP randomPlayerTP) {
        this.randomPlayerTP = randomPlayerTP;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) {
            sender.sendMessage("You must be a player to use this command.");
            return true;
        }

        if (!player.hasPermission("randomplayertp.use")) {
            return true;
        }

        List<RandomPlayerTP.Cooldown> cooldowns = randomPlayerTP.getCooldown().stream().filter(RandomPlayerTP.Cooldown::isExpired).toList();

        if (cooldowns.size() == 0) {
            sender.sendMessage(ChatColor.RED + "There are no players who haven't been teleported to recently.");
            return true;
        }

        RandomPlayerTP.Cooldown cooldown = cooldowns.get(randomInt(0, cooldowns.size() - 1));
        Player target = Bukkit.getPlayer(cooldown.getUuid());
        if (target == null) {
            return true;
        }

        player.teleportAsync(target.getLocation()).thenRun(() -> {
            sender.sendMessage(ChatColor.GREEN+"You have been teleported to " + target.getName() + ".");
        });

        cooldown.addCooldown();
        return true;
    }

    private int randomInt(int min, int max) {
        return (int) (Math.random() * (max - min+1)) + min;
    }
}
