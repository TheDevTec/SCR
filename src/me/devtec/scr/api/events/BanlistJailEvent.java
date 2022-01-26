package me.devtec.scr.api.events;

import me.devtec.theapi.utils.listener.Cancellable;

public class BanlistJailEvent extends BanlistEvent implements Cancellable {
	private String jailId;
	private boolean cancel;
	private long time;
	public BanlistJailEvent(String name, String reason, long duration, String jailId) {
		super(name, reason);
		this.jailId=jailId;
		time=duration;
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

	public long getDuration() {
		return time;
	}

	public void setDuration(long time) {
		this.time = time < 0 ? 0 : time;
	}
	
	public boolean isTemp() {
		return time > 0;
	}
}