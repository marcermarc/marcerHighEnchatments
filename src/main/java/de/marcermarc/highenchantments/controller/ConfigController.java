package de.marcermarc.highenchantments.controller;

import de.marcermarc.highenchantments.Util;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;

public class ConfigController {

    private final PluginController controller;
    private final HashMap<Enchantment, Integer> maxLevels;

    public ConfigController(PluginController controller) {
        this.controller = controller;

        this.maxLevels = new HashMap<>();

        loadConfig();
    }

    private void loadEnchantments() {
        for (Enchantment enchantment : Enchantment.values()) {
            int level = controller.getMain().getConfig().getInt(Util.keyedToString(enchantment));

            if (level < enchantment.getMaxLevel())
                level = enchantment.getMaxLevel();

            maxLevels.put(enchantment, level);
        }
    }

    private void saveEnchantments() {
        for (Enchantment enchantment : Enchantment.values()) {
            controller.getMain().getConfig().set(Util.keyedToString(enchantment), maxLevels.get(enchantment));
        }
    }

    public boolean loadConfig() {
        controller.getMain().reloadConfig();

        loadEnchantments();

        // overrides invalid values with valid values
        return saveConfig();
    }

    public boolean saveConfig() {
        saveEnchantments();

        controller.getMain().saveConfig();

        return true;
    }

    //region getters and setters
    public HashMap<Enchantment, Integer> getMaxLevels() {
        return maxLevels;
    }
    //endregion
}
