package me.devtec.servercontrolreloaded.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.blocksapi.BlockIterator;
import me.devtec.theapi.cooldownapi.CooldownAPI;
import me.devtec.theapi.particlesapi.Particle;
import me.devtec.theapi.particlesapi.ParticleAPI;
import me.devtec.theapi.particlesapi.ParticleData;
import me.devtec.theapi.scheduler.Scheduler;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.Position;
import me.devtec.theapi.utils.StringUtils;
import me.devtec.theapi.utils.json.Writer;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.reflections.Ref;

public class Portal {
	private Position a, b;
	private Particle p;
	private List<String> cmds = new ArrayList<>();
	private String server,id;
	private List<String> bcmds = new ArrayList<>();
	private double cooldown;
	private List<Position> blocks = new ArrayList<>();
	
	public Portal(String id, double cooldown, Position a, Position b, Particle p, List<String> cmds, String s, List<String> bcmds) {
		this.a=a;
		this.id=id;
		this.cooldown=cooldown;
		this.b=b;
		this.p=p;
		this.cmds=cmds;
		server=s;
		this.bcmds=bcmds;
		if(a.getWorld()!=null && b.getWorld()!=null && a.getWorld().equals(b.getWorld()))
		for(Position c : new BlockIterator(a, b)) {
			if(Ref.invoke(c.getIBlockData(),getBlock).equals(air))
				blocks.add(c.clone());
		}
	}
	
	private static Method getBlock = Ref.method(Ref.nms("BlockBase$BlockData")!=null?Ref.nms("BlockBase$BlockData"):Ref.nms("IBlockProperties"),"getMaterial");
	static {
		if(getBlock==null)
			getBlock=Ref.method(Ref.nms("IBlockData"), "getMaterial");
	}
	private static Object air = Ref.getStatic(Ref.nms("Material"),"AIR");
	
	public void spawnParticles() {
		if(p==null||p.getParticle()==null)return;
		for(Position c : blocks)
			ParticleAPI.spawnParticle(a.getWorld().getPlayers(), p, c);
	}
	
	public void processCommands(Player target) {
		for(String f : cmds)
			TheAPI.sudoConsole(TabList.replace(f, target, true));
		if(server!=null && Loader.hasBungee) {
			ByteArrayDataOutput d = ByteStreams.newDataOutput();
			d.writeUTF("portal");
			d.writeUTF(target.getName());
			d.writeUTF(server);
			String a = Writer.write(bcmds);
			while(a.length()>35000) {
				d.writeUTF(a.substring(0, 35000));
				a=a.substring(35000);
			}
			if(!a.equals(""))
			d.writeUTF(a);
			target.sendPluginMessage(Loader.getInstance, "scr:community", d.toByteArray());
		}
	}
	
	static List<Portal> portals = new ArrayList<>();
	static int task, moveTask;
	
	public static void unload() {
		portals.clear();
		if(task!=0)
			Scheduler.cancelTask(task);
		if(moveTask!=0)
			Scheduler.cancelTask(moveTask);
		task=0;
		moveTask=0;
	}

	public static void reload() {
		unload();
		for(String s : Loader.portals.getKeys()) {
			if(Loader.portals.get(s+".pos.1")==null||Loader.portals.get(s+".pos.2")==null)continue;
			Portal p = new Portal(s,Loader.portals.getDouble(s+".cooldown"),Position.fromString(Loader.portals.getString(s+".pos.1")), 
					Position.fromString(Loader.portals.getString(s+".pos.2")), 
					makeParticle(Loader.portals.getString(s+".particle")), 
					Loader.portals.getStringList(s+".cmds"), 
					Loader.portals.getString(s+".server"), 
					Loader.portals.getStringList(s+".bcmds"));
			portals.add(p);
		}
		task=new Tasker() {
			public void run() {
				for(Portal a : portals)a.spawnParticles();
			}
		}.runRepeating(5, 10);
		moveTask=new Tasker() {
			HashMap<Player, Portal> inPortal = new HashMap<>();
			public void run() {
				for(Player p : TheAPI.getOnlinePlayers())
				for(Portal a : portals) {
					if(!a.a.getWorld().equals(p.getWorld()))continue;
					boolean is = isInside(p.getLocation(), a.a, a.b);
					if(is) {
						if(!inPortal.containsKey(p) || !inPortal.get(p).equals(a)) {
							if(a.cooldown==0) {
								inPortal.put(p,a);
								NMSAPI.postToMainThread(() -> a.processCommands(p));
							}else {
								CooldownAPI cd = new CooldownAPI(p);
								if(cd.expired("portals."+a.id)) {
									cd.createCooldown("portals."+a.id, a.cooldown);
									inPortal.put(p,a);
									NMSAPI.postToMainThread(() -> a.processCommands(p));
								}
							}
						}
					}else
						if(a.equals(inPortal.get(p)))
							inPortal.remove(p);
				}
			}
		}.runRepeating(0, 1);
	}
	
	private static Particle makeParticle(String string) {
		if(string==null)return null;
		if(string.contains("{")&&string.contains("}")) {
			String particle = string.split("\\{")[0];
			String[] values = string.split("\\{")[1].split("\\}")[0].split(",");
			if(particle.equalsIgnoreCase("dust")||particle.equalsIgnoreCase("redstone"))
			return new Particle(particle, values.length>=4? new ParticleData.RedstoneOptions(
					StringUtils.getFloat(values[0]), StringUtils.getFloat(values[1]), 
					StringUtils.getFloat(values[2]), StringUtils.getFloat(values[3])):
						new ParticleData.RedstoneOptions(StringUtils.getFloat(values[0]), StringUtils.getFloat(values[1]), 
								StringUtils.getFloat(values[2])));
			if(particle.equalsIgnoreCase("falling_dust")||particle.equalsIgnoreCase("block"))
			return new Particle(particle, new ParticleData.BlockOptions(values.length>=2?new ItemStack(XMaterial.matchXMaterial(values[0]).getMaterial(),1,StringUtils.getByte(values[1])):
				XMaterial.matchXMaterial(values[0]).parseItem()));
			if(particle.equalsIgnoreCase("note"))
				return new Particle(particle, new ParticleData.NoteOptions(StringUtils.getInt(values[0])));
			if(particle.equalsIgnoreCase("item"))
				return new Particle(particle, new ParticleData.ItemOptions(values.length>=2?new ItemStack(XMaterial.matchXMaterial(values[0]).getMaterial(),1,StringUtils.getByte(values[1])):
					XMaterial.matchXMaterial(values[0]).parseItem()));
			return new Particle(particle);
			}
		return new Particle(string);
	}

	public static boolean isInside(Location loc, Position a, Position b) {
		int xMin = Math.min(a.getBlockX(), b.getBlockX());
		int yMin = Math.min(a.getBlockY(), b.getBlockY());
		int zMin = Math.min(a.getBlockZ(), b.getBlockZ());
		int xMax = Math.max(a.getBlockX(), b.getBlockX());
		int yMax = Math.max(a.getBlockY(), b.getBlockY());
		int zMax = Math.max(a.getBlockZ(), b.getBlockZ());
		return loc.getBlockX() >= xMin && loc.getBlockX() <= xMax
				&& loc.getBlockY() >= yMin && loc.getBlockY() <= yMax && loc.getBlockZ() >= zMin
				&& loc.getBlockZ() <= zMax;
	}
}
