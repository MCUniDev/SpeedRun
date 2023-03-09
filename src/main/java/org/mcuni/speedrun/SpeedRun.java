package org.mcuni.speedrun;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.mcuni.speedrun.commands.SpeedRunCommands;
import org.mcuni.speedrun.events.EntityPickupItem;

public class SpeedRun extends JavaPlugin {

    public Material GoalItem = null;
    public boolean GameRunning = false;
    public boolean GameLoading = false;

    protected EntityPickupItem EntityPickupItemClass;
    protected MessageHandler MessageHandlerClass;
    protected GameSystem GameSystemClass;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        loadClasses();
        loadCommands();
        loadEventHandlers();

        WorldHandler world = new WorldHandler(this);

        if (world.WorldExists(getConfig().getString("GameWorld"))) {
            world.DeleteGameWorld();
        }
    }

    @Override
    public void onDisable() {
    }

    /**
     * Loads any classes that can't be loaded by initializers.
     */
    private void loadClasses() {
        EntityPickupItemClass = new EntityPickupItem(this);
        MessageHandlerClass = new MessageHandler(null);
        GameSystemClass = new GameSystem(this);
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
}
