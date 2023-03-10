package org.mcuni.speedrun.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.mcuni.speedrun.GameSystem;
import org.mcuni.speedrun.MessageHandler;
import org.mcuni.speedrun.SpeedRun;

/**
 * This class handles determining when someone has won the game.
 */
public class InventoryClick implements Listener {
    public SpeedRun plugin;

    /**
     * Constructor for the EntityPickupItem event class.
     * @param plugin References to the main SpeedRun plugin class.
     */
    public InventoryClick(SpeedRun plugin) {
        this.plugin = plugin;
        Bukkit.getLogger().info("[SpeedRun] InventoryClickEvent event handler started.");
    }

    /**
     * Detects when an entity picks up an item.
     * @param event Information about the event.
     */
    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        if (plugin.GameRunning) {
            try {
                Player player = (Player) event.getWhoClicked();

                ItemStack Item = event.getCurrentItem();
                if (Item == null) {
                    return;
                }

                if (Item.getType() == plugin.GoalItem) {
                    Bukkit.getLogger().severe("[SpeedRun][InventoryClickEvent] Correct item matched.");
                    MessageHandler message = new MessageHandler(null);
                    message.BroadcastMessage(player.getName() + " has WON this SpeedRun game!\n\n");
                    GameSystem Game = new GameSystem(plugin);
                    Game.End();
                    message.BroadcastMessage("\n\n");
                    message.BroadcastMessage(player.getName() + " has WON this SpeedRun game!");
                }
            } catch (Exception e) {
                Bukkit.getLogger().severe("[SpeedRun][InventoryClickEvent] Error.");
                e.printStackTrace();
            }
        }
    }
}