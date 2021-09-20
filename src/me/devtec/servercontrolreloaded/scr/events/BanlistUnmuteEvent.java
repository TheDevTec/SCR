package me.devtec.servercontrolreloaded.scr.events;

import me.devtec.theapi.punishmentapi.Punishment;

public class BanlistUnmuteEvent extends BanlistEvent {
	private Punishment punishment;
	public BanlistUnmuteEvent(Punishment punish) {
		super(punish.getUser(), punish.getReason());
		this.punishment=punish;
	}
	
	public Punishment getPunishment() {
		return punishment;
	}
}
