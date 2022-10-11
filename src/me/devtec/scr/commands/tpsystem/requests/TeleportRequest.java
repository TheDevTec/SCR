package me.devtec.scr.commands.tpsystem.requests;

import me.devtec.scr.api.User;

public interface TeleportRequest {

	User getRequester();

	User getTarget();

	boolean isFinished();

	void finish();

	void accept();

	void decnile();

	void cancel();

	void timeout();

	void removeRequest();
}
