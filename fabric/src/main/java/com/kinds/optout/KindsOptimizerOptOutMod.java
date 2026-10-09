package com.kinds.optout;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class KindsOptimizerOptOutMod implements ModInitializer {
    private static final OptOutPayload ANCHOR = new OptOutPayload("kinds_anchor_optimizer", "opt_out", "");
    private static final OptOutPayload CRYSTAL_UNDERSCORE = new OptOutPayload("kinds_crystal_optimizer", "opt_out", "");
    private static final OptOutPayload CRYSTAL_COMPACT = new OptOutPayload("kindscrystaloptimizer", "opt_out", "");
    private static final HandshakePayload ANCHOR_HANDSHAKE = new HandshakePayload("kinds_anchor_optimizer", "handshake", ANCHOR);
    private static final HandshakePayload CRYSTAL_UNDERSCORE_HANDSHAKE = new HandshakePayload("kinds_crystal_optimizer", "handshake", CRYSTAL_UNDERSCORE);
    private static final HandshakePayload CRYSTAL_COMPACT_HANDSHAKE = new HandshakePayload("kindscrystaloptimizer", "handshake", CRYSTAL_COMPACT);
    private static Config config;

    @Override
    public void onInitialize() {
        config = Config.load();
        register(ANCHOR);
        register(CRYSTAL_UNDERSCORE);
        register(CRYSTAL_COMPACT);
        receive(ANCHOR_HANDSHAKE);
        receive(CRYSTAL_UNDERSCORE_HANDSHAKE);
        receive(CRYSTAL_COMPACT_HANDSHAKE);
    }

    private static void register(OptOutPayload payload) {
        PayloadTypeRegistry.playS2C().register(payload.id(), payload.codec());
    }

    private static void receive(HandshakePayload payload) {
        PayloadTypeRegistry.playC2S().register(payload.id(), payload.codec());
        ServerPlayNetworking.registerGlobalReceiver(payload.id(), (received, context) -> {
            String reason = config.disconnectReason();
            context.server().execute(() -> ServerPlayNetworking.send(context.player(), payload.optOut().withReason(reason)));
        });
    }

    private record OptOutPayload(CustomPayload.Id<OptOutPayload> id, String reason) implements CustomPayload {
        private OptOutPayload(String namespace, String path, String reason) {
            this(new CustomPayload.Id<>(Identifier.of(namespace, path)), reason);
        }

        private OptOutPayload withReason(String reason) {
            return new OptOutPayload(id, reason);
        }

        private PacketCodec<RegistryByteBuf, OptOutPayload> codec() {
            return PacketCodec.of((value, buf) -> buf.writeString(value.reason), buf -> new OptOutPayload(id, buf.readString()));
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return id;
        }
    }

    private record HandshakePayload(CustomPayload.Id<HandshakePayload> id, OptOutPayload optOut) implements CustomPayload {
        private HandshakePayload(String namespace, String path, OptOutPayload optOut) {
            this(new CustomPayload.Id<>(Identifier.of(namespace, path)), optOut);
        }

        private PacketCodec<RegistryByteBuf, HandshakePayload> codec() {
            return PacketCodec.of((value, buf) -> {
            }, buf -> this);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return id;
        }
    }

    private record Config(String disconnectReason) {
        private static Config load() {
            Path path = FabricLoader.getInstance().getConfigDir().resolve("kinds_optimizer_optout.properties");
            Properties properties = new Properties();
            if (Files.exists(path)) {
                try (var reader = Files.newBufferedReader(path)) {
                    properties.load(reader);
                } catch (IOException ignored) {
                }
            }

            String reason = properties.getProperty("disconnect-reason", "This server does not allow Kind's optimizer mods.");
            properties.setProperty("disconnect-reason", reason);

            try {
                Files.createDirectories(path.getParent());
                try (var writer = Files.newBufferedWriter(path)) {
                    properties.store(writer, "Kind's Optimizer Opt Out");
                }
            } catch (IOException ignored) {
            }

            return new Config(reason);
        }
    }
}
