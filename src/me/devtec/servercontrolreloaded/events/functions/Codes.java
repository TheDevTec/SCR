package me.devtec.servercontrolreloaded.events.functions;


import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.scr.Loader.Placeholder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.TheAPI.SudoType;
import me.devtec.theapi.utils.datakeeper.User;
import me.devtec.theapi.utils.nms.NMSAPI;

public class Codes implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void ChatListener(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		List<String> only = Loader.config.getStringList("Options.Codes.List");
		User d = TheAPI.getUser(p);
		List<String> codes = d.getStringList("Taken-Codes");
		for (String s : codes)
			only.remove(s);
		for (String g : only) {
			if (e.getMessage().toLowerCase().contains(g.toLowerCase())) {
				TheAPI.msg(Loader.placeholder(p, Loader.config.getString("Options.Codes.Message"), Placeholder.c().add("%code%", g)),p);
				NMSAPI.postToMainThread(() -> {
					for (String cmds : Loader.config.getStringList("Options.Codes.Commands"))
						TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(Loader.placeholder(p, cmds, Placeholder.c().add("%code%", g))));
					String random = TheAPI.getRandomFromList(Loader.config.getStringList("Options.Codes.Random-Command"));
					if(random!=null && !random.trim().isEmpty())
						TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(Loader.placeholder(p, random, Placeholder.c().add("%code%", g))));
				});
				codes.add(g);
			}
		}
		d.setAndSave("Taken-Codes", codes);
	}
}
