package me.devtec.servercontrolreloaded.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
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
import me.devtec.theapi.utils.json.Json;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.reflections.Ref;
import me.devtec.theapi.utils.theapiutils.LoaderClass;

public class Portal {
	public final Position a;
	public final Position b;
    public Particle p;
	public String perm;
	public List<String> cmds;
	public String server;
	public final String id;
	public List<String> bcmds;
	public double cooldown;
	public boolean perplayer;
	public boolean kickBack;
	public List<Position> blocks = new ArrayList<>();
	public long lastEnter;
	public String wait;
	
	public Portal(String id, double cooldown, boolean perPlayer, boolean kickBack, Position a, Position b, Particle p, List<String> cmds, String s, List<String> bcmds, String permission, int wait) {
		this.a=a;
		perplayer=perPlayer;
		this.kickBack=kickBack;
		this.id=id;
		perm=permission;
		if(permission!=null && permission.trim().isEmpty())perm=null;
		this.cooldown=cooldown;
		this.b=b;
		this.p=p;
		this.cmds=cmds;
		server=s;
		if(wait==0||wait==-1) //default
			this.wait="5";
		else
			this.wait=""+wait; //custom
		
		this.bcmds=bcmds;
		if(p==null||p.getParticle()==null)return;
		if(a.getWorld()!=null && b.getWorld()!=null && a.getWorld().equals(b.getWorld()))
		for(Position c : new BlockIterator(a, b)) {
			Object o = Ref.invoke(c.getIBlockData(),getBlock);
			if(o==null||air.equals(o))
				blocks.add(c.clone());
		}
	}
	
	private static final Method getBlock = Ref.method(Ref.nmsOrOld("world.level.block.state.BlockBase$BlockData","BlockBase$BlockData"),"getBlock");
	private static final Object air = LoaderClass.air;
	
	public String getPermission() {
		return perm;
	}
	
	public void spawnParticles() {
		if(p==null||p.getParticle()==null||a.getWorld().getPlayers().isEmpty())return;
		for(Position c : blocks)
			ParticleAPI.spawnParticle(a.getWorld().getPlayers(), p, c);
	}
	
	private static final List<Player> processing = new ArrayList<>();
	
	public void processCommands(Player target) {
		if(cmds!=null)
		for(String f : cmds)
			TheAPI.sudoConsole(TabList.replace(f, target, true));
		processing.remove(target);
	}
	
	public void kickBack(Player p) {
        p.setVelocity(p.getLocation().getDirection().multiply(-1).normalize().setY(0.3));
        p.setFallDistance(0);
	}

	public boolean canEnter(Player p) {
		if(perm==null||p.hasPermission(perm)) {
			if(cooldown==0) {
				return true;
			}else {
				if(perplayer) {
					CooldownAPI cd = new CooldownAPI(p);
					if(cd.expired("portals."+id)) {
						cd.createCooldown("portals."+id, cooldown);
						return true;
					}
				}else {
					if(lastEnter-System.currentTimeMillis()/50+cooldown <= 0) {
						lastEnter=System.currentTimeMillis()/50;
						return true;
					}
				}
			}
		}
		return false;
	}
	
	static final List<Portal> portals = new ArrayList<>();
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

	public static void unload(World w) {
		if(w==null)return;
		portals.removeIf(p -> p.a.getWorld().equals(w));
	}
	
	public static void load(World w) {
		if(w==null||Loader.portals==null)return;
		for(String s : Loader.portals.getKeys()) {
			if(Loader.portals.get(s+".pos.1")==null||Loader.portals.get(s+".pos.2")==null)continue;
			if(Position.fromString(Loader.portals.getString(s+".pos.1")).getWorld()!=null &&
					Position.fromString(Loader.portals.getString(s+".pos.1")).getWorld().equals(w)) {
				Portal p = new Portal(s,Loader.portals.getDouble(s+".cooldown")
						,Loader.portals.getBoolean(s+".perPlayerCooldown")
						,Loader.portals.getBoolean(s+".kickBack")
						,Position.fromString(Loader.portals.getString(s+".pos.1")), 
						Position.fromString(Loader.portals.getString(s+".pos.2")), 
						makeParticle(Loader.portals.getString(s+".particle")), 
						Loader.portals.getStringList(s+".cmds"), 
						Loader.portals.getString(s+".server"), 
						Loader.portals.getStringList(s+".bcmds"), 
						Loader.portals.getString(s+".permission"), 
						Loader.portals.getInt(s+".wait"));
				portals.add(p);
			}
		}
	}
	
