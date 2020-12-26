package me.DevTec.ServerControlReloaded.Commands.Other;


import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.Repeat;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.collections.UnsortedList;
import me.devtec.theapi.utils.reflections.Ref;

public class Exp implements CommandExecutor, TabCompleter {
	/*
	 * public ExpMethod() { aliases.put("give", Arrays.asList("give", "add"));
	 * aliases.put("take", Arrays.asList("take", "del", "delete", "remove"));
	 * aliases.put("balance", Arrays.asList("bal", "balance")); aliases.put("set",
	 * Arrays.asList("set")); }
	 */

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (args.length == 0) {
			Loader.Help(s, "Experiences", "Other");
			return true;
		}
		if (args[0].equalsIgnoreCase("set")) {
			if (Loader.has(s, "Experiences", "Other", "Set")) {
				if (args.length == 1 || args.length == 2) {
					Loader.advancedHelp(s, "Experiences", "Other", "Set");
					return true;
				}
				Player p = TheAPI.getPlayer(args[1]);
				if (args.length == 3) {
					if (p == null) {
						if (args[1].equals("*")) {
							Repeat.a(s, "xp set * " + StringUtils.getFloat(args[2]));
							return true;
						}
						Loader.notOnline(s, args[1]);
						return true;
					}
					p.giveExp(-p.getTotalExperience());
					int add = StringUtils.getInt(args[2]);
					p.giveExp(add > 0 ? add : 0);
					Loader.sendMessages(s, "Experiences.Set",
							Placeholder.c().replace("%player%", p.getName()).replace("%playername%", p.getDisplayName())
									.replace("%amount%", "" + add)
									.replace("%type%", Loader.getTranslation("Experiences.Words.Exp").toString()));
					return true;
				} // else ==4
				if (p == null) {
					if (args[1].equals("*")) {
						Repeat.a(s, "xp set * " + StringUtils.getFloat(args[2]) + " " + args[3]);
						return true;
					}
					Loader.notOnline(s, args[1]);
					return true;
				}
				if (args[3].toLowerCase().contains("level")) {
					int add = StringUtils.getInt(args[2]);
					p.setLevel(add > 0 ? add : 0);
					Loader.sendMessages(s, "Experiences.Set",
							Placeholder.c().replace("%player%", p.getName()).replace("%playername%", p.getDisplayName())
									.replace("%amount%", "" + StringUtils.getFloat(args[2]))
									.replace("%type%", Loader.getTranslation("Experiences.Words.Level").toString()));

				} else {
					p.giveExp(-p.getTotalExperience());
					int add = StringUtils.getInt(args[2]);
					p.giveExp(add>0?add:0);
					Loader.sendMessages(s, "Experiences.Set",
							Placeholder.c().replace("%player%", p.getName()).replace("%playername%", p.getDisplayName())
									.replace("%amount%", "" + add)
									.replace("%type%", Loader.getTranslation("Experiences.Words.Exp").toString()));
				}
				return true;
			}
			Loader.noPerms(s, "Experiences", "Other", "Set");
			return true;
		}
		if (args[0].equalsIgnoreCase("Balance")) { //TODO - předělat xp zprávu na to aby to poukazovalo na počet xp z XP baru (0-1) a ne celkově 
			if (Loader.has(s, "Experiences", "Other", "Balance")) {
				if (args.length == 1) {
					Loader.advancedHelp(s, "Experiences", "Other", "Balance");
					return true;
				}
				Player p = TheAPI.getPlayer(args[1]);
				if (p == null) {
					Loader.notOnline(s, args[1]);
					return true;
				}
				Loader.sendMessages(s, "Experiences.Balance",
						Placeholder.c().replace("%player%", p.getName()).replace("%playername%", p.getDisplayName())
								.replace("%amount%",
										"" + ((args.length >= 3 ? args[2].toLowerCase().contains("level") : false)
												? p.getLevel()
												: Ref.get(Ref.player(p),"expTotal")))
								.replace("%type%",
										Loader.getTranslation("Experiences.Words."
												+ (((args.length >= 3 ? args[2].toLowerCase().contains("level") : false)
														? "Level"
														: "Exp")))
												.toString()));
				return true;
			}
			Loader.noPerms(s, "Experiences", "Other", "Balance");
			return true;
		}
		if (args[0].equalsIgnoreCase("give")) {
			if (Loader.has(s, "Experiences", "Other", "Add")) {
				if (args.length == 1 || args.length == 2) {
					Loader.advancedHelp(s, "Experiences", "Other", "Give");
					return true;
				}
				Player p = TheAPI.getPlayer(args[1]);
				if (args.length == 3) {
					if (p == null) {
						if (args[0].equals("*")) {
							Repeat.a(s, "xp add * " + StringUtils.getFloat(args[2]));
							return true;
						}
						Loader.notOnline(s, args[1]);
						return true;
					}
					int add = StringUtils.getInt(args[2]);
					p.giveExp(add > 0 ? add : 0);
					Loader.sendMessages(s, "Experiences.Given",
							Placeholder.c().replace("%player%", p.getName()).replace("%playername%", p.getDisplayName())
									.replace("%amount%", "" + StringUtils.getFloat(args[2]))
									.replace("%type%", Loader.getTranslation("Experiences.Words.Exp").toString()));
					return true;
				}
				if (p == null) {
					if (args[0].equals("*")) {
						Repeat.a(s, "xp add * " + StringUtils.getFloat(args[2]) + " " + args[3]);
						return true;
					}
					Loader.sendMessages(s, "Missing.Player.Offline",
							Placeholder.c().add("%player%", args[1]).add("%playername%", args[1]));
					return true;
				}
				if (args[3].toLowerCase().contains("level")) {
					int add = p.getLevel() + StringUtils.getInt(args[2]);
					p.setLevel(add > 0 ? add : 0);
					Loader.sendMessages(s, "Experiences.Given",
							Placeholder.c().replace("%player%", p.getName()).replace("%playername%", p.getDisplayName())
									.replace("%amount%", "" + StringUtils.getFloat(args[2]))
									.replace("%type%", Loader.getTranslation("Experiences.Words.Level").toString()));
				} else {
					int add = StringUtils.getInt(args[2]);
					p.giveExp(add > 0 ? add : 0);
					Loader.sendMessages(s, "Experiences.Given",
							Placeholder.c().replace("%player%", p.getName()).replace("%playername%", p.getDisplayName())
									.replace("%amount%", "" + StringUtils.getFloat(args[2]))
									.replace("%type%", Loader.getTranslation("Experiences.Words.Exp").toString()));
				}
				return true;
			}
			Loader.noPerms(s, "Experiences", "Other", "Give");
			return true;
		}
		if (args[0].equalsIgnoreCase("take")) {
			if (Loader.has(s, "Experiences", "Other", "Remove")) {
				if (args.length == 1 || args.length == 2) {
					Loader.advancedHelp(s, "Experiences", "Other", "Take");
					return true;
				}
				Player p = TheAPI.getPlayer(args[1]);
				if (args.length == 3) {
					if (p == null) {
						if (args[1].equals("*")) {
							Repeat.a(s, "xp remove * " + StringUtils.getFloat(args[2]));
							return true;
						}
						Loader.notOnline(s, args[1]);
						return true;
					}
					int take = p.getTotalExperience() -StringUtils.getInt(args[2]);
					p.giveExp(take < 0 ? 0 : -StringUtils.getInt(args[2]));
					Loader.sendMessages(s, "Experiences.Taken",
							Placeholder.c().replace("%player%", p.getName()).replace("%playername%", p.getDisplayName())
									.replace("%amount%", "" + StringUtils.getFloat(args[2]))
									.replace("%type%", Loader.getTranslation("Experiences.Words.Exp").toString()));
					return true;
				}
				if (p == null) {
					if (args[1].equals("*")) {
						Repeat.a(s, "xp remove * " + StringUtils.getFloat(args[2]) + " " + args[3]);
						return true;
					}
					Loader.notOnline(s, args[1]);
					return true;
				}
				if (args[3].toLowerCase().contains("level")) {
					int take = p.getLevel() - StringUtils.getInt(args[2]);
					p.setLevel(take < 0 ? 0 : take);
					Loader.sendMessages(s, "Experiences.Taken",
							Placeholder.c().replace("%player%", p.getName()).replace("%playername%", p.getDisplayName())
									.replace("%amount%", "" + StringUtils.getFloat(args[2]))
									.replace("%type%", Loader.getTranslation("Experiences.Words.Level").toString()));
				} else {
					int take = p.getTotalExperience() -StringUtils.getInt(args[2]);
					p.giveExp(take < 0 ? 0 : -StringUtils.getInt(args[2]));
					Loader.sendMessages(s, "Experiences.Taken",
							Placeholder.c().replace("%player%", p.getName()).replace("%playername%", p.getDisplayName())
									.replace("%amount%", "" + StringUtils.getFloat(args[2]))
									.replace("%type%", Loader.getTranslation("Experiences.Words.Exp").toString()));
				}
				return true;
			}
			Loader.noPerms(s, "Experiences", "Other", "Remove");
			return true;
		}
		Loader.Help(s, "Experiences", "Other");
		return true;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] args) {
		List<String> c = new UnsortedList<>();
		
		if (args.length == 1) {
			c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Set", "Give", "Take", "Balance"), new UnsortedList<>()));
		}
		
		if (args.length == 3) { //0 1 2 3 4
			c.addAll(StringUtil.copyPartialMatches(args[0], Arrays.asList("Set", "Give", "Take", "Balance"), new UnsortedList<>()));
		}
		return c;
	}
	
}
