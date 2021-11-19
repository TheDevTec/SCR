package me.devtec.servercontrolreloaded.utils.skins;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import com.google.common.collect.ForwardingMultimap;
import com.google.common.hash.Hashing;
import com.mojang.authlib.properties.Property;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.bungeecord.BungeeListener;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StreamUtils;
import me.devtec.theapi.utils.datakeeper.User;
import me.devtec.theapi.utils.json.Json;
import me.devtec.theapi.utils.reflections.Ref;
import me.devtec.theapi.utils.theapiutils.LoaderClass;

public class SkinManager {
	private static final String URL_FORMAT = "https://api.mineskin.org/generate/url?url=%s&%s",
			USER_FORMAT="https://api.ashcon.app/mojang/v2/user/%s";
	private static final HashMap<String, SkinData> playerSkins = new HashMap<>();
	private static final HashMap<String, SkinData> generator = new HashMap<>();
	
	public static void saveGenerator() {
		
	}
	
	private static String getSkinType(java.awt.image.BufferedImage image) {
		final byte[] pixels = ((java.awt.image.DataBufferByte) image.getRaster().getDataBuffer()).getData();
		int argb = ((int) pixels[4002] & 0xff);
		argb += (((int) pixels[4003] & 0xff) << 8);
		argb += (((int) pixels[4004] & 0xff) << 16);
		return argb==2631720?"steve":"alex";
    }
	
	@SuppressWarnings("unchecked")
	public static synchronized void generateSkin(String urlOrName, SkinCallback onFinish, boolean override) {
		if(urlOrName==null)return;
		new Tasker() {
			public void run() {
				if(generator.containsKey(urlOrName) && !override) {
					if(onFinish!=null)
						onFinish.run(generator.get(urlOrName));
					return;
				}
				if(urlOrName.toLowerCase().startsWith("https://")||urlOrName.toLowerCase().startsWith("http://")) {
					try {
						java.net.URLConnection connection = new URL(urlOrName).openConnection();
						connection.setRequestProperty("User-Agent", "ServerControlReloaded-JavaClient");
						HttpURLConnection conn = (HttpURLConnection)new URL(String.format(URL_FORMAT, urlOrName, "name=DevTec&model="+getSkinType(ImageIO.read(connection.getInputStream()))+"&visibility=1")).openConnection();
						conn.setRequestProperty("User-Agent", "ServerControlReloaded-JavaClient");
						conn.setRequestProperty("Accept-Encoding", "gzip");
						conn.setRequestMethod("POST");
						conn.setConnectTimeout(20000);
						conn.setReadTimeout(20000);
						conn.connect();
						Map<String, Object> text = (Map<String, Object>) Json.reader().simpleRead(StreamUtils.fromStream(new GZIPInputStream(conn.getInputStream())));
						SkinData data = new SkinData();
						if(!text.containsKey("error")) {
							data.signature=(String) ((Map<String, Object>)((Map<String, Object>)text.get("data")).get("texture")).get("signature");
							data.value=(String) ((Map<String, Object>)((Map<String, Object>)text.get("data")).get("texture")).get("value");
							data.url=(String) ((Map<String, Object>)((Map<String, Object>)text.get("data")).get("texture")).get("url");
							data.uuid=UUID.randomUUID();
							data.slim=text.get("model").equals("alex");
						}
						data.skinName=urlOrName;
						generator.put(urlOrName, data);
						if(onFinish!=null)
						onFinish.run(data);
					}catch(Exception err) {}
					return;
				}
				try {
					HttpURLConnection conn = (HttpURLConnection)new URL(String.format(USER_FORMAT, urlOrName)).openConnection();
					conn.setRequestProperty("User-Agent", "ServerControlReloaded-JavaClient");
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(20000);
					conn.setReadTimeout(20000);
					conn.connect();
					Map<String, Object> text = (Map<String, Object>) Json.reader().simpleRead(StreamUtils.fromStream(conn.getInputStream()));
					SkinData data = new SkinData();
					if(!text.containsKey("error")) {
						data.signature=(String) ((Map<String, Object>)((Map<String, Object>)text.get("textures")).get("raw")).get("signature");
						data.value=(String) ((Map<String, Object>)((Map<String, Object>)text.get("textures")).get("raw")).get("value");
						data.url=(String) ((Map<String, Object>)((Map<String, Object>)text.get("textures")).get("skin")).get("url");
						data.slim=(boolean)((Map<String, Object>)text.get("textures")).get("slim");
						data.uuid=Bukkit.getOfflinePlayer(urlOrName).getUniqueId();
					}
					data.skinName=urlOrName;
					generator.put(urlOrName, data);
					if(onFinish!=null)
					onFinish.run(data);
				}catch(Exception err) {}
		}}.runTask();
	}
	
