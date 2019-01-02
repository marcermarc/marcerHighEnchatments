package de.marcermarc.highenchantments.objects;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ItemMetaStoredEnchantmentMap implements Map<Enchantment, Integer> {
    private final ItemStack item;
    private final EnchantmentStorageMeta itemmeta;

    public ItemMetaStoredEnchantmentMap(ItemStack item) {
        this.item = item;
        this.itemmeta = (EnchantmentStorageMeta) item.getItemMeta();
    }

    @Override
    public Integer getOrDefault(Object o, Integer integer) {
        return itemmeta.getStoredEnchants().getOrDefault(o, integer);
    }

    @Override
    public void forEach(BiConsumer<? super Enchantment, ? super Integer> biConsumer) {
        itemmeta.getStoredEnchants().forEach(biConsumer);
    }

    @Override
    public void replaceAll(BiFunction<? super Enchantment, ? super Integer, ? extends Integer> biFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer putIfAbsent(Enchantment enchantment, Integer integer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o, Object o1) {
        if (!(o instanceof Enchantment)) {
            return false;
        }

        if (itemmeta.hasEnchant((Enchantment) o) && itemmeta.getStoredEnchants().get(o).equals(o1)) {
            itemmeta.removeStoredEnchant((Enchantment) o);
            item.setItemMeta(itemmeta);
            return true;
        }
        return false;
    }

    @Override
    public boolean replace(Enchantment enchantment, Integer oldValue, Integer newValue) {
        if (itemmeta.hasEnchant(enchantment) && itemmeta.getStoredEnchantLevel(enchantment) == oldValue) {
            itemmeta.removeStoredEnchant(enchantment);
            itemmeta.addStoredEnchant(enchantment, newValue, true);
            item.setItemMeta(itemmeta);
            return true;
        }
        return false;
    }

    @Override
    public Integer replace(Enchantment enchantment, Integer integer) {
        if (itemmeta.hasEnchant(enchantment)) {
            int old = itemmeta.getStoredEnchantLevel(enchantment);
            itemmeta.removeStoredEnchant(enchantment);
            itemmeta.addStoredEnchant(enchantment, integer, true);
            item.setItemMeta(itemmeta);
            return old;
        }
        return null;
    }

    @Override
    public Integer computeIfAbsent(Enchantment enchantment, Function<? super Enchantment, ? extends Integer> function) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer computeIfPresent(Enchantment enchantment, BiFunction<? super Enchantment, ? super Integer, ? extends Integer> biFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer compute(Enchantment enchantment, BiFunction<? super Enchantment, ? super Integer, ? extends Integer> biFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Integer merge(Enchantment enchantment, Integer integer, BiFunction<? super Integer, ? super Integer, ? extends Integer> biFunction) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int size() {
        return itemmeta.getStoredEnchants().size();
    }

    @Override
    public boolean isEmpty() {
        return !itemmeta.hasEnchants();
    }

    @Override
    public boolean containsKey(Object o) {
        return itemmeta.getStoredEnchants().containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return itemmeta.getStoredEnchants().containsValue(o);
    }

    @Override
    public Integer get(Object o) {
        return itemmeta.getStoredEnchantLevel((Enchantment) o);
    }

    @Override
    public Integer put(Enchantment enchantment, Integer integer) {
        if (itemmeta.getStoredEnchants().containsKey(enchantment)) {
            int old = itemmeta.getStoredEnchantLevel(enchantment);
            itemmeta.removeStoredEnchant(enchantment);
            itemmeta.addStoredEnchant(enchantment, integer, true);
            item.setItemMeta(itemmeta);
            return old;
        }
        itemmeta.addStoredEnchant(enchantment, integer, true);
        item.setItemMeta(itemmeta);
        return null;
    }

    @Override
    public Integer remove(Object o) {
        if (!(o instanceof Enchantment)) {
            return null;
        }

        if (itemmeta.getStoredEnchants().containsKey(o)) {
            int old = itemmeta.getStoredEnchantLevel((Enchantment) o);
            itemmeta.removeStoredEnchant((Enchantment) o);
            item.setItemMeta(itemmeta);
            return old;
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends Enchantment, ? extends Integer> map) {
        for (Entry<? extends Enchantment, ? extends Integer> entry : map.entrySet()) {
            itemmeta.addStoredEnchant(entry.getKey(), entry.getValue(), true);
        }
        item.setItemMeta(itemmeta);
    }

    @Override
    public void clear() {
        for (Entry<Enchantment, Integer> entry : itemmeta.getStoredEnchants().entrySet()) {
            itemmeta.removeStoredEnchant(entry.getKey());
        }
        item.setItemMeta(itemmeta);
    }

    @Override
    public Set<Enchantment> keySet() {
        return itemmeta.getStoredEnchants().keySet();
    }

    @Override
    public Collection<Integer> values() {
        return itemmeta.getStoredEnchants().values();
    }

    @Override
    public Set<Entry<Enchantment, Integer>> entrySet() {
        return itemmeta.getStoredEnchants().entrySet();
    }
}
