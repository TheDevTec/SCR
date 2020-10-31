package Commands.Info;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.APIs.PluginManagerAPI;

public class Staff implements CommandExecutor {
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
	
	public static String joiner() {
		String s = "";
		for (Player a : TheAPI.getOnlinePlayers())
			if(Loader.config.getStringList("Options.StaffList").contains(getGroup(a)))
				s+=(s.equals("")?"":", ")+a.getName();
		return s;
	}
	
	public static String joinercount() {
		int s = 0;
		for (Player a : TheAPI.getOnlinePlayers())
			if(Loader.config.getStringList("Options.StaffList").contains(getGroup(a)))
				++s;
		return s+"";
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "Staff", "Info")) {
			boolean isEmpty = joinercount().equals("0");
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
					.add("%staff_online%", joinercount())
					.add("%staff%", joiner()));
			return true;
		}
		Loader.noPerms(s, "Staff", "Info");
		return true;
	}

}
