package Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import Utils.MultiWorldsGUI;
import me.Straiker123.TheAPI;

public class Worlds implements CommandExecutor, TabCompleter {

	public void tp(final CommandSender s) {
		Loader.Help(s, "/Mw Tp <world> <player>", "MultiWorld.Tp");
	}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (args.length == 0) {

			if (s instanceof Player) {
				MultiWorldsGUI.openInv((Player) s);
				return true;
			}
			Loader.Help(s, "/Mw Tp <world> <player>", "MultiWorld.Tp");
			return true;
		}
		if (args[0].equalsIgnoreCase("tp")) {
			if (API.hasPerm(s, "ServerControl.MultiWorld.Tp")) {
				if (args.length == 1) {
					Loader.msg(Loader.s("Prefix") + ChatColor.YELLOW + "----------------- " + ChatColor.DARK_AQUA + "Tp"
							+ ChatColor.YELLOW + " -----------------", s);
					Loader.msg("", s);
					this.tp(s);
					return true;
				}
				if (args.length == 2) {
					if (Bukkit.getWorld(args[1]) == null) {
						Loader.msg(Loader.s("Prefix") + Loader.s("MultiWorld.NotExists").replace("%world%", args[1]),
								s);
						return true;
					}
					if (s instanceof Player) {
						double x = Loader.mw.getDouble("WorldsSettings." + args[1] + ".Spawn.X");
						double y = Loader.mw.getDouble("WorldsSettings." + args[1] + ".Spawn.Y");
						double z = Loader.mw.getDouble("WorldsSettings." + args[1] + ".Spawn.Z");
						float x_head = Loader.mw.getLong("WorldsSettings." + args[1] + ".Spawn.X_Pos_Head");
						float z_head = Loader.mw.getLong("WorldsSettings." + args[1] + ".Spawn.Z_Pos_Head");
						World world = Bukkit
								.getWorld(Loader.mw.getString("WorldsSettings." + args[1] + ".Spawn.World"));
						Location loc = new Location(world, x, y, z, x_head, z_head);
						Player p2 = (Player) s;
						API.setBack(p2);
						p2.teleport(loc);
						Loader.msg(
								Loader.s("Prefix") + Loader.s("MultiWorld.TeleportedWorld").replace("%world%", args[1]),
								s);
						return true;
					}
					Loader.Help(s, "/Mw Tp " + args[1] + " <player>", "MultiWorld.Tp");
					return true;
				} else if (args.length == 3 && s.hasPermission("ServerControl.MultiWorld.tp.other")) {
					final Player target = TheAPI.getPlayer(args[2]);
					if (target == null) {
						Loader.msg(Loader.PlayerNotOnline(args[2]), s);
						return true;
					}
					if (Bukkit.getWorld(args[1]) != null) {
						double x = Loader.mw.getDouble("WorldsSettings." + args[1] + ".Spawn.X");
						double y = Loader.mw.getDouble("WorldsSettings." + args[1] + ".Spawn.Y");
						double z = Loader.mw.getDouble("WorldsSettings." + args[1] + ".Spawn.Z");
						float x_head = Loader.mw.getLong("WorldsSettings." + args[1] + ".Spawn.X_Pos_Head");
						float z_head = Loader.mw.getLong("WorldsSettings." + args[1] + ".Spawn.Z_Pos_Head");
						World world = Bukkit
								.getWorld(Loader.mw.getString("WorldsSettings." + args[1] + ".Spawn.World"));
						Location loc = new Location(world, x, y, z, x_head, z_head);
						API.setBack(target);
						target.teleport(loc);
						Loader.msg(Loader.s("Prefix") + Loader.s("MultiWorld.PlayerTeleportedWorld")
								.replace("%world%", args[1]).replace("%player%", target.getName()), s);
						Loader.msg(
								Loader.s("Prefix") + Loader.s("MultiWorld.TeleportedWorld").replace("%world%", args[1]),
								target);
						return true;
					}
					Loader.msg(Loader.s("Prefix") + Loader.s("MultiWorld.NotExists").replace("%world%", args[1]), s);
					return true;
				}
			}
			return true;
		}
		return false;
	}

	public List<String> worlds() {
		final List<String> list = new ArrayList<String>();
		for (final World p2 : Bukkit.getWorlds()) {
			list.add(p2.getName());
		}
		return list;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String alias,
			final String[] args) {
		final List<String> c = new ArrayList<String>();
		if (args.length == 1) {
			if (sender.hasPermission("ServerControl.MultiWorld.tp")) {
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Tp"), new ArrayList<>()));
			}

			return c;
		}
		if (args[0].equalsIgnoreCase("Tp") && sender.hasPermission("ServerControl.MultiWorld.tp")) {
			if (args.length == 2) {
				c.addAll(StringUtil.copyPartialMatches(args[1], worlds(), new ArrayList<>()));
			}
			if (args.length == 3) {
				return null;
			}
		}
		return c;
	}
}
