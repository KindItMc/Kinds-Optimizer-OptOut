package com.kinds.optout;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
            registerIncoming(channel);
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
        String reason = getConfig().getString("disconnect-reason", "This server does not allow Kind's optimizer mods.");
        byte[] bytes = reason.getBytes(StandardCharsets.UTF_8);
        ByteArrayOutputStream out = new ByteArrayOutputStream(bytes.length + 3);
        writeVarInt(out, bytes.length);
        out.writeBytes(bytes);
        return out.toByteArray();
    }

    private void writeVarInt(ByteArrayOutputStream out, int value) {
        while ((value & -128) != 0) {
            out.write(value & 127 | 128);
            value >>>= 7;
        }
        out.write(value);
    }

    private void registerIncoming(String channel) {
        try {
            Messenger messenger = getServer().getMessenger();
            Method method = messenger.getClass().getMethod("registerIncomingPluginChannel", org.bukkit.plugin.Plugin.class, String.class, PluginMessageListener.class);
            method.invoke(messenger, this, channel, this);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
            throw new IllegalStateException("Could not register opt out channel " + channel, exception);
        }
    }
}
