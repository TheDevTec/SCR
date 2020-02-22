package Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import ServerControl.API;
import ServerControl.Loader;
import me.Straiker123.TheAPI;

public class EnchantTable implements CommandExecutor, TabCompleter {
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender s, Command arg1, String arg2, String[] args) {
		if(API.hasPerm(s, "ServerControl.Enchant")) {
			if(s instanceof Player) {
				Player p = (Player)s;
				if(args.length==0) {
				Loader.Help(s, "/Enchant <enchant> <level>", "Enchant");
				return true;
				}
				Material a = p.getItemInHand().getType();
				if(args.length==1) {
					if(a!=Material.AIR) {
					e(p.getItemInHand(),1,args[0],s);
					return true;
					}
					Loader.msg(Loader.s("Prefix")+Loader.s("Enchant.HandIsEmpty")
					.replace("%enchant%", args[0]).replace("%level%", "0").replace("%item%", a.name()), s);
					return true;
					}
				if(args.length==2) {
					if(a!=Material.AIR) {
					e(p.getItemInHand(),TheAPI.getNumbersAPI(args[1]).getInt(),args[0],s);
					return true;
					}
					Loader.msg(Loader.s("Prefix")+Loader.s("Enchant.HandIsEmpty")
					.replace("%enchant%", args[0]).replace("%level%", "0").replace("%item%", a.name()), s);
					return true;
					}
				
			}	
			Loader.msg(Loader.s("ConsoleErrorMessage"), s);
			return true;
		}
		
		return true;
	}
	public void e(ItemStack hand, int level, String enechant,CommandSender s) {
		try {
		if(level<=0) {
			hand.removeEnchantment(TheAPI.getEnchantmentAPI().getByName(enechant));
		}else
			hand.addUnsafeEnchantment(TheAPI.getEnchantmentAPI().getByName(enechant), level);
		Loader.msg(Loader.s("Prefix")+Loader.s("Enchant.Enchanted")
		.replace("%enchant%", enechant).replace("%level%", String.valueOf(level)).replace("%item%", hand.getType().name()), s);
		return;
		}catch(Exception error) {
			Loader.msg(Loader.s("Prefix")+Loader.s("Enchant.EnchantNotExist")
			.replace("%enchant%", enechant).replace("%level%", "0").replace("%item%", hand.getType().name()), s);
			return;
		}
		}
	public boolean contains(String s, String[] args) {
		if(args[0].equalsIgnoreCase(s))return true;
		return false;
	}
	
	
	@Override
	public List<String> onTabComplete(CommandSender s, Command a, String ea, String[] args) {
    	List<String> c = new ArrayList<>();
			if(s.hasPermission("ServerControl.Enchant")) {
	    	List<String> enchs = Arrays.asList("ARROW_DAMAGE","POWER","ARROW_FIRE", "FIRE","ARROW_INFINITE", "INFINITY", "ARROW_KNOCKBACK","PUNCH",
	    			"BINDING_CURSE","CURSE_OF_BINDING", "DAMAGE_ALL","SHARPNESS", "DAMAGE_ARTHROPODS","BANE_OF_ARTHROPODS", "DAMAGE_UNDEAD","SMITE"
	    			, "DEPTH_STRIDER","BANEOFARTHROPODS","DAMAGE_ARTHROPODS"
	    			, "DIG_SPEED","EFFICIENCY", "DURABILITY","UNBREAKING", "FIRE_ASPECT","FIREASPECT", "FROST_WALKER", "KNOCKBACK"
	    			, "LOOT_BONUS_BLOCKS","LOOTBLOCKS","FORTUNE"
	    			, "LOOT_BONUS_MOBS","LOOTMOBS","LOOTING", "LUCK", "LURE", "MENDING", "OXYGEN","RESPIRATION", "PROTECTION_ENVIRONMENTAL","PROTECTION",
	    			"PROTECTION_EXPLOSIONS","BLAST_PROTECTION","ALLDAMAGE","ALL_DAMAGE","DAMAGEALL"
	    			, "PROTECTION_FALL","FEATHER_FALLING", "PROTECTION_FIRE","FIRE_PROTECTION", "PROTECTION_PROJECTILE","PROJECTILE_PROTECTION"
	    			, "SILK_TOUCH", "SWEEPING_EDGE"
	    			, "THORNS", "VANISHING_CURSE","CURSE_OF_VANISHING", "WATER_WORKER","AQUA_AFFINITY");
	    	if(TheAPI.isNewVersion()) enchs.addAll(
	    			Arrays.asList("LOYALTY","PIERCING","IMPALING","MULTISHOT","QUICK_CHARGE","RIPTIDE","CHANNELING"));
			if(s instanceof Player) {
				if(args.length==1) {
	            	c.addAll(StringUtil.copyPartialMatches(args[0], enchs, new ArrayList<>()));
				}
					if(args.length==2) {
						if(TheAPI.isNewVersion()) {
							if(args[0].equalsIgnoreCase("QUICK_CHARGE")||contains("MULTISHOT",args)||contains("RIPTIDE",args)||contains("CHANNELING",args))
				            	c.add("1");
							if(args[0].equalsIgnoreCase("LOYALTY")||contains("PIERCING",args)||contains("IMPALING",args))
				            	c.add("3");
						}
					if(args[0].equalsIgnoreCase("ARROW_DAMAGE")||contains("POWER",args))
		            	c.add("5");
		            	
					if(args[0].equalsIgnoreCase("ARROW_FIRE")||contains("FIRE",args))
		            	c.add("2");

		            	if(args[0].equalsIgnoreCase("ARROW_INFINITE")||contains("INFINITY",args))
		    		        c.add("1");

		            	if(args[0].equalsIgnoreCase("ARROW_KNOCKBACK")||contains("PUNCH",args))
		    		        c.add("2");

					    if(args[0].equalsIgnoreCase("BINDING_CURSE")||contains("CURSE_OF_BINDING",args))
			            	c.add("1");

		            	if(args[0].equalsIgnoreCase("DAMAGE_ALL")||contains("SHARPNESS",args)
		            			||contains("ALLDAMAGE",args)||contains("ALL_DAMAGE",args)||contains("DAMAGEALL",args))
			            	c.add("5");

		            	if(args[0].equalsIgnoreCase("DAMAGE_ARTHROPODS")||contains("BANE_OF_ARTHROPODS",args))
			            	c.add("5");

		            	if(args[0].equalsIgnoreCase("DAMAGE_UNDEAD")||contains("SMITE",args))
			            	c.add("5");

		            	if(args[0].equalsIgnoreCase("DEPTH_STRIDER"))
			            	c.add("3");

		            	if(args[0].equalsIgnoreCase("DIG_SPEED")||contains("EFFICIENCY",args))
			            	c.add("5");

		            	if(contains("DURABILITY",args)||contains("UNBREAKING",args))
			            	c.add("3");

		            	if(args[0].equalsIgnoreCase("FIRE_ASPECT")||contains("FIREASPECT",args))
			            	c.add("2");

		            	if(args[0].equalsIgnoreCase("FROST_WALKER"))
			            	c.add("2");

		            	if(args[0].equalsIgnoreCase("KNOCKBACK"))
			            	c.add("2");

		            	if(args[0].equalsIgnoreCase("LOOT_BONUS_BLOCKS")||contains("LOOTBLOCKS",args)||contains("FORTUNE",args))
			            	c.add("3");

		            	if(args[0].equalsIgnoreCase("LOOT_BONUS_MOBS")||contains("LOOTMOBS",args)||contains("LOOTING",args))
			            	c.add("3");

		            	if(args[0].equalsIgnoreCase("LUCK"))
			            	c.add("3");
		            	if(args[0].equalsIgnoreCase("LURE"))
			            	c.add("3");

		            	if(args[0].equalsIgnoreCase("MENDING"))
			            	c.add("1");

		            	if(args[0].equalsIgnoreCase("OXYGEN")||contains("RESPIRATION",args))
			            	c.add("3");

		            	if(args[0].equalsIgnoreCase("PROTECTION_ENVIRONMENTAL")||contains("PROTECTION",args))
			            	c.add("3");

		            	if(args[0].equalsIgnoreCase("PROTECTION_EXPLOSIONS")||contains("BLAST_PROTECTION",args))
			            	c.add("3");

		            	if(args[0].equalsIgnoreCase("PROTECTION_FALL")||contains("FEATHER_FALLING",args))
			            	c.add("3");

		            	if(args[0].equalsIgnoreCase("PROTECTION_FIRE")||contains("FIRE_PROTECTION",args))
			            	c.add("3");

		            	if(args[0].equalsIgnoreCase("PROTECTION_PROJECTILE")||contains("PROJECTILE_PROTECTION",args))
			            	c.add("3");

		            	if(args[0].equalsIgnoreCase("SILK_TOUCH"))
			            	c.add("1");

		            	if(args[0].equalsIgnoreCase("SWEEPING_EDGE"))
			            	c.add("3");

		            	if(args[0].equalsIgnoreCase("THORNS"))
			            	c.add("3");
		            	if(args[0].equalsIgnoreCase("VANISHING_CURSE")||contains("CURSE_OF_VANISHING",args))
			            	c.add("1");

		            	if(args[0].equalsIgnoreCase("WATER_WORKER")||contains("AQUA_AFFINITY",args))
			            	c.add("3");
		        }}}
		return c;
	}}