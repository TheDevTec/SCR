package me.devtec.scr.api;

import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;

import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils;
import me.devtec.scr.commands.tpsystem.requests.TeleportRequest;
import me.devtec.scr.utils.ISuser;
import me.devtec.shared.API;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.dataholder.DataType;
import me.devtec.shared.dataholder.cache.TempList;
import me.devtec.shared.utility.StringUtils;
import me.devtec.theapi.bukkit.BukkitLoader;

public class User implements ISuser {
	// TpSystem
	private TempList<TeleportRequest> requests;
	private Queue<TeleportRequest> sentRequests;

	private UUID uuid;
	private String nickname;
	private String name;
	private Config userFile;
	private Player cached;
	private boolean vanish;

	public User(Player player) {
		this(player.getUniqueId(), player.getName());
	}

	public User(String name) {
		this(API.offlineCache().lookupId(name), name);
	}

	public User(UUID uuid) {
		this(uuid, API.offlineCache().lookupNameById(uuid));
	}

	public User(UUID uuid, String playerName) {
		name = playerName;
		this.uuid = uuid;
		nickname = getNickname();
		userFile = API.getUser(uuid);
	}

	@Override
	public Player getPlayer() {
		return cached;
	}

	@Override
	public Config getFile() {
		return userFile;
	}

	@Override
	public UUID getUUID() {
		return uuid;
	}

	@Override
	public boolean hasNickname() {
		return nickname != null;
	}

	@Override
	public String getNickname() {
		return nickname;
	}

	@Override
	public void resetNickname() {
		nickname = null;
		userFile.remove("nickname");
		Player online = getPlayer();
		if (online != null)
			online.setCustomName(null);
	}

	@Override
	public void setNickname(String nick) {
		nickname = StringUtils.colorize(nick);
		userFile.set("nickname", nick);
		Player online = getPlayer();
		if (online != null)
			online.setCustomName(nickname);
	}

	@Override
	public boolean hasPerm(String permission, boolean noPermsMessage) {
		if (permission == null || permission.isEmpty() || getPlayer().hasPermission(permission))
			return true;
		if (noPermsMessage)
			MessageUtils.noPerm(getPlayer(), permission);
		return false;
	}

	@Override
	public boolean cooldownExpired(String cooldownpath, String cooldowntime) {
		return hasPerm("scr.bypass.cooldowns", false) || userFile.getLong("cooldowns." + cooldownpath) - System.currentTimeMillis() / 1000 + StringUtils.timeFromString(cooldowntime) <= 0;
	}

	@Override
	public long cooldownExpire(String cooldownpath, String cooldowntime) {
		return userFile.getLong("cooldowns." + cooldownpath) - System.currentTimeMillis() / 1000 + StringUtils.timeFromString(cooldowntime);
	}

	@Override
	public void cooldownMake(String cooldownpath) {
		userFile.set("cooldowns." + cooldownpath, System.currentTimeMillis() / 1000);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isIgnoring(String target) {
		return userFile.getBoolean("privateMessage.ignorelist." + target);
	}

	@Override
	public void addIgnore(String target) {
		userFile.set("privateMessage.ignorelist." + target, true);
	}

	@Override
	public void removeIgnore(String target) {
		userFile.set("privateMessage.ignorelist." + target, null);
	}

	@Override
	public void notifyQuit() {
		if (cached != null)
			userFile.set("disconnectWorld", cached.getWorld().getName());
		userFile.set("lastLeave", System.currentTimeMillis() / 1000).save(DataType.YAML);
		cached = null;
		requests = null;
		sentRequests = null;
	}

	@Override
	public void notifyJoin(Player instance, boolean isEvent) {
		cached = instance;
		vanish = userFile.getBoolean("vanish");
		if (isEvent)
			userFile.set("lastLeave", System.currentTimeMillis() / 1000).save(DataType.YAML);
		if (requests == null)
			requests = new TempList<>(20 * Math.min(StringUtils.timeFromString(Loader.config.getString("options.tp-accept_cooldown")), 5));
		if (sentRequests == null)
			sentRequests = new LinkedBlockingDeque<TeleportRequest>() {
				private static final long serialVersionUID = 1L;

				@Override
				public void addLast(TeleportRequest value) {
					super.addFirst(value);
				}
			};
	}

	@Override
	public long seen() {
		long result = System.currentTimeMillis() / 1000 - userFile.getLong("lastLeave");
		return result < 0 ? 0 : result;
	}

	@Override
	public boolean god() {
		return userFile.getBoolean("god");
	}

	@Override
	public void god(boolean status) {
		userFile.set("god", status ? true : null);
	}

	@Override
	public boolean fly() {
		return userFile.getBoolean("fly");
	}

	@Override
	public void fly(boolean status) {
		userFile.set("fly", status ? true : null);
		Player online = getPlayer();
		if (online != null)
			if (status) {
				online.setAllowFlight(true);
				if (online.isOnGround())
					online.setFlying(true);
			} else {
				online.setFlying(false);
				online.setAllowFlight(false);
			}
	}

	@Override
	public void addTpReq(TeleportRequest req) {
		requests.add(req);
	}

	@Override
	public void removeTpReq(TeleportRequest req) {
		requests.remove(req);
	}

	@Override
	public void addSendTpReq(TeleportRequest tpaRequest) {
		sentRequests.add(tpaRequest);
	}

	@Override
	public void removeSendTpReq(TeleportRequest tpaRequest) {
		sentRequests.remove(tpaRequest);
	}

	@Override
	public TeleportRequest getTpReqOf(User sender) {
		for (TeleportRequest req : requests)
			if (req.getRequester().getUUID().equals(sender.getUUID()))
				return req;
		return null;
	}

	@Override
	public TeleportRequest getTpReq() {
		return requests.get(0);
	}

	@Override
	public TeleportRequest getSendTpReq() {
		return sentRequests.peek();
	}

	public boolean isVanished() {
		return vanish;
	}

	public void setVanished(boolean status) {
		vanish = status;
		userFile.set("vanish", vanish ? true : null);
		if (getPlayer() != null)
			if (status) {
				for (Player player : BukkitLoader.getOnlinePlayers())
					if (!player.hasPermission(Loader.commands.getString("vanish.permission.cmd")))
						player.hidePlayer(getPlayer());
			} else
				for (Player player : BukkitLoader.getOnlinePlayers())
					if (!player.canSee(getPlayer()))
						player.showPlayer(getPlayer());
	}

}
