package me.DevTec.ServerControlReloaded.Modules.Mirror;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

public class MirrorSigns {
	
	public static void setSigns(Player p, Block original, String[] lines) {

		List<Location> l = MirrorManager.signs.get(original);

		for(Location loc : l) {
			Sign s = (Sign) loc.getBlock().getState();
			if(s==null) continue;
			int i = 0;
			for(String line : lines) {
			 s.setLine(i, line);
			 i++;
		}
		s.update();
		}
		 
		MirrorManager.signs.remove(original);
	}
}
