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
	if(setting.cool_chat && 
			!p.hasPermission("ServerControl.CooldownBypass.Chat") && Loader.config.getInt("Options.Cooldowns.Chat.Time") > 0) {
	if(!s.expired(p.getName())) {
		Loader.msg(Loader.s("Prefix")+Loader.s("Cooldown.ToSendMessage")
		.replace("%timer%", TheAPI.getTimeConventorAPI().setTimeToString(s.getTimeToExpire(p.getName()))),p);
		e.setCancelled(true);
		return;
	}else
		s.createCooldown(p.getName(), Loader.config.getInt("Cooldown.Chat"));
	}}

private boolean wait(Player p, String cmd) {
	int time = Loader.config.getInt("Options.Cooldowns.Commands.Time");
	boolean find = false, exp = false;
	if(setting.cool_percmd)
	for(String s : Loader.config.getStringList("Options.Cooldowns.Commands.PerCommand.List")) {
		String[] c = s.split(":");
			if(cmd.toLowerCase().startsWith(c[0].toLowerCase())) {
				find = true;
				CooldownAPI as = TheAPI.getCooldownAPI("Cooldown.Cmds-"+c[0]);
				if(as.expired(p)) {
					exp=true;
				as.createCooldown(p, TheAPI.getStringUtils().getInt(c[1]));
				}else {
					exp=false;
					Loader.msg(Loader.s("Prefix")+Loader.s("Cooldown.ToSendCommand")
					.replace("%timer%", TheAPI.getStringUtils().setTimeToString(a.getTimeToExpire(p.getName()))),p);
				}
				break;
			}
		}
	if(!find) {
	if(!a.expired(p.getName())) {
	Loader.msg(Loader.s("Prefix")+Loader.s("Cooldown.ToSendCommand")
	.replace("%timer%", TheAPI.getStringUtils().setTimeToString(a.getTimeToExpire(p.getName()))),p);
	return true;
	}
	a.createCooldown(p.getName(), time);
	return false;
	}else {
		return exp;
	}
}

@EventHandler(priority = EventPriority.LOWEST)
public void CooldownCommands(PlayerCommandPreprocessEvent e) {
	Player p = e.getPlayer();
	if(setting.cool_cmd && 
			!p.hasPermission("ServerControl.CooldownBypass.Commands") && Loader.config.getInt("Options.Cooldowns.Commands.Time") > 0) {
	if(wait(p,e.getMessage().replaceFirst("/", ""))) {
		e.setCancelled(true);
	}else
		a.createCooldown(p.getName(), Loader.config.getInt("Cooldown.Commands"));
	}}
}