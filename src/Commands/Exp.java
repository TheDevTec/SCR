package Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Repeat;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

public class Exp implements CommandExecutor, TabCompleter {
	public Exp() {
		aliases.put("give", Arrays.asList("give", "add"));
		aliases.put("take", Arrays.asList("take", "del", "delete", "remove"));
		aliases.put("balance", Arrays.asList("bal", "balance"));
		aliases.put("set", Arrays.asList("set"));
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (args.length == 0) {
			if (s.hasPermission("servercontrol.xp.give"))
				Loader.Help(s, "/Xp Give <player> <amount> <xp/level>", "Xp.Give");
			if (s.hasPermission("servercontrol.xp.take"))
				Loader.Help(s, "/Xp Take <player> <amount> <xp/level>", "Xp.Take");
			if (s.hasPermission("servercontrol.xp.balance"))
				Loader.Help(s, "/Xp Balance <player> <xp/level>", "Xp.Balance");
			if (s.hasPermission("servercontrol.xp.set"))
				Loader.Help(s, "/Xp Set <player> <xp/level>", "Xp.Set");
			return true;
		}
		if (isAlias(args[0], "set")) {
			if (API.hasPerm(s, "servercontrol.xp.set")) {
				if (args.length == 1 || args.length == 2) {
					Loader.Help(s, "/Xp Set <player> <amount> <xp/level>", "Xp.Set");
					return true;
				}
				Player p = TheAPI.getPlayer(args[1]);
				if(args.length==3) {
					if (p == null) {
						if (args[0].equals("*")) {
							Repeat.a(s, "xp set * " + StringUtils.getInt(args[2]));
							return true;
						}
						TheAPI.msg(Loader.PlayerNotOnline(args[1]), s);
						return true;
					}
					float add = StringUtils.getInt(args[2]);
					p.setExp(add>0?add:0);
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Xp.Taken").replace("%player%", p.getName())
							.replace("%playername%", p.getDisplayName())
							.replace("%amount%", "" + StringUtils.getInt(args[2])), s);
					return true;
					}
					if (p == null) {
						if (args[0].equals("*")) {
							Repeat.a(s, "xp set * " + StringUtils.getInt(args[2])+" "+args[3]);
							return true;
						}
						TheAPI.msg(Loader.PlayerNotOnline(args[1]), s);
						return true;
					}
					if(args[3].toLowerCase().contains("level")) {
						int add = StringUtils.getInt(args[2]);
						p.setLevel(add>0?add:0);
					}else {
						float add = StringUtils.getInt(args[2]);
						p.setExp(add>0?add:0);
					}
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Xp.Set").replace("%player%", p.getName())
							.replace("%playername%", p.getDisplayName())
							.replace("%amount%", "" + StringUtils.getInt(args[2])), s);
					return true;
			}
			return true;
		}
		if (isAlias(args[0], "balance")) {
			if (API.hasPerm(s, "servercontrol.xp.balance")) {
				if (args.length == 1) {
					Loader.Help(s, "/Xp Balance <player> <xp/level>", "Xp.Balance");
					return true;
				}
				Player p = TheAPI.getPlayer(args[1]);
				if (p == null) {
					TheAPI.msg(Loader.PlayerNotOnline(args[1]), s);
					return true;
				}
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Xp.Balance").replace("%player%", p.getName())
						.replace("%playername%", p.getDisplayName())
						.replace("%amount%", "" +((args.length>=3?args[2].toLowerCase().contains("level"):false)?p.getLevel(): p.getExp())), s);
				return true;
			}
			return true;
		}
		if (isAlias(args[0], "give")) {
			if (API.hasPerm(s, "servercontrol.xp.give")) {
				if (args.length == 1 || args.length == 2) {
					Loader.Help(s, "/Xp Give <player> <amount> <xp/level>", "Xp.Give");
					return true;
				}
				Player p = TheAPI.getPlayer(args[1]);
				if(args.length==3) {
				if (p == null) {
					if (args[0].equals("*")) {
						Repeat.a(s, "xp give * " + StringUtils.getInt(args[2]));
						return true;
					}
					TheAPI.msg(Loader.PlayerNotOnline(args[1]), s);
					return true;
				}
				float add = p.getExp()+StringUtils.getInt(args[2]);
				p.setExp(add>0?add:0);
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Xp.Taken").replace("%player%", p.getName())
						.replace("%playername%", p.getDisplayName())
						.replace("%amount%", "" + StringUtils.getInt(args[2])), s);
				return true;
				}
				if (p == null) {
					if (args[0].equals("*")) {
						Repeat.a(s, "xp give * " + StringUtils.getInt(args[2])+" "+args[3]);
						return true;
					}
					TheAPI.msg(Loader.PlayerNotOnline(args[1]), s);
					return true;
				}
				if(args[3].toLowerCase().contains("level")) {
					int add = p.getLevel()+StringUtils.getInt(args[2]);
					p.setLevel(add>0?add:0);
				}else {
					float add = p.getExp()+StringUtils.getInt(args[2]);
					p.setExp(add>0?add:0);
				}
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Xp.Given").replace("%player%", p.getName())
						.replace("%playername%", p.getDisplayName())
						.replace("%amount%", "" + StringUtils.getInt(args[2])), s);
				return true;
		}return true;}
		if (isAlias(args[0], "take")) {
			if (API.hasPerm(s, "servercontrol.xp.take")) {
				if (args.length == 1 || args.length == 2) {
					Loader.Help(s, "/Xp Take <player> <amount> <xp/level>", "Xp.Take");
					return true;
				}
				Player p = TheAPI.getPlayer(args[1]);
				if(args.length==3) {
				if (p == null) {
					if (args[0].equals("*")) {
						Repeat.a(s, "xp take * " + StringUtils.getInt(args[2]));
						return true;
					}
					TheAPI.msg(Loader.PlayerNotOnline(args[1]), s);
					return true;
				}
				float take = p.getExp()-StringUtils.getInt(args[2]);
				p.setExp(take<0?0:take);
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Xp.Taken").replace("%player%", p.getName())
						.replace("%playername%", p.getDisplayName())
						.replace("%amount%", "" + StringUtils.getInt(args[2])), s);
				return true;
				}
				if (p == null) {
					if (args[0].equals("*")) {
						Repeat.a(s, "xp take * " + StringUtils.getInt(args[2])+" "+args[3]);
						return true;
					}
					TheAPI.msg(Loader.PlayerNotOnline(args[1]), s);
					return true;
				}
				if(args[3].toLowerCase().contains("level")) {
					int take = p.getLevel()-StringUtils.getInt(args[2]);
					p.setLevel(take<0?0:take);
				}else {
					float take = p.getExp()-StringUtils.getInt(args[2]);
					p.setExp(take<0?0:take);
				}
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Xp.Taken").replace("%player%", p.getName())
						.replace("%playername%", p.getDisplayName())
						.replace("%amount%", "" + StringUtils.getInt(args[2])), s);
				return true;
			}
			return true;
		}
		if (s.hasPermission("servercontrol.xp.give"))
			Loader.Help(s, "/Xp Give <player> <amount> <xp/level>", "Xp.Give");
		if (s.hasPermission("servercontrol.xp.take"))
			Loader.Help(s, "/Xp Take <player> <amount> <xp/level>", "Xp.Take");
		if (s.hasPermission("servercontrol.xp.balance"))
			Loader.Help(s, "/Xp Balance <player> <xp/level>", "Xp.Balance");
		if (s.hasPermission("servercontrol.xp.set"))
			Loader.Help(s, "/Xp Set <player> <xp/level>", "Xp.Set");
		return true;
	}

	HashMap<String, List<String>> aliases = new HashMap<String, List<String>>();

	boolean isAlias(String a, String b) {
		boolean bol = false;
		for (String s : aliases.get(b))
			if (s.equalsIgnoreCase(a)) {
				bol = true;
				break;
			}
		return bol;
	}

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1, String arg2, String[] args) {
		List<String> c = new ArrayList<>();
		if (args.length == 1) {
			if (s.hasPermission("servercontrol.xp.balance"))
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Balance"), new ArrayList<>()));
			if (s.hasPermission("servercontrol.xp.give"))
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Give"), new ArrayList<>()));
			if (s.hasPermission("servercontrol.xp.take"))
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Take"), new ArrayList<>()));
			if (s.hasPermission("servercontrol.xp.set"))
				c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Set"), new ArrayList<>()));
		}
		if (isAlias(args[0], "balance") && s.hasPermission("servercontrol.xp.balance")
				|| isAlias(args[0], "take") && s.hasPermission("servercontrol.xp.take")
				|| isAlias(args[0], "give") && s.hasPermission("servercontrol.xp.give")
				|| isAlias(args[0], "set") && s.hasPermission("servercontrol.xp.set")) {
			if (args.length == 2) {
				return null;
			}
			if (args.length == 3) {
				if (isAlias(args[0], "take") && s.hasPermission("servercontrol.xp.take")
						|| isAlias(args[0], "give") && s.hasPermission("servercontrol.xp.give")
						|| isAlias(args[0], "set") && s.hasPermission("servercontrol.xp.set"))
					c.addAll(StringUtil.copyPartialMatches(args[2], Arrays.asList("?"), new ArrayList<>()));
			}
		}
		return c;
	}

}
