package me.devtec.servercontrolreloaded.utils;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.dynmap.DynmapCore;
import org.dynmap.SkinUrlProvider;
import org.dynmap.bukkit.DynmapPlugin;
import org.dynmap.common.DynmapPlayer;

import me.devtec.servercontrolreloaded.utils.skins.manager.SkinManager;
import me.devtec.theapi.utils.reflections.Ref;

public class DynmapSupport {
	static DynmapCore core;
	static Constructor<?> sched = Ref.constructor(Ref.getClass("org.dynmap.PlayerFaces$LoadPlayerImages"), String.class, String.class, UUID.class, SkinUrlProvider.class);
	public static void init() {
		core = (DynmapCore) Ref.get(DynmapPlugin.plugin, "core");
		core.skinUrlProvider=new SkinUrlProvider() {
			public URL getSkinUrl(String playerName) {
				try {
					return new URL(SkinManager.getSkin(playerName).url);
				} catch (Exception e) {
					return null;
				}
			}
		};
	}
	
	public static void sendHeadUpdate(Player player, String url) {
		DynmapPlayer pp = null;
		for(DynmapPlayer p : core.getServer().getOnlinePlayers()) {
			if(p.getUUID().equals(player.getUniqueId())) {
				pp=p;
				break;
			}
		}
		if(pp==null)pp=core.getServer().getOfflinePlayer(player.getName());
		Ref.set(pp, "skinurl", url);
		Runnable runnable = (Runnable)Ref.newInstance(sched, pp.getName(), pp.getSkinURL(), pp.getUUID(), core.skinUrlProvider);
		runnable.run();
	}
}
