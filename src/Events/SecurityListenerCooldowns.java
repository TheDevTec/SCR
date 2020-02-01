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
@EventHandler(priority = EventPriority.LOWEST)
public void CooldownCommands(PlayerCommandPreprocessEvent e) {
	Player p = e.getPlayer();
	if(setting.cool_cmd && 
			!p.hasPermission("ServerControl.CooldownBypass.Commands") && Loader.config.getInt("Options.Cooldowns.Commands.Time") > 0) {
	
	if(!a.expired(p.getName())) {
		Loader.msg(Loader.s("Prefix")+Loader.s("Cooldown.ToSendCommand")
		.replace("%timer%", TheAPI.getTimeConventorAPI().setTimeToString(a.getTimeToExpire(p.getName()))),p);
		e.setCancelled(true);
		return;
	}else
		a.createCooldown(p.getName(), Loader.config.getInt("Cooldown.Commands"));
	}
}

}