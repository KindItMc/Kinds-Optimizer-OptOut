# Kind's Optimizer Opt Out

Server opt out for Kind's client optimizer mods.

## Downloads

Paper, Spigot, Purpur:
`release/Kinds-Optimizer-OptOut-Paper-1.0.2.jar`

Fabric:
Build from the Fabric folder with Gradle.

## Install

Paper, Spigot, Purpur:
Put the Paper jar in your server `plugins` folder.
Works on 1.21 through 1.26.2 Bukkit based servers.

Fabric:
Put the Fabric jar in your server `mods` folder.
Fabric API is required.

## What it does

When a Kind's optimizer mod sends its handshake, the server sends back an opt out message.
The client mod disconnects itself with the server reason.

Supported now:
`kinds_anchor_optimizer:handshake`
`kinds_anchor_optimizer:opt_out`

Reserved for crystal support:
`kinds_crystal_optimizer:handshake`
`kinds_crystal_optimizer:opt_out`
`kindscrystaloptimizer:handshake`
`kindscrystaloptimizer:opt_out`

Use Kind's Anchor Optimizer 0.3.0 or newer.
Use Kind's Crystal Optimizer 1.6.3 or newer.

## Config

Paper:
`plugins/KindsOptimizerOptOut/config.yml`

Fabric:
`config/kinds_optimizer_optout.properties`

Set the disconnect reason there.
