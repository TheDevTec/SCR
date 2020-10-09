package Events;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

import ServerControl.Loader;
import Utils.Colors;
import me.DevTec.TheAPI.APIs.SignAPI;
import me.DevTec.TheAPI.APIs.SignAPI.SignAction;
import me.DevTec.TheAPI.TheAPI;

public class Signs implements Listener {
	public Loader ps = Loader.getInstance;

	public String warp(String ss) {
		if (Loader.config.getString("Warps") != null)
			for (String s : Loader.config.getKeys("Warps")) {
				if (s.toLowerCase().equalsIgnoreCase(ss)) {
					return s;
				}
			}
		return null;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void signCreate(SignChangeEvent e) {
		Player p = e.getPlayer();
		int test = 0;
		for (String line : e.getLines()) {
			e.setLine(test, Colors.colorize(line, true, p));
			test = test + 1;
		}
		String l = e.getLine(0);
		String f = e.getLine(1);
		HashMap<SignAction, List<String>> a = new HashMap<SignAction, List<String>>();
		if (l.equalsIgnoreCase("[warp]") && p.hasPermission("ServerControl.SignCreate.Warp")) {
			if (warp(f) != null) {
				e.setLine(0, TheAPI.colorize("&0[&9Warp&0]"));
				e.setLine(1, TheAPI.colorize("&a" + warp(f)));
				e.getBlock().getState().update();
				a.put(SignAction.PLAYER_COMMANDS, Arrays.asList("Warp " + warp(f)));
				SignAPI.setActions((Sign) e.getBlock().getState(), a);
			}
		}
		if (l.equalsIgnoreCase("[workbench]") && p.hasPermission("ServerControl.SignCreate.Workbench")) {
			e.setLine(0, TheAPI.colorize("&0[&9Workbench&0]"));
			e.getBlock().getState().update();

			a.put(SignAction.PLAYER_COMMANDS, Arrays.asList("Workbench"));
			SignAPI.setActions((Sign) e.getBlock().getState(), a);
		}
		if (l.equalsIgnoreCase("[Enderchest]") && p.hasPermission("ServerControl.SignCreate.EnderChest")) {
			e.setLine(0, TheAPI.colorize("&0[&9EnderChest&0]"));
			e.getBlock().getState().update();

			a.put(SignAction.PLAYER_COMMANDS, Arrays.asList("Enderchest"));
			SignAPI.setActions((Sign) e.getBlock().getState(), a);
		}
		if (l.equalsIgnoreCase("[Suicide]") && p.hasPermission("ServerControl.SignCreate.Suicide")) {
			e.setLine(0, TheAPI.colorize("&0[&9Suicide&0]"));
			e.getBlock().getState().update();
			a.put(SignAction.PLAYER_COMMANDS, Arrays.asList("Suicide"));
			SignAPI.setActions((Sign) e.getBlock().getState(), a);
		}
		if (l.equalsIgnoreCase("[repair]") && p.hasPermission("ServerControl.SignCreate.Repair")) {
			if (f == null) {
				e.setLine(0, TheAPI.colorize("&0[&9Repair&0]"));
				e.getBlock().getState().update();
				a.put(SignAction.PLAYER_COMMANDS, Arrays.asList("Repair Hand"));
				SignAPI.setActions((Sign) e.getBlock().getState(), a);
			}
			if (f.equalsIgnoreCase("Hand")) {
				e.setLine(0, TheAPI.colorize("&0[&9Repair&0]"));
				e.setLine(1, TheAPI.colorize("&aHand"));
				e.getBlock().getState().update();
				a.put(SignAction.PLAYER_COMMANDS, Arrays.asList("Repair Hand"));
				SignAPI.setActions((Sign) e.getBlock().getState(), a);
			}
			if (f.equalsIgnoreCase("All")) {
				e.setLine(0, TheAPI.colorize("&0[&9Repair&0]"));
				e.setLine(1, TheAPI.colorize("&aAll"));
				e.getBlock().getState().update();
				a.put(SignAction.PLAYER_COMMANDS, Arrays.asList("Repair All"));
				SignAPI.setActions((Sign) e.getBlock().getState(), a);
			}
		}
		if (l.equalsIgnoreCase("[Feed]") && p.hasPermission("ServerControl.SignCreate.Feed")) {
			e.setLine(0, TheAPI.colorize("&0[&9Feed&0]"));
			e.getBlock().getState().update();
			a.put(SignAction.CONSOLE_COMMANDS, Arrays.asList("Feed %player%"));
			SignAPI.setActions((Sign) e.getBlock().getState(), a);
		}
		if (l.equalsIgnoreCase("[Trash]") && p.hasPermission("ServerControl.SignCreate.Trash")) {
			e.setLine(0, TheAPI.colorize("&0[&9Trash&0]"));
			e.getBlock().getState().update();
			a.put(SignAction.PLAYER_COMMANDS, Arrays.asList("Trash"));
			SignAPI.setActions((Sign) e.getBlock().getState(), a);
		}
		if (l.equalsIgnoreCase("[Heal]") && p.hasPermission("ServerControl.SignCreate.Heal")) {
			e.setLine(0, TheAPI.colorize("&0[&9Heal&0]"));
			e.getBlock().getState().update();
			a.put(SignAction.CONSOLE_COMMANDS, Arrays.asList("Heal %player%"));
			SignAPI.setActions((Sign) e.getBlock().getState(), a);
		}
	}
}