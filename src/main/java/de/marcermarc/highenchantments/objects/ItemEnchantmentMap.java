package de.marcermarc.highenchantments.objects;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ItemEnchantmentMap implements Map<Enchantment, Integer> {
    private final ItemStack item;

    public ItemEnchantmentMap(ItemStack item) {
        this.item = item;
    }

    @Override
    public Integer getOrDefault(Object o, Integer integer) {
        return item.getEnchantments().getOrDefault(o, integer);
    }

    @Override
    public void forEach(BiConsumer<? super Enchantment, ? super Integer> biConsumer) {
        item.getEnchantments().forEach(biConsumer);
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

        if (item.getEnchantments().containsKey(o) && item.getEnchantments().get(o).equals(o1)) {
            item.removeEnchantment((Enchantment) o);
            return true;
        }
        return false;
    }

    @Override
    public boolean replace(Enchantment enchantment, Integer oldValue, Integer newValue) {
        if (item.getEnchantments().containsKey(enchantment) && item.getEnchantmentLevel(enchantment) == oldValue) {
            item.removeEnchantment(enchantment);
            item.addUnsafeEnchantment(enchantment, newValue);
            return true;
        }
        return false;
    }

    @Override
    public Integer replace(Enchantment enchantment, Integer integer) {
        if (item.getEnchantments().containsKey(enchantment)) {
            int old = item.getEnchantmentLevel(enchantment);
            item.removeEnchantment(enchantment);
            item.addUnsafeEnchantment(enchantment, integer);
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
        return item.getEnchantments().size();
    }

    @Override
    public boolean isEmpty() {
        return item.getEnchantments().isEmpty();
    }

    @Override
    public boolean containsKey(Object o) {
        return item.getEnchantments().containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return item.getEnchantments().containsValue(o);
    }

    @Override
    public Integer get(Object o) {
        return item.getEnchantments().get(o);
    }

    @Override
    public Integer put(Enchantment enchantment, Integer integer) {
        if (item.getEnchantments().containsKey(enchantment)) {
            int old = item.getEnchantmentLevel(enchantment);
            item.removeEnchantment(enchantment);
            item.addUnsafeEnchantment(enchantment, integer);
            return old;
        }
        item.addUnsafeEnchantment(enchantment, integer);
        return null;
    }

    @Override
    public Integer remove(Object o) {
        if (!(o instanceof Enchantment)) {
            return null;
        }

        if (item.getEnchantments().containsKey(o)) {
            int old = item.getEnchantmentLevel((Enchantment) o);
            item.removeEnchantment((Enchantment) o);
            return old;
        }
        return null;
    }

    @Override
    public void putAll(Map<? extends Enchantment, ? extends Integer> map) {
        for (Map.Entry<? extends Enchantment, ? extends Integer> entry : map.entrySet()) {
            item.addUnsafeEnchantment(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        for (Map.Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
            item.removeEnchantment(entry.getKey());
        }
    }

    @Override
    public Set<Enchantment> keySet() {
        return item.getEnchantments().keySet();
    }

    @Override
    public Collection<Integer> values() {
        return item.getEnchantments().values();
    }

    @Override
    public Set<Entry<Enchantment, Integer>> entrySet() {
        return item.getEnchantments().entrySet();
    }
}
