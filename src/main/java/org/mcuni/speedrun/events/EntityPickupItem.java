package org.mcuni.speedrun.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.mcuni.speedrun.GameSystem;
import org.mcuni.speedrun.MessageHandler;
import org.mcuni.speedrun.SpeedRun;

/**
 * This class handles determining when someone has won the game.
 */
public class EntityPickupItem implements Listener {
    public SpeedRun plugin;

    /**
     * Constructor for the EntityPickupItem event class.
     * @param plugin References to the main SpeedRun plugin class.
     */
    public EntityPickupItem(SpeedRun plugin) {
        this.plugin = plugin;
        Bukkit.getLogger().info("[SpeedRun] EntityPickupItem event handler started.");
    }

    /**
     * Detects when an entity picks up an item.
     * @param event Information about the event.
     */
    @EventHandler
    public void entityPickupItem(EntityPickupItemEvent event) {
        if (plugin.GameRunning) {
            try {
                Entity entity = event.getEntity();
                if (entity instanceof Player) {

                    Player player = (Player) entity;

                    ItemStack itemStack = event.getItem().getItemStack();

                    if (itemStack == null) return;

                    if (itemStack.getType() == plugin.GoalItem) {
                        Bukkit.getLogger().info("[SpeedRun][EntityPickupItem] Correct item matched.");
                        MessageHandler message = new MessageHandler(null);
                        message.BroadcastMessage(player.getName() + " has WON this SpeedRun game!\n\n");
                        GameSystem Game = new GameSystem(plugin);
                        Game.End();
                        message.BroadcastMessage("\n\n");
                        message.BroadcastMessage(player.getName() + " has WON this SpeedRun game!");
                    }
                }
            } catch (Exception e) {
                Bukkit.getLogger().severe("[SpeedRun][EntityPickupItem] Error.");
                e.printStackTrace();
            }
        }
    }
}