package me.devtec.scr.commands.teleport.requests;

import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.PlaceholderBuilder;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.utils.datakeeper.User;

public class TeleportManager {
	public enum TeleportType {
		TPA, TPAHERE;
	}
	
	public static boolean isBlocking(Player sender, String target) {
		User user = TheAPI.getUser(sender);
		return user.getBoolean("tp-block-global")||user.getStringList("tp-block").contains(target);
	}
	
	public static void block(Player sender, String target) {
		User user = TheAPI.getUser(sender);
		List<String> blocked = user.getStringList("tp-block");
		blocked.add(target);
		user.setSave("tp-block", blocked);
	}
	
	public static void unblock(Player sender, String target) {
		User user = TheAPI.getUser(sender);
		List<String> blocked = user.getStringList("tp-block");
		blocked.remove(target);
		user.setSave("tp-block", blocked);
	}
	
	public static void blockGlobal(Player sender) {
		User user = TheAPI.getUser(sender);
		user.setSave("tp-block-global", true);
	}
	
	public static boolean isBlockingGlobal(Player sender) {
		User user = TheAPI.getUser(sender);
		return user.getBoolean("tp-block-global");
	}
	
	public static void unblockGlobal(Player sender) {
		User user = TheAPI.getUser(sender);
		user.setSave("tp-block-global", false);
	}
	
	public static void request(Player sender, Player target, TeleportType type) {
		User user = TheAPI.getUser(sender);
		if((user.getBoolean("tp-block-global")||user.getStringList("tp-block").contains(target.getName())) && !sender.hasPermission("scr.bypass.tpa")) {
			Loader.send(sender, "teleport."+type.name().toLowerCase()+".blocked", PlaceholderBuilder.make(sender, "sender").player(target, "target"));
			return;
		}
		
		user.set("tp."+target.getName()+".type", type.name());
		user.setSave("tp."+target.getName()+".time", System.currentTimeMillis()/1000);
		user = TheAPI.getUser(target);
		List<String> requests = user.getStringList("tp-req");
		if(!requests.contains(sender.getName())) {
			requests.add(sender.getName());
			user.setAndSave("tp-req", requests);
		}
		Loader.send(sender, "teleport.request."+type.name().toLowerCase()+".sender", PlaceholderBuilder.make(sender, "sender").player(target, "target"));
		Loader.send(target, "teleport.request."+type.name().toLowerCase()+".target", PlaceholderBuilder.make(sender, "sender").player(target, "target"));
	}

	public static void accept(Player sender, boolean all) {
		if(all) {
			User user = TheAPI.getUser(sender);
			int teleported = 0;
			int teleportedHere = 0;
			for(String request : user.getStringList("tp-req")) {
				Object[] target;
				if((target=validOrRemove(sender, request))==null)
					continue;
				++teleported;
				if(target[1]==TeleportType.TPA) {
					((Player)target[0]).teleport(sender);
				}else
					if(teleportedHere++==0) //only 1x tpahere teleport
						sender.teleport((Player)target[0]);
				Loader.send(sender, "teleport.accept."+((TeleportType)target[1]).name().toLowerCase()+".sender", PlaceholderBuilder.make(sender, "sender").player((Player)target[0], "target"));
				Loader.send((Player)target[0], "teleport.accept."+((TeleportType)target[1]).name().toLowerCase()+".target", PlaceholderBuilder.make(sender, "sender").player((Player)target[0], "target"));
			}
			user.setAndSave("tp-req", null);
			if(teleported==0) {
				Loader.send(sender, "teleport.noRequest", PlaceholderBuilder.make(sender, "sender"));
				return;
			}
			return;
		}
		Object[] target = findAndRemove(sender);
		if(target==null) {
			Loader.send(sender, "teleport.noRequest", PlaceholderBuilder.make(sender, "sender"));
			return;
		}
		if(target[1]==TeleportType.TPA) {
			((Player)target[0]).teleport(sender);
		}else
			sender.teleport((Player)target[0]);
		Loader.send(sender, "teleport.accept."+((TeleportType)target[1]).name().toLowerCase()+".sender", PlaceholderBuilder.make(sender, "sender").player((Player)target[0], "target"));
		Loader.send((Player)target[0], "teleport.accept."+((TeleportType)target[1]).name().toLowerCase()+".target", PlaceholderBuilder.make(sender, "sender").player((Player)target[0], "target"));
	}

