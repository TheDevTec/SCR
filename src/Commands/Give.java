package Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import Utils.Repeat;
import Utils.XMaterial;
import me.Straiker123.ItemCreatorAPI;
import me.Straiker123.MultiMap;
import me.Straiker123.TheAPI;

public class Give implements CommandExecutor, TabCompleter {
	List<String> list = new ArrayList<String>();
	public Give() {
		for(Material ss:Material.values()) {
			if(!ss.name().contains("WALL_")&&!ss.name().contains("AIR")&&!ss.name().contains("VOID")
					&&!ss.name().contains("_STEM")&&!ss.name().contains("POTTED_")&&!ss.name().contains("LEGACY_")
					&&!ss.name().equals("END_PORTAL")&&!ss.name().equals("END_GATEWAY")&&!ss.name().equals("NETHER_PORTAL")
					|| ss.isBlock() && ss.isOccluding())
		if(ss != Material.AIR)
		list.add(ss.name());
		}
		for(String add : Arrays.asList("POTION_OF_","SPLASH_POTION_OF_","LINGERING_POTION_OF_")) {
		list.add(add+"INVISIBILITY");
		list.add(add+"INVISIBILITY2");

		list.add(add+"NIGHT_VISION");
		list.add(add+"NIGHT_VISION2");

		list.add(add+"LEAPING");
		list.add(add+"LEAPING2");
		list.add(add+"LEAPING3");

		list.add(add+"FIRE_RESISTANCE");
		list.add(add+"FIRE_RESISTANCE2");
		
		list.add(add+"SPEED");
		list.add(add+"SPEED2");
		list.add(add+"SPEED3");

		list.add(add+"SLOWNESS");
		list.add(add+"SLOWNESS2");
		list.add(add+"SLOWNESS3");

		list.add(add+"WATER_BREATHING");
		list.add(add+"WATER_BREATHING2");
		
		list.add(add+"HEALING");
		list.add(add+"HEALING2");

		list.add(add+"HARMING");
		list.add(add+"HARMING2");

		list.add(add+"POISON");
		list.add(add+"POISON2");
		list.add(add+"POISON3");

		list.add(add+"REGENERATION");
		list.add(add+"REGENERATION2");
		list.add(add+"REGENERATION3");

		list.add(add+"STRENGHT");
		list.add(add+"STRENGHT2");
		list.add(add+"STRENGHT3");

		list.add(add+"WEAKNESS");
		list.add(add+"WEAKNESS2");

		list.add(add+"LUCK");
		
		list.add(add+"SWIFTNESS");
		list.add(add+"SWIFTNESS2");
		list.add(add+"SWIFTNESS3");
		if(TheAPI.isNewVersion()) {
		list.add(add+"TURTLE_MASTER");
		list.add(add+"TURTLE_MASTER2");
		list.add(add+"TURTLE_MASTER3");
		list.add(add+"SLOW_FALLING");
		list.add(add+"SLOW_FALLING2");
		}}
	}
	
