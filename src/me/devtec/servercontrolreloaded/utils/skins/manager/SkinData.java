package me.devtec.servercontrolreloaded.utils.skins.manager;

import java.util.HashMap;
import java.util.UUID;

import me.devtec.theapi.utils.json.JsonWriter;

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
		return JsonWriter.write(data);
	}
}