	private static Object remove, add;
	private static Method oldRemove, oldAdd;
	private static final Class<?> cc = Ref.nms("WorldSettings$EnumGamemode")==null?Ref.nmsOrOld("world.level.EnumGamemode","EnumGamemode"):Ref.nms("WorldSettings$EnumGamemode");
	private static Constructor<?> infoC, headC, handC, respawnC, posC, expC;
	static {
		headC = Ref.constructor(Ref.nmsOrOld("network.protocol.game.PacketPlayOutEntityHeadRotation","PacketPlayOutEntityHeadRotation"), Ref.nmsOrOld("world.entity.Entity","Entity"), byte.class);
		handC = Ref.constructor(Ref.nmsOrOld("network.protocol.game.PacketPlayOutHeldItemSlot","PacketPlayOutHeldItemSlot"), int.class);
		if(TheAPI.isNewerThan(7)) {
			remove=Ref.getNulled(Ref.nmsOrOld("network.protocol.game.PacketPlayOutPlayerInfo$EnumPlayerInfoAction","PacketPlayOutPlayerInfo$EnumPlayerInfoAction"),TheAPI.isNewerThan(16)?"b":"REMOVE_PLAYER");
			add=Ref.getNulled(Ref.nmsOrOld("network.protocol.game.PacketPlayOutPlayerInfo$EnumPlayerInfoAction","PacketPlayOutPlayerInfo$EnumPlayerInfoAction"),TheAPI.isNewerThan(16)?"a":"ADD_PLAYER");
			infoC = Ref.constructor(Ref.nmsOrOld("network.protocol.game.PacketPlayOutPlayerInfo","PacketPlayOutPlayerInfo"), Ref.nmsOrOld("network.protocol.game.PacketPlayOutPlayerInfo$EnumPlayerInfoAction","PacketPlayOutPlayerInfo$EnumPlayerInfoAction"),TheAPI.isNewerThan(16)?Collection.class:Iterable.class);
		}else {
			oldRemove=Ref.method(Ref.nms("PacketPlayOutPlayerInfo"), "removePlayer", Ref.nms("EntityPlayer"));
			oldAdd=Ref.method(Ref.nms("PacketPlayOutPlayerInfo"), "addPlayer", Ref.nms("EntityPlayer"));
		}
		//EXP PACKET
		expC =Ref.constructor(Ref.nmsOrOld("network.protocol.game.PacketPlayOutExperience","PacketPlayOutExperience"), float.class, int.class, int.class);
		//POSITION PACKET
		if(TheAPI.isOlderThan(8)) { //1.7
			posC=Ref.constructor(Ref.nms("PacketPlayOutPosition"), double.class, double.class, double.class, float.class, float.class, boolean.class);
		}else if(TheAPI.isOlderThan(9)) { //1.8
			posC=Ref.constructor(Ref.nms("PacketPlayOutPosition"), double.class, double.class, double.class, float.class, float.class, Set.class);
		}else //1.9+
			posC=Ref.constructor(Ref.nmsOrOld("network.protocol.game.PacketPlayOutPosition","PacketPlayOutPosition"), double.class, double.class, double.class, float.class, float.class, Set.class, int.class);
		if(posC==null)//1.17+
			posC=Ref.constructor(Ref.nmsOrOld("network.protocol.game.PacketPlayOutPosition","PacketPlayOutPosition"), double.class, double.class, double.class, float.class, float.class, Set.class, int.class,boolean.class);
		//RESPAWN PACKET
		if(TheAPI.isNewerThan(16)) { //1.17+
			respawnC=Ref.constructor(Ref.nmsOrOld("network.protocol.game.PacketPlayOutRespawn","PacketPlayOutRespawn"),Ref.nmsOrOld("world.level.dimension.DimensionManager","DimensionManager"), Ref.nmsOrOld("resources.ResourceKey","ResourceKey"), long.class, cc, cc, boolean.class, boolean.class, boolean.class);
		}else
		if(TheAPI.isNewerThan(15)) { //1.16
			if(TheAPI.getServerVersion().split("_")[2].equals("R1"))
			respawnC=Ref.constructor(Ref.nms("PacketPlayOutRespawn"),Ref.nms("ResourceKey"), Ref.nms("ResourceKey"), long.class, cc, cc, boolean.class, boolean.class, boolean.class);
			else
				respawnC=Ref.constructor(Ref.nms("PacketPlayOutRespawn"),Ref.nms("DimensionManager"), Ref.nms("ResourceKey"), long.class, cc, cc, boolean.class, boolean.class, boolean.class);
		}else if(TheAPI.isNewerThan(14)) { //1.15
			respawnC=Ref.constructor(Ref.nms("PacketPlayOutRespawn"),Ref.nms("DimensionManager"), long.class, Ref.nms("WorldType"), cc);
		}else if(TheAPI.isNewerThan(13)) { //1.14
			respawnC=Ref.constructor(Ref.nms("PacketPlayOutRespawn"),Ref.nms("DimensionManager"), Ref.nms("WorldType"), cc);
		}else //1.7-1.13
			respawnC=Ref.constructor(Ref.nms("PacketPlayOutRespawn"),int.class, Ref.nms("EnumDifficulty"), Ref.nms("WorldType"), cc);
	}
	
