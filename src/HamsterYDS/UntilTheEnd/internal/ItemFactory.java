/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/08 18:22:41
 *
 * UntilTheEnd/UntilTheEnd/ItemFactory.java
 */

package HamsterYDS.UntilTheEnd.internal;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.UnsafeValues;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

@SuppressWarnings({"unchecked", "JavaLangInvokeHandleSignature"})
public class ItemFactory {
    private static final Function<String, Material> valueOf;
    private static final Function<ItemStack, Material> getType;
    private static final Function<Block, Material> getTypeBlock;
    private static final Function<Material, Material> fromLegacy;
    private static final Predicate<Material> isLegacy;
    public static final boolean use13;

    static {
        MethodHandles.Lookup lk = MethodHandles.lookup();
        Predicate<Material> isLeg = v -> false;
        try {
            valueOf = (Function<String, Material>) LambdaMetafactory.metafactory(lk, "apply", MethodType.methodType(Function.class),
                    MethodType.methodType(Object.class, Object.class),
                    lk.findStatic(Material.class, "valueOf", MethodType.methodType(Material.class, String.class)),
                    MethodType.methodType(Material.class, String.class)).getTarget().invoke();
            getType = (Function<ItemStack, Material>) LambdaMetafactory.metafactory(lk, "apply", MethodType.methodType(Function.class),
                    MethodType.methodType(Object.class, Object.class),
                    lk.findVirtual(ItemStack.class, "getType", MethodType.methodType(Material.class)),
                    MethodType.methodType(Material.class, ItemStack.class)).getTarget().invoke();
            getTypeBlock = (Function<Block, Material>) LambdaMetafactory.metafactory(lk, "apply", MethodType.methodType(Function.class),
                    MethodType.methodType(Object.class, Object.class),
                    lk.findVirtual(Block.class, "getType", MethodType.methodType(Material.class)),
                    MethodType.methodType(Material.class, Block.class)).getTarget().invoke();
            Function<Material, Material> f;
            boolean u13 = true;
            try {
                f = (Function<Material, Material>) LambdaMetafactory.metafactory(lk, "apply", MethodType.methodType(Function.class, UnsafeValues.class),
                        MethodType.methodType(Object.class, Object.class),
                        lk.findVirtual(UnsafeValues.class, "fromLegacy", MethodType.methodType(Material.class, Material.class)),
                        MethodType.methodType(Material.class, Material.class)).getTarget().invoke(Bukkit.getUnsafe());
            } catch (Throwable ignore) {
                f = Function.identity();
                u13 = false;
            }
            use13 = u13;
            fromLegacy = f;
            try {
                isLeg = (Predicate<Material>) LambdaMetafactory.metafactory(lk, "test", MethodType.methodType(Predicate.class),
                        MethodType.methodType(boolean.class, Object.class),
                        lk.findVirtual(Material.class, "isLegacy", MethodType.methodType(boolean.class)),
                        MethodType.methodType(boolean.class, Material.class)).getTarget().invoke();
            } catch (Throwable exception) {
                if (u13) {
                    throw new ExceptionInInitializerError(exception);
                }
            }
        } catch (Throwable throwable) {
            throw new ExceptionInInitializerError(throwable);
        }
        isLegacy = isLeg;
    }

    public static Material getType(ItemStack stack) {
        return getType.apply(stack);
    }

    public static Material getType(Block block) {
        return getTypeBlock.apply(block);
    }

    public static boolean isLegacy(Material material) {
        return isLegacy.test(material);
    }

    public static Material valueOf(String name) {
        try {
            return valueOf.apply(name);
        } catch (Throwable ignore) {
        }
        return fromLegacy.apply(valueOf.apply("LEGACY_" + name));
    }

    public static Material load(String... names) {
        for (String name : names) {
            try {
                return valueOf(name);
            } catch (Throwable ignore) {
            }
        }
        throw new RuntimeException(String.join(", ", names));
    }

    public static Material fromLegacy(Material material) {
        if (isLegacy(material)) return fromLegacy.apply(material);
        return material;
    }

    public static boolean isSame(ItemStack source, ItemStack check) {
        if (source == check) return true;
        if (source == null || check == null) return false;
        if (use13) {
            if (source.getAmount() != check.getAmount()) return false;
            if (source.hasItemMeta() != check.hasItemMeta()) return false;
            if (fromLegacy(getType(source)) != fromLegacy(getType(check))) return false;
            final ItemMeta i1 = source.getItemMeta();
            final ItemMeta i2 = check.getItemMeta();
            if (i1 == null && i2 == null) return true;
            if (i1 == null || i2 == null) return false;
            if (!Objects.equals(i1.getDisplayName(), i2.getDisplayName())) return false;
            if (!Objects.equals(i1.getLore(), i2.getLore())) return false;
            if (source.getDurability() != check.getDurability()) return false;
            return Objects.equals(i1.getEnchants(), i2.getEnchants());
        } else return source.equals(check);
    }
}
