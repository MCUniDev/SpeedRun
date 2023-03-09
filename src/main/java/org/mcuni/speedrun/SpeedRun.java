package org.mcuni.speedrun;

import org.apache.logging.log4j.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcuni.speedrun.commands.SpeedRunCommands;
import org.mcuni.speedrun.events.EntityPickupItem;

import java.util.Objects;

public class SpeedRun extends JavaPlugin {

    public Material GoalItem = null;
    public boolean GameRunning = false;

    protected EntityPickupItem EntityPickupItemClass;
    protected MessageHandler MessageHandlerClass;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        loadClasses();
        loadCommands();
        loadEventHandlers();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    /**
     * Loads any classes that can't be loaded by initializers.
     */
    private void loadClasses() {
        EntityPickupItemClass = new EntityPickupItem(this);
        MessageHandlerClass = new MessageHandler(null);
    }

    /**
     * Loads and registers the plugin's command handlers.
     */
    private void loadCommands() {
        try {
            this.getCommand("speedrun").setExecutor(new SpeedRunCommands(this));
        } catch (NullPointerException e) {
            Bukkit.getLogger().severe("[SpeedRun] ERROR: Couldn't enable /speedrun command.");
        }
    }

    /**
     * Loads and registers all the plugin's event handlers.
     */
    private void loadEventHandlers() {
        Bukkit.getServer().getPluginManager().registerEvents(EntityPickupItemClass, this);
    }

    public void EndGame() {
        GameRunning = false;
        GoalItem = null;

        MessageHandlerClass.BroadcastMessage("[1/4] Ending, Teleporting players...");
        TeleportPlayers(Objects.requireNonNull(getConfig().getString("ReturnWorld")));
        MessageHandlerClass.BroadcastMessage("[2/4] Ending, Deleting world...");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "world delete "+Objects.requireNonNull(getConfig().getString("GameWorld")));

        while (Bukkit.getWorld(Objects.requireNonNull(getConfig().getString("GameWorld"))) != null) {
            // Wait...
        }

        MessageHandlerClass.BroadcastMessage("[3/4] Setting players gamemode...");
        SetPlayerMode("adventure");
        MessageHandlerClass.BroadcastMessage("[4/4] The game has now ended and is ready to start again.");
    }

    public void StartGame() {
        MessageHandlerClass.BroadcastMessage("SpeedRun is now starting");
        MessageHandlerClass.BroadcastMessage("[1/4] Configuring game...");
        MessageHandlerClass.BroadcastMessage("[2/4] Generating world...");
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "world create "+ Objects.requireNonNull(getConfig().getString("GameWorld")));

        while (Bukkit.getWorld(Objects.requireNonNull(getConfig().getString("GameWorld"))) == null) {
            // Wait...
        }

        MessageHandlerClass.BroadcastMessage("[3/4] Teleporting...");
        TeleportPlayers("SpeedRun");
        MessageHandlerClass.BroadcastMessage("[4/4] Setting gamemode...");
        SetPlayerMode("survival");
        MessageHandlerClass.BroadcastMessage("\n\n");
        MessageHandlerClass.BroadcastMessage(ChatColor.WHITE + "The first person to find a " + ChatColor.GREEN + GoalItem + ChatColor.WHITE + " wins the game.");
        MessageHandlerClass.BroadcastMessage("GO! GO! GO!");
        GameRunning = true;
    }

    public void TeleportPlayers(String World) {
        for(Player p : Bukkit.getOnlinePlayers()){
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tpp " + World + " " + p.getName());
        }
    }

    public void SetPlayerMode(String Mode) {
        for(Player p : Bukkit.getOnlinePlayers()){
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamemode " + Mode + " " + p.getName());
        }
    }
}
