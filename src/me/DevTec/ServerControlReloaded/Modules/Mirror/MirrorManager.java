package me.DevTec.ServerControlReloaded.Modules.Mirror;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.block.data.type.Sign;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.block.data.type.Stairs.Shape;
import org.bukkit.entity.Player;

import me.devtec.theapi.TheAPI;
import me.devtec.theapi.blocksapi.schematic.construct.SerializedBlock;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.reflections.Ref;

public class MirrorManager {
	protected static HashMap<Player, MirrorType> mirror = new HashMap<>();
	protected static HashMap<Player, Position> location = new HashMap<>();
	protected static HashMap<Block, List<Position>> signs = new HashMap<>(); // Original sign X List of new Sign
	
	public static void add(Player player, String t) {
		MirrorType type = null;
		try {
			type = MirrorType.valueOf(t.toUpperCase());
		}catch(Exception | NoSuchFieldError err) {
			return;
		}
		mirror.put(player, type);
		location.put(player, new Position(player.getLocation().getBlock()));
		
	}
	public static void remove(Player player) {
		mirror.remove( player);
		location.remove( player);
	}
	
	public static boolean isMirroring(Player player) {
		if(mirror.containsKey(player) && location.containsKey(player)) return true;
		return false;
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
			n.getBlock().setType(block.getType());
			n.getBlock().setBlockData(block.getBlockData());
			
			if(block.getType().name().contains("_STAIRS")) setStairs(n.getBlock(), block, type);
			else rotate(n.getBlock(), block, type);
			return;
		}

