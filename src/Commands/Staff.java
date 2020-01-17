package Commands;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.API;
import ServerControl.Loader;

public class Staff implements CommandExecutor {
	
	HashMap<Player,String> p = new HashMap<Player,String>(); // Player, group;
	
	public String getGroup(Player a) {
		try {
		if(API.existVaultPlugin())
			if(Loader.vault != null)
				if(Loader.vault.getPrimaryGroup(a) !=null)
			return Loader.vault.getPrimaryGroup(a);
		}catch(Exception e) {
			return "default";
		}
		return "default";
	}
	
	public void sortPlayers() {
		p.clear();
		for(Player a:Bukkit.getOnlinePlayers()) {
			p.put(a, getGroup(a));
		}
	}
	public String getStaff() {
		ArrayList<String> w = new ArrayList<String>();
		for(Player a:Bukkit.getOnlinePlayers()) {
		if(Loader.config.getStringList("StaffList").contains(p.get(a))) {
			w.add(a.getName());
		}}
		if(w.isEmpty())
		return "0";
		return StringUtils.join(w," ");
	}
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s,"ServerControl.Staff")){
			for(String a:Loader.TranslationsFile.getStringList("PlayerList.Staff")) {
				sortPlayers();
			Loader.msg(a
					.replace("%online%",Bukkit.getOnlinePlayers().size()+"")
					.replace("%max_players%", Bukkit.getMaxPlayers()+"")
					.replace("%prefix%", Loader.s("Prefix"))
					.replace("%staff%", getStaff()), s);
		}
			return true;
		}
		return true;
	}

}
