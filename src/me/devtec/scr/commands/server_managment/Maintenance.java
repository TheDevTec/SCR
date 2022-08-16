package me.devtec.scr.commands.server_managment;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.API;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;

public class Maintenance implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			boolean status = Loader.data.getBoolean("maintenance");
			msgSec(s, "info", Placeholders.c().add("status", status?"enabled":"disabled"));
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd")) // perm
		.selector(Selector.BOOLEAN, (s, structure, args) -> {
			boolean b = Boolean.parseBoolean(args[0]);
			if(b) { //Enable
				Loader.data.set("maintenance", b);
				Loader.data.save();
				MessageUtils.message(s, configSection()+".enable", 
						Placeholders.c().addPlayer("player", s), API.getOnlinePlayersWith(permission("cmd")).toArray(new CommandSender[0]) );
			} else { //Disable
				Loader.data.set("maintenance", b);
				Loader.data.save();
				MessageUtils.message(s, configSection()+".disable", Placeholders.c().addPlayer("player", s), API.getOnlinePlayersWith(permission("cmd")).toArray(new CommandSender[0]) );
			}
		})
		.parent()
		.argument("on", (s, structure, args) -> {
			Loader.data.set("maintenance", true);
			Loader.data.save();
			MessageUtils.message(s, configSection()+".enable", 
					Placeholders.c().addPlayer("player", s), API.getOnlinePlayersWith(permission("cmd")).toArray(new CommandSender[0]) );
		})
		.parent()
		.argument("off", (s, structure, args) -> {
			Loader.data.set("maintenance", false);
			Loader.data.save();
			MessageUtils.message(s, configSection()+".disable", 
					Placeholders.c().addPlayer("player", s), API.getOnlinePlayersWith(permission("cmd")).toArray(new CommandSender[0]) );
		})
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "maintenance";
	}
}