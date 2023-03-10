package org.mcuni.speedrun;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamemode " + Mode + " *");
    }

    public void ClearAllPlayerInventory() {
        plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), "clear *");
    }
}
