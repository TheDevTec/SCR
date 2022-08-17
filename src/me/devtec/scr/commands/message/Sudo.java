package me.devtec.scr.commands.message;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.spigotmc.SpigotConfig;

import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.shared.Ref;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.utility.StringUtils;

public class Sudo implements ScrCommand {

	@Override
	public void init(List<String> cmds) {
		
		//sudo [player] [thing]
		//thing:
		// 	message
		//	/cmd
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cm
				help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd"))
		.fallback((s, structure, args) -> { // sudo [player]
			offlinePlayer(s, args[0]);
		})
			.selector(Selector.ENTITY_SELECTOR, (s, structure, args) -> { //sudo [player]
				help(s, "usage");
			})
				.argument(null, (s, structure, args) -> { //sudo [player] [thing]
					for(Player p : playerSelectors(s, args[0])) {
						String thing = StringUtils.buildString(1, args);
						if(thing.startsWith("/")) { //Command
							sudo(p, SudoType.COMMAND, thing);
						}else
							sudo(p, SudoType.CHAT, thing);
						msg(s, "sudo", Placeholders.c().addPlayer("target", p).add("thing", thing));
					}
				})
		.build().register(cmds.remove(0), cmds.toArray(new String[0]));
	}

	@Override
	public String configSection() {
		return "sudo";
	}

	
	public enum SudoType {
		CHAT, COMMAND;
	}
		  
	public static void sudo(Player target, SudoType type, String value) {
		switch (type) {
		case CHAT:
			target.chat(value);
			break;
		case COMMAND:
			dispatchCommandAsync((CommandSender)target, value);
			break;
		} 
	}
		  
	public static void sudoConsole(SudoType type, String value) {
		switch (type) {
		case CHAT:
			dispatchCommandAsync(Bukkit.getConsoleSender(), "say " + value);
			break;
		case COMMAND:
			dispatchCommandAsync(Bukkit.getConsoleSender(), value);
			break;
		} 
	}
		  
	public static void sudoConsole(String value) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), value);
	}
		  
	private static CommandMap cmdMap = (CommandMap)Ref.get(Bukkit.getPluginManager(), "commandMap");
	  
	private static void dispatchCommandAsync(CommandSender sender, String cmd) {
		if (!cmdMap.dispatch(sender, cmd))
			if (Ref.getClass("org.spigotmc.SpigotConfig") != null) {
				if (!SpigotConfig.unknownCommandMessage.isEmpty())
					sender.sendMessage(SpigotConfig.unknownCommandMessage); 
			} else if (sender instanceof Player) {
				sender.sendMessage("Unknown command. Type \"/help\" for help.");
			} else {
				sender.sendMessage("Unknown command. Type \"help\" for help.");
			}  
	}
}
