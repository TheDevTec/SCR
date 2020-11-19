package me.DevTec.ServerControlReloaded.Commands.Other;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.DevTec.ServerControlReloaded.Utils.setting;
import me.DevTec.TheAPI.TheAPI;

public class ScoreboardStats implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}

	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if (Loader.has(s, "ScoreBoard", "Other")) {
			if(args.length==0) {
				if(s instanceof Player) {
				if(me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.isToggleable((Player)s)) {
				if(me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.toggled.contains(s.getName())) {
					me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.toggled.remove(s.getName());
					Loader.sendMessages(s, "ScoreBoard.Toggle.Show.You");
				}else {
					me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.toggled.add(s.getName());
					Loader.sendMessages(s, "ScoreBoard.Toggle.Hide.You");
				}
				return true;
				}
				Loader.sendMessages(s, "ScoreBoard.Toggle.Cancelled.You");
				return true;
				}
				Loader.Help(s, "ScoreBoard", "Other");
				return true;
			}
			if(args[0].equalsIgnoreCase("reload")) {
			if (Loader.has(s, "ScoreBoard", "Other", "Reload")) {
			TheAPI.msg(setting.prefix+" Reloading Scoreboard..", s);
			me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.removeScoreboard();
			Loader.sb.reload();
			if (setting.sb)
				for (Player p : TheAPI.getOnlinePlayers())
					me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.createScoreboard(p);
			TheAPI.msg(setting.prefix+" Scoreboard reloaded", s);
			return true;
			}
			Loader.noPerms(s, "ScoreBoard", "Other", "Reload");
			return true;
			}
			if(args[0].equalsIgnoreCase("toggle")) {
				if(args.length==1) {
					if(s instanceof Player) {
						if(me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.isToggleable((Player)s)) {
						if(me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.toggled.contains(s.getName())) {
							me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.toggled.remove(s.getName());
							Loader.sendMessages(s, "ScoreBoard.Toggle.Show.You");
						}else {
							me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.toggled.add(s.getName());
							Loader.sendMessages(s, "ScoreBoard.Toggle.Hide.You");
						}
						return true;
						}
						Loader.sendMessages(s, "ScoreBoard.Toggle.Cancelled.You");
						return true;
					}
					Loader.advancedHelp(s, "ScoreBoard", "Other", "Toggle");
					return true;
				}
				Player d = TheAPI.getPlayer(args[1]);
				if(d==null) {
					Loader.notOnline(s, args[1]);
					return true;
				}
				if(d==s) {
					if(me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.isToggleable((Player)s)) {
					if(me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.toggled.contains(s.getName())) {
						me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.toggled.remove(s.getName());
						Loader.sendMessages(s, "ScoreBoard.Toggle.Show.You");
					}else {
						me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.toggled.add(s.getName());
						Loader.sendMessages(s, "ScoreBoard.Toggle.Hide.You");
					}
					return true;
					}
					Loader.sendMessages(s, "ScoreBoard.Toggle.Cancelled.You");
					return true;
				}
				if(Loader.has(s, "ScoreBoard", "Other", "ToggleOther")) {
					if(me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.isToggleable(d)) {
					if(me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.toggled.contains(d.getName())) {
						me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.toggled.remove(d.getName());
						Loader.sendMessages(s, "ScoreBoard.Toggle.Show.Other.Sender", Placeholder.c().add("%player%", d.getName()).replace("%playername%", d.getDisplayName()).replace("%customname%", d.getCustomName()));
						Loader.sendMessages(d, "ScoreBoard.Toggle.Show.Other.Receiver", Placeholder.c().add("%player%", s.getName()).replace("%playername%", s.getName()).replace("%customname%", s.getName()));
					}else {
						me.DevTec.ServerControlReloaded.Utils.ScoreboardStats.toggled.add(d.getName());
						Loader.sendMessages(s, "ScoreBoard.Toggle.Hide.Other.Sender", Placeholder.c().add("%player%", d.getName()).replace("%playername%", d.getDisplayName()).replace("%customname%", d.getCustomName()));
						Loader.sendMessages(d, "ScoreBoard.Toggle.Hide.Other.Receiver", Placeholder.c().add("%player%", s.getName()).replace("%playername%", s.getName()).replace("%customname%", s.getName()));
					}
					return true;
					}
					Loader.sendMessages(s, "ScoreBoard.Toggle.Cancelled.Other.Sender", Placeholder.c().add("%player%", d.getName()).replace("%playername%", d.getDisplayName()).replace("%customname%", d.getCustomName()));
					Loader.sendMessages(d, "ScoreBoard.Toggle.Cancelled.Other.Receiver", Placeholder.c().add("%player%", s.getName()).replace("%playername%", s.getName()).replace("%customname%", s.getName()));
					return true;
				}
				Loader.advancedHelp(s, "ScoreBoard", "Other", "ToggleOther");
				return true;
			}
		}
		Loader.noPerms(s, "ScoreBoard", "Other");
		return true;
	}
}
