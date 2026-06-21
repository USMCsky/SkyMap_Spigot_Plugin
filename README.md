# SkyMap (Spigot Plugin)

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://adoptium.net/)
[![Spigot API](https://img.shields.io/badge/Spigot%20API-1.21-blue.svg)](https://www.spigotmc.org/wiki/spigot-plugin-development/)
[![Last Commit](https://img.shields.io/github/last-commit/USMCsky/SkyMap_Spigot_Plugin)](https://github.com/USMCsky/SkyMap_Spigot_Plugin/commits/master)
[![Repo Size](https://img.shields.io/github/repo-size/USMCsky/SkyMap_Spigot_Plugin)](https://github.com/USMCsky/SkyMap_Spigot_Plugin)
[![Author](https://img.shields.io/badge/Author-USMCsky-00bcd4.svg)](https://github.com/USMCsky)

A simple Spigot plugin that adds an intuitive compass system to help players explore and navigate your world.
Perfect for survival and exploration servers where players want straightforward world navigation.

## Features
- Simple compass navigation for players
- Lightweight and easy to use
- Spigot-only setup with no external dependencies

## Requirements
- **Minecraft/Spigot API:** `1.21` (as defined in `plugin.yml`)
- **Java:** 21 (recommended based on modern Spigot 1.21 runtime expectations)

## Player Instructions
1. Get a compass item:
   ```text
   /skymap give
   ```
2. Hide compass display:
   ```text
   /skymap hide
   ```
3. Move compass to offhand behavior:
   ```text
   /skymap offhand
   ```

### Commands (Players)
- `/skymap give`  
  Give yourself the SkyMap compass.
- `/skymap hide`  
  Hide the compass display behavior.
- `/skymap offhand`  
  Toggle/use offhand compass behavior.

### Compass Navigation
- SkyMap provides simple directional guidance for everyday travel.
- Compass behavior is lightweight and designed for regular gameplay.
- Focused on basic navigation without extra systems.

## Permissions
- `skymap.use`  
  Allows usage of SkyMap commands.  
  **Default:** `true`

- `skymap.create`  
  Allows creation of new maps.  
  **Default:** `true`

- `skymap.admin`  
  Allows administrative map management.  
  **Default:** `op`

## Admin Notes
- Main command: `skymap`
- Alias: `map`
- Data is stored in `plugins/SkyMap/`.

## Troubleshooting
- **"Only players can use this command."**
  - Run commands in-game as a player (not from the server console).

- **Command not working**
  - Currently supported commands are `/skymap give`, `/skymap hide`, and `/skymap offhand`.

- **Compass not showing or updating**
  - Try `/skymap give` again.
  - Use `/skymap hide` and `/skymap offhand` to reset display behavior.
