package org.mcuni.speedrun;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class GameSystem {

    SpeedRun plugin;
    MessageHandler MessageHandlerClass;

    public GameSystem(SpeedRun plugin) {
        this.plugin = plugin;
        MessageHandlerClass = new MessageHandler(null);
        Bukkit.getLogger().info("[SpeedRun] GameSystem class loaded.");
    }

    public void EndGame() {
        plugin.GameLoading = true;
        plugin.GameRunning = false;
        plugin.GoalItem = null;

        plugin.TeleportPlayers(Objects.requireNonNull(plugin.getConfig().getString("ReturnWorld")));

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "world delete "+Objects.requireNonNull(plugin.getConfig().getString("GameWorld")));

        new BukkitRunnable() {
            @Override
            public void run() {
                while (Bukkit.getWorld(Objects.requireNonNull(plugin.getConfig().getString("GameWorld"))) != null) {
                    // Wait...
                }
                plugin.GameLoading = false;
                cancel();
            }
        }.runTaskTimer(plugin,20*10,20*10);
    }

    public void StartGame() {
        plugin.GameLoading = true;
        MessageHandlerClass.BroadcastMessage("SpeedRun is now starting");
        MessageHandlerClass.BroadcastMessage("[1/4] Configuring game...");
        MessageHandlerClass.BroadcastMessage("[2/4] Generating world, this could take a few seconds...");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "world create "+ Objects.requireNonNull(plugin.getConfig().getString("GameWorld")));

        new BukkitRunnable() {
            @Override
            public void run() {
                while (Bukkit.getWorld(Objects.requireNonNull(plugin.getConfig().getString("GameWorld"))) == null) {
                    // Wait...
                }

                MessageHandlerClass.BroadcastMessage("[3/4] Teleporting...");
                plugin.TeleportPlayers("SpeedRun");
                MessageHandlerClass.BroadcastMessage("[4/4] Setting gamemode...");
                plugin.SetPlayerMode("survival");
                MessageHandlerClass.BroadcastMessage("\n\n");
                MessageHandlerClass.BroadcastMessage(ChatColor.RED + "It may take a few seconds to load the terrain around you.\n\n");
                MessageHandlerClass.BroadcastMessage(ChatColor.RED + "Spawn protection is ON, walk out 16 blocks to build and mine!\n\n");
                MessageHandlerClass.BroadcastMessage(ChatColor.WHITE + "The first person to find a " + ChatColor.GREEN + plugin.GoalItem + ChatColor.WHITE + " wins the game.");
                MessageHandlerClass.BroadcastMessage("GO! GO! GO!");
                plugin.GameRunning = true;
                plugin.GameLoading = false;
                cancel();
            }
        }.runTaskTimer(plugin,0,20*5);
    }
}
