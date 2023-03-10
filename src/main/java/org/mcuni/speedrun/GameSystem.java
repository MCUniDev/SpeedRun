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

    public void End() {
        plugin.GameLoading = true;
        plugin.GameRunning = false;
        plugin.GoalItem = null;

        PlayerHandler ph = new PlayerHandler(plugin);
        ph.TeleportAll(Objects.requireNonNull(plugin.getConfig().getString("ReturnWorld")));
        ph.ClearAllPlayerInventory();

        WorldHandler wh = new WorldHandler(plugin);
        wh.DeleteGameWorld();
        new BukkitRunnable() {
            @Override
            public void run() {
                while (Bukkit.getWorld(Objects.requireNonNull(plugin.getConfig().getString("GameWorld"))) != null) {
                    // Wait...
                }
                plugin.GameLoading = false;
                cancel();
            }
        }.runTaskTimer(plugin,0,20*5);
    }

    public void Start() {
        plugin.GameLoading = true;
        MessageHandlerClass.BroadcastMessage("SpeedRun is now starting");
        MessageHandlerClass.BroadcastMessage("[1/4] Configuring game...");
        MessageHandlerClass.BroadcastMessage("[2/4] Generating world, this could take a few seconds...");

        WorldHandler world = new WorldHandler(plugin);
        world.CreateGameWorld();
        new BukkitRunnable() {
            @Override
            public void run() {
                while (!world.WorldExists(plugin.getConfig().getString("GameWorld"))) {
                    // Wait...
                }
                MessageHandlerClass.BroadcastMessage("[3/4] Teleporting...");
                PlayerHandler ph = new PlayerHandler(plugin);
                if (ph.TeleportAll("SpeedRun")) {
                    MessageHandlerClass.BroadcastMessage("[4/4] Setting gamemode...");
                    ph.ClearAllPlayerInventory();
                    ph.SetAllPlayerMode("survival");
                    MessageHandlerClass.BroadcastMessage("\n\n");
                    MessageHandlerClass.BroadcastMessage(ChatColor.RED + "It may take a few seconds to load the terrain around you.\n\n");
                    MessageHandlerClass.BroadcastMessage(ChatColor.RED + "Spawn protection is ON, walk out 16 blocks to build and mine!\n\n");
                    MessageHandlerClass.BroadcastMessage(ChatColor.WHITE + "The first person to find a " + ChatColor.GREEN + plugin.GoalItem + ChatColor.WHITE + " wins the game.");
                    MessageHandlerClass.BroadcastMessage("GO! GO! GO!");
                    plugin.GameRunning = true;
                    plugin.GameLoading = false;
                } else {
                    MessageHandlerClass.BroadcastMessage(ChatColor.RED + "There was an error whilst starting the game, please see the console for more information.");
                    plugin.GameRunning = false;
                    plugin.GameLoading = false;
                    plugin.GoalItem = null;
                }

                cancel();
            }
        }.runTaskTimer(plugin,0,20*5);
    }
}
