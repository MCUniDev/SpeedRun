package org.mcuni.speedrun;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BossBarHandler {

    SpeedRun plugin;

    public BossBarHandler(SpeedRun plugin) {
        this.plugin = plugin;
        Bukkit.getLogger().info("[SpeedRun][BossBarHandler] Class started.");
    }

    public void CreateBossBar(String name) {
        plugin.bossbar = BossBar.bossBar(Component.text(name), 1f, BossBar.Color.GREEN, BossBar.Overlay.PROGRESS);

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.showBossBar(plugin.bossbar);
        }

        Bukkit.getLogger().info("[SpeedRun][BossBarHandler] BossBar created.");
    }

    public void DestroyBossBar() {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.hideBossBar(plugin.bossbar);
        }

        plugin.bossbar = null;

        Bukkit.getLogger().info("[SpeedRun][BossBarHandler] BossBar destroyed.");
    }
}
