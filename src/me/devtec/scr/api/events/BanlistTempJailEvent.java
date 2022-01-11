package me.devtec.scr.api.events;

import me.devtec.theapi.utils.listener.Cancellable;

public class BanlistTempJailEvent extends BanlistJailEvent implements Cancellable {
	private long duration;
	public BanlistTempJailEvent(String name, String reason, String jailId, long duration) {
		super(name, reason, jailId);
		this.duration=duration;
	}
	
	public long getDuration() {
		return duration;
	}
	
	public void setDuration(long duration) {
		this.duration=duration;
	}
}