package com.usmcsky;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class SkyMapPlugin extends JavaPlugin implements Listener {

    private static final long REFRESH_INTERVAL_TICKS = 10L;
    private static final int RECENTER_DISTANCE_BLOCKS = 32;
    private static final String MINIMAP_NAME = "SkyMap Minimap";
    private static final String[] COMPASS_DIRECTIONS = {"S", "SW", "W", "NW", "N", "NE", "E", "SE"};

    private final Map<UUID, MapView> playerMinimaps = new HashMap<>();

    private NamespacedKey minimapKey;
    private NamespacedKey hiddenKey;
    private BukkitTask refreshTask;

    @Override
    public void onEnable() {
        minimapKey = new NamespacedKey(this, "minimap");
        hiddenKey = new NamespacedKey(this, "minimap-hidden");
        Bukkit.getPluginManager().registerEvents(this, this);

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!isMinimapHidden(player)) {
                ensureMinimap(player);
            }
        }

        refreshTask = Bukkit.getScheduler().runTaskTimer(this, this::refreshMinimaps, 1L, REFRESH_INTERVAL_TICKS);
    }

    @Override
    public void onDisable() {
        if (refreshTask != null) {
            refreshTask.cancel();
            refreshTask = null;
        }

        playerMinimaps.clear();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!isMinimapHidden(event.getPlayer())) {
            ensureMinimap(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Bukkit.getScheduler().runTask(this, () -> {
            if (!isMinimapHidden(event.getPlayer())) {
                ensureMinimap(event.getPlayer());
            }
        });
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerMinimaps.remove(event.getPlayer().getUniqueId());
    }

    private void refreshMinimaps() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (isMinimapHidden(player)) {
                continue;
            }

            MapView minimap = ensureMinimap(player);
            updateMinimap(minimap, player);
            updateCompass(player);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("skymap")) {
            return false;
        }

        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        if (args.length != 1) {
            player.sendMessage("Usage: /skymap <give|hide|offhand>");
            return true;
        }

        String subcommand = args[0].toLowerCase();
        if (subcommand.equals("give")) {
            setMinimapHidden(player, false);
            ensureMinimap(player);
            player.sendMessage("SkyMap is now available in your inventory.");
            return true;
        }

        if (subcommand.equals("hide")) {
            int removed = removeMinimapItems(player);
            playerMinimaps.remove(player.getUniqueId());
            setMinimapHidden(player, true);
            sendActionBar(player, "");
            if (removed == 0) {
                player.sendMessage("SkyMap is hidden.");
            } else {
                player.sendMessage("SkyMap is hidden and removed from your inventory.");
            }

            return true;
        }

        if (subcommand.equals("offhand")) {
            setMinimapHidden(player, false);
            moveMinimapToOffhand(player);
            player.sendMessage("SkyMap is now in your offhand.");
            return true;
        }

        player.sendMessage("Usage: /skymap <give|hide|offhand>");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!command.getName().equalsIgnoreCase("skymap")) {
            return List.of();
        }

        if (args.length == 1) {
            return List.of("give", "hide", "offhand").stream()
                    .filter(option -> option.startsWith(args[0].toLowerCase()))
                    .toList();
        }

        return List.of();
    }

    private MapView ensureMinimap(Player player) {
        UUID playerId = player.getUniqueId();
        MapView cached = playerMinimaps.get(playerId);
        if (cached != null) {
            return cached;
        }

        ItemStack existingItem = findExistingMinimap(player.getInventory());
        if (existingItem != null) {
            MapView existingView = getMapView(existingItem);
            if (existingView != null) {
                playerMinimaps.put(playerId, existingView);
                updateMinimap(existingView, player);
                return existingView;
            }
        }

        MapView createdView = Bukkit.createMap(player.getWorld());
        updateMinimap(createdView, player);

        ItemStack minimapItem = createMinimapItem(createdView);
        Map<Integer, ItemStack> leftovers = player.getInventory().addItem(minimapItem);
        if (!leftovers.isEmpty()) {
            leftovers.values().forEach(item -> player.getWorld().dropItemNaturally(player.getLocation(), item));
        }

        playerMinimaps.put(playerId, createdView);
        return createdView;
    }

    private ItemStack findExistingMinimap(PlayerInventory inventory) {
        for (ItemStack item : inventory.getContents()) {
            if (!isMinimapItem(item)) {
                continue;
            }

            return item;
        }

        ItemStack offhandItem = inventory.getItemInOffHand();
        if (isMinimapItem(offhandItem)) {
            return offhandItem;
        }

        return null;
    }

    private int findExistingMinimapSlot(PlayerInventory inventory) {
        ItemStack[] storageContents = inventory.getStorageContents();
        for (int slot = 0; slot < storageContents.length; slot++) {
            if (isMinimapItem(storageContents[slot])) {
                return slot;
            }
        }

        return -1;
    }

    private boolean isMinimapItem(ItemStack item) {
        if (item == null || item.getType() != Material.FILLED_MAP) {
            return false;
        }

        if (!(item.getItemMeta() instanceof MapMeta mapMeta)) {
            return false;
        }

        Byte marker = mapMeta.getPersistentDataContainer().get(minimapKey, PersistentDataType.BYTE);
        return marker != null && marker == (byte) 1;
    }

    private MapView getMapView(ItemStack item) {
        if (!(item.getItemMeta() instanceof MapMeta mapMeta)) {
            return null;
        }

        return mapMeta.getMapView();
    }

    private ItemStack createMinimapItem(MapView mapView) {
        ItemStack item = new ItemStack(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) item.getItemMeta();
        if (mapMeta == null) {
            throw new IllegalStateException("Filled map item did not provide map metadata.");
        }

        mapMeta.setDisplayName(MINIMAP_NAME);
        mapMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        mapMeta.setMapView(mapView);
        mapMeta.getPersistentDataContainer().set(minimapKey, PersistentDataType.BYTE, (byte) 1);
        item.setItemMeta(mapMeta);
        return item;
    }

    private boolean isMinimapHidden(Player player) {
        Byte hidden = player.getPersistentDataContainer().get(hiddenKey, PersistentDataType.BYTE);
        return hidden != null && hidden == (byte) 1;
    }

    private void setMinimapHidden(Player player, boolean hidden) {
        if (hidden) {
            player.getPersistentDataContainer().set(hiddenKey, PersistentDataType.BYTE, (byte) 1);
            return;
        }

        player.getPersistentDataContainer().remove(hiddenKey);
    }

    private int removeMinimapItems(Player player) {
        PlayerInventory inventory = player.getInventory();
        int removed = 0;

        ItemStack[] storageContents = inventory.getStorageContents();
        for (int slot = 0; slot < storageContents.length; slot++) {
            if (!isMinimapItem(storageContents[slot])) {
                continue;
            }

            inventory.setItem(slot, null);
            removed++;
        }

        if (isMinimapItem(inventory.getItemInOffHand())) {
            inventory.setItemInOffHand(null);
            removed++;
        }

        return removed;
    }

    private void moveMinimapToOffhand(Player player) {
        PlayerInventory inventory = player.getInventory();
        if (isMinimapItem(inventory.getItemInOffHand())) {
            return;
        }

        MapView minimap = ensureMinimap(player);
        ItemStack minimapItem = findExistingMinimap(inventory);
        if (minimapItem == null) {
            minimapItem = createMinimapItem(minimap);
        }

        int minimapSlot = findExistingMinimapSlot(inventory);
        if (minimapSlot >= 0) {
            inventory.setItem(minimapSlot, null);
        }

        ItemStack offhandItem = inventory.getItemInOffHand();
        if (offhandItem != null && offhandItem.getType() != Material.AIR) {
            Map<Integer, ItemStack> leftovers = inventory.addItem(offhandItem);
            if (!leftovers.isEmpty()) {
                leftovers.values().forEach(item -> player.getWorld().dropItemNaturally(player.getLocation(), item));
            }
        }

        inventory.setItemInOffHand(minimapItem);
    }

    private void updateMinimap(MapView mapView, Player player) {
        Location location = player.getLocation();

        mapView.setWorld(player.getWorld());
        mapView.setScale(MapView.Scale.CLOSE);
        mapView.setTrackingPosition(true);
        mapView.setUnlimitedTracking(false);
        mapView.setLocked(false);

        if (shouldRecenter(mapView, location)) {
            mapView.setCenterX(location.getBlockX());
            mapView.setCenterZ(location.getBlockZ());
        }
    }

    private boolean shouldRecenter(MapView mapView, Location location) {
        if (mapView.getWorld() == null || !mapView.getWorld().getUID().equals(location.getWorld().getUID())) {
            return true;
        }

        return Math.abs(mapView.getCenterX() - location.getBlockX()) >= RECENTER_DISTANCE_BLOCKS
                || Math.abs(mapView.getCenterZ() - location.getBlockZ()) >= RECENTER_DISTANCE_BLOCKS;
    }

    private void updateCompass(Player player) {
        String direction = getCompassDirection(player.getLocation().getYaw());
        sendActionBar(player, "Compass: " + direction);
    }

    private void sendActionBar(Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    private String getCompassDirection(float yaw) {
        float normalizedYaw = yaw % 360;
        if (normalizedYaw < 0) {
            normalizedYaw += 360;
        }

        int index = Math.round(normalizedYaw / 45f) % COMPASS_DIRECTIONS.length;
        return COMPASS_DIRECTIONS[index];
    }
}
