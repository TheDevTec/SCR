package me.devtec.servercontrolreloaded.commands.other;

import me.devtec.servercontrolreloaded.commands.CommandsManager;
import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.HoverMessage.ClickAction;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.datakeeper.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Uuid implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Uuid", "Other") && args.length==1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Collections.emptyList();
	}

	@SuppressWarnings("unchecked")
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(Loader.has(s, "Uuid", "Other")) {
			if(!CommandsManager.canUse("Other.Uuid", s)) {
				Loader.sendMessages(s, "Cooldowns.Commands", Placeholder.c().add("%time%", StringUtils.timeToString(CommandsManager.expire("Other.Uuid", s))));
				return true;
			}
			if(args.length==0) {
				Loader.Help(s, "Uuid", "Other");
				return true;
			}
			if(s instanceof Player) {
				Player pl = TheAPI.getPlayerOrNull(args[0]);
				if (pl!=null) {
					Object o = Loader.getTranslation("Uuid.Message");
					if(o!=null) {
						if(o instanceof String) {
							StringUtils.getHoverMessage(Loader.placeholder(s, (String)o, Placeholder.c().add("%player%", pl.getName()).replace("%uuid%", pl.getUniqueId().toString()))
									).setClickEvent(ClickAction.COPY_TO_CLIPBOARD, pl.getUniqueId().toString()).send((Player)s);
						}
						if(o instanceof Collection) {
							for (String a : (Collection<String>)o) {
								StringUtils.getHoverMessage(Loader.placeholder(s, a, Placeholder.c().add("%player%", pl.getName()).replace("%uuid%", pl.getUniqueId().toString()))
										).setClickEvent(ClickAction.COPY_TO_CLIPBOARD, pl.getUniqueId().toString()).send((Player)s);
							}						
						}
					}
					return true;
				}
				Object o = Loader.getTranslation("Uuid.Message");
				User d = TheAPI.getUser(args[0]);
				String uuid = d.getUUID().toString(), name = d.getName();
				if(o!=null) {
					if(o instanceof String) {
						StringUtils.getHoverMessage(Loader.placeholder(s, (String)o, Placeholder.c().add("%player%", name).replace("%uuid%", uuid))
								).setClickEvent(ClickAction.COPY_TO_CLIPBOARD, uuid).send((Player)s);
					}
					if(o instanceof Collection) {
						for (String a : (Collection<String>)o) {
							StringUtils.getHoverMessage(Loader.placeholder(s, a, Placeholder.c().add("%player%",name).replace("%uuid%", uuid))
									).setClickEvent(ClickAction.COPY_TO_CLIPBOARD, uuid).send((Player)s);
						}						
					}
				}
				return true;
			}
			Player pl = TheAPI.getPlayerOrNull(args[0]);
			if (pl!=null) {
				Object o = Loader.getTranslation("Uuid.Message");
				if(o!=null) {
					if(o instanceof String) {
						TheAPI.msg(Loader.placeholder(s, (String)o, Placeholder.c().add("%player%", pl.getName()).replace("%uuid%", pl.getUniqueId().toString())),s);
					}
					if(o instanceof Collection) {
						for (String a : (Collection<String>)o) {
							TheAPI.msg(Loader.placeholder(s, a, Placeholder.c().add("%player%", pl.getName()).replace("%uuid%", pl.getUniqueId().toString())),s);
						}						
					}
				}
				return true;
			}
			Object o = Loader.getTranslation("Uuid.Message");
			User d = TheAPI.getUser(args[0]);
			String uuid = d.getUUID().toString(), name = d.getName();
			if(o!=null) {
				if(o instanceof String) {
					TheAPI.msg(Loader.placeholder(s, (String)o, Placeholder.c().add("%player%", name).replace("%uuid%", uuid)),s);
				}
				if(o instanceof Collection) {
					for (String a : (Collection<String>)o) {
						TheAPI.msg(Loader.placeholder(s, a, Placeholder.c().add("%player%", name).replace("%uuid%", uuid)),s);
					}						
				}
			}
			return true;
		}
		Loader.noPerms(s, "Uuid", "Other");
		return true;
	}
}
