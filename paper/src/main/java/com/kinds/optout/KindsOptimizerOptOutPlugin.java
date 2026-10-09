package com.kinds.optout;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class KindsOptimizerOptOutPlugin extends JavaPlugin implements Listener, PluginMessageListener {
    private static final Map<String, String> CHANNELS = Map.of(
            "kinds_anchor_optimizer:handshake", "kinds_anchor_optimizer:opt_out",
            "kinds_crystal_optimizer:handshake", "kinds_crystal_optimizer:opt_out",
            "kindscrystaloptimizer:handshake", "kindscrystaloptimizer:opt_out"
    );

    @Override
    public void onEnable() {
        saveDefaultConfig();
        for (String channel : CHANNELS.values()) {
            getServer().getMessenger().registerOutgoingPluginChannel(this, channel);
        }
        for (String channel : CHANNELS.keySet()) {
            getServer().getMessenger().registerIncomingPluginChannel(this, channel, this);
        }
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        String out = CHANNELS.get(channel);
        if (out == null || !player.isOnline()) {
            return;
        }

        player.sendPluginMessage(this, out, reason());
    }

    private byte[] reason() {
        return getConfig()
                .getString("disconnect-reason", "This server does not allow Kind's optimizer mods.")
                .getBytes(StandardCharsets.UTF_8);
    }
}
