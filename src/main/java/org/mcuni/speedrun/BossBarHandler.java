package org.mcuni.speedrun;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BossBarHandler {

    SpeedRun plugin;

    public BossBarHandler(SpeedRun plugin) {
        this.plugin = plugin;
        Bukkit.getLogger().info("[SpeedRun][BossBarHandler] Class started.");
    }

    public void CreateBossBar(String GoalItem) {
        GoalItem = GoalItem.replace('_' , ' ');

        final TextComponent BossBarText = Component.text("Goal item: ", NamedTextColor.GREEN)
                .append(
                        Component.text(String.valueOf(GoalItem), NamedTextColor.GREEN)
                                .decoration(TextDecoration.BOLD, true)
                );

        plugin.bossbar = BossBar.bossBar(BossBarText, 1f, BossBar.Color.GREEN, BossBar.Overlay.PROGRESS);

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
