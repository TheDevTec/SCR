package Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import ServerControl.Loader;
import Utils.setting;
import me.Straiker123.CooldownAPI;
import me.Straiker123.TheAPI;

@SuppressWarnings("deprecation")
public class SecurityListenerCooldowns implements Listener {
	CooldownAPI a = TheAPI.getCooldownAPI("Cooldown.Cmds");
	CooldownAPI s = TheAPI.getCooldownAPI("Cooldown.Msgs");

	@EventHandler(priority = EventPriority.LOWEST)
	public void CooldownChat(PlayerChatEvent e) {
		Player p = e.getPlayer();
		if (setting.cool_chat && !p.hasPermission("ServerControl.CooldownBypass.Chat")
				&& Loader.config.getInt("Options.Cooldowns.Chat.Time") > 0) {
			if (!s.expired(p.getName())) {
				Loader.msg(Loader.s("Prefix") + Loader.s("Cooldown.ToSendMessage").replace("%timer%",
						TheAPI.getStringUtils().setTimeToString(s.getTimeToExpire(p.getName()))), p);
				e.setCancelled(true);
				return;
			} else
				s.createCooldown(p.getName(), Loader.config.getInt("Cooldown.Chat"));
		}
	}

	@EventHandler
	public void CooldownCommands(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPermission("ServerControl.CooldownBypass.Commands")) {
			int time = Loader.config.getInt("Options.Cooldowns.Commands.Time");
			boolean find = false;
			if (setting.cool_percmd)
				for (String s : Loader.config.getStringList("Options.Cooldowns.Commands.PerCommand.List")) {
					String[] c = s.split(":");
					if (e.getMessage().replaceFirst("/", "").toLowerCase().startsWith(c[0].toLowerCase())
							|| c[0].equalsIgnoreCase(e.getMessage().replaceFirst("/", ""))) {
						find = true;
						CooldownAPI as = TheAPI.getCooldownAPI("Cooldown.Cmds." + c[0]);
						if (as.expired(p)) {
							as.createCooldown(p, TheAPI.getStringUtils().getInt(c[1]));
						} else {
							e.setCancelled(true);
							Loader.msg(
									Loader.s("Prefix") + Loader.s("Cooldown.ToSendCommand").replace("%timer%",
											TheAPI.getStringUtils().setTimeToString(as.getTimeToExpire(p.getName()))),
									p);
						}
						break;
					}
				}
			if (!find && setting.cool_cmd && time > 0) {
				if (!a.expired(p.getName())) {
					e.setCancelled(true);
					Loader.msg(Loader.s("Prefix") + Loader.s("Cooldown.ToSendCommand").replace("%timer%",
							TheAPI.getStringUtils().setTimeToString(a.getTimeToExpire(p.getName()))), p);
				} else
					a.createCooldown(p.getName(), time);
			}
		}
	}
}