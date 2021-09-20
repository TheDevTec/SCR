package me.devtec.servercontrolreloaded.scr.events;

import me.devtec.theapi.utils.listener.Cancellable;

public class BanlistJailEvent extends BanlistEvent implements Cancellable {
	private String jailId;
	private boolean cancel;
	public BanlistJailEvent(String name, String reason, String jailId) {
		super(name, reason);
		this.jailId=jailId;
	}
	
	public String getJailId() {
		return jailId;
	}
	
	public void setJailId(String jailId) {
		this.jailId=jailId;
	}

	@Override
	public boolean isCancelled() {
		return cancel;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancel=cancel;
	}
}
