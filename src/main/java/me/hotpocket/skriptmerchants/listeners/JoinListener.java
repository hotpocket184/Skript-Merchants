package me.hotpocket.skriptmerchants.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JoinListener implements Listener {

    private static List<UUID> joined = new ArrayList<>();
    public static boolean updated = true;
    
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!updated) {
            if (event.getPlayer().isOp() && !joined.contains(event.getPlayer().getUniqueId())) {
                event.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&bskript-merchants&7] &cYou are running an &noutdated version&r &cof skript-merchants!"));
                joined.add(event.getPlayer().getUniqueId());
            }
        }
    }
}
