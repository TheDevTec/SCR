package me.DevTec.ServerControlReloaded.Utils.Skins.Manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
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
	private static Class<?> cc = Ref.nms("WorldSettings$EnumGamemode")==null?Ref.nms("EnumGamemode"):Ref.nms("WorldSettings$EnumGamemode");
	private static Constructor<?> infoC, headC,handC,respawnC, posC;
	static {
		headC = Ref.constructor(Ref.nms("PacketPlayOutEntityHeadRotation"), Ref.nms("Entity"), byte.class);
		handC = Ref.constructor(Ref.nms("PacketPlayOutHeldItemSlot"), int.class);
		if(TheAPI.isNewerThan(7)) {
			remove=Ref.getNulled(Ref.nms("PacketPlayOutPlayerInfo$EnumPlayerInfoAction"),"REMOVE_PLAYER");
			add=Ref.getNulled(Ref.nms("PacketPlayOutPlayerInfo$EnumPlayerInfoAction"),"ADD_PLAYER");
			infoC = Ref.constructor(Ref.nms("PacketPlayOutPlayerInfo"), Ref.nms("PacketPlayOutPlayerInfo$EnumPlayerInfoAction"), Iterable.class);
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
			posC=Ref.constructor(Ref.nms("PacketPlayOutPosition"), double.class, double.class, double.class, float.class, float.class, Set.class, int.class);
		//RESPAWN PACKET
		if(TheAPI.isNewerThan(16)) { //1.17+
			respawnC=Ref.constructor(Ref.nms("PacketPlayOutRespawn"),Ref.nms("DimensionManager"), Ref.nms("ResourceKey"), long.class, cc, cc, boolean.class, boolean.class, boolean.class);
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
		Object s = Ref.player(player);
		Object prop = Ref.invoke(Ref.invoke(s, "getProfile"),"getProperties");
		Ref.invoke(prop, "clear");
		Ref.invoke(prop, set, "textures", Ref.createProperty("textures", data.value, data.signature));
		Object destroy = NMSAPI.getPacketPlayOutEntityDestroy(player.getEntityId());
		Object remove = null, add = null;
		if(TheAPI.isOlderThan(8)) {
			remove=Ref.invokeNulled(oldRemove, s);
			add=Ref.invokeNulled(oldAdd, s);
		}else {
			Iterable<?> iterable = Arrays.asList(s);
			remove=Ref.newInstance(infoC, SkinManager.remove, iterable);
			add = Ref.newInstance(infoC, SkinManager.add, iterable);
		}
		Object spawn = NMSAPI.getPacketPlayOutNamedEntitySpawn(s);
		Object head = Ref.newInstance(headC, s, (byte)((float)(Ref.get(s, "yaw"))*256F/360F));
		for(Player p : TheAPI.getPlayers()) {
			Ref.sendPacket(p, remove);
			Ref.sendPacket(p, add);
			if(p == player) {
				Object w = Ref.invoke(s, "getWorld");
				Location a = p.getLocation();
				Object re = null;
				if(TheAPI.isNewerThan(16)) { //1.17+
					re=Ref.newInstance(respawnC, Ref.invoke(w, "getDimensionManager"), Ref.invoke(w, "getDimensionKey"), Ref.invokeNulled(Ref.method(Ref.nms("BiomeManager"), "a", long.class), a.getWorld().getSeed()), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"), Ref.invoke(Ref.get(s, "playerInteractManager"),"c"), false, a.getWorld().getWorldType()==WorldType.FLAT, true);
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
					re=Ref.newInstance(respawnC, (byte)((player.getWorld().getEnvironment().getId() >= 0) ? -1 : 0), Ref.invoke(w, "getDifficulty"), Ref.invoke(Ref.invoke(w, "getWorldData"),"getType"), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"));
				Ref.sendPacket(p, re);
				Object pos = null;
				if(TheAPI.isOlderThan(8)) { //1.7
					pos=Ref.newInstance(posC, a.getX(), a.getY(), a.getZ(), a.getYaw(), a.getPitch(), false);
				}else if(TheAPI.isOlderThan(9)) { //1.8
					pos=Ref.newInstance(posC, a.getX(), a.getY(), a.getZ(), a.getYaw(), a.getPitch(), sset);
				}else //1.9+
					pos=Ref.newInstance(posC, a.getX(), a.getY(), a.getZ(), a.getYaw(), a.getPitch(), sset, 0);
				Ref.sendPacket(p, pos);
				Ref.sendPacket(p, Ref.newInstance(handC, p.getInventory().getHeldItemSlot()));
				Ref.sendPacket(p, NMSAPI.getPacketPlayOutEntityMetadata(s));
				p.updateInventory();
				Ref.invoke(s, "triggerHealthUpdate");
				Ref.invoke(s, "updateAbilities");
			}else {
				Ref.sendPacket(p, destroy);
				Ref.sendPacket(p, spawn);
				Ref.sendPacket(p, head);
			}
		}
	}
	
	public static synchronized SkinData getSkin(String player) {
		return playerSkins.getOrDefault(player, null);
	}
}
