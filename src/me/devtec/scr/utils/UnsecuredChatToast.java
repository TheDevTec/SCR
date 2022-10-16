package me.devtec.scr.utils;

import me.devtec.shared.Ref;
import me.devtec.theapi.bukkit.packetlistener.PacketListener;
import net.minecraft.network.protocol.game.ClientboundServerDataPacket;

public class UnsecuredChatToast {
	static PacketListener listener;

	public static void load() {
		listener = new PacketListener() {

			@Override
			public boolean playOut(String player, Object packet, Object channel) {
				if (packet instanceof ClientboundServerDataPacket)
					Ref.set(packet, "d", true);
				return false;
			}

			@Override
			public boolean playIn(String player, Object packet, Object channel) {
				return false;
			}
		};
		listener.register();
	}

	public static void unload() {
		listener.unregister();
	}
}
