package me.devtec.servercontrolreloaded.scr.events;

import me.devtec.theapi.utils.listener.Event;

public class BanlistEvent extends Event {
	private final String name;
	private String reason;
	public BanlistEvent(String name, String reason) {
		this.name=name;
		this.reason=reason;
	}
	
	public String getName() {
		return name;
	}
	
	public String getReason() {
		return reason;
	}
	
	public void setReason(String reason) {
		this.reason=reason;
	}
}
