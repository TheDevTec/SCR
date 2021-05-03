package me.devtec.servercontrolreloaded.utils;

import java.util.List;

public class Animation {
	private List<String> lines;
	private long last = System.currentTimeMillis() / 50L;
	private long tics;
	private int c;

	public Animation(List<String> text, long ticks) {
		this.lines = text;
		this.tics = ticks;
	}

	public String get() {
		if (this.lines.isEmpty()) {
			return null;
		}
		if (this.c >= this.lines.size()) {
			this.c = 0;
		}
		return this.lines.get(this.c);
	}

	public List<String> getLines() {
		return this.lines;
	}

	public long getTicks() {
		return this.tics;
	}
	
	public void update() {
		if (this.c >= this.lines.size()) {
			this.c = 0;
		}
		if (this.last - System.currentTimeMillis() / 50L + this.tics <= 0L) {
			this.last = System.currentTimeMillis() / 50L;
			if (this.c >= this.lines.size()) {
				this.c = 0;
			}
			++c;
		}
	}
}