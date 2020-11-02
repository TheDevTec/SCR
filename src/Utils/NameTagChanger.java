package Utils;

import java.util.HashMap;

import org.bukkit.entity.Player;

import Commands.Info.Staff;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.APIs.NameTagAPI;

public class NameTagChanger {
	static HashMap<Player, NameTagAPI> t = new HashMap<>();

	public static void setNameTag(Player p, String prefix, String suffix) {
		if(!Tasks.ss.containsKey(p.getName()))
			Tasks.regPlayer(p);
		if (setting.tab_sort) {
			if (setting.tab_nametag) {
				if (!t.containsKey(p))
					t.put(p, TheAPI.getNameTagAPI(p, prefix, suffix));
				NameTagAPI n = t.get(p);
				n.setPrefix(TabList.replace(prefix, p));
				n.setSuffix(TabList.replace(suffix, p));
				n.setNameTag(Staff.getGroup(p) + Tasks.ss.get(p.getName()));
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
				NameTagAPI n = t.get(p);
				n.setPrefix(prefix);
				n.setSuffix(suffix);
				n.setNameTag(Tasks.ss.get(p.getName()));
			}
		}
	}

	public static void remove(Player p) {
		if(p==null)return;
		new NameTagAPI(p, "", "").resetNameTag();
		t.remove(p);
	}
}