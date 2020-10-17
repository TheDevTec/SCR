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
import Utils.MultiWorldsUtils;
import Utils.Tasks;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.APIs.PluginManagerAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class ServerControl implements CommandExecutor, TabCompleter {

	public static boolean clearing = false;

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {

		if (args.length == 0 || args.length == 1 && args[0].equalsIgnoreCase("Help")) {
			if (Loader.has(s, "Help", "Info")) {
				TheAPI.msg(Loader.getTranslation("Prefix").toString() + "&7----------------- &eHelp&7 -----------------", s);
				TheAPI.msg("", s);
				if (s.hasPermission("ServerControl.Reload"))
					Loader.Help(s, "/ServerControl Reload", "Reload");
				if (s.hasPermission("ServerControl.Info")) {
					Loader.Help(s, "/ServerControl Version", "Version");
					Loader.Help(s, "/ServerControl Info", "Info");
				}
				if (s.hasPermission("ServerControl.List"))
					Loader.Help(s, "/ServerControl List", "List");
				return true;
			}
			Loader.noPerms(s, "Help", "Info");
			return true;
		}

		if (args[0].equalsIgnoreCase("List")) {
			if (Loader.has(s, "List", "Info")) {
				TheAPI.msg(Loader.getTranslation("Prefix").toString() + "&7----------------- &eList &7-----------------", s);
				TheAPI.msg("", s);
				TheAPI.msg(Loader.getTranslation("Prefix").toString() + "&cSwear words: "
						+ StringUtils.join(Loader.config.getStringList("SwearWords"), ", "), s);
				TheAPI.msg(
						Loader.getTranslation("Prefix").toString() + "&cSpam words: "
								+ StringUtils.join(Loader.config.getStringList("SpamWords.Words"), ", "),
						s);
				return true;
			}
			Loader.noPerms(s, "List", "Info");
			return true;
		}
		if (args[0].equalsIgnoreCase("Reload")) {
			if (Loader.has(s, "Reload", "Info")) {
				TheAPI.msg(Loader.getTranslation("Prefix").toString() + "&7----------------- &eReloading config&7 -----------------", s);
				TheAPI.msg("", s);
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
			Loader.noPerms(s, "Reload", "Info");
			return true;
		}

		if (args[0].equalsIgnoreCase("Version") || args[0].equalsIgnoreCase("info")) {
			if (Loader.has(s, "Info", "Info")) {
				TheAPI.msg(Loader.getTranslation("Prefix").toString() + "&7----------------- &eVersion&7 -----------------", s);
				TheAPI.msg("", s);
				TheAPI.msg(Loader.getTranslation("Prefix").toString() + "&7Version of ServerControlReloaded: &eV"
						+ PluginManagerAPI.getVersion("ServerControlReloaded"), s);
				TheAPI.msg(Loader.getTranslation("Prefix").toString() + "&7Version of TheAPI: &eV"
						+ PluginManagerAPI.getVersion("TheAPI"), s);
				TheAPI.msg(Loader.getTranslation("Prefix").toString() + "&7Version of Server: &e" + Bukkit.getServer().getBukkitVersion(), s);
				TheAPI.msg(Loader.getTranslation("Prefix").toString() + "&7Our discord: &ehttps://discord.gg/z4kK66g", s);
				return true;
			}
			Loader.noPerms(s, "Info", "Info");
			return true;
		}
		return true;
	}

	final List<String> All = Arrays.asList("Info", "Version", "Reload");

	@Override
	public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
		List<String> c = new ArrayList<>();
		if (args.length == 1) {

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