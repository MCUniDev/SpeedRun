package org.mcuni.speedrun;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageHandler {

    CommandSender cs;

    public MessageHandler(CommandSender cs) {
        this.cs = cs;
    }
    public void PrivateMessage(String Message, boolean Error) {
        if (Error) {
            this.cs.sendMessage(ChatColor.DARK_RED + "[SpeedRun] " + ChatColor.RED + Message);
        } else {
            this.cs.sendMessage(ChatColor.GOLD + "[SpeedRun] " + ChatColor.YELLOW + Message);
        }
    }

    public void BroadcastMessage(String Message) {
        Bukkit.broadcastMessage(ChatColor.GOLD + "[SpeedRun] " + ChatColor.YELLOW + Message);
    }
}
