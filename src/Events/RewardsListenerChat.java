package Events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import ServerControl.Loader;
import Utils.Configs;
import me.Straiker123.TheAPI;
import me.Straiker123.TheAPI.SudoType;

@SuppressWarnings("deprecation")
public class RewardsListenerChat implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void ChatListener(PlayerChatEvent e) {
		if(Loader.config.getBoolean("Codes-Rewards.Enabled")==true) {
	Player p = e.getPlayer();
		String name = p.getName();
		String msg = e.getMessage();
		ArrayList<String> words = new ArrayList<String>();
		List<String> only = Loader.config.getStringList("Codes-Rewards.Words");
		List<String> msgs = Loader.config.getStringList("Codes-Rewards.Messages");
		List<String> cmds = Loader.config.getStringList("Codes-Rewards.Commands");
		List<String> codes = Loader.me.getStringList("Players."+name+".Taken-Codes");
		if(Loader.config.getString("Codes-Rewards.Words-PerCMD")!=null) {
		for(String get: Loader.config.getConfigurationSection("Codes-Rewards.Words-PerCMD").getKeys(false)) {
			words.add(get);
		}}
		for(String g: only) {
			if(msg.contains(g)) {
				if(!codes.contains(g)) {
				for(String c : msgs) {
					Loader.msg(c
							.replace("%player%", name)
							.replace("%code%", String.valueOf(g))
							.replace("%playername%", p.getDisplayName())
							.replace("%group%", Loader.getInstance.getGroup(p))
							.replace("%group-prefix%", Loader.getInstance.getPrefix(p))
							.replace("%group-suffix%", Loader.getInstance.getSuffix(p))
							.replace("%vault-group%", Loader.getInstance.getGroup(p))
							.replace("%prefix%", Loader.s("Prefix")), p);
				}
  	        	new BukkitRunnable() {
    					@Override
    					public void run() {
	    		    	for(String cmds: cmds) {
	    		    		TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", name)
						.replace("%code%", String.valueOf(g))
						.replace("%playername%", p.getDisplayName())
						.replace("%group%", Loader.getInstance.getGroup(p))
						.replace("%group-prefix%", Loader.getInstance.getPrefix(p))
						.replace("%group-suffix%", Loader.getInstance.getSuffix(p))
						.replace("%vault-group%", Loader.getInstance.getGroup(p))
						.replace("%prefix%", Loader.s("Prefix")))); 
	    		    	}}
    				}.runTask(Loader.getInstance);{
    				}
				codes.add(g);
				Loader.me.set("Players."+name+".Taken-Codes", codes);
				Configs.chatme.save();}}
		}
		if(!words.isEmpty()) {
		for(String g: words) {
			if(msg.contains(g)) {
				if(!codes.contains(g)) {
				for(String c : Loader.config.getStringList("Codes-Rewards.Words-PerCMD."+g+".Messages")) {
					Loader.msg(c
							.replace("%player%", name)
							.replace("%code%", String.valueOf(g))
							.replace("%playername%", p.getDisplayName())
							.replace("%group%", Loader.getInstance.getGroup(p))
							.replace("%group-prefix%", Loader.getInstance.getPrefix(p))
							.replace("%group-suffix%", Loader.getInstance.getSuffix(p))
							.replace("%vault-group%", Loader.getInstance.getGroup(p))
							.replace("%prefix%", Loader.s("Prefix")), p);
				}
  	        	new BukkitRunnable() {
    					@Override
    					public void run() {
	    		    	for(String cmds: Loader.config.getStringList("Codes-Rewards.Words-PerCMD."+g+".Commands")) {
	    		    		TheAPI.sudoConsole(SudoType.COMMAND, TheAPI.colorize(cmds.replace("%player%", name)
						.replace("%code%", String.valueOf(g))
						.replace("%playername%", p.getDisplayName())
						.replace("%group%", Loader.getInstance.getGroup(p))
						.replace("%group-prefix%", Loader.getInstance.getPrefix(p))
						.replace("%group-suffix%", Loader.getInstance.getSuffix(p))
						.replace("%vault-group%", Loader.getInstance.getGroup(p))
						.replace("%prefix%", Loader.s("Prefix")))); 
	    		    	}}
    				}.runTask(Loader.getInstance);{
    				}
				codes.add(g);
				Loader.me.set("Players."+name+".Taken-Codes", codes);
				Configs.chatme.save();}}
		}
		}
		}
	}
}
