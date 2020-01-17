package Commands;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Repeat;
import Utils.XMaterial;
import me.Straiker123.CooldownAPI;
import me.Straiker123.ItemCreatorAPI;
import me.Straiker123.TheAPI;

public class Kit implements CommandExecutor,TabCompleter{
	public ArrayList<String> kits(CommandSender p){
		 ArrayList<String> list = new ArrayList<String>();
		if(Kits().isEmpty()==false) {
			if(!p.hasPermission("servercontrol.kit.*")) {
			for(String name:Kits()) {
			if(p.hasPermission("ServerControl.Kit."+name))
			list.add(name);
		}
			}else {
				list=Kits();
			}
		}
		return list;
	}
	public static ArrayList<String> Kits(){
		 ArrayList<String> list = new ArrayList<String>();
		 if(Loader.kit.getString("Kits")!=null)
			 for(String s:Loader.kit.getConfigurationSection("Kits").getKeys(false))list.add(s);
		 return list;
	}
	public static String getKitName(String original) {
		 if(Loader.kit.getString("Kits")!=null)
		for(String s:Loader.kit.getConfigurationSection("Kits").getKeys(false)) {
		if(s.toLowerCase().equalsIgnoreCase(original)) {
			return s;
		}
		}
		return null;
	}
	public static void giveKit(String p, String KitName, boolean cooldown, boolean economy) {
		
		if(Bukkit.getPlayer(p)!=null) {
			if(!cooldown) {
				if(!economy) {
		setupKit(Bukkit.getPlayer(p),KitName);
		return;
				}else {
					if(TheAPI.getEconomyAPI().has(p, Loader.kit.getDouble("Kits."+KitName+".Price"))) {
						setupKit(Bukkit.getPlayer(p),KitName);
	                            Loader.msg(Loader.s("Prefix")+Loader.s("Kit.Used")
	                            		.replace("%kit%", getKitName(KitName))
	            						.replace("%player%", Bukkit.getPlayer(p).getName())
	            						.replace("%playername%", Bukkit.getPlayer(p).getDisplayName()),Bukkit.getPlayer(p));
	            				TheAPI.getEconomyAPI().withdrawPlayer(p, Loader.kit.getDouble("Kits."+KitName+".Price"));
	            				return;
	                        }
					Loader.msg(Loader.s("Economy.NoMoney")
							.replace("%money%",API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(p), true))
							.replace("%currently%",API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(p), true))
							.replace("%player%", Bukkit.getPlayer(p).getName())
							.replace("%playername%", Bukkit.getPlayer(p).getDisplayName()), Bukkit.getPlayer(p));
    				return;
				}
			}
			else {
				if(economy) {
				if(TheAPI.getEconomyAPI().has(p, Loader.kit.getDouble("Kits."+KitName+".Price"))) {
					CooldownAPI a = TheAPI.getCooldownAPI("Kit."+KitName);
					if(!a.expired(p)) {
            				Loader.msg(Loader.s("Prefix")+Loader.s("Kit.Cooldown").
            						replace("%cooldown%", TheAPI.getTimeConventorAPI().setTimeToString(a.getTimeToExpire(p)))
            						.replace("%kit%", getKitName(KitName))
            						.replace("%player%", Bukkit.getPlayer(p).getName())
            						.replace("%playername%", Bukkit.getPlayer(p).getDisplayName()),Bukkit.getPlayer(p));
            				return;
                        }
					setupKit(Bukkit.getPlayer(p),KitName);
                            Loader.msg(Loader.s("Prefix")+Loader.s("Kit.Used").
                            		replace("%kit%", getKitName(KitName))
            						.replace("%player%", Bukkit.getPlayer(p).getName())
            						.replace("%playername%", Bukkit.getPlayer(p).getDisplayName()),Bukkit.getPlayer(p));
                            a.createCooldown(p, Loader.kit.getDouble("Kits."+KitName+".Cooldown"));
            				TheAPI.getEconomyAPI().withdrawPlayer(p, Loader.kit.getDouble("Kits."+getKitName(KitName)+".Price"));
            				return;
                        }
				Loader.msg(Loader.s("Economy.NoMoney")
						.replace("%money%",API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(p), true))
						.replace("%currently%",API.setMoneyFormat(TheAPI.getEconomyAPI().getBalance(p), true))
						.replace("%player%", Bukkit.getPlayer(p).getName())
						.replace("%playername%", Bukkit.getPlayer(p).getDisplayName()), Bukkit.getPlayer(p));
				return;
				}else {
					CooldownAPI a = TheAPI.getCooldownAPI("Kit."+KitName);
					if(!a.expired(p)) {
            				Loader.msg(Loader.s("Prefix")+Loader.s("Kit.Cooldown").
            						replace("%cooldown%", TheAPI.getTimeConventorAPI().setTimeToString(a.getTimeToExpire(p)))
            						.replace("%kit%", getKitName(KitName))
            						.replace("%player%", Bukkit.getPlayer(p).getName())
            						.replace("%playername%", Bukkit.getPlayer(p).getDisplayName()),Bukkit.getPlayer(p));
            				return;
                        }
					setupKit(Bukkit.getPlayer(p),KitName);
                            Loader.msg(Loader.s("Prefix")+Loader.s("Kit.Used").
                            		replace("%kit%", getKitName(KitName))
            						.replace("%player%", Bukkit.getPlayer(p).getName())
            						.replace("%playername%", Bukkit.getPlayer(p).getDisplayName()),Bukkit.getPlayer(p));
                            a.createCooldown(p, Loader.kit.getDouble("Kits."+KitName+".Cooldown"));
            				return;
				}
			}
		}
	}
	private static void setupKit(Player p,String kitName) {
		if(getKitName(kitName) != null) {
			for(String def:Loader.kit.getConfigurationSection("Kits."+getKitName(kitName)+".Items").getKeys(false)) {
				Material m = XMaterial.matchXMaterial(def.toUpperCase()).parseMaterial();
				if(m==null) {
					Loader.warn("Error when giving kit '"+kitName+"', material '"+def+"' is invalid !");
					return;
				}
					
				ItemCreatorAPI a = TheAPI.getItemCreatorAPI(m);
				a.setAmount(Loader.kit.getInt("Kits."+getKitName(kitName)+".Items."+def+".Amount"));
	            a.setLore(Loader.kit.getStringList("Kits."+getKitName(kitName)+".Items."+def+".Lore"));
	            a.setDisplayName(Loader.kit.getString("Kits."+getKitName(kitName)+".Items."+def+".CustomName"));
	            if(Loader.kit.getString("Kits."+getKitName(kitName)+".Items."+def+".Enchantments")!=null) {
	            for(String enchs:Loader.kit.getStringList("Kits."+getKitName(kitName)+".Items."+def+".Enchantments")) {
	            	String nonum = enchs.replace(":", "").replaceAll("[0-9]+", "");
	            	String num = enchs.replace(":", "").replaceAll("[A-Za-z]+", "").replace("_", "");
	            	if(!TheAPI.getEnchantmentAPI().isEnchantment(nonum))Loader.warn("Error when giving kit '"+kitName+"', enchant '"+nonum+"' is invalid !");
	            	else
	            		a.addEnchantment(TheAPI.getEnchantmentAPI().getByName(nonum), TheAPI.getNumbersAPI(num).getInt());
	            }}
	            TheAPI.giveItem(p, a.create());
		}}}

	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label,String[] args) {

			if(args.length==0) {
				Loader.msg(Loader.s("Prefix")+Loader.s("Kit.List")
						.replace("%player%", s.getName())
						.replace("%kits%", StringUtils.join(kits(s), ", "))
						.replace("%playername%", s.getName()),s);
				return true;
			}
			if(args.length==1) {
				String kit = getKitName(args[0]);
				if(kit!=null) {
				if(s.hasPermission("servercontrol.kit."+kit)||s.hasPermission("ServerControl.Kit.*")) {
				if(s instanceof Player) {
    				giveKit(s.getName(), kit, true, true);
        			return true;
					}
				
				Loader.Help(s, "/Kit "+kit+" <player>", "Kit-Give");
				return true;
				}
				s.sendMessage(TheAPI.colorize(Loader.s("NotPermissionsMessage")
						.replace("%player%", s.getName())
						.replace("%playername%", s.getName())
						.replace("%permission%", "servercontrol.kit."+kit)));
				return true;
			}

				Loader.msg(Loader.s("Prefix")+Loader.s("Kit.NotExists").
						replace("%kit%", args[0])
						.replace("%player%", s.getName())
						.replace("%playername%", s.getName()),s);
				return true;
			}
			if(args.length==2) {
				String kit = getKitName(args[0]);
				if(kit==null) {
					Loader.msg(Loader.s("Prefix")+Loader.s("Kit.NotExists").
							replace("%kit%", args[0])
							.replace("%player%", s.getName())
							.replace("%playername%", s.getName()),s);
					return true;
				}
					Player t = Bukkit.getPlayer(args[1]);
					if(t!=null){
						if(t!=s) {
						if(API.hasPerm(s, "servercontrol.kit.give")) {
					Loader.msg(Loader.s("Prefix")+Loader.s("Kit.Got").
							replace("%kit%", getKitName(args[0]))
							.replace("%player%", t.getName())
							.replace("%playername%", t.getDisplayName()),t);
					Loader.msg(Loader.s("Prefix")+Loader.s("Kit.Given").
							replace("%kit%", getKitName(args[0]))
							.replace("%player%", t.getName())
							.replace("%playername%", t.getDisplayName()),s);
					giveKit(t.getName(), kit, false, false);
					return true;
						}return true;
					}else {
						giveKit(t.getName(), kit, true, true);
						return true;
					}}
					if(args[0].equals("*")) {
						Repeat.a(s,"kit "+kit+" *");
						return true;
					}
					Loader.msg(Loader.PlayerNotOnline(args[1]),s);
					return true;
			}
		return false;
	}
	public List<String> kitss(CommandSender s){
		List<String> w = new ArrayList<String>();
		if(Loader.kit.getString("Kits")!=null)
		for(String d: Loader.kit.getConfigurationSection("Kits").getKeys(false)) {
		if(s.hasPermission("ServerControl.Kit."+d)) {
			w.add(d);
		}}
		return w;
	}
    @Override
    public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
    	List<String> c = new ArrayList<>();
    	if(args.length==1) {
         c.addAll(StringUtil.copyPartialMatches(args[0], kitss(s), new ArrayList<>()));
    	}
    	if(args.length==2)return null;
    return c;
}	

}
