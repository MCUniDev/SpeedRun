package org.mcuni.speedrun;

import org.apache.logging.log4j.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Boss;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcuni.speedrun.commands.SpeedRunCommands;
import org.mcuni.speedrun.events.EntityPickupItem;

import java.util.Objects;

public class SpeedRun extends JavaPlugin {

    public Material GoalItem = null;
    public boolean GameRunning = false;
    public boolean GameLoading = false;

    protected EntityPickupItem EntityPickupItemClass;
    protected MessageHandler MessageHandlerClass;
    protected GameSystem GameSystemClass;
    protected BossBarSystem BossBarSystemClass;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        loadClasses();
        loadCommands();
        loadEventHandlers();

        if (Bukkit.getOnlinePlayers().size() > 0) {
            for (Player on : Bukkit.getOnlinePlayers()) {
                BossBarSystemClass.AddPlayer(on);
            }
        }
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
        GameSystemClass = new GameSystem(this);
        BossBarSystemClass = new BossBarSystem(this);
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

    public void StartGame() {
        GameSystemClass.StartGame();
        BossBarSystemClass.ShowItem();
    }

    public void EndGame() {
        GameSystemClass.EndGame();
        BossBarSystemClass.HideItem();
    }

    public void TeleportPlayers(String World) {
        for(Player p : Bukkit.getOnlinePlayers()){
            p.getInventory().clear();
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "tpp " + World + " " + p.getName());
        }
    }

    public void SetPlayerMode(String Mode) {
        for(Player p : Bukkit.getOnlinePlayers()){
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "gamemode " + Mode + " " + p.getName());
        }
    }

    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        if (!BossBarSystemClass.GetBar().getPlayers().contains(event.getPlayer())) {
            BossBarSystemClass.AddPlayer(event.getPlayer());
        }
    }
}
