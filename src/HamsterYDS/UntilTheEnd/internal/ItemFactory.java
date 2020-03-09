/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/08 18:22:41
 *
 * UntilTheEnd/UntilTheEnd/ItemFactory.java
 */

package HamsterYDS.UntilTheEnd.internal;

import HamsterYDS.UntilTheEnd.guide.CraftGuide;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.UnsafeValues;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Function;

@SuppressWarnings({"unchecked", "JavaLangInvokeHandleSignature"})
public class ItemFactory {
    private static final Function<String, Material> valueOf;
    private static final Function<ItemStack, Material> getType;
    private static final Function<Block, Material> getTypeBlock;
    private static final Function<Material, Material> fromLegacy;
    private static boolean use13 = true;

    public static ItemStack newGray(String name, String type, int data) {
        if (use13) {
            try {
                ItemStack item = new ItemStack(valueOf.apply(type + "_STAINED_GLASS_PANE"));
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(name);
                item.setItemMeta(meta);
                return item;
            } catch (Throwable ignore) {
                use13 = false;
            }
        }
        return CraftGuide.getItem(name, Material.STAINED_GLASS_PANE, data);
    }

    static {
        MethodHandles.Lookup lk = MethodHandles.lookup();
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
            try {
                f = (Function<Material, Material>) LambdaMetafactory.metafactory(lk, "apply", MethodType.methodType(Function.class, UnsafeValues.class),
                        MethodType.methodType(Object.class, Object.class),
                        lk.findVirtual(UnsafeValues.class, "fromLegacy", MethodType.methodType(Material.class, Material.class)),
                        MethodType.methodType(Material.class, Material.class)).getTarget().invoke(Bukkit.getUnsafe());
            } catch (Throwable ignore) {
                f = Function.identity();
            }
            fromLegacy = f;
        } catch (Throwable throwable) {
            throw new ExceptionInInitializerError(throwable);
        }
    }

    public static Material getType(ItemStack stack) {
        return getType.apply(stack);
    }

    public static Material getType(Block block) {
        return getTypeBlock.apply(block);
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
}