	public static void reload() {
		unload();
		if(Loader.portals==null)return;
		for(String s : Loader.portals.getKeys()) {
			if(Loader.portals.get(s+".pos.1")==null||Loader.portals.get(s+".pos.2")==null
					||Position.fromString(Loader.portals.getString(s+".pos.1")).getWorld()==null)continue;
			Portal p = new Portal(s,Loader.portals.getDouble(s+".cooldown")
					,Loader.portals.getBoolean(s+".perPlayerCooldown")
					,Loader.portals.getBoolean(s+".kickBack")
					,Position.fromString(Loader.portals.getString(s+".pos.1")), 
					Position.fromString(Loader.portals.getString(s+".pos.2")), 
					makeParticle(Loader.portals.getString(s+".particle")), 
					Loader.portals.getStringList(s+".cmds"), 
					Loader.portals.getString(s+".server"), 
					Loader.portals.getStringList(s+".bcmds"), 
					Loader.portals.getString(s+".permission"), 
					Loader.portals.getInt(s+".wait"));
			portals.add(p);
		}
		if(!portals.isEmpty()) {
			task=new Tasker() {
				public void run() {
					for(Portal a : portals) {
						if(a.a.getWorld()!=null && !a.a.getWorld().getPlayers().isEmpty())
							try {
								a.spawnParticles();
							} catch (Exception e) {
							}
					}
				}
			}.runRepeating(5, 10);
			
			moveTask=new Tasker() {
				final HashMap<Player, Portal> inPortal = new HashMap<>();
				public void run() {
					for(Portal a : portals) {
						for(Player p : a.a.getWorld().getPlayers()) {
							if(processing.contains(p))continue;
							boolean is = isInside(p.getLocation(), a.a, a.b);
							if(is) {
								Portal f = inPortal.get(p);
								if(f==null || !f.equals(a)) {
									if(a.canEnter(p)) {
										processing.add(p);
										if(a.server!=null && Loader.hasBungee) {
											ByteArrayDataOutput d = ByteStreams.newDataOutput();
											d.writeUTF("portal");
											d.writeUTF(p.getName());
											d.writeUTF(a.server);
											d.writeUTF(a.wait);
											String ac = Json.writer().simpleWrite(a.bcmds);
											while(ac.length()>35000) {
												d.writeUTF(ac.substring(0, 35000));
												ac=ac.substring(35000);
											}
											if(!ac.equals(""))
												d.writeUTF(ac);
											p.sendPluginMessage(Loader.getInstance, "scr:community", d.toByteArray());
										}
										NMSAPI.postToMainThread(() -> a.processCommands(p));
									}else if(a.kickBack)a.kickBack(p);
								}else
									if(a.equals(inPortal.get(p)))
										inPortal.remove(p);
							}
						}
					}
				}
			}.runRepeating(40, 1);
		}
	}
	
	private static Particle makeParticle(String string) {
		if(string==null)return null;
		try {
		if(string.contains("{")&&string.contains("}")) {
			String particle = string.split("\\{")[0];
			String[] values = string.split("\\{")[1].split("\\}")[0].split(",");
			if(particle.equalsIgnoreCase("dust")||particle.equalsIgnoreCase("redstone")||particle.equalsIgnoreCase("dust_color_transition"))
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
			if(particle.equalsIgnoreCase("item")||particle.equalsIgnoreCase("crack_item"))
				return new Particle(particle, new ParticleData.ItemOptions(values.length>=2?new ItemStack(XMaterial.matchXMaterial(values[0]).getMaterial(),1,StringUtils.getByte(values[1])):
					XMaterial.matchXMaterial(values[0]).parseItem()));
			return new Particle(particle);
			}
		return new Particle(string);
		}catch(Exception | NoSuchFieldError err) {
			return null;
		}
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
