package me.devtec.servercontrolreloaded.commands.other.mirror;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;

import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.reflections.Ref;

public class MirrorManagerLegacy implements MirrorManager {
	protected final HashMap<Player, MirrorType> mirror = new HashMap<>();
	protected final HashMap<Player, Position> location = new HashMap<>();
	
	public void add(Player player, String t) {
		MirrorType type;
		try {
			type = MirrorType.valueOf(t.toUpperCase());
		}catch(Exception | NoSuchFieldError err) {
			return;
		}
		mirror.put(player, type);
		location.put(player, new Position(player.getLocation().getBlock()));
		
	}
	public void remove(Player player) {
		mirror.remove(player);
		location.remove(player);
	}
	
	public boolean isMirroring(Player player) {
		return mirror.containsKey(player) && location.containsKey(player);
	}
	
	public MirrorType getType(Player player) {
		return mirror.get(player);
	}
	
	public Position getLocation(Player player) {
		return location.get(player);
	}
	
	public void mirrorPlace(Player p, MirrorType type, Block block) {
		Position loc = location.get(p);
		if(type==MirrorType.AXISX || type==MirrorType.AXISZ) {
			int axis, bl, v;
			if(type==MirrorType.AXISX) {
				axis=loc.getBlockZ();
				bl=block.getZ();
			}else  {
				axis=loc.getBlockX();
				bl=block.getX();
			}
			if(axis==bl) return;
			v= axis+(axis-bl);
			Position n;
			if(type==MirrorType.AXISX)
				n = new Position(loc.getWorld(), block.getX(), block.getY(), v);
			else
				n = new Position(loc.getWorld(), v, block.getY(), block.getZ());
			n.getBlock().setType(block.getType());
			Block b = n.getBlock();
			if(TheAPI.isNewerThan(12))
				b.setBlockData(block.getBlockData());
			BlockState st = b.getState();
			for(Field f : Ref.getAllFields(st.getClass())) {
				if(!f.getName().equals("world") && !f.getName().equals("position") && !f.getName().equals("x") && !f.getName().equals("y") && !f.getName().equals("z") && !f.getName().equals("chunk"))
					Ref.set(st, f, Ref.get(block.getState(), f));
			}
			st.update(true, true);
			rotate(b, type); //block
			
			
			if(block.getType().name().contains("_SIGN")) {
				List<Position> list = new ArrayList<>();
				if(!signs.isEmpty() && signs.containsKey(p)) list = signs.get(p);
				list.add(n);
				signs.put(p, list);
			}
			
			return;
		}
		if(type==MirrorType.CENTER) {
			int axisX;
			int vX;
			Position loc1;
			axisX = loc.getBlockX();
			vX = axisX- block.getX();
			loc1 = new Position( loc.getWorld(), axisX+vX, block.getY(), block.getZ());
			mirrorPlace(p, MirrorType.AXISZ, block);
			mirrorPlace(p, MirrorType.AXISX, block);
			mirrorPlace(p, MirrorType.AXISX, loc1.getBlock());
		}
		
	}
	
	public void rotate(Block block, MirrorType type) {
        if (block == null)
            return;
    	BlockState state = block.getState();
        MaterialData d = state.getData();
        if(d instanceof Directional) {
            Directional dir = (Directional)d;
            BlockFace f =dir.getFacing();
            f = getFace(f, type);
            dir.setFacingDirection(f);
            state.setData((MaterialData) dir);
            state.update(true,false);
        }
    }
	
	public BlockFace getFace(BlockFace f, MirrorType type) {
		if(type==MirrorType.AXISX) {
	        if(f==BlockFace.SOUTH) return BlockFace.NORTH;
	        if(f==BlockFace.NORTH) return BlockFace.SOUTH;
	        
	        if(f==BlockFace.SOUTH_EAST) return BlockFace.NORTH_EAST;
	        if(f==BlockFace.NORTH_EAST) return BlockFace.SOUTH_EAST;
	        if(f==BlockFace.SOUTH_SOUTH_EAST) return BlockFace.NORTH_NORTH_EAST;
	        if(f==BlockFace.NORTH_NORTH_EAST) return BlockFace.SOUTH_SOUTH_EAST;
	        if(f==BlockFace.EAST_SOUTH_EAST) return BlockFace.EAST_NORTH_EAST;
	        if(f==BlockFace.EAST_NORTH_EAST) return BlockFace.EAST_SOUTH_EAST;
	        
	        if(f==BlockFace.SOUTH_WEST) return BlockFace.NORTH_WEST;
	        if(f==BlockFace.NORTH_WEST) return BlockFace.SOUTH_WEST;
	        if(f==BlockFace.SOUTH_SOUTH_WEST) return BlockFace.NORTH_NORTH_WEST;
	        if(f==BlockFace.NORTH_NORTH_WEST) return BlockFace.SOUTH_SOUTH_WEST;
	        if(f==BlockFace.WEST_SOUTH_WEST) return BlockFace.WEST_NORTH_WEST;
	        if(f==BlockFace.WEST_NORTH_WEST) return BlockFace.WEST_SOUTH_WEST;
		}
		if(type==MirrorType.AXISZ) {
	        if(f==BlockFace.EAST) return BlockFace.WEST;
	        if(f==BlockFace.WEST) return BlockFace.EAST;
	        
	        if(f==BlockFace.SOUTH_EAST) return BlockFace.SOUTH_WEST;
	        if(f==BlockFace.SOUTH_SOUTH_EAST) return BlockFace.SOUTH_SOUTH_WEST;
	        if(f==BlockFace.EAST_SOUTH_EAST) return BlockFace.WEST_SOUTH_WEST;
	        if(f==BlockFace.SOUTH_WEST) return BlockFace.SOUTH_EAST;
	        if(f==BlockFace.SOUTH_SOUTH_WEST) return BlockFace.SOUTH_SOUTH_EAST;
	        if(f==BlockFace.WEST_SOUTH_WEST) return BlockFace.EAST_SOUTH_EAST;
	        
	        if(f==BlockFace.NORTH_EAST) return BlockFace.NORTH_WEST;
	        if(f==BlockFace.NORTH_NORTH_EAST) return BlockFace.NORTH_NORTH_WEST;
	        if(f==BlockFace.EAST_NORTH_EAST) return BlockFace.WEST_NORTH_WEST;
	        if(f==BlockFace.NORTH_WEST) return BlockFace.NORTH_EAST;
	        if(f==BlockFace.NORTH_NORTH_WEST) return BlockFace.NORTH_NORTH_EAST;
	        if(f==BlockFace.WEST_NORTH_WEST) return BlockFace.EAST_NORTH_EAST;
		}
		if(type==MirrorType.CENTER) {
	        if(f==BlockFace.SOUTH) return BlockFace.NORTH;
	        if(f==BlockFace.NORTH) return BlockFace.SOUTH;
	        if(f==BlockFace.EAST) return BlockFace.WEST;
	        if(f==BlockFace.WEST) return BlockFace.EAST;
		}
		return f;
	}
}
