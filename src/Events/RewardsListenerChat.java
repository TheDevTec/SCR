package Events;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import com.google.common.collect.Lists;
import com.mojang.authlib.yggdrasil.response.User;

import ServerControl.Loader;
import ServerControl.Loader.Placeholder;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.TheAPI.SudoType;

@SuppressWarnings("deprecation")
public class RewardsListenerChat implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void ChatListener(PlayerChatEvent e) {
		if (setting.code) {
			Player p = e.getPlayer();
			List<String> only = Loader.config.getStringList("Options.Codes.List");
			User d = TheAPI.getUser(p);
			List<String> codes = d.exist("Taken-Codes")?d.getStringList("Taken-Codes"):Lists.newArrayList();
			if (!codes.isEmpty())
				for (String s : codes)
					only.remove(s);
			if (!only.isEmpty())
				for (String g : only) {
					if (e.getMessage().toLowerCase().contains(g.toLowerCase())) {
						TheAPI.msg(Loader.placeholder(p, Loader.config.getString("Options.Codes.Message"), Placeholder.c().add("%code%", g)),p);
						for (String cmds : Loader.config.getStringList("Options.Codes.Commands")) {
							TheAPI.sudoConsole(SudoType.COMMAND,
									TheAPI.colorize(Loader.placeholder(p, cmds, Placeholder.c().add("%code%", g))));
						}
						TheAPI.sudoConsole(SudoType.COMMAND,
								TheAPI.colorize(Loader.placeholder(p, TheAPI
										.getRandomFromList(Loader.config.getStringList("Options.Codes.Random-Command")), Placeholder.c().add("%code%", g))));
						codes.add(g);
						d.setAndSave("Taken-Codes", codes);
					}
				}
		}
	}
}
