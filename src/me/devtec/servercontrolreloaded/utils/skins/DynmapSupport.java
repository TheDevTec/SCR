package me.devtec.servercontrolreloaded.utils.skins;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.dynmap.DynmapCore;
import org.dynmap.PlayerFaces;
import org.dynmap.SkinUrlProvider;
import org.dynmap.bukkit.DynmapPlugin;
import org.dynmap.common.DynmapPlayer;

import me.devtec.theapi.utils.reflections.Ref;

public class DynmapSupport {
	static DynmapCore core;
	static Constructor<?> sched = Ref.constructor(Ref.getClass("org.dynmap.PlayerFaces$LoadPlayerImages"), PlayerFaces.class, String.class, String.class, UUID.class, SkinUrlProvider.class);
	public static void init() {
		core = (DynmapCore) Ref.get(DynmapPlugin.plugin, "core");
		core.skinUrlProvider=new SkinUrlProvider() {
			public URL getSkinUrl(String playerName) {
				try {
					String url = SkinManager.getSkin(playerName).url;
					for(DynmapPlayer p : core.getServer().getOnlinePlayers())
						if(p.getName().equals(playerName)) {
							Ref.set(p, "skinurl", url);
							break;
						}
					return new URL(url);
				} catch (Exception e) {
					return null;
				}
			}
		};
		Ref.set(core.playerfacemgr, "fetchskins", true);
		Ref.set(core.playerfacemgr, "refreshskins", true);
	}
	
	public static void sendHeadUpdate(Player player, String url) {
		((Runnable)Ref.newInstance(sched, core.playerfacemgr, player.getName(), url, player.getUniqueId(), core.skinUrlProvider)).run();
	}
}
