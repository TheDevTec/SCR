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
import me.devtec.shared.scheduler.Tasker;
import me.devtec.shared.utility.StringUtils;

public class User implements ISuser {
	// TpSystem
	private final Queue<TeleportRequest> requests = new LinkedBlockingDeque<TeleportRequest>() {
		private static final long serialVersionUID = 1L;

		@Override
		public void addLast(TeleportRequest value) {
			super.addLast(value);
			new Tasker() {

				@Override
				public void run() {
					value.timeout();
				}
			}.runLater(20 * StringUtils.timeFromString(Loader.config.getString("options.tp-accept_cooldown")));
		}
	};
	private final Queue<TeleportRequest> sentRequests = new LinkedBlockingDeque<TeleportRequest>() {
		private static final long serialVersionUID = 1L;

		@Override
		public void addLast(TeleportRequest value) {
			super.addFirst(value);
		}
	};

	private UUID uuid;
	private String nickname;
	private String name;
	private Config userFile;
	private Player cached;

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
		userFile.remove("nickname").save(DataType.YAML);
		Player online = getPlayer();
		if (online != null)
			online.setCustomName(null);
	}

	@Override
	public void setNickname(String nick) {
		nickname = StringUtils.colorize(nick);
		userFile.set("nickname", nick).save(DataType.YAML);
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
		userFile.set("cooldowns." + cooldownpath, System.currentTimeMillis() / 1000).save(DataType.YAML);
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
		userFile.set("privateMessage.ignorelist." + target, true).save(DataType.YAML);
	}

	@Override
	public void removeIgnore(String target) {
		userFile.remove("privateMessage.ignorelist." + target).save(DataType.YAML);
	}

	@Override
	public void notifyQuit() {
		userFile.remove("lastLeave" + System.currentTimeMillis() / 1000).save(DataType.YAML);
		cached = null;
	}

	@Override
	public void notifyJoin(Player instance) {
		cached = instance;
	}

	public enum SeenType {
		ONLINE, OFFLINE, CURRENT_STATE;
	}

	@Override
	public long seen(SeenType type) {
		Config s = userFile;
		Player player = getPlayer();
		switch (type) {
		case ONLINE:
			if (player != null)
				return System.currentTimeMillis() / 1000 - player.getLastPlayed();
			return 0; // Not online
		case OFFLINE:
			if (s.exists("lastLeave"))
				return System.currentTimeMillis() / 1000 - s.getLong("lastLeave");
			return 0;
		case CURRENT_STATE:
			if (player != null)
				return System.currentTimeMillis() / 1000 - player.getLastPlayed();
			return System.currentTimeMillis() / 1000 - s.getLong("lastLeave");
		}
		return 0;
	}

	@Override
	public boolean god() {
		return userFile.getBoolean("god");
	}

	@Override
	public void god(boolean status) {
		userFile.set("god", status).save(DataType.YAML);
	}

	@Override
	public boolean fly() {
		return userFile.getBoolean("fly");
	}

	@Override
	public void fly(boolean status) {
		userFile.set("fly", status).save(DataType.YAML);
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
		return requests.poll();
	}

	public TeleportRequest getSendTpReq() {
		return sentRequests.peek();
	}

}
