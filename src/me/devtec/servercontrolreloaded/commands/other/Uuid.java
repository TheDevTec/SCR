package me.devtec.servercontrolreloaded.commands.other;

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
import java.util.List;

public class Uuid implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender s, Command arg1,
			String arg2, String[] args) {
		if(Loader.has(s, "Uuid", "Other") && args.length==1)
			return StringUtils.copyPartialMatches(args[0], API.getPlayerNames(s));
		return Arrays.asList();
	}

	@SuppressWarnings("unchecked")
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(Loader.has(sender, "Uuid", "Other")) {
			if(args.length==0) {
				Loader.Help(sender, "Uuid", "Other");
				return true;
			}
			if(sender instanceof Player) {
				Player pl = TheAPI.getPlayerOrNull(args[0]);
				if (pl!=null) {
					Object o = Loader.getTranslation("Uuid.Message");
					if(o!=null) {
						if(o instanceof String) {
							StringUtils.getHoverMessage(Loader.placeholder(sender, (String)o, Placeholder.c().add("%player%", pl.getName()).replace("%uuid%", pl.getUniqueId().toString()))
									).setClickEvent(ClickAction.COPY_TO_CLIPBOARD, pl.getUniqueId().toString()).send((Player)sender);
						}
						if(o instanceof Collection) {
							for (String a : (Collection<String>)o) {
								StringUtils.getHoverMessage(Loader.placeholder(sender, (String)a, Placeholder.c().add("%player%", pl.getName()).replace("%uuid%", pl.getUniqueId().toString()))
										).setClickEvent(ClickAction.COPY_TO_CLIPBOARD, pl.getUniqueId().toString()).send((Player)sender);
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
						StringUtils.getHoverMessage(Loader.placeholder(sender, (String)o, Placeholder.c().add("%player%", name).replace("%uuid%", uuid))
								).setClickEvent(ClickAction.COPY_TO_CLIPBOARD, uuid).send((Player)sender);
					}
					if(o instanceof Collection) {
						for (String a : (Collection<String>)o) {
							StringUtils.getHoverMessage(Loader.placeholder(sender, (String)a, Placeholder.c().add("%player%",name).replace("%uuid%", uuid))
									).setClickEvent(ClickAction.COPY_TO_CLIPBOARD, uuid).send((Player)sender);
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
						TheAPI.msg(Loader.placeholder(sender, (String)o, Placeholder.c().add("%player%", pl.getName()).replace("%uuid%", pl.getUniqueId().toString())),sender);
					}
					if(o instanceof Collection) {
						for (String a : (Collection<String>)o) {
							TheAPI.msg(Loader.placeholder(sender, a, Placeholder.c().add("%player%", pl.getName()).replace("%uuid%", pl.getUniqueId().toString())),sender);
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
					TheAPI.msg(Loader.placeholder(sender, (String)o, Placeholder.c().add("%player%", name).replace("%uuid%", uuid)),sender);
				}
				if(o instanceof Collection) {
					for (String a : (Collection<String>)o) {
						TheAPI.msg(Loader.placeholder(sender, a, Placeholder.c().add("%player%", name).replace("%uuid%", uuid)),sender);
					}						
				}
			}
			return true;
		}
		Loader.noPerms(sender, "Uuid", "Other");
		return true;
	}
}
