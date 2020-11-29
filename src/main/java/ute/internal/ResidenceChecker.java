/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/18 24:04:00
 *
 * UntilTheEnd/UntilTheEnd/NPCChecker.java
 */

package ute.internal;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Location;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import ute.UntilTheEnd;

// 代码隔离
@SuppressWarnings("Convert2Lambda")
public class ResidenceChecker {
    public static final LinkedList<Consumer<ProtectedEvent>> handlers = new LinkedList<>();

    public static class ProtectedEvent {
        public boolean cancelled;
        public final Location location;

        public ProtectedEvent(Location location) {
            this.location = location;
        }
    }

    static {
        try {
            // Residence
            Class.forName("com.bekvon.bukkit.residence.api.ResidenceApi");
            List<String> ignoredList = UntilTheEnd.getInstance().getConfig().getStringList("ignoreResidences");
            handlers.add(new Consumer<ProtectedEvent>() {
                @Override
                public void accept(ProtectedEvent event) {
                    if (event.cancelled) return;
                    ClaimedResidence residence = ResidenceApi.getResidenceManager().getByLoc(event.location);
                    while (residence != null) {
                        if (ignoredList.contains(residence.getName())) {
                            event.cancelled = true;
                            return;
                        }
                        residence = residence.getParent();
                    }
                }
            });
        } catch (Throwable ignore) {
        }
    }

    public static boolean isProtected(Location loc) {
        ProtectedEvent event = new ProtectedEvent(loc);
        for (Consumer<ProtectedEvent> handler : handlers) {
            handler.accept(event);
        }
        return event.cancelled;
    }
}
