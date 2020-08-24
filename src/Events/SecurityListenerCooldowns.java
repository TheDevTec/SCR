package Events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import ServerControl.Loader;
import Utils.setting;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.CooldownAPI.CooldownAPI;
import me.DevTec.TheAPI.Utils.StringUtils;

@SuppressWarnings("deprecation")
public class SecurityListenerCooldowns implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void CooldownChat(PlayerChatEvent e) {
		Player p = e.getPlayer();
		if (setting.cool_chat && !p.hasPermission("ServerControl.CooldownBypass.Chat")
				&& Loader.config.getInt("Options.Cooldowns.Chat.Time") > 0) {
			CooldownAPI s = TheAPI.getCooldownAPI(p.getName());
			if (!s.expired("Cooldown.Msgs")) {
				TheAPI.msg(Loader.s("Prefix") + Loader.s("Cooldown.ToSendMessage").replace("%timer%",
						StringUtils.setTimeToString(s.getTimeToExpire("Cooldown.Msgs"))), p);
				e.setCancelled(true);
				return;
			} else
				s.createCooldown("Cooldown.Msgs", Loader.config.getInt("Cooldown.Chat"));
		}
	}

	@EventHandler
	public void CooldownCommands(PlayerCommandPreprocessEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPermission("ServerControl.CooldownBypass.Commands")) {
			int time = Loader.config.getInt("Options.Cooldowns.Commands.Time");
			boolean find = false;
			CooldownAPI as = TheAPI.getCooldownAPI(p.getName());
			if (setting.cool_percmd)
				for (String s : Loader.config.getStringList("Options.Cooldowns.Commands.PerCommand.List")) {
					String[] c = s.split(":");
					if (e.getMessage().replaceFirst("/", "").toLowerCase().startsWith(c[0].toLowerCase())
							|| c[0].equalsIgnoreCase(e.getMessage().replaceFirst("/", ""))) {
						find = true;
						if (as.expired("Cooldown.Cmds." + c[0])) {
							as.createCooldown("Cooldown.Cmds." + c[0], StringUtils.getInt(c[1]));
						} else {
							e.setCancelled(true);
							TheAPI.msg(
									Loader.s("Prefix") + Loader.s("Cooldown.ToSendCommand").replace("%timer%",
											StringUtils.setTimeToString(as.getTimeToExpire("Cooldown.Cmds." + c[0]))),p);
						}
						break;
					}
				}
			if (!find && setting.cool_cmd && time > 0) {
				if (!as.expired("Cooldown.Cmdss")) {
					e.setCancelled(true);
					TheAPI.msg(Loader.s("Prefix") + Loader.s("Cooldown.ToSendCommand").replace("%timer%",
							StringUtils.setTimeToString(as.getTimeToExpire("Cooldown.Cmdss"))), p);
				} else
					as.createCooldown("Cooldown.Cmdss", time);
			}
		}
	}
}