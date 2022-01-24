package me.devtec.scr.modules.events;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.entity.Player;

public class Rule {
	private List<String> whitelist;
	private String value, type, replaceValue, name;
	private int patternFlags;
	private Pattern pattern;
	private boolean doReplace, bypassable;
	
	public Rule(String name, String value, String type, boolean replacement, String replaceValue, int patternFlags, List<String> wl, boolean bypassable) {
		whitelist=wl;
		this.bypassable=bypassable;
		this.value=value;
		this.name=name;
		this.type=type.toUpperCase();
		doReplace=replacement;
		this.replaceValue=replaceValue;
		this.patternFlags=patternFlags;
		pattern=patternFlags==0?Pattern.compile(value):Pattern.compile(value, patternFlags);
	}
	
	public String getValue() {
		return value;
	}
	
	public int getPatternFlags() {
		return patternFlags;
	}
	
	public String getType() {
		return type;
	}
	
	public String getReplaceValue() {
		return replaceValue;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isReplacing() {
		return doReplace;
	}
	
	public String apply(String text, Player s) {
		if(bypassable && s.hasPermission("scr.bypass.rules"))return text;
		if(text==null)return null;
		switch(type) {
		case "REGEX":
			Matcher m = pattern.matcher(text);
			if(m.find()) {
				if (whitelist.contains(m.group().toLowerCase()))
					return text;
				if(doReplace)
					text = m.replaceAll(replaceValue);
				else return null;
			}
			break;
		case "CONTAINS":
			if(text.contains(value)) {
				if (whitelist.contains(text.toLowerCase()))
					return text;
				if(doReplace)
					text=text.replace(value, replaceValue);
				else return null;
			}
			break;
		case "STARTS_WITH":
			if(text.startsWith(value)) {
				if (whitelist.contains(text.toLowerCase()))
					return text;
				if(doReplace)
					text=text.replace(value, replaceValue);
				else return null;
			}
			break;
		case "ENDS_WITH":
			if(text.endsWith(value)) {
				if (whitelist.contains(text.toLowerCase()))
					return text;
				if(doReplace)
					text=text.replace(value, replaceValue);
				else return null;
			}
			break;
		case "EQUALS":
			if(text.equalsIgnoreCase(value)) {
				if (whitelist.contains(text.toLowerCase()))
					return text;
				if(doReplace)
					text=text.replace(value, replaceValue);
				else return null;
			}
			break;
		}
		return text;
	}

	public boolean isBypassable() {
		return bypassable;
	}
}