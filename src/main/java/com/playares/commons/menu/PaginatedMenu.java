package com.playares.commons.menu;

import com.google.common.collect.Lists;
import com.playares.commons.item.ItemBuilder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Collection;
import java.util.List;

public abstract class PaginatedMenu<T> extends Menu {
    @Getter public final Collection<T> entries;
    @Getter @Setter public int page;

    /**
     * Create a new Menu instance
     *
     * @param plugin Bukkit Plugin
     * @param player Bukkit Player
     * @param title  Inventory Title
     * @param rows   Inventory Rows
     */
    public PaginatedMenu(Plugin plugin, Player player, String title, int rows, Collection<T> entries) {
        super(plugin, player, title, rows);
        this.entries = entries;
    }

    /**
     * Optional call to sort the collection before applying it in the inventory
     */
    public abstract List<T> sort();

    /**
     * Handles converting the provided entry to an ItemStack icon
     * @param t
     * @return
     */
    public abstract ClickableItem getItem(T t, int pos);

    @Override
    public void open() {
        super.open();
        update();
    }

    /**
     * Performs an update on the items
     */
    private void update() {
        clear();

        int cursor = 0;
        final int start = page * 52;
        final int end = start + 52;
        final boolean hasNextPage = entries.size() > end;
        final boolean hasPrevPage = start > 0;

        final List<T> entries = Lists.newArrayList(this.entries);
        sort();

        for (int i = start; i < end; i++) {
            if (cursor >= 52 || entries.size() <= i) {
                break;
            }

            final T entry = entries.get(i);
            addItem(getItem(entry, cursor));
            cursor += 1;
        }

        if (hasNextPage) {
            final ItemStack nextPageIcon = new ItemBuilder().setMaterial(Material.EMERALD_BLOCK).setName(ChatColor.GREEN + "Next Page").build();
            addItem(new ClickableItem(nextPageIcon, 53, click -> {
                setPage(page + 1);
                update();
            }));
        }

        if (hasPrevPage) {
            final ItemStack prevPageIcon = new ItemBuilder().setMaterial(Material.REDSTONE_BLOCK).setName(ChatColor.RED + "Previous Page").build();
            addItem(new ClickableItem(prevPageIcon, 52, click -> {
                setPage(page - 1);
                update();
            }));
        }

        player.updateInventory();
    }
}
