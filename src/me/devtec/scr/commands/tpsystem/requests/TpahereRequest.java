package me.devtec.scr.commands.tpsystem.requests;

import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.User;

public class TpahereRequest implements TeleportRequest {
	private User requester;
	private User target;

	private boolean done;

	public TpahereRequest(User requester, User target) {
		this.requester = requester;
		this.target = target;
	}

	@Override
	public User getRequester() {
		return requester;
	}

	@Override
	public User getTarget() {
		return target;
	}

	@Override
	public boolean isFinished() {
		return done;
	}

	@Override
	public void finish() {
		done = true;
	}

	@Override
	public void accept() {
		if (isFinished())
			return;
		finish();
		MessageUtils.message(requester.getPlayer(), "teleportreq.tpahere.accept.sender", Placeholders.c().addPlayer("target", target.getPlayer()).addPlayer("player", requester.getPlayer()));
		MessageUtils.message(target.getPlayer(), "teleportreq.tpahere.accept.receiver", Placeholders.c().addPlayer("target", target.getPlayer()).addPlayer("player", requester.getPlayer()));
		target.getPlayer().teleport(requester.getPlayer());
		removeRequest();
	}

	@Override
	public void decnile() {
		if (isFinished())
			return;
		finish();
		MessageUtils.message(requester.getPlayer(), "teleportreq.tpahere.reject.sender", Placeholders.c().addPlayer("target", target.getPlayer()).addPlayer("player", requester.getPlayer()));
		MessageUtils.message(target.getPlayer(), "teleportreq.tpahere.reject.receiver", Placeholders.c().addPlayer("target", target.getPlayer()).addPlayer("player", requester.getPlayer()));
		removeRequest();
	}

	@Override
	public void cancel() {
		if (isFinished())
			return;
		finish();
		MessageUtils.message(requester.getPlayer(), "teleportreq.tpahere.cancel.sender", Placeholders.c().addPlayer("player", target.getPlayer()).addPlayer("target", requester.getPlayer()));
		MessageUtils.message(target.getPlayer(), "teleportreq.tpahere.cancel.receiver", Placeholders.c().addPlayer("player", target.getPlayer()).addPlayer("target", requester.getPlayer()));
		removeRequest();
	}

	@Override
	public void timeout() {
		if (isFinished())
			return;
		finish();
		if (target.isOnline())
			MessageUtils.message(requester.getPlayer(), "teleportreq.tpahere.expired.online", Placeholders.c().addPlayer("player", target.getPlayer()));
		else
			MessageUtils.message(requester.getPlayer(), "teleportreq.tpahere.expired.offline", Placeholders.c().add("player", target.getName()));
		removeRequest();
	}

	public void removeRequest() {
		requester.removeSendTpReq(this);
		target.removeTpReq(this);
	}

}
