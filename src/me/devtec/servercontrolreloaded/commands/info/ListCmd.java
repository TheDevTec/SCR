package me.devtec.servercontrolreloaded.commands.info;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.servercontrolreloaded.utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class ListCmd implements CommandExecutor, TabCompleter {
	
	public static int joinercount(CommandSender d, String value) {
		int s = 0;
		List<String> groups = Arrays.asList(value.toLowerCase().split("[ ]*,[ ]*"));
		List<String> stafff = Loader.config.getStringList("Options.StaffList");
		List<String> staff = new ArrayList<>(stafff.size());
		for(String f : stafff)
			staff.add(f.toLowerCase());
		for (Player a : TheAPI.getOnlinePlayers()) {
			if(d instanceof Player && !API.canSee((Player) d, a.getName()))continue;
			String as = Staff.getGroup(a);
			if(groups.contains("{staff}") && staff.contains(as.toLowerCase())||groups.contains(as.toLowerCase()))
				++s;
		}
		return s;
	}
	
	public static String joiner(CommandSender d, String value) {
		StringBuilder b = new StringBuilder();
		List<String> groups = Arrays.asList(value.toLowerCase().split("[ ]*,[ ]*"));
		List<String> stafff = Loader.config.getStringList("Options.StaffList");
		List<String> staff = new ArrayList<>(stafff.size());
		for(String f : stafff)
			staff.add(f.toLowerCase());
		for (Player a : TheAPI.getOnlinePlayers()) {
			if(d instanceof Player && !API.canSee((Player) d, a.getName()))continue;
			String as = Staff.getGroup(a);
			if(groups.contains("{staff}") && staff.contains(as.toLowerCase())||groups.contains(as.toLowerCase()))
				b.append(Loader.config.getString("Options.List.Splitter")).append(Staff.playerNameFormatter(Loader.config.getString("Options.List.PlayerName-Format"), a));
		}
		return b.length()>Loader.config.getString("Options.List.Splitter").length()?b.toString().substring(Loader.config.getString("Options.List.Splitter").length()):b.toString();
	}
	
	private static final Pattern a = Pattern.compile("\\%joiner\\{(.*?)\\}\\%");
	private static final Pattern b = Pattern.compile("\\%joiner-count\\{(.*?)\\}\\%");

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "List", "Info")) {
			if(!CommandsManager.canUse("Info.List", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Info.List", s))));
				return true;
			}
			Object o = Loader.getTranslation("List.Normal");
			if(o==null || o instanceof Collection && ((Collection<?>)o).isEmpty() || (o+"").equals(""))return true;
			if(o instanceof Collection) {
				for(Object sd : (Collection<?>)o) {
					boolean empty = true, contains = false;
					Matcher f = a.matcher(sd+"");
					while(f.find()) {
						contains=true;
						String join = joiner(s,f.group(1));
						if(!join.equals(""))empty=false;
						sd=(sd+"").replace(f.group(), join);
					}
					f = b.matcher(sd+"");
					while(f.find())
						sd=(sd+"").replace(f.group(), joinercount(s,f.group(1))+"");
					if(!contains || !empty || !setting.list)
					TheAPI.msg(Loader.placeholder(s, sd+"", null), s);
				}
				return true;
			}
			String sd = o+"";
			Matcher f = a.matcher(sd);
			while(f.find())
				sd=sd.replace(f.group(), joiner(s,f.group(1)));
			f = b.matcher(sd);
			while(f.find())
				sd=sd.replace(f.group(), joinercount(s,f.group(1))+"");
			TheAPI.msg(Loader.placeholder(s, sd, null), s);
			return true;
		}
		Loader.noPerms(s, "List", "Info");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] arg3) {
		return Collections.emptyList();
	}
}
