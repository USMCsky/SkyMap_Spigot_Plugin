# SkyMap (Spigot Plugin)

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://adoptium.net/)
[![Spigot API](https://img.shields.io/badge/Spigot%20API-1.21-blue.svg)](https://www.spigotmc.org/wiki/spigot-plugin-development/)
[![Last Commit](https://img.shields.io/github/last-commit/USMCsky/SkyMap_Spigot_Plugin)](https://github.com/USMCsky/SkyMap_Spigot_Plugin/commits/master)
[![Repo Size](https://img.shields.io/github/repo-size/USMCsky/SkyMap_Spigot_Plugin)](https://github.com/USMCsky/SkyMap_Spigot_Plugin)
[![Author](https://img.shields.io/badge/Author-USMCsky-00bcd4.svg)](https://github.com/USMCsky)

A Spigot plugin that brings interactive mapping and navigation tools to your server through **SkyMap** — an intuitive map and compass system that helps players explore and navigate your world.
Perfect for survival and exploration servers where players want seamless world navigation and location tracking.

## Features
- Create and manage **custom maps** of your server world
- **Real-time compass navigation** to guide players to key locations
- **Waypoint system** for marking important locations
- **Map sharing** between players
- **Location tracking** and distance calculations
- **Customizable map markers** and labels
- **Server-wide map synchronization**

## Requirements
- **Minecraft/Spigot API:** `1.21` (as defined in `plugin.yml`)
- **Java:** 21 (recommended based on modern Spigot 1.21 runtime expectations)

## Player Instructions
1. Create your personal map:
   ```text
   /skymap create
   ```
2. Add waypoints and markers to your map:
   ```text
   /skymap marker add <name>
   ```
3. Use the compass to navigate:
   ```text
   /skymap compass <waypoint>
   ```
4. Share your map with other players:
   ```text
   /skymap share <player>
   ```
5. View map information:
   ```text
   /skymap info
   ```

### Commands (Players)
- `/skymap create`  
  Create a new personal map for your account.
- `/skymap marker add <name>`  
  Add a waypoint marker at your current location.
- `/skymap marker remove <name>`  
  Remove a waypoint marker from your map.
- `/skymap compass <waypoint>`  
  Activate compass navigation to a specific waypoint.
- `/skymap share <player>`  
  Share your map with another player.
- `/skymap list`  
  Display all your waypoints and markers.
- `/skymap info`  
  Show detailed information about your current map.

### Map Navigation
- Maps provide real-time location data for seamless navigation.
- **Waypoints** are persistent and stored per-player, surviving server restarts.
- **Compass guidance** updates as you move, keeping you oriented toward your destination.
- **Distance calculations** help you plan routes and explorations.

### Map Sharing
- Share maps with trusted players to collaborate on exploration and navigation.
- Shared maps display read-only waypoints and markers from other players.
- Control which waypoints are visible when sharing your map.

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
- Data is stored in `plugins/SkyMap/` including player maps, waypoints, and shared map data per player UUID.

## Troubleshooting
- **"Only players can use this command."**
  - Run commands in-game as a player (not from the server console).

- **Waypoint not showing on map**
  - Make sure the waypoint was successfully added with `/skymap marker add`.
  - Try `/skymap list` to confirm the waypoint exists.

- **Compass not pointing to waypoint**
  - Ensure the waypoint name is spelled correctly.
  - Try deactivating and reactivating the compass with `/skymap compass <waypoint>`.

- **Can't share map with player**
  - Verify the player name is spelled correctly.
  - Both players must be online to establish map sharing connections.

- **Map data not persisting**
  - Check that `plugins/SkyMap/` directory exists and has proper permissions.
  - Try rejoining the server; if the issue persists, check server logs for plugin errors.

- **Compass performance issues**
  - Having too many active waypoints may affect performance. Try reducing the number of active maps or waypoints.
