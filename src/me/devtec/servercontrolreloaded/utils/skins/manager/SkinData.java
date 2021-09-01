package me.devtec.servercontrolreloaded.utils.skins.manager;

import java.util.HashMap;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import me.devtec.theapi.utils.json.Json;

public class SkinData {
	@Nullable
	public UUID uuid;
	@Nonnull
	public String value;
	@Nonnull
	public String signature;
	@Nullable
	public String url;
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
		return Json.writer().simpleWrite(data);
	}
}
