package me.devtec.servercontrolreloaded.utils;

import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.NameTagAPI;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;

public class NameTagChanger {
	static HashMap<Player, NameTagAPI> t = new HashMap<>();
	static AnimationManager anim = new AnimationManager();

	public static void setNameTag(Player p, String prefix, String suffix) {
		Tasks.regPlayer(p);
		if (setting.tab_sort) {
			if (setting.tab_nametag) {
				if (!t.containsKey(p))
					t.put(p, TheAPI.getNameTagAPI(p, anim.replace(p,prefix), anim.replace(p,suffix)));
				NameTagAPI n = t.get(p);
				n.setPrefix(anim.replace(p,prefix));
				n.setSuffix(anim.replace(p,suffix));
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
					t.put(p, TheAPI.getNameTagAPI(p, anim.replace(p,prefix), anim.replace(p,suffix)));
				NameTagAPI n = t.get(p);
				n.setPrefix(anim.replace(p,prefix));
				n.setSuffix(anim.replace(p,suffix));
				n.setNameTag(Tasks.sss.get(p.getName()));
			}
		}
	}
	
	public static void update() {
		anim.update();
	}

	public static void remove(Player p) {
		if(p==null || p.getScoreboard()==null)return;
		for(Team t : p.getScoreboard().getTeams())t.unregister();
		NameTagAPI a = t.remove(p);
		if(a!=null)a.resetNameTag();
	}
}