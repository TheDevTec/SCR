package me.devtec.servercontrolreloaded.scr.events;

import me.devtec.theapi.punishmentapi.Punishment;

public class BanlistUnjailEvent extends BanlistEvent {
	private Punishment punishment;
	public BanlistUnjailEvent(Punishment punish) {
		super(punish.getUser(), punish.getReason());
		this.punishment=punish;
	}
	
	public Punishment getPunishment() {
		return punishment;
	}
}
