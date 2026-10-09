package com.kinds.optout;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public final class KindsOptimizerOptOutMod implements ModInitializer {
    private static final OptOutPayload ANCHOR = new OptOutPayload("kinds_anchor_optimizer", "opt_out");
    private static final OptOutPayload CRYSTAL_UNDERSCORE = new OptOutPayload("kinds_crystal_optimizer", "opt_out");
    private static final OptOutPayload CRYSTAL_COMPACT = new OptOutPayload("kindscrystaloptimizer", "opt_out");

    @Override
    public void onInitialize() {
        register(ANCHOR);
        register(CRYSTAL_UNDERSCORE);
        register(CRYSTAL_COMPACT);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> server.execute(() -> {
            ServerPlayerEntity player = handler.player;
            send(player, ANCHOR);
            send(player, CRYSTAL_UNDERSCORE);
            send(player, CRYSTAL_COMPACT);
        }));
    }

    private static void register(OptOutPayload payload) {
        PayloadTypeRegistry.playS2C().register(payload.id(), payload.codec());
    }

    private static void send(ServerPlayerEntity player, OptOutPayload payload) {
        try {
            ServerPlayNetworking.send(player, payload);
        } catch (RuntimeException ignored) {
        }
    }

    private record OptOutPayload(CustomPayload.Id<OptOutPayload> id) implements CustomPayload {
        private OptOutPayload(String namespace, String path) {
            this(new CustomPayload.Id<>(Identifier.of(namespace, path)));
        }

        private PacketCodec<RegistryByteBuf, OptOutPayload> codec() {
            return PacketCodec.of((value, buf) -> {
            }, buf -> this);
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return id;
        }
    }
}
