package Events;

import org.bukkit.Bukkit;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import ServerControl.Loader;
import Utils.Colors;
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
		if(p.hasPermission("ServerControl.SignCreate.Warp")) {
		String l = e.getLine(0);
		String f = e.getLine(1);
		if(l.equalsIgnoreCase("[warp]")) {
			if(warp(f)!=null) {
				e.setLine(0,TheAPI.colorize("&0[&9Warp&0]"));
				e.setLine(1, TheAPI.colorize("&a"+warp(f)));
				e.getBlock().getState().update();
		}}
    }
		if(p.hasPermission("ServerControl.SignCreate.Workbench")) {
		String l = e.getLine(0);
		if(l.equalsIgnoreCase("[workbench]")) {
				e.setLine(0,TheAPI.colorize("&0[&9Workbench&0]"));
				e.getBlock().getState().update();
		}
    }
		if(p.hasPermission("ServerControl.SignCreate.EnderChest")) {
		String l = e.getLine(0);
		if(l.equalsIgnoreCase("[Enderchest]")) {
				e.setLine(0,TheAPI.colorize("&0[&9EnderChest&0]"));
				e.getBlock().getState().update();
		}
    }
		if(p.hasPermission("ServerControl.SignCreate.Suicide")) {
		String l = e.getLine(0);
		if(l.equalsIgnoreCase("[Suicide]")) {
				e.setLine(0,TheAPI.colorize("&0[&9Suicide&0]"));
				e.getBlock().getState().update();
		}
    }
		String l = e.getLine(0);
		String f = e.getLine(1);
		if(p.hasPermission("ServerControl.SignCreate.Repair")) {
		if(l.equalsIgnoreCase("[repair]")) {
    		if(f==null) {
				e.setLine(0, TheAPI.colorize("&0[&9Repair&0]"));
				e.setLine(1, TheAPI.colorize("&aHand"));
				e.getBlock().getState().update();
    		}
    		if(f.equalsIgnoreCase("Hand")) {
				e.setLine(0, TheAPI.colorize("&0[&9Repair&0]"));
				e.setLine(1, TheAPI.colorize("&aHand"));
				e.getBlock().getState().update();
		}
    		if(f.equalsIgnoreCase("All")) {
				e.setLine(0,TheAPI.colorize("&0[&9Repair&0]"));
				e.setLine(1, TheAPI.colorize("&aAll"));
				e.getBlock().getState().update();
		}}}
		if(p.hasPermission("ServerControl.SignCreate.Feed")) {
		if(l.equalsIgnoreCase("[Feed]")) {
			e.setLine(0, TheAPI.colorize("&0[&9Feed&0]"));
			e.getBlock().getState().update();
	}}
		if(p.hasPermission("ServerControl.SignCreate.Trash")) {
		if(l.equalsIgnoreCase("[Trash]")) {
			e.setLine(0, TheAPI.colorize("&0[&9Trash&0]"));
			e.getBlock().getState().update();
	}}
		if(p.hasPermission("ServerControl.SignCreate.Heal")) {
		if(l.equalsIgnoreCase("[Heal]")) {
			e.setLine(0, TheAPI.colorize("&0[&9Heal&0]"));
			e.getBlock().getState().update();
	}}}
    @EventHandler(priority = EventPriority.LOWEST)
    public void SignClick(PlayerInteractEvent e) {
    	if (e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if (e.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) e.getClickedBlock().getState();
				String s1 = Colors.remove(sign.getLine(0));
				String s2 = Colors.remove(sign.getLine(1));
		    	if(s1.equals("[Warp]")){
		    		if(warp(s2)!=null) {
		    		Bukkit.dispatchCommand(e.getPlayer(), "Warp "+s2);
		    	}}
		    	if(s1.equals(TheAPI.colorize("&0[&9Repair&0]"))){
		    		if(s2.equals("Hand"))
		    		Bukkit.dispatchCommand(e.getPlayer(), "Repair Hand");
		    		if(s2.equalsIgnoreCase("All"))
		    		Bukkit.dispatchCommand(e.getPlayer(), "Repair All");
		    	}
		    	if(s1.equals("&0[&9Heal&0]"))
		    		Bukkit.dispatchCommand(e.getPlayer(), "Heal");
		    	if(s1.equals("&0[&9Suicide&0]"))
		    		Bukkit.dispatchCommand(e.getPlayer(), "Suicide");
		    	if(s1.equals("&0[&9EnderChest&0]"))
		    		Bukkit.dispatchCommand(e.getPlayer(), "EnderChest");
		    	if(s1.equals("&0[&9Workbench&0]"))
		    		Bukkit.dispatchCommand(e.getPlayer(), "Workbench");
		    	if(s1.equals("&0[&9Trash&0]"))
		    		Bukkit.dispatchCommand(e.getPlayer(), "Trash");
		    	if(s1.equals("&0[&9Feed&0]"))
		    		Bukkit.dispatchCommand(e.getPlayer(), "Feed");
    	}}}}