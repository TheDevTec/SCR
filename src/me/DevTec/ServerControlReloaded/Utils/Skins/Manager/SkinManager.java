package me.DevTec.ServerControlReloaded.Utils.Skins.Manager;

import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import me.DevTec.ServerControlReloaded.Utils.Skins.mineskin.MineskinClient;
import me.DevTec.ServerControlReloaded.Utils.Skins.mineskin.data.Skin;
import me.DevTec.ServerControlReloaded.Utils.Skins.mineskin.data.SkinCallback;
import me.DevTec.ServerControlReloaded.Utils.Skins.mineskin.data.SkinData;
import me.DevTec.ServerControlReloaded.Utils.Skins.mineskin.data.Texture;
import me.DevTec.TheAPI.TheAPI;
import me.DevTec.TheAPI.Scheduler.Tasker;
import me.DevTec.TheAPI.Utils.DataKeeper.Maps.NonSortedMap;
import me.DevTec.TheAPI.Utils.NMS.NMSAPI;
import me.DevTec.TheAPI.Utils.Reflections.Ref;

public class SkinManager {
	private static Map<String, SkinData> skins = new NonSortedMap<>();
	private static Map<UUID, SkinData> generator = new NonSortedMap<>();
	private static MineskinClient client = new MineskinClient();
	private static Object parser = TheAPI.isOlderThan(8)?new net.minecraft.util.com.google.gson.JsonParser():new JsonParser();
	
	public static synchronized UUID generateSkin(String urlOrName, SkinCallable onFinish) {
		UUID random = UUID.randomUUID();
		new Tasker() {
			public void run() {
				if(urlOrName.toLowerCase().startsWith("https://")||urlOrName.toLowerCase().startsWith("http://")) {
					client.generateUrl(urlOrName, new SkinCallback() {
						public void done(Skin skin) {
							generator.put(random, skin.data);
							if(onFinish!=null)
							onFinish.run(random, skin.data);
						}
					});
				}
				SkinData data = new SkinData();
				data.uuid=random;
				Texture tex = new Texture();
				tex.url=urlOrName;
				try {
					String texture = null, signature = null;
					if(TheAPI.isOlderThan(8)) {
						net.minecraft.util.com.google.gson.JsonObject textureProperty = ((net.minecraft.util.com.google.gson.JsonParser)parser).parse(new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + ((net.minecraft.util.com.google.gson.JsonParser)parser).parse(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + urlOrName)
			        			.openStream())).getAsJsonObject().get("id").getAsString() + "?unsigned=false").openStream())).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
			        	texture = textureProperty.get("value").getAsString();
			        	signature = textureProperty.get("signature").getAsString();
					}else {
			        	JsonObject textureProperty = ((JsonParser)parser).parse(new InputStreamReader(new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + ((JsonParser)parser).parse(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + urlOrName)
			        			.openStream())).getAsJsonObject().get("id").getAsString() + "?unsigned=false").openStream())).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
			        	texture = textureProperty.get("value").getAsString();
			        	signature = textureProperty.get("signature").getAsString();
					}
		    		tex.signature=signature;
		    		tex.value=texture;
		    	} catch (Exception err) {}
				data.texture=tex;
				generator.put(random, data);
				if(onFinish!=null)
				onFinish.run(random, data);
		}}.runTask();
		return random; //queue ID
	}
	
	public static synchronized SkinData getGeneratedSkin(UUID id) {
		return generator.getOrDefault(id, null);
	}
	
	public static synchronized void setSkin(String player, SkinData data) {
		skins.put(player, data);
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
		if(player==null || data==null || data.texture==null || data.texture.signature==null || data.texture.value==null)return;
		Object s = Ref.player(player);
		Object prop = Ref.invoke(Ref.invoke(s, "getProfile"),"getProperties");
		Ref.invoke(prop, "clear");
		if(put==null)
			for(Method m : prop.getClass().getMethods())
				if(m.getName().equals("put"))
					put=m;
		Ref.invoke(prop, put, "textures", Ref.createProperty("textures", data.texture.value, data.texture.signature));
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
		return skins.getOrDefault(player, null);
	}
}
