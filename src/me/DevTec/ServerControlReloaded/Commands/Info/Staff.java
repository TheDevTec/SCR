package me.DevTec.ServerControlReloaded.Commands.Info;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.PluginManagerAPI;

public class Staff implements CommandExecutor, TabCompleter {
	public static String getGroup(Player a) {
		try {
			if (PluginManagerAPI.getPlugin("Vault") != null)
				if (Loader.vault != null)
					if (Loader.vault.getPrimaryGroup(a) != null)
						return Loader.vault.getPrimaryGroup(a);
		} catch (Exception e) {
			return "default";
		}
		return "default";
	}
	
	public static String getGroup(String a) {
		try {
			if (PluginManagerAPI.getPlugin("Vault") != null)
				if (Loader.vault != null)
					if (Loader.vault.getPrimaryGroup(Bukkit.getWorld("world"), a) != null)
						return Loader.vault.getPrimaryGroup(Bukkit.getWorld("world"), a);
		} catch (Exception e) {
			return "default";
		}
		return "default";
	}
	
	public static String joiner(CommandSender sender) {
		String s = "";
		for (Player a : TheAPI.getOnlinePlayers()) 
			if(Loader.config.getStringList("Options.StaffList").contains(getGroup(a))) { 
				if(sender instanceof Player == false ? false : !TheAPI.canSee((Player)sender,a.getName()))continue;
				s+=(s.equals("")?"":", ")+a.getName();
			}
		return s;
	}
	
	
	public static String joinercount(CommandSender sender) {
		int s = 0;
		for (Player a : TheAPI.getOnlinePlayers()) 
			if(Loader.config.getStringList("Options.StaffList").contains(getGroup(a))) {
				if(sender instanceof Player == false ? false : !TheAPI.canSee((Player)sender,a.getName()))continue;
				++s;
			}
		return s+"";
	}
	public static String jner() {
		String s = "";
		for (Player a : TheAPI.getOnlinePlayers()) 
			if(Loader.config.getStringList("Options.StaffList").contains(getGroup(a)))				
				s+=(s.equals("")?"":", ")+a.getName();
		return s;
	}
	public static String jcount() {
		int s = 0;
		for (Player a : TheAPI.getOnlinePlayers()) 
			if(Loader.config.getStringList("Options.StaffList").contains(getGroup(a)))
				++s;
		return s+"";
	}
	

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Staff", "Info")) {
			if(Loader.has(s, "Staff", "Info", "Vanished")) {
				boolean e=jcount().equals("0");
				if (e && setting.staff_hide) {
					if(setting.staff_replace) {
						for(String a : Loader.config.getStringList("Options.Staff.ReplaceWith")){
							TheAPI.msg(Loader.placeholder(s, a, null), s);
						}
						return true;
					}
					return true;
				}else if(!e || !setting.staff_hide)
				Loader.sendMessages(s, "List.Staff", Placeholder.c()
						.add("%staff_online%", jcount())
						.add("%staff%", jner()));
				return true;
				}
			boolean isEmpty = joinercount(s).equals("0");
			if(isEmpty && setting.staff_hide) {
				if(setting.staff_replace) {
					for(String a : Loader.config.getStringList("Options.Staff.ReplaceWith")){
						TheAPI.msg(Loader.placeholder(s, a, null), s);
					}
					return true;
				}
				return true;
			}else if(!isEmpty || !setting.staff_hide)
			Loader.sendMessages(s, "List.Staff", Placeholder.c()
					.add("%staff_online%", joinercount(s))
					.add("%staff%", joiner(s)));
			return true;
		}
		Loader.noPerms(s, "Staff", "Info");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}
