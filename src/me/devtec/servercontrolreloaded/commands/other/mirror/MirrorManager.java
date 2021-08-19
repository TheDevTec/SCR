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
import me.devtec.theapi.blocksapi.schematic.construct.SerializedBlock;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.reflections.Ref;

public class MirrorManager {
	protected static final HashMap<Player, MirrorType> mirror = new HashMap<>();
	protected static final HashMap<Player, Position> location = new HashMap<>();
	protected static final HashMap<Player, List<Position>> signs = new HashMap<>();
	
	public static void add(Player player, String t) {
		MirrorType type;
		try {
			type = MirrorType.valueOf(t.toUpperCase());
		}catch(Exception | NoSuchFieldError err) {
			return;
		}
		mirror.put(player, type);
		location.put(player, new Position(player.getLocation().getBlock()));
		
	}
	public static void remove(Player player) {
		mirror.remove(player);
		location.remove(player);
	}
	
	public static boolean isMirroring(Player player) {
		return mirror.containsKey(player) && location.containsKey(player);
	}
	
	public static MirrorType getType(Player player) {
		return mirror.get(player);
	}
	
	public static Position getLocation(Player player) {
		return location.get(player);
	}
	
	public static void mirrorPlace(Player p, MirrorType type, Block block) {
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
			n.setTypeAndUpdate(block.getType());
			BlockState st = n.getBlock().getState();
			st.setData(block.getState().getData());
			st.update(true, true);
			if(TheAPI.isNewerThan(12))
				n.setBlockDataAndUpdate(block.getBlockData());
			rotate(n.getBlock(), block, type);
			
			
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
	
	public static void rotate(Block block, Block old,  MirrorType type) {
        if (block == null)
            return;
        // Clone BlockState
        Position o = new Position(old);
        Object ed = SerializedBlock.getState(o);
        if(ed!=null) { //for banners, heads...
            Object sec = SerializedBlock.getState(new Position(block));
        	for(Field f : Ref.getDeclaredFields(ed.getClass())) {
        		Ref.set(sec, f, Ref.get(ed, f));
        	}
        	Ref.sendPacket(TheAPI.getOnlinePlayers(), Ref.invoke(ed, "getUpdatePacket"));
        }
        try {
        	if(TheAPI.isNewerThan(12)) {
        	org.bukkit.block.data.BlockData state = block.getBlockData(); //1.13+
		        if(state instanceof org.bukkit.block.data.Directional) {
		        	org.bukkit.block.data.Directional dir = (org.bukkit.block.data.Directional)state;
		            BlockFace f =dir.getFacing();
		            f = getFace(f, type);
		            dir.setFacing(f);
		            block.setBlockData(dir);
		        }
		        if(state instanceof org.bukkit.block.data.Rotatable) {
		        	org.bukkit.block.data.Rotatable dir = (org.bukkit.block.data.Rotatable)state;
		            BlockFace f =dir.getRotation();
		            f = getFace(f, type);
		            dir.setRotation(f);
		            block.setBlockData(dir);
		        }
		        if(state instanceof org.bukkit.block.data.type.Stairs) {
		        	org.bukkit.block.data.type.Stairs dir = (org.bukkit.block.data.type.Stairs)state;
		        	org.bukkit.block.data.type.Stairs.Shape f = dir.getShape();
		            f = (org.bukkit.block.data.type.Stairs.Shape) getShape(f, type);
		            dir.setShape(f);
		            block.setBlockData(dir);
		        }
		        /*
		        if(state instanceof org.bukkit.block.data.Rail) {
		        	org.bukkit.block.data.Rail dir = (org.bukkit.block.data.Rail)state;
		            org.bukkit.block.data.Rail.Shape f = dir.getShape();
		            f = getShape(f, type);
		            dir.setShape(f);
		            block.setBlockData(dir);
		        }
		        */
        	}else {
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
        }catch(Exception | NoSuchMethodError|NoSuchFieldError|NoClassDefFoundError e) { //1.12.2 - 1.7.10
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
    }
	
	public static BlockFace getFace(BlockFace f, MirrorType type) {
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
	
	public static Object getShape(org.bukkit.block.data.type.Stairs.Shape shape, MirrorType type) {
		if(shape == org.bukkit.block.data.type.Stairs.Shape.STRAIGHT) return org.bukkit.block.data.type.Stairs.Shape.STRAIGHT;
		if(type==MirrorType.AXISX) {
			if(shape== org.bukkit.block.data.type.Stairs.Shape.INNER_LEFT) return org.bukkit.block.data.type.Stairs.Shape.INNER_RIGHT;
			if(shape== org.bukkit.block.data.type.Stairs.Shape.INNER_RIGHT) return org.bukkit.block.data.type.Stairs.Shape.INNER_LEFT;
			if(shape== org.bukkit.block.data.type.Stairs.Shape.OUTER_LEFT) return org.bukkit.block.data.type.Stairs.Shape.OUTER_RIGHT;
			if(shape== org.bukkit.block.data.type.Stairs.Shape.OUTER_RIGHT) return org.bukkit.block.data.type.Stairs.Shape.OUTER_LEFT;
		}
		if(type==MirrorType.AXISZ) {
			if(shape== org.bukkit.block.data.type.Stairs.Shape.INNER_LEFT) return org.bukkit.block.data.type.Stairs.Shape.INNER_RIGHT;
			if(shape== org.bukkit.block.data.type.Stairs.Shape.INNER_RIGHT) return org.bukkit.block.data.type.Stairs.Shape.INNER_LEFT;
			if(shape== org.bukkit.block.data.type.Stairs.Shape.OUTER_LEFT) return org.bukkit.block.data.type.Stairs.Shape.OUTER_RIGHT;
			if(shape== org.bukkit.block.data.type.Stairs.Shape.OUTER_RIGHT) return org.bukkit.block.data.type.Stairs.Shape.OUTER_LEFT;
		}
		return shape;
	}
}
