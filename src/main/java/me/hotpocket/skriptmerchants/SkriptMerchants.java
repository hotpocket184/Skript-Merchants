package me.hotpocket.skriptmerchants;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.hotpocket.skriptmerchants.listeners.JoinListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public final class SkriptMerchants extends JavaPlugin {

    private static SkriptMerchants instance;
    private SkriptAddon addon;

    public static SkriptMerchants getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        this.addon = Skript.registerAddon(this);
        this.addon.setLanguageFileDirectory("lang");

        Bukkit.getPluginManager().registerEvents(new JoinListener(), this);

        try {
            this.addon.loadClasses("me.hotpocket.skriptmerchants", "elements");
        } catch (IOException e) {
            e.printStackTrace();
        }

        log("&7[&bskript-merchants&7] &fChecking for updates...");
        if (getDescription().getVersion().equals(getVersion())) {
            log("&7[&bskript-merchants&7] &aNo updates found!");
            JoinListener.updated = true;
        } else {
            log("&7[&bskript-merchants&7] &cYou are running an &noutdated version&r &cof skript-merchants!");
            JoinListener.updated = false;
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private String getVersion() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://api.github.com/repos/hotpocket184/Skript-Merchants/releases/latest").openStream()));
            return new Gson().fromJson(reader, JsonObject.class).get("tag_name").getAsString();
        } catch (IOException exception) {
            log("&7[&bskript-merchants&7] &cUnable to check for updates!");
        }
        return "";
    }

    private void log(String text) {
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', text));
    }
}
