package org.mcuni.speedrun;

import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcuni.speedrun.commands.SpeedRunCommands;
import org.mcuni.speedrun.events.EntityPickupItem;
import org.mcuni.speedrun.events.InventoryClick;

public class SpeedRun extends JavaPlugin {

    public Material GoalItem = null;
    public BossBar bossbar;
    public boolean GameRunning = false;
    public boolean GameLoading = false;

    protected EntityPickupItem EntityPickupItemClass;
    protected InventoryClick InventoryClickClass;
    protected MessageHandler MessageHandlerClass;
    protected GameSystem GameSystemClass;

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("[SpeedRun] Beginning startup sequence.");
        saveDefaultConfig();

        loadClasses();
        loadCommands();
        loadEventHandlers();

        WorldHandler world = new WorldHandler(this);

        if (world.WorldExists(getConfig().getString("GameWorld"))) {
            world.DeleteGameWorld();
        }
        Bukkit.getLogger().info("[SpeedRun] Startup completed.");
    }

    @Override
    public void onDisable() {}

    /**
     * Loads any classes that can't be loaded by initializers.
     */
    private void loadClasses() {
        Bukkit.getLogger().info("[SpeedRun] Loading classes...");
        EntityPickupItemClass = new EntityPickupItem(this);
        InventoryClickClass = new InventoryClick(this);
        MessageHandlerClass = new MessageHandler(null);
        GameSystemClass = new GameSystem(this);
        Bukkit.getLogger().info("[SpeedRun] Classes loaded...");
    }

    /**
     * Loads and registers the plugin's command handlers.
     */
    private void loadCommands() {
        Bukkit.getLogger().info("[SpeedRun] Loading commands...");
        try {
            this.getCommand("speedrun").setExecutor(new SpeedRunCommands(this));
        } catch (NullPointerException e) {
            Bukkit.getLogger().severe("[SpeedRun] ERROR: Couldn't enable /speedrun command.");
        }
        Bukkit.getLogger().info("[SpeedRun] Commands loaded.");
    }

    /**
     * Loads and registers all the plugin's event handlers.
     */
    private void loadEventHandlers() {
        Bukkit.getLogger().info("[SpeedRun] Loading event handlers...");
        Bukkit.getServer().getPluginManager().registerEvents(EntityPickupItemClass, this);
        Bukkit.getServer().getPluginManager().registerEvents(InventoryClickClass, this);
        Bukkit.getLogger().info("[SpeedRun] Event handlers loaded.");
    }
}
