package me.devtec.servercontrolreloaded.commands.info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.apis.PluginManagerAPI;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;

public class Staff implements CommandExecutor, TabCompleter {
	public static String getGroup(Player p) {
		if(PluginManagerAPI.isEnabledPlugin("LuckPerms"))
			return LuckPermsProvider.get().getUserManager().getUser(p.getUniqueId()).getPrimaryGroup();
		if (Loader.vault != null) {
			return Loader.vault.getPrimaryGroup(p);
		}
		if (Loader.perms != null) {
			return Loader.perms.getPrimaryGroup(p);
		}
		return "default";
	}
	
	public static String getGroup(String p) {
		if(PluginManagerAPI.isEnabledPlugin("LuckPerms")) {
			User u = LuckPermsProvider.get().getUserManager().getUser(p);
			if(u==null)
				try {
					u=LuckPermsProvider.get().getUserManager().loadUser(TheAPI.getUser(p).getUUID()).get();
				} catch (Exception e) {
				}
			if(u!=null)
				return u.getPrimaryGroup();
		}
		if (Loader.vault != null) {
			return Loader.vault.getPrimaryGroup(Bukkit.getWorlds().get(0),p);
		}
		if (Loader.perms != null) {
			return Loader.perms.getPrimaryGroup(Bukkit.getWorlds().get(0),p);
		}
		return "default";
	}
	
	public static int joinercount(CommandSender d) {
		int s = 0;
		List<String> stafff = Loader.config.getStringList("Options.StaffList");
		List<String> staff = new ArrayList<>(stafff.size());
		for(String f : stafff)
			staff.add(f.toLowerCase());
		for (Player a : TheAPI.getOnlinePlayers()) {
			if(d instanceof Player == false ? false : !API.canSee((Player)d,a.getName()))continue;
			String as = Staff.getGroup(a);
			if(staff.contains(as.toLowerCase()))
				++s;
		}
		return s;
	}
	
	public static String joiner(CommandSender d) {
		StringBuilder b = new StringBuilder();
		List<String> stafff = Loader.config.getStringList("Options.StaffList");
		List<String> staff = new ArrayList<>(stafff.size());
		for(String f : stafff)
			staff.add(f.toLowerCase());
		for (Player a : TheAPI.getOnlinePlayers()) {
			if(d instanceof Player == false ? false : !API.canSee((Player)d,a.getName()))continue;
			String as = Staff.getGroup(a);
			if(staff.contains(as.toLowerCase()))
				b.append(", "+a.getName());
		}
		return b.length()>2?b.toString().substring(2):b.toString();
	}
	
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Staff", "Info")) {
			if(Loader.has(s, "Staff", "Info", "Vanished")) {
				boolean e=joinercount(s)==0;
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
						.add("%staff_online%", joinercount(s)+"")
						.add("%staff%", joiner(s)));
				return true;
			}
			boolean isEmpty = joinercount(s)==0;
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
					.add("%staff_online%", joinercount(s)+"")
					.add("%staff%", joiner(s)));
			return true;
		}
		Loader.noPerms(s, "Staff", "Info");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] arg3) {
		return Arrays.asList();
	}
}
