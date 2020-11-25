package me.DevTec.ServerControlReloaded.Utils.Skins.Manager;

import java.util.UUID;

import me.DevTec.ServerControlReloaded.Utils.Skins.mineskin.data.SkinData;

public interface SkinCallable {
	public void run(UUID uuid, SkinData data);
}
