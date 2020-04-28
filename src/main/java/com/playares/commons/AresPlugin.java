package com.playares.commons;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.PaperCommandManager;
import com.comphenix.protocol.ProtocolManager;
import com.google.common.collect.Maps;
import com.playares.commons.connect.Connectable;
import com.playares.commons.logger.Logger;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

public abstract class AresPlugin extends JavaPlugin {
    @Getter public Map<Class<? extends AresService>, AresService> services;
    @Getter public Map<Class<? extends Connectable>, Connectable> databases;
    @Getter public PaperCommandManager commandManager;
    @Getter public ProtocolManager protocolManager;

    /**
     * Register a new Ares Service
     * @param service Ares Service
     */
    public void registerService(AresService service) {
        if (services == null) {
            services = Maps.newHashMap();
        }

        services.put(service.getClass(), service);
        Logger.print("Registered service: " + service.getName());
    }

    /**
     * Registers a new database instance
     * @param connectable Connectable Instance
     */
    public void registerDatabase(Connectable connectable) {
        if (databases == null) {
            databases = Maps.newHashMap();
        }

        databases.put(connectable.getClass(), connectable);
        Logger.print("Registered database: " + connectable.getConnectionType().name());
    }

    /**
     * Registers a new Bukkit Listener
     * @param listener Bukkit Listener
     */
    public void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, this);
    }

    /**
     * Registers a new Command Manager
     *
     * Currently only supports ACF
     * @param commandManager Paper Command Manager
     */
    public void registerCommandManager(PaperCommandManager commandManager) {
        if (commandManager != null) {
            this.commandManager = null;
        }

        this.commandManager = commandManager;
        Logger.print("Registered command manager: ACF");
    }

    /**
     * Register a ProtcolLib manager
     * @param manager ProtocolManager
     */
    public void registerProtocolLibrary(ProtocolManager manager) {
        this.protocolManager = manager;
    }

    /**
     * Register a new Base Command
     * @param command Base Command
     */
    public void registerCommand(BaseCommand command) {
        if (this.commandManager == null) {
            throw new NullPointerException("Command Manager was not registered");
        }

        this.commandManager.registerCommand(command);
        Logger.print("Registered command: " + command.getName());
    }

    /**
     * Handles starting all the registered services
     */
    protected void startServices() {
        services.values().forEach(service -> {
            service.start();
            Logger.print("Started service: " + service.getName());
        });
    }

    /**
     * Handles stopping all the registered services
     */
    protected void stopServices() {
        services.values().forEach(service -> {
            service.stop();
            Logger.warn("Stopped service: " + service.getName());
        });
    }

    /**
     * Handles reloading all the registered services
     */
    protected void reloadServices() {
        services.values().forEach(service -> {
            service.reload();
            Logger.print("Reloading service: " + service.getName());
        });
    }

    /**
     * Returns an Ares Service matching the provided class file
     * @param clazz Java Class
     * @return Ares Service
     */
    public AresService getService(Class<? extends AresService> clazz) {
        return services.getOrDefault(clazz, null);
    }

    /**
     * Returns a Connectable instance matching the provided class file
     * @param clazz Java Class
     * @return Connectable Instance
     */
    public Connectable getDatabaseInstance(Class<? extends Connectable> clazz) {
        return databases.getOrDefault(clazz, null);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        startServices();
    }

    @Override
    public void onDisable() {
        super.onDisable();
        stopServices();

        databases.values().forEach(Connectable::closeConnection);
    }
}