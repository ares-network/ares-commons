package com.llewkcor.ares.commons.util.bukkit;

import org.bukkit.Material;

public final class Blocks {
    /**
     * Returns true if the provided Bukkit Material can be interacted with
     * @param material Bukkit Material
     * @return True if interactable
     */
    public static boolean isInteractable(Material material) {
        return
                material.name().endsWith("_CHEST") || material.equals(Material.WORKBENCH) || material.equals(Material.FURNACE) ||
                        material.equals(Material.ENCHANTMENT_TABLE) || material.name().endsWith("ANVIL") || material.equals(Material.ITEM_FRAME) ||
                        material.equals(Material.BED) || material.equals(Material.BED_BLOCK) || material.equals(Material.LEVER) || material.name().endsWith("_PLATE") ||
                        material.name().endsWith("_BUTTON") || material.name().endsWith("_TRAPDOOR") || material.name().endsWith("_FENCE_GATE") ||
                        material.equals(Material.DAYLIGHT_DETECTOR) || material.equals(Material.HOPPER) || material.equals(Material.DROPPER) || material.name().endsWith("_DOOR") || material.equals(Material.DIODE) ||
                        material.equals(Material.REDSTONE_COMPARATOR) || material.equals(Material.REDSTONE_COMPARATOR_OFF) || material.equals(Material.REDSTONE_COMPARATOR_ON) ||
                        material.equals(Material.BEACON) || material.equals(Material.CAULDRON) || material.equals(Material.FLOWER_POT);
    }
}
