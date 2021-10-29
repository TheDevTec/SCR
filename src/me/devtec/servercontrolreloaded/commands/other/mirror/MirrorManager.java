package me.devtec.servercontrolreloaded.commands.other.mirror;

import java.util.HashMap;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import me.devtec.theapi.utils.Position;

public interface MirrorManager {
	HashMap<Player, List<Position>> signs = new HashMap<>();

	public void add(Player player, String t);

	public void remove(Player player);
	

	public boolean isMirroring(Player player);
	
	public MirrorType getType(Player player);
	
	public Position getLocation(Player player);
	
	public void mirrorPlace(Player p, MirrorType type, Block block);
}
