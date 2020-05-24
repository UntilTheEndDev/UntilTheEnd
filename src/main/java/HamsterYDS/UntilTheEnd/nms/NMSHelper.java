/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/05/23 21:08:31
 *
 * until-the-end/until-the-end.main/NMSHelper.java
 */

package HamsterYDS.UntilTheEnd.nms;

import org.bukkit.entity.Entity;

import java.lang.reflect.Method;
import java.util.function.Function;

// For Debugging
public class NMSHelper {
    public static final Class<?> CraftEntity = ReflectionUtil.getOBCClass("entity.CraftEntity");
    public static final Method CraftEntity$getHandle = ReflectionUtil.getMethod(CraftEntity, "getHandle");
    private static final Function<Entity, Object> CraftEntity$getHandle0 = ReflectionUtil.bindTo(Object.class, CraftEntity$getHandle);

    public static Object getHandle(Entity entity) {
        return CraftEntity$getHandle0.apply(entity);
    }
}
