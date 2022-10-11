package me.devtec.scr.utils;

import java.util.UUID;

import javax.annotation.Nullable;

import org.bukkit.entity.Player;

import me.devtec.scr.api.User;
import me.devtec.scr.api.User.SeenType;
import me.devtec.scr.commands.tpsystem.requests.TeleportRequest;
import me.devtec.shared.dataholder.Config;

public interface ISuser {

	void addTpReq(TeleportRequest req);

	@Nullable
	TeleportRequest getTpReqOf(User sender);

	void removeTpReq(TeleportRequest req);

	void addSendTpReq(TeleportRequest req);

	void removeSendTpReq(TeleportRequest req);

	@Nullable
	TeleportRequest getTpReq();

	boolean hasPerm(String permission, boolean noPermsMessage);

	boolean cooldownExpired(String cooldownpath, String cooldowntime);

	long cooldownExpire(String cooldownpath, String cooldowntime);

	void cooldownMake(String cooldownpath);

	@Nullable
	Player getPlayer();

	default boolean isOnline() {
		return getPlayer() != null;
	}

	Config getFile();

	UUID getUUID();

	String getName();

	@Nullable
	String getNickname();

	boolean hasNickname();

	void resetNickname();

	void setNickname(String nick);

	boolean isIgnoring(String target);

	void addIgnore(String target);

	void removeIgnore(String target);

	void notifyJoin(Player instance);

	void notifyQuit();

	long seen(SeenType type);

	boolean god();

	void god(boolean status);

	boolean fly();

	void fly(boolean status);

}
