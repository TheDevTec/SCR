package me.DevTec.ServerControlReloaded.Commands.Info;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.API;
import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.devtec.theapi.TheAPI;

public class ListCmd implements CommandExecutor, TabCompleter {
	
	public int joinercount(CommandSender d , String value) {
		int s = 0;
		List<String> groups = Arrays.asList(value.split("[ ]*,[ ]*"));
		for (Player a : TheAPI.getOnlinePlayers()) {
			if(d instanceof Player == false ? false : !API.canSee((Player)d,a.getName()))continue;
			String as = Staff.getGroup(a);
			if(groups.contains("{staff}") && Loader.config.getStringList("Options.StaffList").contains(as)||groups.contains(as))
				++s;
		}
		return s;
	}
	
	public String joiner(CommandSender d , String value) {
		String s = "";
		List<String> groups = Arrays.asList(value.split("[ ]*,[ ]*"));
		for (Player a : TheAPI.getOnlinePlayers()) {
			if(d instanceof Player == false ? false : !API.canSee((Player)d,a.getName()))continue;
			String as = Staff.getGroup(a);
			if(groups.contains("{staff}") && Loader.config.getStringList("Options.StaffList").contains(as)||groups.contains(as))
				s+=", "+a.getName();
		}
		return s.equals("")?s:s.substring(2);
	}
	
	Pattern a = Pattern.compile("\\%joiner\\{(.*?)\\}%"), b = Pattern.compile("\\%joiner-count\\{(.*?)\\}%");

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "List", "Info")) {
			Object o = Loader.getTranslation("List.Normal");
			if(o==null || o instanceof Collection && ((Collection<?>)o).isEmpty() || (o+"").equals(""))return true;
			if(o instanceof Collection) {
				for(Object sd : (Collection<?>)o) {
					boolean empty = true, contains = false;
					Matcher f = a.matcher(sd+"");
					while(f.find()) {
						contains=true;
						if(!joiner(s,f.group(1)).equals(""))empty=false;
						sd=(sd+"").replace(f.group(), joiner(s,f.group(1)));
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
		return Arrays.asList();
	}
}
