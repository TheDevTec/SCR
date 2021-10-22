package me.devtec.servercontrolreloaded.utils.punishment;

import java.util.HashMap;
import java.util.Map;

import me.devtec.theapi.punishmentapi.Punishment;
import me.devtec.theapi.utils.datakeeper.Data;
import me.devtec.theapi.utils.json.Json;

public class SPunishment implements Punishment {
	private final String user, punishName, prefix;
	private final PunishmentType type;
	private final Data data;
	public SPunishment(Data data, String user, PunishmentType type, String punishName) {
		this.user = user;
		this.punishName = punishName;
		this.type = type;
		this.data = data;
		prefix=punishName.endsWith("ip")?"i.":"u.";
		
	}

	public boolean isIP() {
		return prefix.equals("i.");
	}
	
	@Override
	public long getDuration() {
		return data.getLong(prefix+user.toLowerCase().replace(".", "_")+"."+punishName+".duration");
	}

	@Override
	public String getReason() {
		return data.getString(prefix+user.toLowerCase().replace(".", "_")+"."+punishName+".reason").replace("\\n", "\n");
	}

	@Override
	public long getStart() {
		return data.getLong(prefix+user.toLowerCase().replace(".", "_")+"."+punishName+".start");
	}

	@Override
	public PunishmentType getType() {
		return type;
	}

	@Override
	public String getTypeName() {
		return punishName;
	}

	@Override
	public String getUser() {
		return user;
	}
	
	public void remove() {
		data.remove(prefix+user.toLowerCase().replace(".", "_")+"."+punishName);
		data.save();
	}

	@Override
	public Object getValue(String path) {
		return data.get(prefix+user.toLowerCase().replace(".", "_")+"."+punishName+"."+path);
	}
	
	public String toString() {
		Map<String, Object> s = new HashMap<>();
		s.put("user", user);
		s.put("punishName", punishName);
		s.put("type", type.name());
		return Json.writer().simpleWrite(s);
	}

}
