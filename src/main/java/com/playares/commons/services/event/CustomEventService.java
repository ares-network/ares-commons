package com.playares.commons.services.event;

import com.google.common.collect.Lists;
import com.playares.commons.AresPlugin;
import com.playares.commons.AresService;
import com.playares.commons.event.PlayerBigMoveEvent;
import com.playares.commons.event.PlayerDamagePlayerEvent;
import com.playares.commons.event.PlayerSplashPlayerEvent;
import com.playares.commons.event.ProcessedChatEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.List;

public final class CustomEventService implements Listener, AresService {
    @Getter public final String name = "Custom Event Service";
    @Getter public final AresPlugin owner;

    public CustomEventService(AresPlugin owner) {
        this.owner = owner;
    }

    @Override
    public void start() {
        owner.registerListener(this);
    }

    @Override
    public void stop() {
        AsyncPlayerChatEvent.getHandlerList().unregister(this);
        PlayerMoveEvent.getHandlerList().unregister(this);
        EntityDamageByEntityEvent.getHandlerList().unregister(this);
        PotionSplashEvent.getHandlerList().unregister(this);
    }

    /**
     * Listens for Bukkit Chat events and converts them in to Ares ProcessedChatEvent
     * @param event Bukkit AsyncPlayerChatEvent
     */
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        final ProcessedChatEvent aresEvent = new ProcessedChatEvent(event.getPlayer(), event.getMessage(), event.getRecipients());

        Bukkit.getPluginManager().callEvent(aresEvent);

        event.setCancelled(true);

        if (aresEvent.isCancelled()) {
            return;
        }

        aresEvent.getRecipients().forEach(viewer -> {
            if (viewer != null && viewer.isOnline()) {
                viewer.sendMessage(aresEvent.getDisplayName() + ": " + aresEvent.getMessage());
            }
        });
    }

    /**
     * Listens for Bukkit PlayerMoveEvent and converts it to an Ares PlayerBigMoveEvent if they move a full block
     *
     * The PlayerBigMoveEvent allows for significantly more efficient move checks and should always be used whenever possible
     *
     * @param event Bukkit PlayerMoveEvent
     */
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        final Location from = event.getFrom();
        final Location to = event.getTo();

        if (from.getBlockX() == to.getBlockX() && from.getBlockY() == to.getBlockY() && from.getBlockZ() == to.getBlockZ()) {
            return;
        }

        final PlayerBigMoveEvent customEvent = new PlayerBigMoveEvent(player, from, to);
        Bukkit.getPluginManager().callEvent(customEvent);

        if (customEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    /**
     * Listens for Bukkit EntityDamageByEntityEvent and converts it to an Ares PlayerDamagePlayerEvent if it meets the proper requirements
     * @param event Bukkit EntityDamageByEntityEvent
     */
    @EventHandler (priority = EventPriority.MONITOR)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        final Player damaged = (Player)event.getEntity();
        Player damager = null;
        PlayerDamagePlayerEvent.DamageType type = null;

        if (event.getDamager() instanceof Player) {
            damager = (Player)event.getDamager();
            type = PlayerDamagePlayerEvent.DamageType.PHYSICAL;
        }

        else if (event.getDamager() instanceof Projectile) {
            final Projectile projectile = (Projectile)event.getDamager();
            final ProjectileSource source = projectile.getShooter();

            if (!(source instanceof Player)) {
                return;
            }

            damager = (Player)source;
            type = PlayerDamagePlayerEvent.DamageType.PROJECTILE;
        }

        if (damager == null) {
            return;
        }

        final PlayerDamagePlayerEvent customEvent = new PlayerDamagePlayerEvent(damager, damaged, type);
        Bukkit.getPluginManager().callEvent(customEvent);

        if (customEvent.isCancelled()) {
            event.setCancelled(true);
        }
    }

    /**
     * Listens for Bukkit PotionSplashEvent and converts it to an Ares PlayerSplashPlayerEvent if it meets the requirements
     * @param event Bukkit PotionSplashEvent
     */
    @EventHandler
    public void onPotionSplash(PotionSplashEvent event) {
        final ThrownPotion potion = event.getPotion();

        if (!(potion.getShooter() instanceof Player)) {
            return;
        }

        if (event.getAffectedEntities().isEmpty()) {
            return;
        }

        final Player player = (Player)potion.getShooter();
        final List<LivingEntity> toRemove = Lists.newArrayList();

        for (LivingEntity entity : event.getAffectedEntities()) {
            if (!(entity instanceof Player)) {
                continue;
            }

            final Player affected = (Player)entity;
            final PlayerSplashPlayerEvent customEvent = new PlayerSplashPlayerEvent(player, affected, potion);

            Bukkit.getPluginManager().callEvent(customEvent);

            if (customEvent.isCancelled()) {
                toRemove.add(affected);
            }
        }

        event.getAffectedEntities().removeAll(toRemove);
    }
}
