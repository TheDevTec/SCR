package me.devtec.scr.modules;

public interface Module {
	public Module load();

	public Module unload();

	public boolean isLoaded();
}
