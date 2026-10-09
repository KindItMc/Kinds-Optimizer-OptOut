# Kind's Optimizer Opt-Out

Server-side opt-out for Kind's client optimizer mods.

## Downloads

Paper, Spigot, Purpur:
`release/Kinds-Optimizer-OptOut-Paper-1.0.0.jar`

Fabric:
`release/Kinds-Optimizer-OptOut-Fabric-1.0.0.jar`

## Install

Paper, Spigot, Purpur:
Put the Paper jar in your server `plugins` folder.

Fabric:
Put the Fabric jar in your server `mods` folder.
Fabric API is required.

## What it does

When a player joins, the server sends opt-out messages for Kind's optimizer mods.

Supported now:
`kinds_anchor_optimizer:opt_out`

Reserved for crystal support:
`kinds_crystal_optimizer:opt_out`
`kindscrystaloptimizer:opt_out`

Kind's Anchor Optimizer already supports server opt-out.
Kind's Crystal Optimizer needs a client-side opt-out receiver before those crystal channels can disable it.