	private ItemStack getPotion(String s) {
		Material args1 = null; //type
		PotionEffectType args2 = null; //eff
		int args3=0; //level
		int args4=0; //time
		boolean multi =false;
		MultiMap a = TheAPI.getMultiMap();
		if(s.toUpperCase().startsWith("POTION_OF_")) {
			args1=XMaterial.POTION.parseMaterial();
			args2=PotionEffectType.getByName(s.toUpperCase().replaceFirst("POTION_OF_", "").replaceAll("[0-9]", ""));
			args3=TheAPI.getStringUtils().getInt(s);
		}
		if(s.toUpperCase().startsWith("SPLASH_POTION_OF_")) {
			args1=XMaterial.SPLASH_POTION.parseMaterial();
			args2=PotionEffectType.getByName(s.toUpperCase().replaceFirst("SPLASH_POTION_OF_", "").replaceAll("[0-9]", ""));
			args3=TheAPI.getStringUtils().getInt(s);
		}
		if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
			args1=XMaterial.LINGERING_POTION.parseMaterial();
			args2=PotionEffectType.getByName(s.toUpperCase().replaceFirst("LINGERING_POTION_OF_", "").replaceAll("[0-9]", ""));
			args3=TheAPI.getStringUtils().getInt(s);
			
		}
		if(args1==null) return null;
		switch(s.toUpperCase().replaceFirst("LINGERING_POTION_OF_", "").replaceFirst("SPLASH_POTION_OF_", "").replaceFirst("POTION_OF_", "")) {
		case "INVISIBILITY":
			args3=1;
			args4=180;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=45;
			}
			break;
		case "INVISIBILITY2":
			args3=1;
			args4=480;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=120;
			}
			break;
		case "NIGHT_VISION":
			args3=1;
			args4=180;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=45;
			}
			break;
		case "NIGHT_VISION2":
			args3=1;
			args4=480;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=120;
			}
			break;

		case "FIRE_RESISTANCE":
			args3=1;
			args4=180;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=45;
			}
			break;
		case "FIRE_RESISTANCE2":
			args3=1;
			args4=480;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=120;
			}
			break;
		case "SWIFTNESS":
		case "SPEED":
			args3=1;
			args4=180;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=45;
			}
			break;
		case "SWIFTNESS2":
		case "SPEED2":
			args3=1;
			args4=480;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=120;
			}
			break;
		case "SWIFTNESS3":
		case "SPEED3":
			args3=2;
			args4=90;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=22;
			}
			break;

		case "WATER_BREATHING":
			args3=1;
			args4=180;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=45;
			}
			break;
		case "WATER_BREATHING2":
			args3=1;
			args4=480;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=120;
			}
			break;

		case "HEALING":
			args3=1;
			args4=0;
			break;
		case "HEALING2":
			args3=2;
			args4=0;
			break;

		case "HARMING":
			args3=1;
			args4=0;
			break;
		case "HARMING2":
			args3=2;
			args4=0;
			break;

		case "POISON":
			args3=1;
			args4=45;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=11;
			}
			break;
		case "POISON2":
			args3=1;
			args4=90;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=22;
			}
			break;
		case "POISON3":
			args3=2;
			args4=21;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=5;
			}
			break;

		case "REGENERATION":
			args3=1;
			args4=45;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=11;
			}
			break;
		case "REGENERATION2":
			args3=1;
			args4=90;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=22;
			}
			break;
		case "REGENERATION3":
			args3=2;
			args4=22;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=5;
			}
			break;

		case "STRENGHT":
			args3=1;
			args4=180;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=45;
			}
			break;
		case "STRENGHT2":
			args3=1;
			args4=480;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=120;
			}
			break;
		case "STRENGHT3":
			args3=2;
			args4=90;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=22;
			}
			break;

		case "WEAKNESS":
			args3=1;
			args4=180;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=22;
			}
			break;
		case "WEAKNESS2":
			args3=1;
			args4=480;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=60;
			}
			break;
		case "LUCK":
			args3=1;
			args4=480;
			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=75;
			}
			break;
		case "TURTLE_MASTER":
			multi=true;
			a.put("WATER_BREATHING", 1,0);
			a.put("SLOWNESS", 4,20);
			a.put("DAMAGE_RESISTANCE", 3,20);
			break;
		case "TURTLE_MASTER2":
			multi=true;
			a.put("WATER_BREATHING", 1,0);
			a.put("SLOWNESS", 4,40);
			a.put("DAMAGE_RESISTANCE", 3,40);
			break;
		case "TURTLE_MASTER3":
			multi=true;
			a.put("WATER_BREATHING", 1,0);
			a.put("SLOWNESS", 4,20);
			a.put("DAMAGE_RESISTANCE", 4,20);
			break;

		case "SLOW_FALLING":
			args3=1;
			args4=90;

			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=22;
			}
			break;
		case "SLOW_FALLING2":
			args3=1;
			args4=240;

			if(s.toUpperCase().startsWith("LINGERING_POTION_OF_")) {
				args4=60;
			}
			break;
			
		}
		ItemCreatorAPI g = TheAPI.getItemCreatorAPI(args1);
		if(multi) {
		for(Object f : a.getKeySet()) { //effect
		Object[] o = a.getValues(f).toArray();//values
		int i = TheAPI.getStringUtils().getInt(o[0].toString());
		int ib = TheAPI.getStringUtils().getInt(o[1].toString());
		g.addPotionEffect(f.toString(), i, (ib == 0 ? 1 : ib));
		}
		}else
			g.addPotionEffect(args2, args3, (args4 == 0 ? 1 : args4));

		if(s.toUpperCase().startsWith("LINGERING_POTION_OF_"))
			g.addPotionEffect("WATER_BREATHING", 0, 1);
		return g.create();
	}
	
	public List<String> items(){
		return list;
	}
	public String getItem(String s) {
		if(list.contains(s.toUpperCase()))return s.toUpperCase();
		return null;
	}
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Give")) {
			if(args.length==0) {
				Loader.Help(s, "/Give <player> <item> <amount>", "Give");
				return true;
			}
				if(args.length==1) {
					Player ps = Bukkit.getPlayer(args[0]);
					if(ps==null) {

						if(s instanceof Player==false) {
							Loader.Help(s, "/Give <player> <item> <amount>", "Give");
							return true;
						}
					if(getItem(args[0])!=null) {
						if(API.hasPerm(s, "ServerControl."+getItem(args[0]))||API.hasPerm(s, "ServerControl.Give.*")) {
							Player p = (Player)s;
							try {
								String g = args[0].toUpperCase();
								if(!g.startsWith("LINGERING_POTION_OF_")||g.startsWith("SPLASH_POTION_OF_")||g.startsWith("POTION_OF_"))
							TheAPI.giveItem(p, Material.matchMaterial(args[0]),1);
								else
									TheAPI.giveItem(p, getPotion(args[0]));
							Loader.msg(Loader.s("Prefix")+API.replacePlayerName(Loader.s("Give.Given"),p).replace("%amount%", "1").replace("%item%", getItem(args[0])), s);
						return true;
							}catch(Exception e) {
								Loader.msg(Loader.s("Prefix")+Loader.s("Give.UknownItem").replace("%item%", args[0]), s);
								return true;
							}
						}return true;
					}
					Loader.msg(Loader.s("Prefix")+Loader.s("Give.UknownItem").replace("%item%", args[0]), s);
						return true;
					}
						Loader.Help(s, "/Give <player> <item> <amount>", "Give");
					return true;
					}
				if(args.length==2) {
					Player ps = Bukkit.getPlayer(args[0]);
					if(ps==null) {
						if(args[0].equals("*")) {
							Repeat.a(s,"give * "+args[1]);
							return true;
						}
						if(getItem(args[0])!=null) {
							ps=(Player)s;
							try {
							TheAPI.giveItem(ps, new ItemStack(Material.matchMaterial(args[0]),TheAPI.getStringUtils().getInt(args[1])));
							Loader.msg(Loader.s("Prefix")+API.replacePlayerName(Loader.s("Give.Given"),ps).replace("%amount%",TheAPI.getStringUtils().getInt(args[1])+"").replace("%item%", getItem(args[0])), s)
							;return true;
							
						}catch(Exception e) {
							Loader.msg(Loader.s("Prefix")+Loader.s("Give.UknownItem").replace("%item%", args[0]), s);
							return true;
						}
						}
						if(s instanceof Player ==false)
							Loader.msg(Loader.PlayerNotOnline(args[1]), s);
						else
							Loader.msg(Loader.s("Prefix")+Loader.s("Give.UknownItem").replace("%item%", args[0]), s);
							return true;
						
					}
					if(getItem(args[1])!=null) {
						TheAPI.giveItem(ps, new ItemStack(Material.matchMaterial(args[1])));
						Loader.msg(Loader.s("Prefix")+API.replacePlayerName(Loader.s("Give.Given"),ps).replace("%item%", getItem(args[1])).replace("%amount%", "1"), s);
						return true;
						}
						Loader.msg(Loader.s("Prefix")+Loader.s("Give.UknownItem").replace("%item%", args[1]), s);
						return true;
				}
				if(args.length==3) {
					Player ps = Bukkit.getPlayer(args[0]);
					if(ps!=null) {
						if(args[0].equals("*")) {
							Repeat.a(s,"give * "+args[1]+" "+args[2]);
							return true;
						}
						if(getItem(args[1])!=null) {
							String g = args[0].toUpperCase();
							if(!g.startsWith("LINGERING_POTION_OF_")||g.startsWith("SPLASH_POTION_OF_")||g.startsWith("POTION_OF_"))
						TheAPI.giveItem(ps, Material.matchMaterial(args[0]),TheAPI.getStringUtils().getInt(args[2]));
							else {
								ItemStack a = getPotion(args[0]);
								a.setAmount(TheAPI.getStringUtils().getInt(args[2]));
								TheAPI.giveItem(ps, a);
							}
							Loader.msg(Loader.s("Prefix")+API.replacePlayerName(Loader.s("Give.Given"),ps).replace("%item%", getItem(args[1])).replace("%amount%", TheAPI.getStringUtils().getInt(args[2])+""), s);
							return true;
							}
							Loader.msg(Loader.s("Prefix")+Loader.s("Give.UknownItem").replace("%item%", args[1]), s);
							return true;			
					}
					Loader.msg(Loader.PlayerNotOnline(args[1]), s);
					return true;
				}
				
	}return true;
	}
	  @Override
	  public List<String> onTabComplete(CommandSender s, Command cmd, String alias, String[] args) {
	  	List<String> c = new ArrayList<>();
	      	if(s.hasPermission("ServerControl.Give")) {
	    	  	if(args.length==1)return null;
	    	  	if(args.length==2) {
		      		c.addAll(StringUtil.copyPartialMatches(args[1], items(), new ArrayList<>()));
	    	  		}
	    	  	if(args.length==3) {
		      		c.addAll(StringUtil.copyPartialMatches(args[1], Arrays.asList("?"), new ArrayList<>()));
	    	  	}
	    	  	}
	      return c;}
}
