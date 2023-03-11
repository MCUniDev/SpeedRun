package org.mcuni.speedrun;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class PlayerHandler {

    SpeedRun plugin;

    public PlayerHandler(SpeedRun plugin) {
        this.plugin = plugin;
        Bukkit.getLogger().info("[SpeedRun][PlayerHandler] Class started.");
    }

    public boolean TeleportAll(String world, int delay) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Bukkit.getLogger().info("[SpeedRun][PlayerHandler] Teleporting all players...");
                WorldHandler wh = new WorldHandler(plugin);

                if (wh.WorldExists(world)) {
                    for(Player player : plugin.getServer().getOnlinePlayers()) {
                        player.teleport(Objects.requireNonNull(plugin.getServer().getWorld(world)).getSpawnLocation());
                    }
                    plugin.getServer().getLogger().info("[SpeedRun][PlayerHandler] Teleported all players to "+world);
                } else {
                    plugin.getServer().getLogger().severe("[SpeedRun][PlayerHandler] Attempted to teleport players to non-existent world (aborted).");
                }
            }
        }.runTaskTimer(plugin,20*delay,20*10);
        return true;
    }

    public void SetAllPlayerMode(String Mode) {
        Bukkit.getLogger().info("[SpeedRun][PlayerHandler] Setting all players gamemodes...");
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamemode " + Mode + " " + player.getName());
        }
        Bukkit.getLogger().info("[SpeedRun][PlayerHandler] Set all players gamemodes.");
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
        Bukkit.getLogger().info("[SpeedRun][PlayerHandler] Clearing all players inventories...");
        for(Player player : plugin.getServer().getOnlinePlayers()) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "clear " + player.getName());
        }
        Bukkit.getLogger().info("[SpeedRun][PlayerHandler] Cleared all players inventories.");
    }
}
