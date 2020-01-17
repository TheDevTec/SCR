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
    private String setColors(String s, Player p) {
    	if(Loader.config.getBoolean("Colored-Chat.Enabled")==true
				&&
				Loader.config.getBoolean("Colored-Chat.Enabled-Permissions")==true
				&&
				p.hasPermission(Loader.config.getString("Colored-Chat.Permission"))
				||
				Loader.config.getBoolean("Colored-Chat.Enabled")==true
				&&
				Loader.config.getBoolean("Colored-Chat.Enabled-Permissions")==false)
		return TheAPI.colorize(s);
    	return s;
    }
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
			e.setLine(test, setColors(line,p));
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
		    		Bukkit.dispatchCommand(e.getPlayer(), "warp "+s2);
		    	}}
		    	if(s1.equals("[Repair]")){
		    		if(s2.equals("Hand"))
		    		Bukkit.dispatchCommand(e.getPlayer(), "repair hand");
		    		if(s2.equals("All"))
		    		Bukkit.dispatchCommand(e.getPlayer(), "repair all");
		    	}
		    	if(s1.equals("[Heal]"))
		    		Bukkit.dispatchCommand(e.getPlayer(), "heal");
		    	if(s1.equals("[Trash]"))
		    		Bukkit.dispatchCommand(e.getPlayer(), "trash");
		    	if(s1.equals("[Feed]"))
		    		Bukkit.dispatchCommand(e.getPlayer(), "feed");
    	}}}}