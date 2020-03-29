package Events;

import java.util.HashMap;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import ServerControl.Loader;
import Utils.Colors;
import me.Straiker123.SignAPI.SignAction;
import me.Straiker123.TheAPI;

public class Signs implements Listener {
	public Loader ps = Loader.getInstance;
	public String warp(String ss) {
		if(Loader.config.getString("Warps")!=null)
		for(String s:Loader.config.getConfigurationSection("Warps").getKeys(false)) {
		if(s.toLowerCase().equalsIgnoreCase(ss)) {
			return s;
		}
		}
		return null;
	}
	
    @EventHandler(priority = EventPriority.LOWEST)
    public void signCreate(SignChangeEvent e){
    	Player p = e.getPlayer();
		int test = 0;
		for(String line: e.getLines()) {
			e.setLine(test, Colors.colorize(line,true,p));
			test=test+1;
		}
		String l = e.getLine(0);
		String f = e.getLine(1);
		if(l.equalsIgnoreCase("[warp]") && p.hasPermission("ServerControl.SignCreate.Warp")) {
			if(warp(f)!=null) {
				e.setLine(0,TheAPI.colorize("&0[&9Warp&0]"));
				e.setLine(1, TheAPI.colorize("&a"+warp(f)));
				e.getBlock().getState().update();
				HashMap<SignAction, Object> a = new HashMap<SignAction, Object>();
				a.put(SignAction.PLAYER_COMMANDS, "Warp "+warp(f));
				TheAPI.getSignAPI().setActions((Sign) e.getBlock().getState(), a);
		}}
		if(l.equalsIgnoreCase("[workbench]") && p.hasPermission("ServerControl.SignCreate.Workbench")) {
				e.setLine(0,TheAPI.colorize("&0[&9Workbench&0]"));
				e.getBlock().getState().update();

				HashMap<SignAction, Object> a = new HashMap<SignAction, Object>();
				a.put(SignAction.PLAYER_COMMANDS, "Workbench");
				TheAPI.getSignAPI().setActions((Sign) e.getBlock().getState(), a);
		}
		if(l.equalsIgnoreCase("[Enderchest]") && p.hasPermission("ServerControl.SignCreate.EnderChest")) {
				e.setLine(0,TheAPI.colorize("&0[&9EnderChest&0]"));
				e.getBlock().getState().update();

				HashMap<SignAction, Object> a = new HashMap<SignAction, Object>();
				a.put(SignAction.PLAYER_COMMANDS, "Enderchest");
				TheAPI.getSignAPI().setActions((Sign) e.getBlock().getState(), a);
		}
		if(l.equalsIgnoreCase("[Suicide]") && p.hasPermission("ServerControl.SignCreate.Suicide")) {
				e.setLine(0,TheAPI.colorize("&0[&9Suicide&0]"));
				e.getBlock().getState().update();
				HashMap<SignAction, Object> a = new HashMap<SignAction, Object>();
				a.put(SignAction.PLAYER_COMMANDS, "Suicide");
				TheAPI.getSignAPI().setActions((Sign) e.getBlock().getState(), a);
		}
		if(l.equalsIgnoreCase("[repair]") && p.hasPermission("ServerControl.SignCreate.Repair")) {
    		if(f==null) {
				e.setLine(0, TheAPI.colorize("&0[&9Repair&0]"));
				e.getBlock().getState().update();
				HashMap<SignAction, Object> a = new HashMap<SignAction, Object>();
				a.put(SignAction.PLAYER_COMMANDS, "Repair Hand");
				TheAPI.getSignAPI().setActions((Sign) e.getBlock().getState(), a);
    		}
    		if(f.equalsIgnoreCase("Hand")) {
				e.setLine(0, TheAPI.colorize("&0[&9Repair&0]"));
				e.setLine(1, TheAPI.colorize("&aHand"));
				e.getBlock().getState().update();
				HashMap<SignAction, Object> a = new HashMap<SignAction, Object>();
				a.put(SignAction.PLAYER_COMMANDS, "Repair Hand");
				TheAPI.getSignAPI().setActions((Sign) e.getBlock().getState(), a);
		}
    		if(f.equalsIgnoreCase("All")) {
				e.setLine(0,TheAPI.colorize("&0[&9Repair&0]"));
				e.setLine(1, TheAPI.colorize("&aAll"));
				e.getBlock().getState().update();
				HashMap<SignAction, Object> a = new HashMap<SignAction, Object>();
				a.put(SignAction.PLAYER_COMMANDS, "Repair All");
				TheAPI.getSignAPI().setActions((Sign) e.getBlock().getState(), a);
		}}
		if(l.equalsIgnoreCase("[Feed]") && p.hasPermission("ServerControl.SignCreate.Feed")) {
			e.setLine(0, TheAPI.colorize("&0[&9Feed&0]"));
			e.getBlock().getState().update();
			HashMap<SignAction, Object> a = new HashMap<SignAction, Object>();
			a.put(SignAction.CONSOLE_COMMANDS, "Feed %player%");
			TheAPI.getSignAPI().setActions((Sign) e.getBlock().getState(), a);
	}
		if(l.equalsIgnoreCase("[Trash]")&&p.hasPermission("ServerControl.SignCreate.Trash")) {
			e.setLine(0, TheAPI.colorize("&0[&9Trash&0]"));
			e.getBlock().getState().update();
			HashMap<SignAction, Object> a = new HashMap<SignAction, Object>();
			a.put(SignAction.PLAYER_COMMANDS, "Trash");
			TheAPI.getSignAPI().setActions((Sign) e.getBlock().getState(), a);
	}
		if(l.equalsIgnoreCase("[Heal]")&&p.hasPermission("ServerControl.SignCreate.Heal")) {
			e.setLine(0, TheAPI.colorize("&0[&9Heal&0]"));
			e.getBlock().getState().update();
			HashMap<SignAction, Object> a = new HashMap<SignAction, Object>();
			a.put(SignAction.CONSOLE_COMMANDS, "Heal %player%");
			TheAPI.getSignAPI().setActions((Sign) e.getBlock().getState(), a);
	}}}