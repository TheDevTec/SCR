package Commands.Info;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import Utils.MultiWorldsUtils;
import Utils.Tasks;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.APIs.PluginManagerAPI;

public class ServerControl implements CommandExecutor, TabCompleter {

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (Loader.has(s, "SCR", "Info")) {
			if (args.length == 0) {
				Loader.Help(s, "ServerControl", "Info");
				return true;
				}
			
			if (args[0].equalsIgnoreCase("Reload")) {
					Loader.config.reload();
					setting.load();
					Tasks.reload();
					MultiWorldsUtils.LoadWorlds();
					if (setting.timezone) {
						try {
							TimeZone.setDefault(TimeZone.getTimeZone(Loader.config.getString("Options.TimeZone.Zone")));
						} catch (Exception e) {
							TheAPI.msg("&6Invalid time zone: &c" + Loader.config.getString("Options.TimeZone.Zone"),
									TheAPI.getConsole());
							TheAPI.msg("&6List of available time zones:", TheAPI.getConsole());
							TheAPI.msg(" &6https://greenwichmeantime.com/time-zone/", TheAPI.getConsole());
						}
					}
					Loader.sendMessages(s, "Config");
					return true;
			}
			
			if (args[0].equalsIgnoreCase("Version") || args[0].equalsIgnoreCase("info")) {
					
				Loader.sendMessages(s, "SCR.Info", Placeholder.c()
						.add("%version%", PluginManagerAPI.getVersion("ServerControlReloaded"))
						.add("%server%", Bukkit.getServer().getBukkitVersion()));
					return true;
			}
			Loader.Help(s, "SCR", "Info");
			return true;
		}
		Loader.noPerms(s, "SCR", "Info");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		List<String> c = new ArrayList<>();
		if (args.length == 1) {
			if (Loader.has(s, "SCR", "Info")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Version", "Info", "Reload"), new ArrayList<>()));
			}
		}
		return c;
	}

}