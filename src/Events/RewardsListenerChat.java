package Events;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import ServerControl.Loader;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.TheAPI.SudoType;
import me.DevTec.TheAPI.Utils.DataKeeper.User;

@SuppressWarnings("deprecation")
public class RewardsListenerChat implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void ChatListener(PlayerChatEvent e) {
		if (setting.code) {
			Player p = e.getPlayer();
			String name = p.getName();
			List<String> only = Loader.config.getStringList("Options.Codes.List");
			User d = TheAPI.getUser(p);
			List<String> codes = d.getStringList("Taken-Codes");
			if (!codes.isEmpty())
				for (String s : codes)
					only.remove(s);
			if (!only.isEmpty())
				for (String g : only) {
					if (e.getMessage().toLowerCase().contains(g.toLowerCase())) {
						TheAPI.msg(Loader.config.getString("Options.Codes.Message").replace("%player%", name)
								.replace("%code%", g).replace("%playername%", p.getDisplayName())
								.replace("%group%", Loader.getInstance.getGroup(p))
								.replace("%group-prefix%", Loader.getInstance.getPrefix(p))
								.replace("%group-suffix%", Loader.getInstance.getSuffix(p))
								.replace("%vault-group%", Loader.getInstance.getGroup(p))
								.replace("%prefix%", Loader.s("Prefix")), p);
						for (String cmds : Loader.config.getStringList("Options.Codes.Commands")) {
							TheAPI.sudoConsole(SudoType.COMMAND,
									TheAPI.colorize(cmds.replace("%player%", name).replace("%code%", g)
											.replace("%playername%", p.getDisplayName())
											.replace("%group%", Loader.getInstance.getGroup(p))
											.replace("%group-prefix%", Loader.getInstance.getPrefix(p))
											.replace("%group-suffix%", Loader.getInstance.getSuffix(p))
											.replace("%vault-group%", Loader.getInstance.getGroup(p))
											.replace("%prefix%", Loader.s("Prefix"))));
						}
						TheAPI.sudoConsole(SudoType.COMMAND,
								TheAPI.colorize(TheAPI
										.getRandomFromList(Loader.config.getStringList("Options.Codes.Random-Command"))
										.toString().replace("%player%", name).replace("%code%", g)
										.replace("%playername%", p.getDisplayName())
										.replace("%group%", Loader.getInstance.getGroup(p))
										.replace("%group-prefix%", Loader.getInstance.getPrefix(p))
										.replace("%group-suffix%", Loader.getInstance.getSuffix(p))
										.replace("%vault-group%", Loader.getInstance.getGroup(p))
										.replace("%prefix%", Loader.s("Prefix"))));
						codes.add(g);
						d.setAndSave("Taken-Codes", codes);
					}
				}
		}
	}
}
