package Events;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import ServerControl.Loader;
import Utils.setting;
import me.Straiker123.TheAPI;
import me.Straiker123.TheAPI.SudoType;

@SuppressWarnings("deprecation")
public class RewardsListenerChat implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void ChatListener(PlayerChatEvent e) {
		if(setting.code) {
			Player p = e.getPlayer();
			String name = p.getName();
		List<String> only = Loader.config.getStringList("Options.Codes.List");
		List<String> codes = Loader.me.getStringList("Players."+name+".Taken-Codes");
		if(!codes.isEmpty())
			for(String s : codes)only.remove(s);
		if(!only.isEmpty())
		for(String g: only) {
			if(e.getMessage().toLowerCase().contains(g.toLowerCase())) {
					Loader.msg(Loader.config.getString("Options.Codes.Message")
							.replace("%player%", name)
							.replace("%code%",g)
							.replace("%playername%", p.getDisplayName())
							.replace("%group%", Loader.getInstance.getGroup(p))
							.replace("%group-prefix%", Loader.getInstance.getPrefix(p))
							.replace("%group-suffix%", Loader.getInstance.getSuffix(p))
							.replace("%vault-group%", Loader.getInstance.getGroup(p))
							.replace("%prefix%", Loader.s("Prefix")), p);
	    		    	for(String cmds: Loader.config.getStringList("Options.Codes.Commands")) {
	    		    		TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", name)
						.replace("%code%", g)
						.replace("%playername%", p.getDisplayName())
						.replace("%group%", Loader.getInstance.getGroup(p))
						.replace("%group-prefix%", Loader.getInstance.getPrefix(p))
						.replace("%group-suffix%", Loader.getInstance.getSuffix(p))
						.replace("%vault-group%", Loader.getInstance.getGroup(p))
						.replace("%prefix%", Loader.s("Prefix")))); 
	    		    	}
	    		    	TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(TheAPI.getRandomFromList(Loader.config.getStringList("Options.Codes.Random-Command")).toString().replace("%player%", name)
	    						.replace("%code%", g)
	    						.replace("%playername%", p.getDisplayName())
	    						.replace("%group%", Loader.getInstance.getGroup(p))
	    						.replace("%group-prefix%", Loader.getInstance.getPrefix(p))
	    						.replace("%group-suffix%", Loader.getInstance.getSuffix(p))
	    						.replace("%vault-group%", Loader.getInstance.getGroup(p))
	    						.replace("%prefix%", Loader.s("Prefix"))));
				codes.add(g);
				Loader.me.set("Players."+name+".Taken-Codes", codes);
				}
		}
		}
	}
}
