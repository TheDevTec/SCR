package me.devtec.servercontrolreloaded.utils.skins.manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import com.google.common.hash.Hashing;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StreamUtils;
import me.devtec.theapi.utils.json.Reader;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.reflections.Ref;

public class SkinManager {
	private static final String URL_FORMAT = "https://api.mineskin.org/generate/url?url=%s&%s",
			USER_FORMAT="https://api.ashcon.app/mojang/v2/user/%s";
	private static HashMap<String, SkinData> playerSkins = new HashMap<>();
	private static HashMap<String, SkinData> generator = new HashMap<>();
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
						HttpURLConnection conn = (HttpURLConnection)new URL(String.format(URL_FORMAT, urlOrName, "name=none&model=steve&visibility=1")).openConnection();
						conn.setRequestProperty("User-Agent", "ServerControlReloaded-JavaClient");
						conn.setRequestProperty("Accept-Encoding", "gzip");
						conn.setRequestMethod("POST");
						conn.setConnectTimeout(20000);
						conn.setReadTimeout(20000);
						conn.connect();
						Map<String, Object> text = (Map<String, Object>) Reader.read(StreamUtils.fromStream(new GZIPInputStream(conn.getInputStream())));
						SkinData data = new SkinData();
						if(!text.containsKey("error")) {
							data.signature=(String) ((Map<String, Object>)((Map<String, Object>)text.get("data")).get("texture")).get("signature");
							data.value=(String) ((Map<String, Object>)((Map<String, Object>)text.get("data")).get("texture")).get("value");
							data.url=(String) ((Map<String, Object>)((Map<String, Object>)text.get("data")).get("texture")).get("url");
							data.uuid=UUID.randomUUID();
							data.slim=text.get("model").equals("alex");
						}
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
					Map<String, Object> text = (Map<String, Object>) Reader.read(StreamUtils.fromStream(conn.getInputStream()));
					SkinData data = new SkinData();
					if(!text.containsKey("error")) {
						data.signature=(String) ((Map<String, Object>)((Map<String, Object>)text.get("textures")).get("raw")).get("signature");
						data.value=(String) ((Map<String, Object>)((Map<String, Object>)text.get("textures")).get("raw")).get("value");
						data.url=(String) ((Map<String, Object>)((Map<String, Object>)text.get("textures")).get("skin")).get("url");
						data.slim=(boolean)((Map<String, Object>)text.get("textures")).get("slim");
						data.uuid=Bukkit.getOfflinePlayer(urlOrName).getUniqueId();
					}
					generator.put(urlOrName, data);
					if(onFinish!=null)
					onFinish.run(data);
				}catch(Exception err) {}
		}}.runTask();
	}
	
	private static Object remove, add;
	private static Method oldRemove, oldAdd;
	private static Class<?> cc = Ref.nms("WorldSettings$EnumGamemode")==null?Ref.nmsOrOld("world.level.EnumGamemode","EnumGamemode"):Ref.nms("WorldSettings$EnumGamemode");
	private static Constructor<?> infoC, headC,handC,respawnC, posC;
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
	
	private static Set<?> sset = new HashSet<>();

	private static Method set = Ref.method(Ref.getClass("com.google.common.collect.ForwardingMultimap"), "put", Object.class, Object.class);
	
	static {
		if(set==null)
			set = Ref.method(Ref.getClass("net.minecraft.util.com.google.common.collect.ForwardingMultimap"), "put", Object.class, Object.class);
	}
	
	public static synchronized void loadSkin(Player player, SkinData data) {
		if(player==null || data==null || !data.isFinite())return;
		playerSkins.put(player.getName(), data);
		if(Loader.hasBungee)
			sendBungee(player,data);
		Object s = Ref.player(player);
		Object prop = Ref.invoke(Ref.invoke(s, "getProfile"),"getProperties");
		if(prop==null)return;
		Ref.invoke(prop, "clear");
		Ref.invoke(prop, set, "textures", Ref.createProperty("textures", data.value, data.signature));
		Object destroy = NMSAPI.getPacketPlayOutEntityDestroy(player.getEntityId());
		Object remove = null, add = null;
		if(TheAPI.isOlderThan(8)) {
			remove=Ref.invokeNulled(oldRemove, s);
			add=Ref.invokeNulled(oldAdd, s);
		}else {
			if(TheAPI.isNewerThan(16)) {
				Collection<?> iterable = Arrays.asList(s);
				remove=Ref.newInstance(infoC, SkinManager.remove, iterable);
				add = Ref.newInstance(infoC, SkinManager.add, iterable);
			}else {
				Iterable<?> iterable = Arrays.asList(s);
				remove=Ref.newInstance(infoC, SkinManager.remove, iterable);
				add = Ref.newInstance(infoC, SkinManager.add, iterable);
			}
		}
		Object spawn = NMSAPI.getPacketPlayOutNamedEntitySpawn(s);
		Object head = Ref.newInstance(headC, s, (byte)((float)(Ref.get(s, TheAPI.isNewerThan(16)?"aZ":"yaw"))*256F/360F));
		for(Player p : TheAPI.getPlayers()) {
			Ref.sendPacket(p, remove);
			Ref.sendPacket(p, add);
			if(p == player) {
				Object w = Ref.world(p.getWorld());
				Location a = p.getLocation();
				Object re = null;
				if(TheAPI.isNewerThan(16)) { //1.17+
					re=Ref.newInstance(respawnC, Ref.invoke(w, "getDimensionManager"), Ref.invoke(w, "getDimensionKey"), Ref.invokeNulled(Ref.method(Ref.nmsOrOld("world.level.biome.BiomeManager","BiomeManager"), "a", long.class), a.getWorld().getSeed()), Ref.invoke(Ref.get(s, "d"),"getGameMode"), Ref.invoke(Ref.get(s, "d"),"c"), false, a.getWorld().getWorldType()==WorldType.FLAT, true);
				}else
				if(TheAPI.isNewerThan(15)) { //1.16
					if(TheAPI.getServerVersion().split("_")[2].equals("R1"))
						re=Ref.newInstance(respawnC, Ref.invoke(w, "getTypeKey"), Ref.invoke(w, "getDimensionKey"), Ref.invokeNulled(Ref.method(Ref.nms("BiomeManager"), "a", long.class), a.getWorld().getSeed()), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"), Ref.invoke(Ref.get(s, "playerInteractManager"),"c"), false, a.getWorld().getWorldType()==WorldType.FLAT, true);	
					else
						re=Ref.newInstance(respawnC, Ref.invoke(w, "getDimensionManager"), Ref.invoke(w, "getDimensionKey"), Ref.invokeNulled(Ref.method(Ref.nms("BiomeManager"), "a", long.class), a.getWorld().getSeed()), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"), Ref.invoke(Ref.get(s, "playerInteractManager"),"c"), false, a.getWorld().getWorldType()==WorldType.FLAT, true);
				}else if(TheAPI.isNewerThan(14)) { //1.15
					re=Ref.newInstance(respawnC, Ref.invoke(Ref.invoke(Ref.get(w,"worldProvider"), "getDimensionManager"),"getType"), Hashing.sha256().hashLong(a.getWorld().getSeed()).asLong(), Ref.invoke(Ref.invoke(w, "getWorldData"),"getType"), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"));
				}else if(TheAPI.isNewerThan(13)) { //1.14
					re=Ref.newInstance(respawnC, Ref.get(w, "dimension"), Ref.invoke(Ref.invoke(w, "getWorldData"),"getType"), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"));
				}else //1.7 - 1.13
					re=Ref.newInstance(respawnC, player.getWorld().getEnvironment().getId(), Ref.invoke(w, "getDifficulty"), Ref.invoke(Ref.invoke(w, "getWorldData"),"getType"), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"));
				Ref.sendPacket(p, re);
				Ref.invoke(p, "updateAbilities");
				Object pos = null;
				if(TheAPI.isOlderThan(8)) { //1.7
					pos=Ref.newInstance(posC, a.getX(), a.getY(), a.getZ(), a.getYaw(), a.getPitch(), false);
				}else if(TheAPI.isOlderThan(9)) { //1.8
					pos=Ref.newInstance(posC, a.getX(), a.getY(), a.getZ(), a.getYaw(), a.getPitch(), sset);
				}else //1.9+
					if(TheAPI.isNewerThan(16))
						pos=Ref.newInstance(posC, a.getX(), a.getY(), a.getZ(), a.getYaw(), a.getPitch(), sset, 0, false);
					else
						pos=Ref.newInstance(posC, a.getX(), a.getY(), a.getZ(), a.getYaw(), a.getPitch(), sset, 0);
				Ref.sendPacket(p, pos);
				Ref.sendPacket(p, Ref.newInstance(handC, p.getInventory().getHeldItemSlot()));
				p.updateInventory();
				Ref.invoke(s, "updateScaledHealth");
				Ref.invoke(s, "updateInventory");
				Ref.invoke(p, "triggerHealthUpdate");
				if (player.isOp()) {
					new Tasker() {
						public void run() {
		                    player.setOp(false);
		                    player.setOp(true);
						}
					}.runTaskSync();
	            }
			}else {
				Ref.sendPacket(p, destroy);
				Ref.sendPacket(p, spawn);
				Ref.sendPacket(p, head);
			}
		}
	}
	
	private static void sendBungee(Player player, SkinData data) {
		ByteArrayDataOutput d = ByteStreams.newDataOutput();
		d.writeUTF("skin");
		d.writeUTF(player.getName());
		d.writeUTF(data.value);
		d.writeUTF(data.signature);
		player.sendPluginMessage(Loader.getInstance, "scr:community", d.toByteArray());
	}

	public static synchronized SkinData getSkin(String player) {
		return playerSkins.get(player);
	}
}
