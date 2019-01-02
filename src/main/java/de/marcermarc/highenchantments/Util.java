package de.marcermarc.highenchantments;


import de.marcermarc.highenchantments.objects.ItemEnchantmentMap;
import de.marcermarc.highenchantments.objects.ItemMetaStoredEnchantmentMap;
import org.bukkit.Keyed;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Util {
    /**
     * Damit keine Instanz erstellt wird
     */
    private Util() {
    }

    public static String keyedToString(Keyed keyed) {
        return keyed.getKey().toString();
    }

    public static Material stringToMaterial(String material) {
        return Material.matchMaterial(material);
    }

    public static Enchantment stringToEntchantment(String enchantment) {
        return Arrays.stream(Enchantment.values()).filter(x -> x.getKey().toString().equals(enchantment)).findFirst().orElse(null);
    }

    public static List<String> tabCompleteFilter(List<String> full, String startetText) {
        return full.stream().filter(s -> startetText.isEmpty() || s.contains(startetText)).collect(Collectors.toList());
    }

    public static Map<Enchantment, Integer> getEnchantments(ItemStack item) {
        if (item.getType().getMaxDurability() > 0 && item.getType().getMaxStackSize() == 1) {
            return new ItemEnchantmentMap(item);
        } else if (item.getItemMeta() instanceof EnchantmentStorageMeta) {
            return new ItemMetaStoredEnchantmentMap(item);
        } else {
            return Collections.EMPTY_MAP;
        }
    }
}
