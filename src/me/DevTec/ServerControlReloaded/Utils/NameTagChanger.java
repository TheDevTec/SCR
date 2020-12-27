package me.DevTec.ServerControlReloaded.Utils;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.NameTagAPI;
import me.devtec.theapi.utils.datakeeper.maps.UnsortedMap;

public class NameTagChanger {
	static UnsortedMap<Player, NameTagAPI> t = new UnsortedMap<>();

	public static void setNameTag(Player p, String prefix, String suffix) {
		Tasks.regPlayer(p);
		if (setting.tab_sort) {
			if (setting.tab_nametag) {
				if (!t.containsKey(p))
					t.put(p, TheAPI.getNameTagAPI(p, AnimationManager.replace(p,prefix), AnimationManager.replace(p,suffix)));
				NameTagAPI n = t.get(p);
				n.setPrefix(AnimationManager.replace(p,prefix));
				n.setSuffix(AnimationManager.replace(p,suffix));
				n.setNameTag(Tasks.sss.get(p.getName()));
			} else {
				String pname = p.getName();
				if (pname.length() > 16)
					pname = pname.substring(0, 15);
				if (!t.containsKey(p))
					t.put(p, TheAPI.getNameTagAPI(p, null, null));
				NameTagAPI n = t.get(p);
				n.setPrefix(null);
				n.setSuffix(null);
				n.setNameTag(pname);
			}
		} else {
			if (setting.tab_nametag) {
				if (!t.containsKey(p))
					t.put(p, TheAPI.getNameTagAPI(p, AnimationManager.replace(p,prefix), AnimationManager.replace(p,suffix)));
				NameTagAPI n = t.get(p);
				n.setPrefix(AnimationManager.replace(p,prefix));
				n.setSuffix(AnimationManager.replace(p,suffix));
				n.setNameTag(Tasks.sss.get(p.getName()));
			}
		}
	}

	public static void remove(Player p) {
		if(p==null || p.getScoreboard()==null)return;
		for(Team t : p.getScoreboard().getTeams())t.unregister();
		t.remove(p);
	}
}