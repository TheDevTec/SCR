package me.devtec.servercontrolreloaded.utils.skins;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.google.common.collect.ForwardingMultimap;
import com.mojang.authlib.properties.Property;

import me.devtec.servercontrolreloaded.scr.API;
import me.devtec.servercontrolreloaded.scr.Loader;
import me.devtec.servercontrolreloaded.utils.bungeecord.BungeeListener;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.scheduler.Tasker;
import me.devtec.theapi.utils.StreamUtils;
import me.devtec.theapi.utils.datakeeper.User;
import me.devtec.theapi.utils.json.Json;
import me.devtec.theapi.utils.nms.NmsProvider.PlayerInfoType;
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
	
	private static Method set = Ref.method(Ref.getClass("net.minecraft.util.com.google.common.collect.ForwardingMultimap"), "put", Object.class, Object.class);

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
		Object prop = Ref.invoke(Ref.invoke(s, TheAPI.isNewerThan(17)?"fp":"getProfile"),"getProperties");
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
		//SETUP PACKETS
		Object destroy = LoaderClass.nmsProvider.packetEntityDestroy(player.getEntityId());
		Object remove = LoaderClass.nmsProvider.packetPlayerInfo(PlayerInfoType.REMOVE_PLAYER, player), add = LoaderClass.nmsProvider.packetPlayerInfo(PlayerInfoType.ADD_PLAYER, player);
		Object spawn = LoaderClass.nmsProvider.packetNamedEntitySpawn(s);
		Object head = LoaderClass.nmsProvider.packetEntityHeadRotation(player);
		
		for(Player p : API.getPlayersThatCanSee(player)) {
			Ref.sendPacket(p, remove);
			Ref.sendPacket(p, add);
			if(p == player) {
				//SETUP PACKETS
				Location a = p.getLocation();
				Object packetMetadata = LoaderClass.nmsProvider.packetEntityMetadata(player), packetRespawn = LoaderClass.nmsProvider.packetRespawn(player), 
						packetPosition = LoaderClass.nmsProvider.packetPosition(a.getX(), a.getY(), a.getZ(), a.getYaw(), a.getPitch()), 
						packetExp = LoaderClass.nmsProvider.packetExp(p.getExp(), p.getTotalExperience(), p.getExpToLevel()), 
						packetHeldSlot = LoaderClass.nmsProvider.packetHeldItemSlot(p.getInventory().getHeldItemSlot());
				//SEND PACKETS
				Ref.sendPacket(p, remove);
				Ref.sendPacket(p, add);
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
