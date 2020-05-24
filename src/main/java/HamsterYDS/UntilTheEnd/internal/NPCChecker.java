/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/18 24:04:00
 *
 * UntilTheEnd/UntilTheEnd/NPCChecker.java
 */

package HamsterYDS.UntilTheEnd.internal;

import HamsterYDS.UntilTheEnd.nms.NMSHelper;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.npc.ai.NPCHolder;
import org.bukkit.entity.Entity;

import com.infumia.fakeplayer.api.INPC;

import java.util.LinkedList;
import java.util.function.Predicate;

@SuppressWarnings("Convert2Lambda")
public class NPCChecker {
    private static final LinkedList<Predicate<Entity>> checkers = new LinkedList<>();

    public static void clearAll() {
        checkers.clear();
    }

    public static boolean isNPC(Entity entity) {
        for (Predicate<Entity> checker : checkers) {
            if (checker.test(entity)) return true;
        }
        return false;
    }

    public static void register(Predicate<Entity> checker) {
        checkers.add(checker);
    }

    static {
        try {
            // For Citizens
            Class.forName("net.citizensnpcs.npc.ai.NPCHolder");
            register(new Predicate<Entity>() {
                @Override
                public boolean test(Entity entity) {
                    return entity instanceof NPC || entity instanceof NPCHolder;
                }
            });
        } catch (Throwable ignore) {
        }
        try {
            // For FakePlayer (73139)
            Class.forName("com.infumia.fakeplayer.api.INPC");
            register(new Predicate<Entity>() {
                @Override
                public boolean test(Entity entity) {
                    return NMSHelper.getHandle(entity) instanceof INPC;
                }
            });
        } catch (Throwable ignore) {
        }
    }
}
