package me.devtec.servercontrolreloaded.events.functions;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.bekvon.bukkit.residence.containers.Flags;
import com.bekvon.bukkit.residence.protection.ClaimedResidence;

import me.devtec.theapi.utils.reflections.Ref;

public class FarmingSystemAccess {
	public static boolean hasAccess(Player player, Location loc) {
		boolean has = true;
		if(Bukkit.getPluginManager().getPlugin("Residence")!=null)
		 has = has(player, loc);
		if(Bukkit.getPluginManager().getPlugin("WorldGuard")!=null && Bukkit.getPluginManager().getPlugin("WorldEdit")!=null)
			has=WGAccess.has(player,loc);
		return has;
	}
	
	private static boolean has(Player p, Location l) {
		ClaimedResidence r = com.bekvon.bukkit.residence.api.ResidenceApi.getResidenceManager().getByLoc(l);
		if(r==null)return true;
		return r.getPermissions().has(Flags.build, false) || r.getPermissions().playerHas(p, Flags.build, false) || p.hasPermission("residence.admin");
	}
    
    private static class WGAccess {
    	private static final Object[] block_break = new Object[] {Ref.getStatic(Ref.getClass("com.sk89q.worldguard.protection.flags.DefaultFlag")!=null?Ref.getClass("com.sk89q.worldguard.protection.flags.DefaultFlag"):Ref.getClass("com.sk89q.worldguard.protection.flags.Flags"), "BLOCK_BREAK")};
    	private static final Object inst = Ref.invokeStatic(Ref.getClass("com.sk89q.worldguard.bukkit.WorldGuardPlugin"), "inst");
    	private static final Method wrap = Ref.method(inst.getClass(), "warpPlayer", Player.class);
        private static final Method test=Ref.findMethodByName(Ref.getClass("com.sk89q.worldguard.protection.ApplicableRegionSet"), "testState");
    	private static final Object region = Ref.invoke(Ref.invoke(Ref.invokeStatic(Ref.getClass("com.sk89q.worldguard.WorldGuard"), "getInstance"),"getPlatform"),"getRegionContainer");
    	private static final Method get = Ref.method(Ref.nms("com.sk89q.worldguard.protection.regions.RegionContainer"), "get", Ref.getClass("com.sk89q.worldedit.world.World"));
    	private static final Method applicable = Ref.method(Ref.nms("com.sk89q.worldguard.protection.managers.RegionManager"), "getApplicableRegions", Ref.nms("com.sk89q.worldedit.math.BlockVector3"));
    	
    	public static boolean has(Player player, Location l) {
    		Object r;
        	Object reg = Ref.invoke(inst, "getRegionContainer");
        	if(reg!=null)
        		r=Ref.invoke(Ref.invoke(reg, "get", l.getWorld()), "getApplicableRegions", l);
        	else r=Ref.invoke(Ref.invoke(region,get,adapt(l.getWorld())),applicable,at(l.getX(), l.getY(), l.getZ()));
    		if(r==null)return true;
    		return (boolean) Ref.invoke(r, test, Ref.invoke(inst, wrap, player), block_break);
        }
    	
    	private static final Method at = Ref.method(Ref.nms("com.sk89q.worldedit.math.BlockVector3"), "at", double.class, double.class, double.class);
    	private static final Method adapt = Ref.method(Ref.nms("com.sk89q.worldedit.bukkit.BukkitAdapter"), "adapt", World.class);
    	
    	static Object adapt(World w) {
    		return Ref.invokeStatic(adapt, w);
    	}
    	
    	static Object at(double x, double y, double z) {
    		return Ref.invokeStatic(at, x,y,z);
    	}
    }
}
