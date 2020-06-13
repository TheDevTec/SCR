package Utils;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import me.Straiker123.ConfigAPI;
import me.Straiker123.TheAPI;
import me.Straiker123.User;

public class Converter {
/*
		  Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
				public void run() {
					Converter.convert();
				}
			}, 300);
 */
	public static void convert() {
		if(new File("plugins/ServerControlReloaded/ChatMe.yml").exists()) {
			ConfigAPI me = TheAPI.getConfig("ServerControlReloaded", "ChatMe");
			if(!me.exist("Players")) {
				Bukkit.getConsoleSender().sendMessage("Players neexistuje!");
				return;
			}
			me.create();
			for(String playername: me.getConfigurationSection("Players").getKeys(false)) {
				User user = TheAPI.getUser(playername);
				ConfigurationSection sec = me.getConfigurationSection("Players."+playername);
				for(String s : sec.getKeys(true))
					user.set(s, sec.get(s));
				user.save();
			}
			//me.delete();
			Bukkit.getConsoleSender().sendMessage(TheAPI.colorize("&2&lDONE!!!"));
		}else {
			Bukkit.getConsoleSender().sendMessage("ChatMe neexistuje!");
		}
		
	}
	/*private static void homesConventor(String player, User user) {
		if(me.getString("Players."+player+".Homes")==null)return;
		for(String home: me.getConfigurationSection("Players."+player+".Homes").getKeys(false)) {
			World world = Bukkit.getWorld(me.getString("Players."+player+".Homes."+home+".World"));
			if(world==null) {
				Bukkit.getConsoleSender().sendMessage("Svìt: "+me.getString("Players."+player+".Homes."+home+".World")+" neexistuje u homu: "+home);
				continue;
			}
			double x = me.getDouble("Players."+player+".Homes."+home+".X");
			double y = me.getDouble("Players."+player+".Homes."+home+".Y");
			double z = me.getDouble("Players."+player+".Homes."+home+".Z");
			int pitch = me.getInt("Players."+player+".Homes."+home+".Pitch");
			int yaw = me.getInt("Players."+player+".Homes."+home+".Yaw");
			if(world!=null) {
				Location loc = new Location(world,x,y,z,pitch,yaw);
				user.setAndSave("Homes."+home, TheAPI.getStringUtils().getLocationAsString(loc));
			}
			continue;
		}
		Bukkit.getConsoleSender().sendMessage("Converted Homes of player:"+player+" to new config files!");
	}
	private static void moneyConventor(String player, User user) {
		if(me.getString("Players."+player+".Money")==null)return;
		for(String economygroup: me.getConfigurationSection("Players."+player+".Money").getKeys(false)) {
			if(me.getString("Players."+player+".Money."+economygroup) ==null)continue;
			long money = me.getLong("Players."+player+".Money."+economygroup);
			user.setAndSave("Players."+player+"Money."+economygroup, money);
		}
	}
	private static void inventoryConventor(String player, User user) {
		if(me.getString("Players."+player+".PerWorldInventory")==null)return;
		for(String group: me.getConfigurationSection("Players."+player+".PerWorldInventory").getKeys(false)) {
			
			if(me.getString("Players."+player+".PerWorldInventory."+group+".SURVIVAL")!=null) { //SURVIVAL
				ItemStack inventory = me.getItemStack("Players."+player+".PerWorldInventory."+group+".SURVIVAL.Inventory");
				ItemStack endchest = me.getItemStack("Players."+player+".PerWorldInventory."+group+".SURVIVAL.EnderChest");
				user.set("PerWorldInventory."+group+".SURVIVAL.Inventory", inventory); 
				user.set("PerWorldInventory."+group+".SURVIVAL.EnderChest", endchest);
				
				double points = me.getDouble("Players."+player+".PerWorldInventory."+group+".SURVIVAL.Exps.Points");
				double level = me.getDouble("Players."+player+".PerWorldInventory."+group+".SURVIVAL.Exps.Level");
				if(points != 0.0) user.set("PerWorldInventory."+group+".SURVIVAL.Exps.Points", points);
				if(level != 0.0) user.set("PerWorldInventory."+group+".SURVIVAL.Exps.Level", level);
			}
			if(me.getString("Players."+player+".PerWorldInventory."+group+".CREATIVE")!=null) { //CREATIVE
				ItemStack inventory = me.getItemStack("Players."+player+".PerWorldInventory."+group+".CREATIVE.Inventory");
				ItemStack endchest = me.getItemStack("Players."+player+".PerWorldInventory."+group+".CREATIVE.EnderChest");
				user.set("PerWorldInventory."+group+".CREATIVE.Inventory", inventory);
				user.set("PerWorldInventory."+group+".CREATIVE.EnderChest", endchest);			
			}
			/*if(me.getString("Players."+player+".PerWorldInventory."+group+".SPECTATOR")!=null) { //SPECTATOR
				ItemStack inventory = me.getItemStack("Players."+player+".PerWorldInventory."+group+".SPECTATOR.Inventory");
				ItemStack endchest = me.getItemStack("Players."+player+".PerWorldInventory."+group+".SPECTATOR.EnderChest");
				if(me.getString("Players."+player+".PerWorldInventory."+group+".SPECTATOR.Inventory") !=null)
					user.set("PerWorldInventory."+group+".SPECTATOR.Inventory", inventory);
				if(me.getString("Players."+player+".PerWorldInventory."+group+".SPECTATOR.EnderChest") !=null) 
					user.set("PerWorldInventory."+group+".SPECTATOR.EnderChest", endchest);
			}
			user.save();
		}
	}
	private static void ChatMeConventor(String player, User user) {
		//if(me.getString("Players."+player+".Joins")!=null) user.set("Joins", me.getInt("Players."+player+".Joins"));
		if(me.getString("Players."+player+".FirstJoin")!=null) user.set("FirstJoin", me.getString("Players."+player+".FirstJoin"));
		//if(me.getString("Players."+player+".Deaths")!=null) user.set("Deaths", me.getInt("Players."+player+".Deaths"));
		if(me.getString("Players."+player+".LastLeave")!=null) user.set("LastLeave", me.getString("Players."+player+".LastLeave"));
		//if(me.getString("Players."+player+".Kills")!=null) user.set("Kills", me.getInt("Players."+player+".Kills"));
	}*/
	//TODO
	//-nefungujou inventáøe
	//- nejde chat me Houska02

}
