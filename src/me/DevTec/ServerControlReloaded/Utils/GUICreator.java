package me.DevTec.ServerControlReloaded.Utils;

import me.DevTec.ServerControlReloaded.SCR.Loader;
import me.devtec.theapi.TheAPI;
import me.devtec.theapi.TheAPI.SudoType;
import me.devtec.theapi.apis.ItemCreatorAPI;
import me.devtec.theapi.configapi.Config;
import me.devtec.theapi.economyapi.EconomyAPI;
import me.devtec.theapi.guiapi.GUI;
import me.devtec.theapi.guiapi.HolderGUI;
import me.devtec.theapi.guiapi.ItemGUI;
import me.devtec.theapi.utils.StringUtils;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

public class GUICreator implements CommandExecutor {
    private Config c = Loader.guicreator;
    private String a;

    public GUICreator(String a){
        this.a=a;
    }

    public boolean onCommand(CommandSender s, Command uu, String u, String[] args) {
        if(!(s instanceof Player)){
            return true;
        }
        udelator(a,(Player)s);
        return true;
    }
    public void udelator(String b,Player s){
        GUI gui = new GUI(c.getString("GUI."+b+".title"),c.getInt("GUI."+b+".size"));
        ItemGUI itemGUI = null;
        for(String j : c.getKeys("GUI."+b+".items")){
            if(c.exists("GUI."+b+".items."+j+".headURL")){
                if(c.exists("GUI."+b+".items."+j+".lore")){
                    itemGUI=MethodaHead(gui,itemGUI,c.getString("GUI."+b+".items."+j+".name"),c.getStringList("GUI."+b+".items."+j+".lore"),c.getString("GUI."+b+".items."+j+".headURL"),j,b);
                }else{
                    itemGUI=MethodaHead(gui,itemGUI,c.getString("GUI."+b+".items."+j+".name"),c.getString("GUI."+b+".items."+j+".headURL"),j,b);
                }
            } else if(c.exists("GUI."+b+".items."+j+".type")){
                if(c.exists("GUI."+b+".items."+j+".lore")){
                    itemGUI=MethodaItem(gui,itemGUI,c.getString("GUI."+b+".items."+j+".name"),c.getString("GUI."+b+".items."+j+".type").toUpperCase(),j,c.getStringList("GUI."+b+".items."+j+".lore"),b);
                }else{
                    itemGUI=MethodaItem(gui,itemGUI,c.getString("GUI."+b+".items."+j+".name"),c.getString("GUI."+b+".items."+j+".type").toUpperCase(),j,b);
                }
            } else {
                if(c.get("GUI."+b+".items."+j+".headURL")==null){
                    TheAPI.msg(Loader.getTranslation("Missing.Material")+"",s);
                }else{
                    TheAPI.msg(Loader.getTranslation("Missing.HeadURL")+"",s);
                }
                return;
            }
            for(String slot : j.split(",[ ]*"))
                gui.setItem(StringUtils.getInt(slot), itemGUI);
        }
        gui.open(s);
    }

