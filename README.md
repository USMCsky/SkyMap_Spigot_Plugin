# SkyMap (Spigot Plugin)

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://adoptium.net/)
[![Spigot API](https://img.shields.io/badge/Spigot%20API-1.21-blue.svg)](https://www.spigotmc.org/wiki/spigot-plugin-development/)
[![Last Commit](https://img.shields.io/github/last-commit/USMCsky/SkyMap_Spigot_Plugin)](https://github.com/USMCsky/SkyMap_Spigot_Plugin/commits/master)
[![Repo Size](https://img.shields.io/github/repo-size/USMCsky/SkyMap_Spigot_Plugin)](https://github.com/USMCsky/SkyMap_Spigot_Plugin)
[![Author](https://img.shields.io/badge/Author-USMCsky-00bcd4.svg)](https://github.com/USMCsky)

A lightweight Spigot plugin that provides a handheld map and simple compass navigation for players.
Great for survival and exploration servers that want straightforward navigation tools.

## Features
- Handheld map for player navigation
- Simple compass guidance
- Lightweight and easy to use
- No external dependencies (Spigot only)

## Requirements
- **Minecraft/Spigot API:** `1.21` (as defined in `plugin.yml`)
- **Java:** 21

### Commands (Players)
- `/skymap give`  
  Give yourself the SkyMap item.
- `/skymap hide`  
  Hide map display if in offhand.
- `/skymap offhand`  
  Toggle/use offhand behavior.

## Troubleshooting
- **"Only players can use this command."**
  - Run commands in-game as a player (not from the server console).

- **Command not working**
  - Currently supported commands are `/skymap give`, `/skymap hide`, and `/skymap offhand`.
