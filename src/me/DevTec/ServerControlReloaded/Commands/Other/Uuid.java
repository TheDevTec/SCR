package me.DevTec.ServerControlReloaded.Commands.Other;

import java.util.Collection;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.DevTec.ServerControlReloaded.SCR.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.HoverMessage.ClickAction;
import me.devtec.theapi.utils.StringUtils;

public class Uuid implements CommandExecutor, TabCompleter {

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1,
			String arg2, String[] arg3) {
		return null;
	}

	@SuppressWarnings("unchecked")
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		Player p = (Player) sender;
		if(Loader.has(p, "Uuid", "Other")) {
		if(args.length==0) {
			Loader.Help(p, "Uuid", "Other");
			return true;
		}
		if (args.length==1) {
			Player pl = TheAPI.getPlayerOrNull(args[0]);
			if (pl!=null) {
				Object o = Loader.getTranslation("Uuid.Message");
				if(o!=null) {
					if(o instanceof String) {
						StringUtils.getHoverMessage(Loader.placeholder(sender, (String)o, Placeholder.c().add("%player%", pl.getName()).replace("%uuid%", pl.getUniqueId().toString()))
								).setClickEvent(ClickAction.COPY_TO_CLIPBOARD, pl.getUniqueId().toString()).send(p);
					}
					if(o instanceof Collection) {
						for (String a : (Collection<String>)o) {
							StringUtils.getHoverMessage(Loader.placeholder(sender, (String)a, Placeholder.c().add("%player%", pl.getName()).replace("%uuid%", pl.getUniqueId().toString()))
									).setClickEvent(ClickAction.COPY_TO_CLIPBOARD, pl.getUniqueId().toString()).send(p);
						}						
					}
				}
				return true;
			} else {
				Object o = Loader.getTranslation("Uuid.Message");
				String name = args[0];
				String uuid = TheAPI.getUser(args[0]).getUUID().toString();
				if(o!=null) {
					if(o instanceof String) {
						StringUtils.getHoverMessage(Loader.placeholder(sender, (String)o, Placeholder.c().add("%player%", name).replace("%uuid%", uuid))
								).setClickEvent(ClickAction.COPY_TO_CLIPBOARD, uuid).send(p);
					}
					if(o instanceof Collection) {
						for (String a : (Collection<String>)o) {
							StringUtils.getHoverMessage(Loader.placeholder(sender, (String)a, Placeholder.c().add("%player%",name).replace("%uuid%", uuid))
									).setClickEvent(ClickAction.COPY_TO_CLIPBOARD, uuid).send(p);
						}						
					}
				}
				return true;
			}}
		Loader.noPerms(p, "Uuid", "Other");
		}
	return true;
	}
}
