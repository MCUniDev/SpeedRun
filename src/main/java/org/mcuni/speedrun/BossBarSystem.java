package org.mcuni.speedrun;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

public class BossBarSystem {

    protected SpeedRun plugin;
    protected int TaskID = -1;
    protected BossBar bar;

    public BossBarSystem(SpeedRun plugin) {
        this.plugin = plugin;
        Bukkit.getLogger().info("[SpeedRun] BossBarSystem class loaded.");
    }

    public void ShowItem() {
        Bukkit.getLogger().info("BossBar is visible.");
        bar = Bukkit.createBossBar("Find a " + plugin.GoalItem, BarColor.GREEN, BarStyle.SOLID);
        bar.setVisible(true);
    }

    public void HideItem() {
        Bukkit.getLogger().info("BossBar is Invisible.");
        bar.setVisible(false);
    }

    public BossBar GetBar() {
        return bar;
    }

    public void AddPlayer(Player player) {
        bar.addPlayer(player);
    }
}
