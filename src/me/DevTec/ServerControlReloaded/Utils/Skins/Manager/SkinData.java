package me.DevTec.ServerControlReloaded.Utils.Skins.Manager;

import me.devtec.theapi.utils.json.Writer;

import java.util.HashMap;
import java.util.UUID;

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
		HashMap<String, String> data = new HashMap<>();
		data.put("uuid", uuid.toString());
		data.put("texture.value", value);
		data.put("texture.signature", signature);
		data.put("texture.url", url);
		data.put("texture.slim", slim+"");
		return Writer.write(data);
	}
}
