package me.DevTec.ServerControlReloaded.Commands.Info;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;

public class ListCmd implements CommandExecutor, TabCompleter {
	
	public String joiner(CommandSender d , String value) {
		HashMap<String, List<String>> p = new HashMap<String, List<String>>();
		for (Player a : TheAPI.getOnlinePlayers()) {
			if(d instanceof Player == false ? false : !TheAPI.canSee((Player)d,a.getName()))continue;
			String as = Staff.getGroup(a);
			List<String> s = p.getOrDefault(as, new ArrayList<>());
			s.add(a.getName());
			p.put(as, s);
		}
		String s = "";
		for(String group : value.split("[ ]*,[ ]*")) {
			if(group.equalsIgnoreCase("{staff}")) {
				if(!Staff.joiner(d).equals(""))
				s+=(s.equals("")?"":", ")+Staff.joiner(d);
				continue;
			}
			//ex. default, vip, supervip
			String ss = StringUtils.join(p.getOrDefault(group, Arrays.asList()), ", ");
			if(!ss.equals("")) //empty
				//empty String -> ""
			s+=(s.equals("")?"":", ")+ss;
		}
		return s;
	}
	
	public String joinercount(CommandSender d , String value) {
		HashMap<String, List<String>> p = new HashMap<String, List<String>>();
		for (Player a : TheAPI.getOnlinePlayers()) {
			if(d instanceof Player == false ? false : !TheAPI.canSee((Player)d,a.getName()))continue;
			String as = Staff.getGroup(a);
			List<String> s = p.getOrDefault(as, new ArrayList<>());
			s.add(a.getName());
			p.put(as, s);
		}
		int s = 0;
		for(String group : value.split("[ ]*,[ ]*")) {
			if(group.equalsIgnoreCase("{staff}")) {
				s+=StringUtils.getInt(Staff.joinercount(d));
				continue;
			}
			s+=p.getOrDefault(group, Arrays.asList()).size();
		}
		return s+"";
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
						sd=(sd+"").replace(f.group(), joinercount(s,f.group(1)));
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
				sd=sd.replace(f.group(), joinercount(s,f.group(1)));
			TheAPI.msg(Loader.placeholder(s, sd, null), s);
			return true;
		}
		Loader.noPerms(s, "List", "Info");
		return true;
	}
	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}
}
