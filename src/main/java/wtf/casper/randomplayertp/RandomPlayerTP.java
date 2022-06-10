package wtf.casper.randomplayertp;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Getter
public final class RandomPlayerTP extends JavaPlugin implements Listener {

    private final List<Cooldown> cooldown = new ArrayList<>();

    @Override
    public void onEnable() {

        getCommand("rpt").setExecutor(new RPTCommand(this));
        getServer().getPluginManager().registerEvents(this, this);

        for (Player onlinePlayer : getServer().getOnlinePlayers()) {
            cooldown.add(new Cooldown(onlinePlayer.getUniqueId()));
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        cooldown.add(new Cooldown(event.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        cooldown.removeIf(cooldown1 -> cooldown1.getUuid().equals(event.getPlayer().getUniqueId()));
    }

    @Getter
    static class Cooldown{
        private final UUID uuid;
        private long time;

        Cooldown(UUID uuid) {
            this.uuid = uuid;
            this.time = System.currentTimeMillis();
        }

        public void addCooldown(){
            time = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(15);
        }

        public boolean isExpired() {
            return System.currentTimeMillis() >= time;
        }
    }
}
