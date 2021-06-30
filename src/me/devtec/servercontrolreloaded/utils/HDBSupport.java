package me.devtec.servercontrolreloaded.utils;

import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;

import me.arcaniax.hdb.api.HeadDatabaseAPI;
import me.devtec.theapi.apis.ItemCreatorAPI;
import me.devtec.theapi.utils.nms.NMSAPI;

public class HDBSupport {
	static Object api;
	static {
		try {
			api = new HeadDatabaseAPI();
		}catch(Exception | NoClassDefFoundError e) {}
	}
	
	public static ItemStack parse(String text) {
		if(api!=null && text.toLowerCase().startsWith("hdb:"))
		return ((HeadDatabaseAPI)api).getItemHead(text.substring(4));
		return null;
	}
	
	public static ItemStack parse(String text, String nbt) {
		ItemStack head = parse(text);
		if(head==null)head=ItemCreatorAPI.createHead(1, null, SkullType.PLAYER);
		return NMSAPI.setNBT(head, nbt);
	}
}
