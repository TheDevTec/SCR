package me.DevTec.ServerControlReloaded.Utils.Skins.Manager;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import org.bukkit.Location;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Scheduler.Tasker;
import me.DevTec.TheAPI.Utils.StreamUtils;
import me.DevTec.TheAPI.Utils.DataKeeper.Maps.UnsortedMap;
import me.DevTec.TheAPI.Utils.Json.Reader;
import me.DevTec.TheAPI.Utils.NMS.NMSAPI;
import me.DevTec.TheAPI.Utils.Reflections.Ref;

public class SkinManager {
	private static final String URL_FORMAT = "https://api.mineskin.org/generate/url?url=%s&%s",
			USER_FORMAT="https://api.ashcon.app/mojang/v2/user/%s";
	private static UnsortedMap<String, SkinData> playerSkins = new UnsortedMap<>();
	private static UnsortedMap<String, SkinData> generator = new UnsortedMap<>();
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
				}
				try {
					HttpURLConnection conn = (HttpURLConnection)new URL(String.format(USER_FORMAT, urlOrName)).openConnection();
					conn.setRequestProperty("User-Agent", "ServerControlReloaded-JavaClient");
					conn.setRequestMethod("GET");
					conn.connect();
					Map<String, Object> text = (Map<String, Object>) Reader.read(StreamUtils.fromStream(conn.getInputStream()));
					SkinData data = new SkinData();
					if(!text.containsKey("error")) {
						data.signature=(String) ((Map<String, Object>)((Map<String, Object>)text.get("textures")).get("raw")).get("signature");
						data.value=(String) ((Map<String, Object>)((Map<String, Object>)text.get("textures")).get("raw")).get("value");
						data.url=(String) ((Map<String, Object>)((Map<String, Object>)text.get("textures")).get("skin")).get("url");
						data.slim=(boolean)((Map<String, Object>)text.get("textures")).get("slim");
						data.uuid=UUID.nameUUIDFromBytes(("OfflinePlayer:"+urlOrName).getBytes(StandardCharsets.UTF_8));
					}
					generator.put(urlOrName, data);
					if(onFinish!=null)
					onFinish.run(data);
				}catch(Exception err) {}
		}}.runTask();
	}
	
	public static synchronized void setSkin(String player, SkinData data) {
		playerSkins.put(player, data);
	}
	
	private static Object remove=Ref.getNulled(Ref.nms("PacketPlayOutPlayerInfo$EnumPlayerInfoAction"),"REMOVE_PLAYER"), add=Ref.getNulled(Ref.nms("PacketPlayOutPlayerInfo$EnumPlayerInfoAction"),"ADD_PLAYER");
	private static Method put, oldRemove, oldAdd;
	private static Constructor<?> infoC, headC= Ref.getConstructors(Ref.nms("PacketPlayOutEntityHeadRotation"))[0],
	respawnC=Ref.getConstructors(Ref.nms("PacketPlayOutRespawn"))[0], posC=Ref.getConstructors(Ref.nms("PacketPlayOutPosition"))[0],
	handC=Ref.getConstructors(Ref.nms("PacketPlayOutHeldItemSlot"))[0];
	static {
		if(TheAPI.isNewerThan(7)) {
			infoC = Ref.getConstructors(Ref.nms("PacketPlayOutPlayerInfo"))[2];
		}else {
			oldRemove=Ref.method(Ref.nms("PacketPlayOutPlayerInfo"), "removePlayer", Ref.nms("EntityPlayer"));
			oldAdd=Ref.method(Ref.nms("PacketPlayOutPlayerInfo"), "addPlayer", Ref.nms("EntityPlayer"));
		}
	}
	
	public static synchronized void loadSkin(Player player, SkinData data) {
		if(player==null || data==null || !data.isFinite())return;
		Object s = Ref.player(player);
		Object prop = Ref.invoke(Ref.invoke(s, "getProfile"),"getProperties");
		Ref.invoke(prop, "clear");
		if(put==null)
			for(Method m : prop.getClass().getMethods())
				if(m.getName().equals("put"))
					put=m;
		Ref.invoke(prop, put, "textures", Ref.createProperty("textures", data.value, data.signature));
		Object destroy = NMSAPI.getPacketPlayOutEntityDestroy(player.getEntityId());
		Object remove = null, add = null;
		if(TheAPI.isOlderThan(8)) {
			destroy=Ref.invokeNulled(oldRemove, s);
			add=Ref.invokeNulled(oldAdd, s);
		}else {
			Iterable<?> iterable = Arrays.asList(s);
			destroy=Ref.newInstance(infoC, SkinManager.remove, iterable);
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
				if(TheAPI.isNewerThan(15)) { //1.16
					if(TheAPI.getServerVersion().split("_")[2].equals("R1")) {
						re=Ref.newInstance(respawnC, Ref.invoke(w, "getTypeKey"), Ref.invoke(w, "getDimensionKey"), Ref.invokeNulled(Ref.method(Ref.nms("BiomeManager"), "a", long.class), a.getWorld().getSeed()), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"), Ref.invoke(Ref.get(s, "playerInteractManager"),"c"), false, a.getWorld().getWorldType()==WorldType.FLAT, true);	
					}else
					re=Ref.newInstance(respawnC, Ref.invoke(w, "getDimensionManager"), Ref.invoke(w, "getDimensionKey"), Ref.invokeNulled(Ref.method(Ref.nms("BiomeManager"), "a", long.class), a.getWorld().getSeed()), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"), Ref.invoke(Ref.get(s, "playerInteractManager"),"c"), false, a.getWorld().getWorldType()==WorldType.FLAT, true);
				}else if(TheAPI.isNewerThan(14)) { //1.15
					re=Ref.newInstance(respawnC, Ref.invoke(w, "getDimensionManager"), a.getWorld().getSeed(), Ref.invoke(Ref.invoke(w, "getWorldData"),"getType"), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"));
				}else if(TheAPI.isNewerThan(13)) { //1.14
					re=Ref.newInstance(respawnC, Ref.invoke(w, "getDimensionManager"), Ref.invoke(Ref.invoke(w, "getWorldData"),"getType"), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"));
				}else if(TheAPI.isNewerThan(12)) { //1.13
					re=Ref.newInstance(respawnC, Ref.invoke(Ref.invoke(w, "getEnvironment"),"getId"), Ref.invoke(w, "getDifficulty"), Ref.invoke(Ref.invoke(w, "getWorldData"),"getType"), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"));
				}else { //1.12 - 1.7
					re=Ref.newInstance(respawnC, Ref.invoke(w, "getDimensionManager"), Ref.invoke(Ref.invoke(w, "getWorldData"),"getType"), Ref.invoke(Ref.get(s, "playerInteractManager"),"getGameMode"));
				}
				Ref.sendPacket(player, re);
				Object pos = null;
				if(TheAPI.isOlderThan(8)) { //1.7
					pos=Ref.newInstance(posC, a.getX(), a.getY(), a.getZ(), a.getYaw(), a.getPitch(), false);
				}else if(TheAPI.isOlderThan(9)) { //1.8
					pos=Ref.newInstance(posC, a.getX(), a.getY(), a.getZ(), a.getYaw(), a.getPitch(), new HashSet<>());
				}else //1.9+
					pos=Ref.newInstance(posC, a.getX(), a.getY(), a.getZ(), a.getYaw(), a.getPitch(), new HashSet<>(), 0);
				Ref.sendPacket(player, pos);
				Ref.sendPacket(player, Ref.newInstance(handC, p.getInventory().getHeldItemSlot()));
				Ref.sendPacket(player, NMSAPI.getPacketPlayOutEntityMetadata(s));
				player.updateInventory();
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
