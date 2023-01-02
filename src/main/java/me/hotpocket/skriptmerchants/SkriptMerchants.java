package me.hotpocket.skriptmerchants;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public final class SkriptMerchants extends JavaPlugin implements Listener {

    private static SkriptMerchants instance;
    private SkriptAddon addon;
    private static List<UUID> joined = new ArrayList<>();
    private static boolean updated = true;

    public static SkriptMerchants getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.addon = Skript.registerAddon(this);
        this.addon.setLanguageFileDirectory("lang");

        Bukkit.getPluginManager().registerEvents(this, this);

        try {
            this.addon.loadClasses("me.hotpocket.skriptmerchants", "elements");
        } catch (IOException e) {
            e.printStackTrace();
        }

        log("§7[§bskript-merchants§7] §fChecking for updates...");
        if (getDescription().getVersion().equals(getVersion())) {
            log("§7[§bskript-merchants§7] §aNo updates found!");
            updated = true;
        } else {
            log("§7[§bskript-merchants§7] §cYou are running an §noutdated version§r §cof skript-merchants!");
            updated = false;
        }
        log("§c[skript-merchants] Remember to change the resource ID!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        if (!updated) {
            if (event.getPlayer().isOp() && !joined.contains(event.getPlayer().getUniqueId())) {
                event.getPlayer().sendMessage("§7[§bskript-merchants§7] §cYou are running an §noutdated version§r §cof skript-merchants!");
                joined.add(event.getPlayer().getUniqueId());
            }
        }
    }

    private String getVersion() {
        try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=96702").openStream(); Scanner scanner = new Scanner(inputStream)) {
            if (scanner.hasNext()) {
                return scanner.next();
            }
        } catch (IOException exception) {
            this.getLogger().info("Unable to check for updates: " + exception.getMessage());
        }
        return "";
    }

    private void log(String text) {
        getServer().getConsoleSender().sendMessage(text);
    }
}