	public static void deny(Player sender, boolean all) {
		if(all) {
			User user = TheAPI.getUser(sender);
			int teleported = 0;
			for(String request : user.getStringList("tp-req")) {
				Object[] target;
				if((target=validOrRemove(sender, request))==null)
					continue;
				++teleported;
				Loader.send(sender, "teleport.accept."+((TeleportType)target[1]).name().toLowerCase()+".sender", PlaceholderBuilder.make(sender, "sender").player((Player)target[0], "target"));
				Loader.send((Player)target[0], "teleport.accept."+((TeleportType)target[1]).name().toLowerCase()+".target", PlaceholderBuilder.make(sender, "sender").player((Player)target[0], "target"));
			}
			user.setAndSave("tp-req", null);
			if(teleported==0) {
				Loader.send(sender, "teleport.noRequest", PlaceholderBuilder.make(sender, "sender"));
				return;
			}
			return;
		}
		Object[] target = findAndRemove(sender);
		if(target==null) {
			Loader.send(sender, "teleport.noRequest", PlaceholderBuilder.make(sender, "sender"));
			return;
		}
		Loader.send(sender, "teleport.accept."+((TeleportType)target[1]).name().toLowerCase()+".sender", PlaceholderBuilder.make(sender, "sender").player((Player)target[0], "target"));
		Loader.send((Player)target[0], "teleport.accept."+((TeleportType)target[1]).name().toLowerCase()+".target", PlaceholderBuilder.make(sender, "sender").player((Player)target[0], "target"));
	}

	public static void cancel(Player sender, boolean all) {
		User user = TheAPI.getUser(sender);
		if(all) {
			for(String player : user.getKeys("tp")) {
				User sub = TheAPI.getUser(player);
				List<String> set = sub.getStringList("tp-req");
				set.remove(sender.getName());
				sub.save();
				Player target = TheAPI.getPlayer(player);
				if(target!=null) {
					String type = user.getString("tp."+player+".type").toLowerCase();
					Loader.send(sender, "teleport.cancel."+type+".sender", PlaceholderBuilder.make(sender, "sender").player(target, "target"));
					Loader.send(target, "teleport.cancel."+type+".target", PlaceholderBuilder.make(sender, "sender").player(target, "target"));
				}
			}
			user.remove("tp");
		}else {
			for(String request : user.getKeys("tp")) {
				Player target = TheAPI.getPlayer(request);
				User sub = TheAPI.getUser(request);
				List<String> set = sub.getStringList("tp-req");
				set.remove(sender.getName());
				sub.save();
				String type = user.getString("tp."+request+".type").toLowerCase();
				user.remove("tp."+request);
				if(target==null)continue;
				Loader.send(sender, "teleport.cancel."+type+".sender", PlaceholderBuilder.make(sender, "sender").player(target, "target"));
				Loader.send(target, "teleport.cancel."+type+".target", PlaceholderBuilder.make(sender, "sender").player(target, "target"));
				break;
			}
		}
		user.save();
	}

	private static Object[] validOrRemove(Player sender, String requester) {
		User user=TheAPI.getUser(requester);
		Player player = TheAPI.getPlayer(requester);
		if(player==null||user.getLong("tp."+requester+".time")-System.currentTimeMillis()/1000 + Loader.getRequestTime() <= 0) {
			String val = user.getString("tp."+sender.getName()+".type");
			user.remove("tp."+sender.getName());
			user.save();
			return new Object[] {player,TeleportType.valueOf(val)};
		}
		return null;
	}

	private static Object[] findAndRemove(Player sender) {
		User user = TheAPI.getUser(sender);
		List<String> req = user.getStringList("tp-req");
		Iterator<String> reqs = req.iterator();
		while(reqs.hasNext()) {
			String request = reqs.next();
			reqs.remove();
			Object[] result;
			if((result=validOrRemove(sender, request))==null)
				continue;
			user.setAndSave("tp-req", req);
			return result;
		}
		user.setAndSave("tp-req", req);
		return null;
	}
}
