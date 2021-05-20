package me.devtec.servercontrolreloaded.utils;

import java.util.List;

import com.mojang.authlib.GameProfile;

import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.nms.NMSAPI;
import me.devtec.theapi.utils.packetlistenerapi.PacketListener;
import me.devtec.theapi.utils.reflections.Ref;

public class InstantTAB {
	PacketListener a = new PacketListener() {
		Class<?> p = Ref.nms("PacketPlayOutPlayerInfo");
		@Override
		public boolean PacketPlayOut(String player, Object packet, Object channel) {
			if(packet.getClass()==p) {
				if(!setting.tab || !setting.tab_name)return false;
				if(TheAPI.isOlderThan(8)) {
					Ref.set(packet, "username", TabList.getTabListName(TheAPI.getPlayer(((GameProfile)Ref.get(packet, "player")).getName())));
				}else {
					for(Object info : (List<?>)Ref.get(packet, "b"))
					Ref.set(info, "e", NMSAPI.getFixedIChatBaseComponent(TabList.getTabListName(TheAPI.getPlayer(((GameProfile)Ref.get(info, "d")).getName()))));
				}
			}
			return false;
		}
		
		@Override
		public boolean PacketPlayIn(String player, Object packet, Object channel) {
			return false;
		}
	};
	public void reg() {
		a.register();
	}
	
	public void unreg() {
		a.unregister();
	}
}