		if(type==MirrorType.CENTER) {
			int axisX , axisZ ; // Souřadnice os X a Z
			int vX , vZ ; // Výsledný rozdíl mezi osou a položeným blockem
			Position loc1 , loc2, loc3 ;
			axisX = loc.getBlockX();
			axisZ = loc.getBlockZ();
			
			vZ = axisZ- block.getZ();
			vX = axisX- block.getX();
			loc1 = new Position( loc.getWorld(), axisX+vX, block.getY(), block.getZ());
			loc2 = new Position( loc.getWorld(), block.getX(), block.getY(), axisZ+vZ );
			loc3 = new Position( loc.getWorld(), axisX+vX , block.getY(), axisZ+vZ );
			
			if(block.getType().name().contains("_SIGN")) {
				List<Position> list = new ArrayList<>();
				if(!signs.isEmpty() && signs.containsKey(block)) list = signs.get(block);
				list.add(loc1);
				list.add(loc2);
				list.add(loc3);
				signs.put(block, list);
			}
			
			mirrorPlace(p, MirrorType.AXISZ, block);
			mirrorPlace(p, MirrorType.AXISX, block);
			mirrorPlace(p, MirrorType.AXISX, loc1.getBlock());



			/*loc1.getBlock().setType(block.getType());
			loc2.getBlock().setType(block.getType());
			loc3.getBlock().setType(block.getType());
			
			loc1.getBlock().setBlockData(block.getBlockData());
			loc2.getBlock().setBlockData(block.getBlockData());
			loc3.getBlock().setBlockData(block.getBlockData());

			rotate(loc1.getBlock(), block, type);
			rotate(loc2.getBlock(), block, type);
			rotate(loc3.getBlock(), loc2.getBlock(), type);*/
		}
		
	}
	/*
    X  |  O
       |
 ------|-------
       |
       |
 */
	public static void rotate(Block block, Block old,  MirrorType type) {
        if (block == null)
            return;
		if(block.getType().name().contains("_STAIRS")) { 
			setStairs(block, old, type);
			return;
		}
		if(block.getType().name().contains("_SIGN")) { 
			rotateSign(block, old, type);
			return;
		}
        // -------- Straikerina - Clone BlockState
        Position o = new Position(old);
        Object ed = SerializedBlock.getState(o);
        if(ed!=null) { //for banners, heads...
            Object sec = SerializedBlock.getState(new Position(block));
        	for(Field f : Ref.getDeclaredFields(ed.getClass())) {
        		Ref.set(sec, f, Ref.get(ed, f));
        	}
        	Ref.sendPacket(TheAPI.getOnlinePlayers(), Ref.invoke(ed, "getUpdatePacket"));
        }
        // -------- End
        BlockData d = old.getBlockData();
        if(d==null) return;
        // -------- Straikerina - Clone BlockData
        block.setBlockData(d);
        // -------- End
        if(d instanceof Directional) {
              Directional dir = (Directional)d;
              BlockFace f =dir.getFacing();
              if(f==null)
              	return;
              f = getFace(f, type);
              dir.setFacing(f);
              block.setBlockData(dir);
        }
    }
	
	public static void setStairs(Block block, Block old,  MirrorType type) {
        if (block == null)
            return;
		BlockFace face = old.getFace(block);
        Stairs stairs = (Stairs) old.getBlockData();
        BlockFace f = stairs.getFacing();
        if(f==null)
        	return;
        face=getFace(f, type);
        stairs.setFacing(face);
        stairs.setShape(getShape(stairs.getShape(), type));
        
        block.setBlockData(stairs);
    }

	public static void rotateSign(Block block, Block old,  MirrorType type) {
        if (block == null)
            return;
		//TODO - WALL SIGN
		Sign sign = (Sign) old.getBlockData();
		BlockFace rotation = sign.getRotation();
        if(rotation==null)
        	return;
        rotation=getFace(rotation, type);
        sign.setRotation(rotation);
        
        block.setBlockData(sign);
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
        //Fixed Sign rotation:
        if(f==BlockFace.SOUTH_EAST) return BlockFace.SOUTH_WEST;
        if(f==BlockFace.SOUTH_SOUTH_EAST) return BlockFace.SOUTH_SOUTH_WEST;
        if(f==BlockFace.EAST_SOUTH_EAST) return BlockFace.WEST_SOUTH_WEST;
        if(f==BlockFace.SOUTH_WEST) return BlockFace.SOUTH_EAST;
        if(f==BlockFace.SOUTH_SOUTH_WEST) return BlockFace.SOUTH_SOUTH_EAST;
        if(f==BlockFace.WEST_SOUTH_WEST) return BlockFace.EAST_SOUTH_EAST;
        
        if(f==BlockFace.NORTH_EAST) return BlockFace.NORTH_WEST;
        if(f==BlockFace.NORTH_NORTH_EAST) return BlockFace.NORTH_NORTH_WEST;
        if(f==BlockFace.EAST_NORTH_EAST) return BlockFace.WEST_NORTH_WEST;
        if(f==BlockFace.NORTH_WEST) return BlockFace.NORTH_WEST;
        if(f==BlockFace.NORTH_NORTH_WEST) return BlockFace.NORTH_NORTH_WEST;
        if(f==BlockFace.WEST_NORTH_WEST) return BlockFace.WEST_NORTH_WEST;
        
        
        
        //if(f==BlockFace.SOUTH_EAST) return BlockFace.NORTH_EAST;
        //if(f==BlockFace.NORTH_EAST) return BlockFace.NORTH_WEST;
        //if(f==BlockFace.SOUTH_SOUTH_EAST) return BlockFace.NORTH_NORTH_EAST;
        //if(f==BlockFace.NORTH_NORTH_EAST) return BlockFace.NORTH_NORTH_WEST;
        //if(f==BlockFace.EAST_SOUTH_EAST) return BlockFace.EAST_NORTH_EAST;
        //if(f==BlockFace.EAST_NORTH_EAST) return BlockFace.WEST_NORTH_WEST;
        

      /* if(f==BlockFace.SOUTH_WEST) return BlockFace.NORTH_WEST;
        if(f==BlockFace.NORTH_WEST) return BlockFace.SOUTH_WEST;
        if(f==BlockFace.SOUTH_SOUTH_WEST) return BlockFace.NORTH_NORTH_WEST;
        if(f==BlockFace.NORTH_NORTH_WEST) return BlockFace.SOUTH_SOUTH_WEST;
        if(f==BlockFace.WEST_SOUTH_WEST) return BlockFace.WEST_NORTH_WEST;
        if(f==BlockFace.WEST_NORTH_WEST) return BlockFace.WEST_SOUTH_WEST;*/
		}
		if(type==MirrorType.CENTER) { //TODO - DODĚLAT PRO 4 KVADRANTY!!!
	        if(f==BlockFace.SOUTH) return BlockFace.NORTH;
	        if(f==BlockFace.NORTH) return BlockFace.SOUTH;
	        if(f==BlockFace.EAST) return BlockFace.WEST;
	        if(f==BlockFace.WEST) return BlockFace.EAST;
		}
		return f;
	}
	public static Shape getShape(Shape shape, MirrorType type) {
		if(shape == Shape.STRAIGHT) return Shape.STRAIGHT;
		if(type==MirrorType.AXISX) {
			if(shape== Shape.INNER_LEFT) return Shape.INNER_RIGHT;
			if(shape== Shape.INNER_RIGHT) return Shape.INNER_LEFT;
			if(shape== Shape.OUTER_LEFT) return Shape.OUTER_RIGHT;
			if(shape== Shape.OUTER_RIGHT) return Shape.OUTER_LEFT;
		}
		if(type==MirrorType.AXISZ) {
			if(shape== Shape.INNER_LEFT) return Shape.INNER_RIGHT;
			if(shape== Shape.INNER_RIGHT) return Shape.INNER_LEFT;
			if(shape== Shape.OUTER_LEFT) return Shape.OUTER_RIGHT;
			if(shape== Shape.OUTER_RIGHT) return Shape.OUTER_LEFT;
		}
        /*if(f==BlockFace.SOUTH_EAST) face=BlockFace.NORTH_WEST;
        if(f==BlockFace.NORTH_WEST) face=BlockFace.SOUTH_EAST;
        if(f==BlockFace.SOUTH_WEST) face=BlockFace.NORTH_EAST;
        if(f==BlockFace.NORTH_EAST) face=BlockFace.SOUTH_WEST;*/
		
		return shape;
	}
}
