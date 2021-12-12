package me.devtec.servercontrolreloaded.utils.displaymanager;

public interface SBossBar {
	public void setTitle(String title);

	public void setProgress(double progress);

	public void setStyle(String styleName);
	
	public void setRandomStyle();

	public void setColor(String colorName);
	
	public void setRandomColor();

	public boolean isVisible();
	
	public void hide();
	
	public void show();

	public void remove();
}
