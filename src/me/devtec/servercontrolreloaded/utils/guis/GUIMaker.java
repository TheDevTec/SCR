package me.devtec.servercontrolreloaded.utils.guis;

import org.bukkit.entity.Player;

import me.devtec.servercontrolreloaded.utils.TabList;
import me.devtec.servercontrolreloaded.utils.guis.Layout.ItemBuilder;
import me.devtec.theapi.guiapi.GUI;
import me.devtec.theapi.guiapi.GUI.ClickType;
import me.devtec.theapi.guiapi.HolderGUI;
import me.devtec.theapi.guiapi.ItemGUI;
import me.devtec.theapi.utils.datakeeper.Data;

public class GUIMaker {
	public String title;
	public boolean dynamic;
	public Layout layout;
	private GUI gui;
	
	public GUIMaker(Data file) {
		dynamic = file.getBoolean("dynamic");
		title = file.getString("title");
		layout = new Layout(file);
		if(!dynamic)
			build(null);
	}
	
	public void open(Player p) {
		if(dynamic)
			build(p);
		gui.open(p);
	}

	private void build(Player p) {
		gui = new GUI(TabList.replace(title, p, true), layout.lines.size()*9);
		
		int pos = -1;
		for(String cc : layout.lines) {
			for(char c : cc.toCharArray()) {
				++pos;
				ItemBuilder builder = layout.find(p, gui, c);
				if(builder!=null)
					gui.setItem(pos, new ItemGUI(builder.build(p)) {
						public void onClick(Player player, HolderGUI gui, ClickType click) {
							builder.process(player, gui, click);
						}
					});
			}
		}
	}
}
