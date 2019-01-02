package de.marcermarc.highenchantments.listener;

import de.marcermarc.highenchantments.Util;
import de.marcermarc.highenchantments.controller.PluginController;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class Anvil implements Listener {

    private final PluginController controller;

    public Anvil(PluginController controller) {
        this.controller = controller;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getSlot() >= 0 && event.getInventory() instanceof AnvilInventory) {
            switch (event.getAction()) {
                case PLACE_ALL:
                case PLACE_ONE:
                case PLACE_SOME:
                case MOVE_TO_OTHER_INVENTORY:
                case SWAP_WITH_CURSOR:
                    // Only this actions are relevant

                    Bukkit.getScheduler().runTask(controller.getMain(), () -> onTrigger((Player) event.getWhoClicked(), (AnvilInventory) event.getInventory()));
            }
        }
    }

    /**
     * this method is triggered one tick after the Inventory click event
     *
     * @see Anvil#onInventoryClick(InventoryClickEvent)
     */
    private void onTrigger(Player player, AnvilInventory inventory) {

        ItemStack item0 = inventory.getItem(0);
        ItemStack item1 = inventory.getItem(1);
        ItemStack item2 = inventory.getItem(2);

        if (item0 == null || item1 == null || item2 == null) {
            return;
        }

        Map<Enchantment, Integer> mapItem1 = Util.getEnchantments(item1);

        if (mapItem1.size() > 0) {
            Map<Enchantment, Integer> mapItem0 = Util.getEnchantments(item0);
            Map<Enchantment, Integer> mapItem2 = Util.getEnchantments(item2);

            // Only the map from item 1 is iterated because minecraft automatic copies enchantments from item 0 to item 2 if it is not changed by item 1.
            for (Map.Entry<Enchantment, Integer> entry : mapItem1.entrySet()) {
                if (mapItem2.containsKey(entry.getKey())) { // Enchantments that aren't on the 2nd item aren't allowed on the 2nd item.
                    int lvl1 = entry.getValue();
                    int lvl2 = mapItem2.get(entry.getKey());

                    if (mapItem0.containsKey(entry.getKey())) { // If
                        int lvl0 = mapItem0.get(entry.getKey());

                        if (lvl0 > lvl1 && lvl2 != lvl0) {
                            mapItem2.put(entry.getKey(), lvl0);
                        } else if (lvl1 > lvl0 && lvl2 != lvl1) {
                            mapItem2.put(entry.getKey(), lvl1);
                        } else if (lvl0 == lvl1) {
                            if (lvl2 != lvl0 + 1 && lvl0 < controller.getConfig().getMaxLevels().get(entry.getKey())) {
                                mapItem2.put(entry.getKey(), lvl0 + 1);
                            } else if (lvl2 != lvl0) {
                                mapItem2.put(entry.getKey(), lvl0);
                            }
                        }
                    } else if (lvl1 != lvl2) {
                        mapItem2.put(entry.getKey(), entry.getValue());
                    }
                }
            }
        }
    }
}