/*
 * Copyright (c) 2018-2020 Karlatemp. All rights reserved.
 * @author Karlatemp <karlatemp@vip.qq.com> <https://github.com/Karlatemp>
 * @create 2020/03/18 24:04:00
 *
 * UntilTheEnd/UntilTheEnd/NPCChecker.java
 */

package HamsterYDS.UntilTheEnd.internal;

import java.util.List;

import org.bukkit.Location;

import com.bekvon.bukkit.residence.api.ResidenceApi;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import HamsterYDS.UntilTheEnd.UntilTheEnd;

public class ResidenceChecker {
	static List<String> ignoreResidences=UntilTheEnd.getInstance().getConfig().getStringList("ignoreResidences");
	public static boolean isProtected(Location loc) {
		try {
			ClaimedResidence res=ResidenceApi.getResidenceManager().getByLoc(loc);
			if(res==null) return false;
			if(ignoreResidences.contains(res.getName()))
				return true;
			return false;
		}catch(Exception e) {
			return false;
		}
	}
}
