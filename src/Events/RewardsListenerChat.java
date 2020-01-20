package Events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import ServerControl.Loader;
import Utils.Configs;
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
		String msg = e.getMessage();
		List<String> only = Loader.config.getStringList("Options.Codes.List");
		if(Loader.me.getString("Players."+name+".Taken-Codes")!=null)
		only.removeAll(Loader.me.getStringList("Players."+name+".Taken-Codes"));
		List<String> codes = Loader.me.getStringList("Players."+name+".Taken-Codes");
		for(String g: only) {
			if(msg.contains(g)) {
					Loader.msg(Loader.config.getString("Options.Codes.Message")
							.replace("%player%", name)
							.replace("%code%", String.valueOf(g))
							.replace("%playername%", p.getDisplayName())
							.replace("%group%", Loader.getInstance.getGroup(p))
							.replace("%group-prefix%", Loader.getInstance.getPrefix(p))
							.replace("%group-suffix%", Loader.getInstance.getSuffix(p))
							.replace("%vault-group%", Loader.getInstance.getGroup(p))
							.replace("%prefix%", Loader.s("Prefix")), p);
	    		    	for(String cmds: Loader.config.getStringList("Options.Codes.Commands")) {
	    		    		TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", name)
						.replace("%code%", String.valueOf(g))
						.replace("%playername%", p.getDisplayName())
						.replace("%group%", Loader.getInstance.getGroup(p))
						.replace("%group-prefix%", Loader.getInstance.getPrefix(p))
						.replace("%group-suffix%", Loader.getInstance.getSuffix(p))
						.replace("%vault-group%", Loader.getInstance.getGroup(p))
						.replace("%prefix%", Loader.s("Prefix")))); 
	    		    	}
	    		    	List<Object> list = new ArrayList<Object>();
	    		    	for(String s : Loader.config.getStringList("Options.Codes.Random-Command"))list.add(s);
	    		    	TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(TheAPI.getRandomFromList(list).toString().replace("%player%", name)
	    						.replace("%code%", String.valueOf(g))
	    						.replace("%playername%", p.getDisplayName())
	    						.replace("%group%", Loader.getInstance.getGroup(p))
	    						.replace("%group-prefix%", Loader.getInstance.getPrefix(p))
	    						.replace("%group-suffix%", Loader.getInstance.getSuffix(p))
	    						.replace("%vault-group%", Loader.getInstance.getGroup(p))
	    						.replace("%prefix%", Loader.s("Prefix"))));
				codes.add(g);
				Loader.me.set("Players."+name+".Taken-Codes", codes);
				Configs.chatme.save();}
		}
		}
	}
}
