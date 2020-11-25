package me.DevTec.ServerControlReloaded.Utils.Skins.Manager;

import java.util.UUID;

import me.DevTec.TheAPI.Utils.DataKeeper.Maps.NonSortedMap;
import me.DevTec.TheAPI.Utils.Json.Writer;

public class SkinData {
	public UUID uuid; //Nullable
	public String value;
	public String signature;
	public String url; //Nullable
	public boolean slim;
	
	public boolean isFinite() {
		return value != null && signature != null;
	}
	
	public String toString() {
		NonSortedMap<String, String> data = new NonSortedMap<>();
		data.put("uuid", uuid.toString());
		data.put("texture.value", value);
		data.put("texture.signature", signature);
		data.put("texture.url", url);
		data.put("texture.slim", slim+"");
		return Writer.write(data);
	}
}
