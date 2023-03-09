package org.mcuni.speedrun.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mcuni.speedrun.MessageHandler;
import org.mcuni.speedrun.SpeedRun;

import java.util.Objects;

/**
     * Handles all /sr based commands.
     */
public class SpeedRunCommands implements CommandExecutor {
    public SpeedRun plugin;

    /**
     * Constructor for the KitCommands class.
     * @param plugin References to the main kit plugin class.
     */
    public SpeedRunCommands(SpeedRun plugin) {
        this.plugin = plugin;
        Bukkit.getLogger().info("[SpeedRun] SpeedRunCommands command handler loaded.");
    }

    /**
    * /kit command handler.
    * @param commandSender Information about who sent the command - player or console.
     * @param command Information about what command was sent.
     * @param s Command label - not used here.
     * @param args The command's arguments.
     * @return boolean true/false - was the command accepted and processed or not?
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        MessageHandler message = new MessageHandler(commandSender);
        Player player = (Player) commandSender;
        if (command.getName().equalsIgnoreCase("sr") || command.getName().equalsIgnoreCase("speedrun")) {
            if (args.length > 0) {
                if ("help".equals(args[0])) {
                    message.PrivateMessage("------------ SpeedRun Help ------------", false);
                    message.PrivateMessage("/speedrun status - Check the current status of the game.", false);
                    if (player.hasPermission("speedrun.admin")) {
                        message.PrivateMessage("/speedrun item - See / set the goal item for the game.", false);
                        message.PrivateMessage("/speedrun start - Start the game.", false);
                        message.PrivateMessage("/speedrun end - End the game.", false);
                    } else {
                        message.PrivateMessage("/speedrun item - See goal item for the game.", false);
                    }
                    message.PrivateMessage("------------ SpeedRun Help ------------", false);
                    return true;
                } else if ("item".equals(args[0])) {
                    if (args.length != 2) {
                        if (plugin.GoalItem == null) {
                            message.PrivateMessage("There is no item currently set. Use /speedrun item <item> to set an item.", true);
                        } else {
                            message.PrivateMessage("The current goal item is " + plugin.GoalItem, false);
                        }
                        return true;
                    }
                    if (plugin.GameRunning) {
                        message.PrivateMessage("You can't change the item whilst the game is running.", true);
                        return true;
                    }
                    if (player.hasPermission("speedrun.admin")) {
                        String materialName = args[1].toUpperCase();
                        try
                        {
                            plugin.GoalItem = Material.valueOf(materialName );
                            message.PrivateMessage("Item has been set to " + plugin.GoalItem, false);
                        }
                        catch (Exception e)
                        {
                            message.PrivateMessage("That's not a valid Minecraft block or item.", true);
                            return true;
                        }
                        return true;
                    } else {
                        message.PrivateMessage("You don't have the required permissions to use this command.", true);
                        return true;
                    }
                } else if ("start".equals(args[0])) {
                    if (player.hasPermission("speedrun.admin")) {
                        if (plugin.GoalItem == null) {
                            message.PrivateMessage("You must set a goal item to start. Use /speedrun item <item> to set the goal item.", true);
                            return true;
                        }
                        message.BroadcastMessage("SpeedRun is about to start. This is a global game and all players will automatically join the game.");
                        message.BroadcastMessage("If you do not wish to join, please leave the server now.");
                        message.PrivateMessage("Please wait 5 seconds, then send /speedrun startconfirm.", true);
                        return true;
                    } else {
                        message.PrivateMessage("You don't have the required permissions to use this command.", true);
                        return true;
                    }
                } else if ("end".equals(args[0])) {
                    if (player.hasPermission("speedrun.admin")) {
                        if (plugin.GameRunning) {
                            plugin.EndGame();
                            message.BroadcastMessage("\n\n");
                            message.BroadcastMessage("Nobody won this SpeedRun game, better luck next time!");
                            return true;
                        } else {
                            message.PrivateMessage("There are no games currently running.", true);
                            return true;
                        }
                    } else {
                        message.PrivateMessage("You don't have the required permissions to use this command.", true);
                        return true;
                    }
                } else if ("startconfirm".equals(args[0])) {
                    if (player.hasPermission("speedrun.admin")) {
                        if (plugin.GoalItem == null) {
                            message.PrivateMessage("You must set a goal item to start. Use /speedrun item <item> to set the goal item.", true);
                            return true;
                        }
                        plugin.StartGame();
                    } else {
                        message.PrivateMessage("You don't have the required permissions to use this command.", true);
                        return true;
                    }
                } else if ("status".equals(args[0])) {
                    message.BroadcastMessage("------ Game Status ------");
                    if (plugin.GameRunning) {
                        message.BroadcastMessage("PROGRESS: In progress");
                    } else {
                        message.BroadcastMessage("PROGRESS: Waiting");
                    }
                    if (plugin.GoalItem == null) {
                        message.BroadcastMessage("GOAL: Not set");
                    } else {
                        message.BroadcastMessage("GOAL: " + plugin.GoalItem);
                    }
                    message.BroadcastMessage("------ Game Status ------");
                } else {
                    message.PrivateMessage("Unknown command. Use /speedrun help for help.", true);
                    return true;
                }
            } else {
                commandSender.sendMessage(ChatColor.GOLD + "[SpeedRun] " + ChatColor.YELLOW + "Running SpeedRun "+plugin.getDescription().getVersion()+" for Minecraft "+plugin.getDescription().getAPIVersion());
                commandSender.sendMessage(ChatColor.GOLD + "[SpeedRun] " + ChatColor.YELLOW + "Created by MCUni and contributors.");
                commandSender.sendMessage(ChatColor.GOLD + "[SpeedRun] " + ChatColor.WHITE + "Use /speedrun help for more commands");
                return true;
            }
        }
        return true;
    }
}
