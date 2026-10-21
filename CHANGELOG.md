# Changelog

## 0.3.1-relics0.12-lukenightstar.1 (2026-10-21)

Unofficial port of Relics of Rain 0.3.1 by catboybinary to Relics 0.12.9, by LukeNightstar. Stat ranges, per-level increases, stat caps and leveling costs are unchanged. Starting stats roll differently; see Known differences.

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
- Some starting stats can roll fewer distinct values. Relics 0.12 rounds a rolled stat through its tooltip format, so stats shown as whole numbers collapse onto those numbers. Frost Relic max radius rolls only 6, 7 or 8, and Ukulele max targets only 2, 3 or 4. Ukulele max distance and proc chance, Bustling Fungus heal amount and Energy Drink sprint multiplier have 4 to 6 possible starting values instead of 11.
