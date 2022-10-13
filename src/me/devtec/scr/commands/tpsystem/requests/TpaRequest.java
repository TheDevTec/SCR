package me.devtec.scr.commands.tpsystem.requests;

import me.devtec.scr.MessageUtils;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.api.User;

public class TpaRequest implements TeleportRequest {
	private User requester;
	private User target;

	private boolean done;

	public TpaRequest(User requester, User target) {
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
		MessageUtils.message(requester.getPlayer(), "teleportreq.tpa.accept.sender", Placeholders.c().addPlayer("target", requester.getPlayer()).addPlayer("player", target.getPlayer()));
		MessageUtils.message(target.getPlayer(), "teleportreq.tpa.accept.receiver", Placeholders.c().addPlayer("target", target.getPlayer()).addPlayer("player", requester.getPlayer()));
		requester.getPlayer().teleport(target.getPlayer());
		removeRequest();
	}

	@Override
	public void decnile() {
		if (isFinished())
			return;
		finish();
		MessageUtils.message(requester.getPlayer(), "teleportreq.tpa.reject.sender", Placeholders.c().addPlayer("target", requester.getPlayer()).addPlayer("player", target.getPlayer()));
		MessageUtils.message(target.getPlayer(), "teleportreq.tpa.reject.receiver", Placeholders.c().addPlayer("target", target.getPlayer()).addPlayer("player", requester.getPlayer()));
		removeRequest();
	}

	@Override
	public void cancel() {
		if (isFinished())
			return;
		finish();
		MessageUtils.message(requester.getPlayer(), "teleportreq.tpa.cancel.sender", Placeholders.c().addPlayer("player", requester.getPlayer()).addPlayer("target", target.getPlayer()));
		MessageUtils.message(target.getPlayer(), "teleportreq.tpa.cancel.receiver", Placeholders.c().addPlayer("player", target.getPlayer()).addPlayer("target", requester.getPlayer()));
		removeRequest();
	}

	@Override
	public void timeout() {
		if (isFinished())
			return;
		finish();
		if (target.isOnline())
			MessageUtils.message(requester.getPlayer(), "teleportreq.tpa.expired.online", Placeholders.c().addPlayer("target", target.getPlayer()));
		else
			MessageUtils.message(requester.getPlayer(), "teleportreq.tpa.expired.offline", Placeholders.c().add("target", target.getName()));
		removeRequest();
	}

	@Override
	public void removeRequest() {
		requester.removeSendTpReq(this);
		target.removeTpReq(this);
	}

}
