package me.devtec.scr.commands.teleport.warp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpManager {
	static List<WarpHolder> registered_warps = new ArrayList<>();
	
	public static List<WarpHolder> availableWarps(CommandSender s) {
		if(s instanceof Player) {
			List<WarpHolder> warps = new ArrayList<>();
			for(WarpHolder warp : registered_warps) {
				if(warp.canTeleport((Player)s) == 0)
					warps.add(warp);
			}
			return warps;
		}
		return registered_warps;
	}
	
	public static List<WarpHolder> warpsOf(UUID owner) {
		List<WarpHolder> warps = new ArrayList<>();
		for(WarpHolder warp : registered_warps) {
			if(warp.owner() == null && owner == null || warp.owner().equals(owner))
				warps.add(warp);
		}
		return warps;
	}
	
	public static WarpHolder create(@Nullable UUID owner, String name, Location location, @Nullable String permission, double cost) {
		WarpHolder warp = new WarpHolder(owner, name, location, permission, cost);
		registered_warps.add(warp);
		return warp;
	}
	
	public static WarpHolder delete(String name) {
		WarpHolder warp = find(name);
		registered_warps.remove(warp);
		return warp;
	}

	public static WarpHolder find(String name) {
		for(WarpHolder holder : registered_warps) {
			if(holder.name().equals(name)) {
				return holder;
			}
		}
		return null;
	}
}
