package org.mcuni.speedrun;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class GameSystem {

    SpeedRun plugin;
    MessageHandler MessageHandlerClass;
    BossBarHandler BossBarHandlerClass;

    public GameSystem(SpeedRun plugin) {
        this.plugin = plugin;
        MessageHandlerClass = new MessageHandler(null);
        this.BossBarHandlerClass = new BossBarHandler(plugin);
        Bukkit.getLogger().info("[SpeedRun][GameSystem] Class started.");
    }

    public void End() {
        Bukkit.getLogger().info("[SpeedRun][GameSystem] Ending game...");
        plugin.GameLoading = true;
        plugin.GameRunning = false;
        plugin.GoalItem = null;

        PlayerHandler ph = new PlayerHandler(plugin);
        ph.TeleportAll(Objects.requireNonNull(plugin.getConfig().getString("ReturnWorld")), 0);
        ph.ClearAllPlayerInventory();

        WorldHandler wh = new WorldHandler(plugin);
        wh.DeleteGameWorld();

        BossBarHandlerClass.DestroyBossBar();
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.GameLoading = false;
                Bukkit.getLogger().info("[SpeedRun][GameSystem] Game ended.");
                cancel();
            }
        }.runTaskTimer(plugin,0,20*5);
    }

    public void Start() {
        Bukkit.getLogger().info("[SpeedRun][GameSystem] Starting game...");
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
                if (ph.TeleportAll("SpeedRun", 10)) {
                    MessageHandlerClass.BroadcastMessage("[4/4] Setting gamemode...");
                    ph.ClearAllPlayerInventory();
                    ph.SetAllPlayerMode("survival");
                    MessageHandlerClass.BroadcastMessage("\n\n");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            String GoalItem = String.valueOf(plugin.GoalItem);
                            GoalItem = GoalItem.replace('_', ' ');

                            MessageHandlerClass.BroadcastMessage(ChatColor.RED + "It may take a few seconds to load the terrain around you.\n\n");
                            MessageHandlerClass.BroadcastMessage(ChatColor.RED + "Spawn protection is ON, walk out 16 blocks to build and mine!\n\n");
                            MessageHandlerClass.BroadcastMessage(ChatColor.WHITE + "The first person to find a " + ChatColor.GREEN + GoalItem + ChatColor.WHITE + " wins the game.");
                            MessageHandlerClass.BroadcastMessage("GO! GO! GO!");
                            BossBarHandlerClass.CreateBossBar(String.valueOf(plugin.GoalItem));
                            plugin.GameRunning = true;
                            plugin.GameLoading = false;
                            cancel();
                        }
                    }.runTaskTimer(plugin,20*17,20*10);
                } else {
                    MessageHandlerClass.BroadcastMessage(ChatColor.RED + "There was an error whilst starting the game, please see the console for more information.");
                    plugin.GameRunning = false;
                    plugin.GameLoading = false;
                    plugin.GoalItem = null;
                }

                Bukkit.getLogger().info("[SpeedRun][GameSystem] Game started...");
                cancel();
            }
        }.runTaskTimer(plugin,0,20*5);
    }
}
