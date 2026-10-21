# Changelog

## 0.3.1-relics0.12-lukenightstar.1 (2026-10-21)

Unofficial port of Relics of Rain 0.3.1 by catboybinary to Relics 0.12.9, by LukeNightstar. Stats, costs, chances and radii are unchanged.

### Changed

- The mod requires Relics 0.12.9 or later, OctoLib (shatterbyte-lib) 0.6 or later and Curios 9.3.1+1.21.1 or later. It no longer loads with Relics 0.10.
- Relic data uses the Relics 0.12 template API. Relics have no ranks, so each ability keeps its old maximum level, its per-level stat increase and its stat cap.
- Relic description lang keys use the Relics 0.12 `relics.description.*` format. Relics 0.12 shows one line per experience source, so the old leveling source titles are removed. All other text is unchanged.
- The Reliquified Artifacts Mimic tags moved from the `rarcompat` namespace to `reliquified_artifacts`.
- Frost Relic, Platinum Horseshoe and Ukulele use new curio renderers with the same models and textures.
- Tooltip border colours and flawless shimmer colours carry over as Relics 0.12 relic styles.
- The Wax Quail relic screen lists its experience source (boosted jumps). The relic already gained experience this way.
- The relic screen does not show a documentation button, because no Relics documentation page exists for these relics.

### Known differences from 0.3.1

- Experience no longer spreads 25% to other equipped relics. Relics 0.12 removed this feature.
- You can no longer make a Broken Mirror by dropping a Magic Mirror from 12 or more blocks, because Relics 0.12 removed the Magic Mirror. The Broken Mirror item still works.
- A new relic rolls its starting stats in 11 even steps between the minimum and maximum, instead of any value in that range. Relics 0.12 made this change.