    public void vecinator(GUI g ,Player p, String b, String s, GUI.ClickType clickType){
        if(EconomyAPI.has(p,c.getDouble("GUI."+b+".items."+s+".cost"))) {
            if(c.getBoolean("GUI."+b+".items."+s+".takeMoney")){
                EconomyAPI.withdrawPlayer(p,c.getDouble("GUI."+b+".items."+s+".cost"));
            }
            if (c.get("GUI." + b + ".items." + s + ".action") instanceof Collection&&c.exists("GUI."+b+".items."+s+".action")) {
                for (String a : c.getStringList("GUI." + b + ".items." + s + ".action")) {
                    if (a.startsWith("sleft")) {
                        if (clickType == GUI.ClickType.SHIFT_LEFT_PICKUP) {
                            methodenzi(g, p, a.substring(6));
                        }
                    } else if (a.startsWith("sright")) {
                        if (clickType == GUI.ClickType.SHIFT_RIGHT_PICKUP) {
                            methodenzi(g, p, a.substring(7));
                        }
                    } else if (a.startsWith("left")) {
                        if (clickType == GUI.ClickType.LEFT_PICKUP) {
                            methodenzi(g, p, a.substring(5));
                        }
                    } else if (a.startsWith("right")) {
                        if (clickType == GUI.ClickType.RIGHT_PICKUP) {
                            methodenzi(g, p, a.substring(6));
                        }
                    } else if (a.startsWith("any")) {
                        methodenzi(g, p, a.substring(4));
                    }
                }
            } else {
                String a = c.getString("GUI." + b + ".items." + s + ".action");
                if(a==null)return;
                if (a.startsWith("sleft")) {
                    if (clickType == GUI.ClickType.SHIFT_LEFT_PICKUP) {
                        methodenzi(g, p, a.substring(6));
                    }
                } else if (a.startsWith("sright")) {
                    if (clickType == GUI.ClickType.SHIFT_RIGHT_PICKUP) {
                        methodenzi(g, p, a.substring(7));
                    }
                } else if (a.startsWith("left")) {
                    if (clickType == GUI.ClickType.LEFT_PICKUP) {
                        methodenzi(g, p, a.substring(5));
                    }
                } else if (a.startsWith("right")) {
                    if (clickType == GUI.ClickType.RIGHT_PICKUP) {
                        methodenzi(g, p, a.substring(6));
                    }
                } else if (a.startsWith("any")) {
                    methodenzi(g, p, a.substring(4));
                }
            }
        }else{
            if (c.get("GUI." + b + ".items." + s + ".noMoney") instanceof Collection&&c.exists("GUI."+b+".items."+s+".noMoney")) {
                for (String a : c.getStringList("GUI." + b + ".items." + s + ".noMoney")) {
                    if (a.startsWith("sleft")) {
                        if (clickType == GUI.ClickType.SHIFT_LEFT_PICKUP) {
                            methodenzi(g, p, a.substring(6));
                        }
                    } else if (a.startsWith("sright")) {
                        if (clickType == GUI.ClickType.SHIFT_RIGHT_PICKUP) {
                            methodenzi(g, p, a.substring(7));
                        }
                    } else if (a.startsWith("left")) {
                        if (clickType == GUI.ClickType.LEFT_PICKUP) {
                            methodenzi(g, p, a.substring(5));
                        }
                    } else if (a.startsWith("right")) {
                        if (clickType == GUI.ClickType.RIGHT_PICKUP) {
                            methodenzi(g, p, a.substring(6));
                        }
                    } else if (a.startsWith("any")) {
                        methodenzi(g, p, a.substring(4));
                    }
                }
            } else {
                String a = c.getString("GUI." + b + ".items." + s + ".noMoney");
                if(a==null)return;
                if (a.startsWith("sleft")) {
                    if (clickType == GUI.ClickType.SHIFT_LEFT_PICKUP) {
                        methodenzi(g, p, a.substring(6));
                    }
                } else if (a.startsWith("sright")) {
                    if (clickType == GUI.ClickType.SHIFT_RIGHT_PICKUP) {
                        methodenzi(g, p, a.substring(7));
                    }
                } else if (a.startsWith("left")) {
                    if (clickType == GUI.ClickType.LEFT_PICKUP) {
                        methodenzi(g, p, a.substring(5));
                    }
                } else if (a.startsWith("right")) {
                    if (clickType == GUI.ClickType.RIGHT_PICKUP) {
                        methodenzi(g, p, a.substring(6));
                    }
                } else if (a.startsWith("any")) {
                    methodenzi(g, p, a.substring(4));
                }
            }
        }
    }

    public void methodenzi(GUI g ,Player p,String a){
        if(a.startsWith("close")){
            g.close(p);
        } else if (a.startsWith("msg")){
            TheAPI.msg(a.replace("%player%",p.getName()).substring(4),p);
        } else if(a.startsWith("cmd")){
            if(a.substring(4).startsWith("player")){
            	TheAPI.sudo(p,SudoType.COMMAND,a.substring(11).replace("%player%",p.getName()));
            } else if(a.substring(4).startsWith("console")){
                TheAPI.sudoConsole(a.substring(12).replace("%player%",p.getName()));
            }
        } else if(a.startsWith("open")){
            udelator(a.substring(5),p);
        }
    }

    private ItemGUI MethodaHead(GUI gui,ItemGUI g,String name,List<String> list,String headURL,String item,String other){
        g=new ItemGUI(ItemCreatorAPI.createHeadByValues(1,name,list,headURL)) {
            @Override
            public void onClick(Player player, HolderGUI hgui, GUI.ClickType click) {
                try {vecinator(gui,player,other,item,click);}catch (Exception e){e.printStackTrace();}
            }
        };
        return g;
    }
    private ItemGUI MethodaHead(GUI gui, ItemGUI g, String name, String headURL,String item,String other){
        g=new ItemGUI(ItemCreatorAPI.createHeadByValues(1,name,headURL)) {
            @Override
            public void onClick(Player player, HolderGUI hgui, GUI.ClickType click) {
                try {vecinator(gui,player,other,item,click);}catch (Exception e){e.printStackTrace();}
            }
        };
        return g;
    }
    private ItemGUI MethodaItem(GUI gui, ItemGUI g, String name, String material, String item, List<String> lore, String other){
        g=new ItemGUI(ItemCreatorAPI.create(Material.getMaterial(material),1,name,lore)) {
            @Override
            public void onClick(Player player, HolderGUI hgui, GUI.ClickType click) {
                try {vecinator(gui,player,other,item,click);}catch (Exception e){e.printStackTrace();}
            }
        };
        return g;
    }
    private ItemGUI MethodaItem(GUI gui, ItemGUI g, String name, String material,String item,String other){
        g=new ItemGUI(ItemCreatorAPI.create(Material.getMaterial(material),1,name)) {
            @Override
            public void onClick(Player player, HolderGUI hgui, GUI.ClickType click) {
                try {vecinator(gui,player,other,item,click);}catch (Exception e){e.printStackTrace();}
            }
        };
        return g;
    }

}
