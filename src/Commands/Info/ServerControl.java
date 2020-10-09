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

import ServerControl.API;
import ServerControl.Loader;
import Utils.Configs;
import Utils.MultiWorldsUtils;
import Utils.Tasks;
import Utils.setting;
import me.DevTec.TheAPI.APIs.PluginManagerAPI;
import me.DevTec.TheAPI.TheAPI;

public class ServerControl implements CommandExecutor, TabCompleter {

	public static boolean clearing = false;

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (args.length == 0 || args.length == 1 && args[0].equalsIgnoreCase("Help")) {
			if (API.hasPerm(s, "ServerControl.Help")) {
				TheAPI.msg(Loader.s("Prefix") + "&e----------------- &bHelp&e -----------------", s);
				TheAPI.msg("", s);
				if (s.hasPermission("ServerControl.Reload"))
					Loader.Help(s, "/ServerControl Reload", "Reload");
				if (s.hasPermission("ServerControl.Info")) {
					Loader.Help(s, "/ServerControl Version", "Version");
					Loader.Help(s, "/ServerControl Info", "Info");
				}
				if (s.hasPermission("ServerControl.List"))
					Loader.Help(s, "/ServerControl List", "List");

				if (args.length == 2) {
					for (String v : All)
						if (args[1].equalsIgnoreCase(v)) {
							TheAPI.msg(Loader.s("Prefix") + "&e----------------- &bHelp for " + "&b" + v
									+ "&e -----------------", s);
							TheAPI.msg("", s);
							Loader.Help(s, "/ServerControl " + v, v);
							return true;
						}
					TheAPI.msg(Loader.s("Prefix") + "&e----------------- &bHelp " + "&4" + args[1]
							+ " &e-----------------", s);
					TheAPI.msg("", s);
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Help.NoHelpForCommand").replace("%command%", args[1]), s);
					return true;
				}
				return true;
			}
			return true;
		}

		if (args[0].equalsIgnoreCase("List")) {
			if (API.hasPerm(s, "ServerControl.List")) {
				TheAPI.msg(Loader.s("Prefix") + "&e----------------- &bList &e-----------------", s);
				TheAPI.msg("", s);
				TheAPI.msg(Loader.s("Prefix") + "&cSwear words: "
						+ StringUtils.join(Loader.config.getStringList("SwearWords"), ", "), s);
				TheAPI.msg(
						Loader.s("Prefix") + "&cSpam words: "
								+ StringUtils.join(Loader.config.getStringList("SpamWords.Words"), ", "),
						s);
				return true;
			}
			return true;
		}
		if (args[0].equalsIgnoreCase("Reload")) {
			if (API.hasPerm(s, "ServerControl.Reload")) {
				TheAPI.msg(Loader.s("Prefix") + "&e----------------- &bReloading config&e -----------------", s);
				TheAPI.msg("", s);
				Configs.reload();
				setting.load();
				Tasks.reload();
				MultiWorldsUtils.LoadWorlds();
				Loader.SoundsChecker();
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
				TheAPI.msg(Loader.s("Prefix") + Loader.s("ConfigReloaded"), s);
				return true;
			}
			return true;
		}

		if (args[0].equalsIgnoreCase("Version") || args[0].equalsIgnoreCase("info")) {
			if (API.hasPerm(s, "ServerControl.Info")) {
				TheAPI.msg(Loader.s("Prefix") + "&e----------------- &bVersion&e -----------------", s);
				TheAPI.msg("", s);
				TheAPI.msg(Loader.s("Prefix") + "&7Version of ServerControlReloaded: &eV"
						+ PluginManagerAPI.getVersion("ServerControlReloaded"), s);
				TheAPI.msg(Loader.s("Prefix") + "&7Version of TheAPI: &eV"
						+ PluginManagerAPI.getVersion("TheAPI"), s);
				TheAPI.msg(Loader.s("Prefix") + "&7Version of Server: &e" + Bukkit.getServer().getBukkitVersion(), s);
				TheAPI.msg(Loader.s("Prefix") + "&7Our discord: &ehttps://discord.gg/z4kK66g", s);
				return true;
			}
			return true;
		}
		TheAPI.msg(Loader.s("Prefix") + Loader.s("UknownCommand"), s);
		return true;
	}

	final List<String> All = Arrays.asList("Info", "Version", "Help", "Reload");

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		List<String> c = new ArrayList<>();
		if (args.length == 1) {

			if (s.hasPermission("ServerControl.Help")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Help"), new ArrayList<>()));
			}
			if (s.hasPermission("ServerControl.Reload")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Reload"), new ArrayList<>()));
			}
			if (s.hasPermission("ServerControl.List")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("List"), new ArrayList<>()));
			}
			if (s.hasPermission("ServerControl.Info")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Version", "Info"), new ArrayList<>()));
			}
		}
		if (args[0].equalsIgnoreCase("Help") && args.length == 2) {
			if (s.hasPermission("ChatControl.Help")) {
				c.addAll(StringUtil.copyPartialMatches(args[1], All, new ArrayList<>()));

			}

		}
		return c;
	}

}