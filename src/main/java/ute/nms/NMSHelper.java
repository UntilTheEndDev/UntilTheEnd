package ute.nms;

import org.bukkit.entity.Entity;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.function.BiConsumer;
import java.util.function.Function;

// For Debugging
public class NMSHelper {
    public static final Class<?> CraftEntity = ReflectionUtil.getOBCClass("entity.CraftEntity");
    public static final Method CraftEntity$getHandle = ReflectionUtil.getMethod(CraftEntity, "getHandle");
    private static final Function<Entity, Object> CraftEntity$getHandle0 = ReflectionUtil.bindTo(Object.class, CraftEntity$getHandle);
    public static final Class<?> EntityPlayer = ReflectionUtil.getNMSClass("EntityPlayer");
    public static final Field EntityPlayer$playerConnection = ReflectionUtil.getField(EntityPlayer, "playerConnection");
    private static final Function<Object, Object> EntityPlayer$getPlayerConnection =
            ReflectionUtil.bindTo(Object.class, EntityPlayer$playerConnection);
    public static final Class<?> PlayerConnection = ReflectionUtil.getNMSClass("PlayerConnection");
    public static final Class<?> Packet = ReflectionUtil.getNMSClass("Packet");

    @SuppressWarnings("unchecked")
    private static final BiConsumer<Object, Object> PlayerConnection$sendPacket = ReflectionUtil.runUncheck((args) -> {
        final MethodHandles.Lookup lookup = MethodHandles.lookup();
        return (BiConsumer<Object, Object>) LambdaMetafactory.metafactory(lookup, "accept",
                MethodType.methodType(BiConsumer.class),
                MethodType.methodType(void.class, Object.class, Object.class),
                lookup.unreflect(PlayerConnection.getMethod("sendPacket", Packet)),
                MethodType.methodType(void.class, Packet)).getTarget().invoke();

    });

    public static Object getHandle(Entity entity) {
        return CraftEntity$getHandle0.apply(entity);
    }

    public static Object getPlayerConnection(Object entityPlayer) {
        return EntityPlayer$getPlayerConnection.apply(entityPlayer);
    }

    public static void sendPacket(Object playerConnection, Object packet) {
        PlayerConnection$sendPacket.accept(playerConnection, packet);
    }
}
