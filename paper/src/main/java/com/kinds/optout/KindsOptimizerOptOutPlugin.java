package com.kinds.optout;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class KindsOptimizerOptOutPlugin extends JavaPlugin implements Listener {
    private static final byte[] EMPTY = new byte[0];
    private static final List<String> CHANNELS = List.of(
            "kinds_anchor_optimizer:opt_out",
            "kinds_crystal_optimizer:opt_out",
            "kindscrystaloptimizer:opt_out"
    );

    @Override
    public void onEnable() {
        for (String channel : CHANNELS) {
            getServer().getMessenger().registerOutgoingPluginChannel(this, channel);
        }
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        sendOptOut(player);
        Bukkit.getScheduler().runTaskLater(this, () -> sendOptOut(player), 20L);
        Bukkit.getScheduler().runTaskLater(this, () -> sendOptOut(player), 60L);
    }

    private void sendOptOut(Player player) {
        if (!player.isOnline()) {
            return;
        }

        for (String channel : CHANNELS) {
            player.sendPluginMessage(this, channel, EMPTY);
        }
    }
}
