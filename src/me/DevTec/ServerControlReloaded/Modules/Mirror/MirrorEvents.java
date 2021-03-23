package me.DevTec.ServerControlReloaded.Modules.Mirror;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

public class MirrorEvents implements Listener {

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		if(!MirrorManager.isMirroring(p)) return;
		MirrorType type = MirrorManager.getType(p);
		Block block = e.getBlock();
		MirrorManager.mirrorPlace(p, type, block);
		
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		Player p = e.getPlayer();
		if(!MirrorManager.isMirroring(p)) return;
		MirrorType type = MirrorManager.getType(p);
		Location loc = MirrorManager.getLocation(p);
		Block block = e.getBlock();
		if(type==MirrorType.AXISX || type==MirrorType.AXISZ) {
			int axis, bl, v;
			if(type==MirrorType.AXISX) {
				axis=loc.getBlockZ();
				bl=block.getZ();
			}
			else  {
				axis=loc.getBlockX();
				bl=block.getX();
			}
			if(axis==bl) return;
			v= axis+(axis-bl);
			Location n;
			if(type==MirrorType.AXISX)
				n = new Location(loc.getWorld(), block.getX(), block.getY(), v);
			else
				n = new Location(loc.getWorld(), v, block.getY(), block.getZ());
			n.getBlock().setType(Material.AIR);
		}
		if(type==MirrorType.CENTER) {
			int axisX, axisZ; // Souřadnice os X a Z
			int vX, vZ; // Výsledný rozdíl mezi osou a položeným blockem
			Location loc1, loc2, loc3;
			axisX = loc.getBlockX();
			axisZ = loc.getBlockZ();
			
			vZ = axisZ- block.getZ();
			vX = axisX- block.getX();
			loc1 = new Location( loc.getWorld(), axisX+vX, block.getY(), block.getZ());
			loc2 = new Location( loc.getWorld(), block.getX(), block.getY(), axisZ+vZ );
			loc3 = new Location( loc.getWorld(), axisX+vX , block.getY(), axisZ+vZ );
			
			loc1.getBlock().setType(Material.AIR);
			loc2.getBlock().setType(Material.AIR);
			loc3.getBlock().setType(Material.AIR);
		}
	}
	@EventHandler
	public void eventSignChanged(SignChangeEvent event){
		Block original = event.getBlock();
		if(!MirrorManager.isSign(original)) return;
		//String[] lines = event.getLines();
		MirrorSigns.setSigns(event.getPlayer(), original, event.getLines());
	}
	
	
}
