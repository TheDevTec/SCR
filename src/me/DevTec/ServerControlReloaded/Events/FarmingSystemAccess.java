package me.DevTec.ServerControlReloaded.Events;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;

import me.devtec.theapi.apis.PluginManagerAPI;

public class FarmingSystemAccess {
	public static boolean hasAccess(Player player, Location loc) {
		boolean has = has(player, loc);
		if(has)return true;
		has=hasW(player, loc);
		return has;
	}
	
	private static boolean has(Player p, Location l) {
		ClaimedResidence r = getResidence(l);
		if(r!=null)
			return r.getPermissions().has(Flags.build, false) || r.getPermissions().playerHas(p, Flags.build, false) || p.hasPermission("residence.admin");
		return true;
	}
	
    private static ClaimedResidence getResidence(Location p) {
    	ClaimedResidence region = null;
    	if(PluginManagerAPI.isEnabledPlugin("Residence")) {
            if(com.bekvon.bukkit.residence.api.ResidenceApi.getResidenceManager().getByLoc(p) != null)
            	region=com.bekvon.bukkit.residence.api.ResidenceApi.getResidenceManager().getByLoc(p);
         
    	}
    	return region;
    }
	
	private static boolean hasW(Player p, Location l) {
		return getRegion(p, l) != null;
	}
    
    public static ProtectedRegion getRegion(Player p, Location l) {
    	ProtectedRegion region = null;
    	if(PluginManagerAPI.isEnabledPlugin("WorldGuard")) {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            if(BukkitAdapter.adapt(l.getWorld())!=null) {
            RegionManager reg = container.get(BukkitAdapter.adapt(l.getWorld()));
            if(reg != null)
            for(String r:reg.getRegions().keySet())
            if (reg.getRegion(r).contains(l.getBlockX(), l.getBlockY(), l.getBlockZ()))region=reg.getRegion(r);
            }
    	}
    	return region;
    }
}
