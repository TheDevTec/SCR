package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class Addons implements CommandExecutor {
	public Plugin find(String a) {
		Plugin d = null;
		for (Plugin s : Loader.addons) {
			if (s.getName().toLowerCase().equalsIgnoreCase(a))
				d = s;
		}
		return d;
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (args.length == 0) {
			if (s.hasPermission("ServerControl.Addons")) {
				Loader.Help(s, "/Addons Enable <addon>", "Addons.Enable");
				Loader.Help(s, "/Addons Disable <addon>", "Addons.Disable");
			}
			Loader.msg(Loader.s("Prefix") + "&6Hooked addons:", s);
			for (Plugin f : Loader.addons)
				Loader.msg(Loader.s("Prefix") + getAddon(f.getName()), s);
			return true;
		}
		if (args[0].equalsIgnoreCase("enable")) {
			if (API.hasPerm(s, "ServerControl.Addons")) {
				String plugin = TheAPI.getPluginsManagerAPI().getPlugin(args[0]).getName();
				if (TheAPI.getPluginsManagerAPI().enablePlugin(plugin))
					Loader.msg(Loader.s("Prefix") + "&6Addon " + plugin + " &aenabled", s);
				else
					Loader.msg(Loader.s("Prefix") + "&cError when enabling addon &4" + plugin, s);
				return true;
			}
			return true;
		}

		if (args[0].equalsIgnoreCase("disable")) {
			if (API.hasPerm(s, "ServerControl.Addons")) {
				String plugin = TheAPI.getPluginsManagerAPI().getPlugin(args[0]).getName();
				if (TheAPI.getPluginsManagerAPI().disablePlugin(plugin))
					Loader.msg(Loader.s("Prefix") + "&6Addon " + plugin + " &cdisabled", s);
				else
					Loader.msg(Loader.s("Prefix") + "&cError when disabling addon &4" + plugin, s);
				return true;
			}
			return true;
		}
		return false;
	}

	private String getAddon(String s) {
		if (Bukkit.getPluginManager().isPluginEnabled(s))
			return "&2+ &a" + s;
		return "&4- &c" + s;
	}

}
