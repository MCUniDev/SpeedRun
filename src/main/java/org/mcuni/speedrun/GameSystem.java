package org.mcuni.speedrun;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

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
        plugin.GameRunning = false;
        plugin.GoalItem = null;

        MessageHandlerClass.BroadcastMessage("[1/4] Ending, Teleporting players...");
        plugin.TeleportPlayers(Objects.requireNonNull(plugin.getConfig().getString("ReturnWorld")));
        MessageHandlerClass.BroadcastMessage("[2/4] Ending, Deleting world...");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "world delete "+Objects.requireNonNull(plugin.getConfig().getString("GameWorld")));

        while (Bukkit.getWorld(Objects.requireNonNull(plugin.getConfig().getString("GameWorld"))) != null) {
            // Wait...
        }

        MessageHandlerClass.BroadcastMessage("[3/4] Setting players gamemode...");
        plugin.SetPlayerMode("adventure");
        MessageHandlerClass.BroadcastMessage("[4/4] The game has now ended and is ready to start again.");
    }

    public void StartGame() {
        MessageHandlerClass.BroadcastMessage("SpeedRun is now starting");
        MessageHandlerClass.BroadcastMessage("[1/4] Configuring game...");
        MessageHandlerClass.BroadcastMessage("[2/4] Generating world...");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "world create "+ Objects.requireNonNull(plugin.getConfig().getString("GameWorld")));

        while (Bukkit.getWorld(Objects.requireNonNull(plugin.getConfig().getString("GameWorld"))) == null) {
            // Wait...
        }

        MessageHandlerClass.BroadcastMessage("[3/4] Teleporting...");
        plugin.TeleportPlayers("SpeedRun");
        MessageHandlerClass.BroadcastMessage("[4/4] Setting gamemode...");
        plugin.SetPlayerMode("survival");
        MessageHandlerClass.BroadcastMessage("\n\n");
        MessageHandlerClass.BroadcastMessage(ChatColor.WHITE + "The first person to find a " + ChatColor.GREEN + plugin.GoalItem + ChatColor.WHITE + " wins the game.");
        MessageHandlerClass.BroadcastMessage("GO! GO! GO!");
        plugin.GameRunning = true;
    }
}
