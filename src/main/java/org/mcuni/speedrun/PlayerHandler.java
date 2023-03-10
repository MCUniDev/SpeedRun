package org.mcuni.speedrun;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class PlayerHandler {

    SpeedRun plugin;

    public PlayerHandler(SpeedRun plugin) {
        this.plugin = plugin;
    }

    public boolean TeleportAll(String world) {
        WorldHandler wh = new WorldHandler(plugin);

        if (wh.WorldExists(world)) {
            for(Player player : plugin.getServer().getOnlinePlayers()) {
                player.teleport(Objects.requireNonNull(plugin.getServer().getWorld(world)).getSpawnLocation());
            }
            plugin.getServer().getLogger().info("[SpeedRun][PlayerHandler] Teleported all players to "+world);
            return true;
        } else {
            plugin.getServer().getLogger().severe("[SpeedRun][PlayerHandler] Attempted to teleport players to non-existent world (aborted).");
            return false;
        }
    }

    public void SetAllPlayerMode(String Mode) {
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamemode " + Mode + " " + player.getName());
        }
    }

    public void ClearAllPlayerInventory() {
        new BukkitRunnable() {
            @Override
            public void run() {
                DoClearAllPlayerInventory();
                cancel();
            }
        }.runTaskTimer(plugin,20*2,20*10);
    }

    private void DoClearAllPlayerInventory() {
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "clear " + player.getName());
        }
    }
}
