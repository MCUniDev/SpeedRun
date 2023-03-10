package org.mcuni.speedrun;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class WorldHandler {
    SpeedRun plugin;

    public WorldHandler(SpeedRun plugin) {
        this.plugin = plugin;
        Bukkit.getLogger().info("[SpeedRun][WorldHandler] Started.");
    }

    public void CreateGameWorld() {
        Bukkit.getLogger().info("[SpeedRun][WorldHandler] Creating game world, this could take some time...");

        if (WorldExists(Objects.requireNonNull(plugin.getConfig().getString("GameWorld")))) {
            Bukkit.getLogger().warning("[SpeedRun][WorldHandler] Attempted to create already existing world (deleting world first).");
            DeleteGameWorld();
        }

        WorldCreator wc = new WorldCreator(Objects.requireNonNull(plugin.getConfig().getString("GameWorld")));

        wc.environment(World.Environment.NORMAL);
        wc.type(WorldType.NORMAL);

        wc.createWorld();
        Bukkit.getLogger().info("[SpeedRun][WorldHandler] Game world created.");
    }

    public void DeleteGameWorld() {
        if (WorldExists(Objects.requireNonNull(plugin.getConfig().getString("GameWorld")))) {

            Bukkit.getLogger().info("[SpeedRun][WorldHandler] Deleting game world, this could take some time...");
            Bukkit.unloadWorld(Objects.requireNonNull(plugin.getConfig().getString("GameWorld")), false);

            try {
                FileUtils.deleteDirectory(new File(plugin.getServer().getWorldContainer()+"/"+plugin.getConfig().getString("GameWorld")));
            } catch (IOException e) {
                Bukkit.getLogger().severe("[SpeedRun][WorldHandler] Unable to delete world (crashed).");
                throw new RuntimeException(e);
            }

            Bukkit.getLogger().info("[SpeedRun][WorldHandler] Game world deleted.");
        } else {
            Bukkit.getLogger().warning("[SpeedRun][WorldHandler] Attempted to delete non-existing world (aborted).");
        }
    }

    public boolean WorldExists(String world) {
        if (plugin.getServer().getWorld(world) == null) {
            return false;
        } else {
            return true;
        }
    }
}
