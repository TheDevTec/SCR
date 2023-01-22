package me.devtec.scr.commands.server_managment;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.devtec.scr.Configs;
import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.commands.structures.CommandStructure;

public class Scr implements ScrCommand {

	@Override
	public void init(List<String> cmds) {

		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> {
			help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s)).permission(permission("cmd")) // perm
				.argument("reload", (s, structure, args) -> { // cmd reload - reload all configs
					msgSec(s, "reload.reloading", null);
					try {
						Configs.loadConfigs();
						msgSec(s, "reload.reload_all", null);
					} catch (Exception e) {
						msgSec(s, "reload.error_all", null);
						Loader.plugin.getLogger().warning(e.getMessage());
					}
				}).argument("config", (s, structure, args) -> { // cmd reload [config]
					String c = args[1];
					msgSec(s, "reload.reloading", null);
					try {
						Configs.reloadConfig(c);
						msgSec(s, "reload.reload", Placeholders.c().add("config", c + ".yml"));
					} catch (Exception e) {
						msgSec(s, "reload.error", Placeholders.c().add("config", c + ".yml"));
						Loader.plugin.getLogger().warning(e.getMessage());
					}

				}, "translation", "commands", "economy", "tablist", "scoreboard", "join-listener", "quit-listener", "chat", "custom_commands", "placeholders", "kits", "guis").build()
				.register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "scr";
	}

}