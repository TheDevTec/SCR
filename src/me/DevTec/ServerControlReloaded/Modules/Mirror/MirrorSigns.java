package me.DevTec.ServerControlReloaded.Modules.Mirror;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.devtec.theapi.apis.SignAPI;
import me.devtec.theapi.utils.Position;

public class MirrorSigns {
	
	public static void setSigns(Player p, Block original, String[] lines) {

		List<Position> l = MirrorManager.signs.get(original);

		for(Position loc : l) {
			if(!loc.getBukkitType().name().contains("SIGN"))continue;
			SignAPI.setLines(loc, lines);
		}
		MirrorManager.signs.remove(original);
	}
}
