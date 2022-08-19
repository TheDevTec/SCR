package me.devtec.scr.commands.kits;

import java.io.File;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.devtec.scr.Loader;
import me.devtec.scr.MessageUtils.Placeholders;
import me.devtec.scr.commands.ScrCommand;
import me.devtec.scr.commands.kits.KitUtils.Kit;
import me.devtec.shared.commands.selectors.Selector;
import me.devtec.shared.commands.structures.CommandStructure;
import me.devtec.shared.dataholder.Config;
import me.devtec.shared.utility.StringUtils;

public class KitEditor implements ScrCommand {

	@SuppressWarnings("deprecation")
	@Override
	public void init(List<String> cmds) {
		
		CommandStructure.create(CommandSender.class, PERMS_CHECKER, (s, structure, args) -> { // cmd
			help(s, "usage");
		}).cooldownDetection((s, structure, args) -> inCooldown(s))
		.permission(permission("cmd"))
		.argument("list", (s, structure, args) -> { // cmd list
			String loaded = "";
			String split = Loader.translations.getString("kiteditor.list_split");
			File file = new File("plugins/SCR/kits");
			if(file.exists() && file.isDirectory()) {
				for(File f : file.listFiles()) {
					Config c = new Config(f);
					if(c.getBoolean("enabled"))
						loaded = loaded.equalsIgnoreCase("")?loaded+"&a"+c.getString("name"):loaded+split+"&a"+c.getString("name");
					else
						loaded = loaded.equalsIgnoreCase("")?loaded+"&c"+c.getString("name"):loaded+split+"&c"+c.getString("name");
				}
			}
			msgSec(s, "list", Placeholders.c().add("kits", loaded));
		})
		.parent() // cmd
		
		.argument("create", (s, structure, args) -> { // cmd create
			help(s, "create");
		}).permission(permission("create"))
			.argument(null, (s, structure, args) -> { // cmd create [kit]
				if(!KitUtils.kitExist(args[1])) { //then create
					KitUtils.createKit(args[1]);
					msgSec(s, "create", Placeholders.c().add("kit", args[1]).add("kit_name", args[1]) );
				} else // already exist
					msgSec(s, "exist", Placeholders.c().add("kit", args[1]).add("kit_name", args[1]) );
			})
			.parent() // cmd create
		.parent() // cmd
		
		.argument("delete", (s, structure, args) -> { // cmd delete
			help(s, "delete");
		}).permission(permission("delete"))
			.argument(null, (s, structure, args) -> { // cmd delete [kit]
				if(KitUtils.kitExist(args[1])) { //then create
					KitUtils.deleteKit(args[1]);
					msgSec(s, "delete", Placeholders.c().add("kit", args[1]).add("kit_name", args[1]) );
				} else // already exist
					msgSec(s, "notFound", Placeholders.c().add("kit", args[1]).add("kit_name", args[1]) );
			})
			.parent() // cmd delete
		.parent() // cmd
		
		.argument("item", (s, structure, args) -> { // cmd item
			help(s, "item");
		}).permission(permission("item"))
			.fallback((s, structure, args) -> { // cmd item [kit]
				msgSec(s, "notFound", Placeholders.c().add("kit", args[1]).add("kit_name", args[1]) );
			})
			.callableArgument((s, structure, args) ->
			 KitUtils.getKitsFor(s), (s, structure, args) -> { // cmd item [kit]
				help(s, "item");
			})
				.argument("add", (s, structure, args) -> { // cmd item [kit] add
					help(s, "item");
				})
					.argument(null, (s, structure, args) -> { // cmd item [kit] add [id]
						me.devtec.scr.commands.kits.KitUtils.Kit kit = KitUtils.loaded_kits.get(args[1]);
						if(kit !=null) {
							String id = args[3];
							if(StringUtils.isNumber(id)) {
								msg(s, "missing.useWords");
								return;
							}
							if(kit.config.exists("items.add."+id)) { // if item already exist in kit
								msgSec(s, "item.exist", Placeholders.c()
										.add("kit", kit.displayName()).add("kit_name", kit.getName())
										.add("item", id) );
								return;
							}
							Player p = (Player) s;
							Material hand = p.getItemInHand().getType();
							if(hand != Material.AIR) {
								kit.setItem("items.add."+id, p.getItemInHand());
								msgSec(s, "item.add", Placeholders.c()
										.add("kit", kit.displayName()).add("kit_name", kit.getName())
										.add("item", id) );
							} else
								msg(s, "missing.handEmpty");
						}
					})
					.parent()  // cmd item [kit] add
				.parent()  // cmd item [kit]
				.argument("set", (s, structure, args) -> { // cmd item [kit] set [slot]
					help(s, "item");
				})
					.fallback((s, structure, args) -> { // cmd item [kit] set [slot]
						msg(s, "missing.number");
					})
					.selector(Selector.INTEGER, (s, structure, args) -> { // cmd item [kit] set [slot]
						me.devtec.scr.commands.kits.KitUtils.Kit kit = KitUtils.loaded_kits.get(args[1]);
						if(kit !=null) {
							int slot = StringUtils.getInt(args[3]);
							if(kit.config.exists("items.set."+slot)) { // if item already exist in kit
								msgSec(s, "item.exist", Placeholders.c()
										.add("kit", kit.displayName()).add("kit_name", kit.getName())
										.add("item", slot) );
								return;
							}
							Player p = (Player) s;
							Material hand = p.getItemInHand().getType();
							if(hand != Material.AIR) {
								kit.setItem("items.set."+slot, p.getItemInHand());
								msgSec(s, "item.add", Placeholders.c()
										.add("kit", kit.displayName()).add("kit_name", kit.getName())
										.add("slot", slot) );
							} else
								msg(s, "missing.handEmpty");
						}
					})
					.parent()  // cmd item [kit] set
				.parent()  // cmd item [kit]
				
				.argument("remove", (s, structure, args) -> { // cmd item [kit] remove
					help(s, "item");
				})
					// cmd item [kit] remove [id]
					.callableArgument((s, structure, args) ->
					 KitUtils.loaded_kits.get(args[1]).getItems(), (s, structure, args) -> {
						Kit kit = KitUtils.loaded_kits.get(args[1]);
						String id = args[3];
						if(StringUtils.isNumber(id)) { // items.set
							int slot = StringUtils.getInt(id);
							kit.config.remove("items.set."+slot);
							kit.config.save();
							msgSec(s, "item.remove", Placeholders.c().add("item", slot));
						}else { // items.add
							kit.config.remove("items.add."+id);
							kit.config.save();
							msgSec(s, "item.remove", Placeholders.c().add("item", id));
						}
						 
					})
		.build().register(cmds.remove(0), cmds.toArray(new String[0])).getStructure();
	}

	@Override
	public String configSection() {
		return "kiteditor";
	}


}

//cmd create/delete [name]
//cmd item [kit] add [id] //ads item to kit
//cmd item [kit] set [slot] //add item to kit (SET)
//cmd item [kit] remove [id] //delete kit
//cmd list
// TODO - displayName, commands, messages, permission
