package me.devtec.scr.api.events;

import me.devtec.theapi.punishmentapi.Punishment;

public class BanlistUnbanEvent extends BanlistEvent {
	private Punishment punishment;
	public BanlistUnbanEvent(Punishment punish) {
		super(punish.getUser(), punish.getReason());
		this.punishment=punish;
	}
	
	public Punishment getPunishment() {
		return punishment;
	}
}