	private static final Set<?> sset = new HashSet<>();

	private static Method set = Ref.method(Ref.getClass("net.minecraft.util.com.google.common.collect.ForwardingMultimap"), "put", Object.class, Object.class);
	private static final Method cf = Ref.method(Ref.nmsOrOld("world.level.biome.BiomeManager","BiomeManager"), "a", long.class);

	static Field res;
	public static synchronized void loadSkin(Player player, SkinData data) {
		if(player==null || data==null || !data.isFinite())return;
		User user = TheAPI.getUser(player);
		user.set("skin.name", data.skinName);
		user.set("skin.value", data.value);
		user.setAndSave("skin.signature", data.signature);
		if(Loader.config.getBoolean("Options.Skins.DynmapSupport") && Bukkit.getPluginManager().getPlugin("dynmap")!=null) {
			new Tasker() {
				public void run() {
					DynmapSupport.sendHeadUpdate(player, data.url);
				}
			}.runTask();
		}
		playerSkins.put(player.getName(), data);
		if(Loader.hasBungee)
			BungeeListener.requestSkinUpdate(player,data);
		Object s = Ref.player(player);
		Object prop = Ref.invoke(Ref.invoke(s, "getProfile"),"getProperties");
		if(prop==null)return;
		if(TheAPI.isOlderThan(8)) {
			Ref.invoke(prop, "clear");
			Ref.invoke(prop, set, "textures", Ref.createProperty("textures", data.value, data.signature));
		}else {
			@SuppressWarnings("unchecked")
			ForwardingMultimap<String, Property> e = (ForwardingMultimap<String, Property>) prop;
			e.clear();
			e.put("textures", new Property("textures", data.value, data.signature));
		}
		Object destroy = LoaderClass.nmsProvider.packetEntityDestroy(player.getEntityId());
		Object remove, add;
		if(TheAPI.isOlderThan(8)) {
			remove=Ref.invokeNulled(oldRemove, s);
			add=Ref.invokeNulled(oldAdd, s);
		}else {
			if(TheAPI.isNewerThan(16)) {
				Collection<?> iterable = Collections.singletonList(s);
				remove=Ref.newInstance(infoC, SkinManager.remove, iterable);
				add = Ref.newInstance(infoC, SkinManager.add, iterable);
			}else {
				Iterable<?> iterable = Collections.singletonList(s);
				remove=Ref.newInstance(infoC, SkinManager.remove, iterable);
				add = Ref.newInstance(infoC, SkinManager.add, iterable);
			}
		}
		Object spawn = LoaderClass.nmsProvider.packetNamedEntitySpawn(s);
		Object head = Ref.newInstance(headC, s, (byte)((float)(Ref.get(s, TheAPI.isNewerThan(16)?"aZ":"yaw"))*256F/360F));
		for(Player p : API.getPlayersThatCanSee(player)) {
			Ref.sendPacket(p, remove);
			Ref.sendPacket(p, add);
			if(p == player) {
				Ref.sendPacket(p, remove);
				Ref.sendPacket(p, add);
				Object w = Ref.world(p.getWorld());
				Location a = p.getLocation();
				
				Object packetMetadata = LoaderClass.nmsProvider.packetEntityMetadata(player), packetRespawn, packetPosition, packetExp, packetHeldSlot = Ref.newInstance(handC, p.getInventory().getHeldItemSlot());
				
				//RESPAWN PACKET
				if(TheAPI.isNewerThan(16)) { //1.17+
					if(res==null)
					for(Field f : Ref.getDeclaredFields(w.getClass().getSuperclass()))
						if(f.getType()==Ref.nmsOrOld("resources.ResourceKey","ResourceKey"))
							if(f.getName().equals("G"))res=f;
					packetRespawn=Ref.newInstance(respawnC, Ref.invoke(w, "getDimensionManager"), Ref.invoke(w, "getDimensionKey")==null?Ref.get(w, res):Ref.invoke(w, "getDimensionKey"), 
							Ref.invokeNulled(cf, a.getWorld().getSeed()), 
							Ref.invoke(Ref.get(s, "d"),"getGameMode"), Ref.invoke(Ref.get(s, "d"),"c"), 
							false, a.getWorld().getWorldType()==WorldType.FLAT, true);
				}else
				if(TheAPI.isNewerThan(15)) { //1.16
					Object key = Ref.invoke(w, "getDimensionKey");
					if(key==null)key=Ref.get(w, "dimensionKey");
					if(TheAPI.getServerVersion().split("_")[2].equals("R1"))
						packetRespawn=Ref.newInstance(respawnC, Ref.invoke(w, "getTypeKey"), key, Ref.invokeNulled(cf, a.getWorld().getSeed()), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"), Ref.invoke(Ref.get(s, "playerInteractManager"),"c"), false, a.getWorld().getWorldType()==WorldType.FLAT, true);	
					else {
						packetRespawn=Ref.newInstance(respawnC, Ref.invoke(w, "getDimensionManager"), key, Ref.invokeNulled(cf, a.getWorld().getSeed()), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"), Ref.invoke(Ref.get(s, "playerInteractManager"),"c"), false, a.getWorld().getWorldType()==WorldType.FLAT, true);
					}
				}else if(TheAPI.isNewerThan(14)) { //1.15
					packetRespawn=Ref.newInstance(respawnC, Ref.invoke(Ref.invoke(Ref.get(w,"worldProvider"), "getDimensionManager"),"getType"), Hashing.sha256().hashLong(a.getWorld().getSeed()).asLong(), Ref.invoke(Ref.invoke(w, "getWorldData"),"getType"), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"));
				}else if(TheAPI.isNewerThan(13)) { //1.14
					packetRespawn=Ref.newInstance(respawnC, Ref.get(w, "dimension"), Ref.invoke(Ref.invoke(w, "getWorldData"),"getType"), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"));
				}else //1.7 - 1.13
					packetRespawn=Ref.newInstance(respawnC, player.getWorld().getEnvironment().getId(), Ref.invoke(w, "getDifficulty"), Ref.invoke(Ref.invoke(w, "getWorldData"),"getType"), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"));
				
				//POSITION PACKET
				if(TheAPI.isOlderThan(8)) { //1.7
					packetPosition=Ref.newInstance(posC, a.getX(), a.getY(), a.getZ(), a.getYaw(), a.getPitch(), false);
				}else if(TheAPI.isOlderThan(9)) { //1.8
					packetPosition=Ref.newInstance(posC, a.getX(), a.getY(), a.getZ(), a.getYaw(), a.getPitch(), sset);
				}else //1.9+
					if(TheAPI.isNewerThan(16))
						packetPosition=Ref.newInstance(posC, a.getX(), a.getY(), a.getZ(), a.getYaw(), a.getPitch(), sset, 0, false);
					else
						packetPosition=Ref.newInstance(posC, a.getX(), a.getY(), a.getZ(), a.getYaw(), a.getPitch(), sset, 0);
				
				//EXPERIENCE PACKET
				packetExp=Ref.newInstance(expC, p.getExp(), p.getTotalExperience(), p.getExpToLevel());
				
				//SEND PACKETS
				Ref.sendPacket(p, packetRespawn);
				Ref.sendPacket(p, packetPosition);
				Ref.sendPacket(p, packetHeldSlot);
				Ref.sendPacket(p, packetMetadata);
				if(packetExp!=null)
					Ref.sendPacket(p, packetExp);
				
				//TRIGGER UPDATES
				p.updateInventory();
			}else {
				Ref.sendPacket(p, destroy);
				Ref.sendPacket(p, spawn);
				Ref.sendPacket(p, head);
			}
		}
	}

	public static synchronized SkinData getSkin(String player) {
		return playerSkins.get(player);
	}
}
