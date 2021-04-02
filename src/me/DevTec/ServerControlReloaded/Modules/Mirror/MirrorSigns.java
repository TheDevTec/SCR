package me.DevTec.ServerControlReloaded.Modules.Mirror;

import me.devtec.theapi.apis.SignAPI;
import me.devtec.theapi.utils.Position;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

public class MirrorSigns {
	
	public static void setSigns(Player p, Block original, String[] lines) {
		List<Position> l = MirrorManager.signs.get(p);
		if(l==null)return;
		for(Position loc : l) {
			if(!loc.getBukkitType().name().contains("SIGN"))continue;
			SignAPI.setLines(loc, lines);
		}
		MirrorManager.signs.remove(p);
	}
}
