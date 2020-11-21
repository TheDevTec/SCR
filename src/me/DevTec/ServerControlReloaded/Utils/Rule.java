package me.DevTec.ServerControlReloaded.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rule {
	private final String a, b, e, f;
	private final int patternFlags;
	private final Pattern pattern;
	private final boolean d;
	public Rule(String name, String value, String type, boolean replacement, String replaceValue, int patternFlags) {
		a=value;
		f=name;
		b=type;
		d=replacement;
		e=replaceValue;
		this.patternFlags=patternFlags;
		pattern=patternFlags==0?Pattern.compile(a):Pattern.compile(a, patternFlags);
	}
	
	public String getValue() {
		return a;
	}
	
	public int getPatternFlags() {
		return patternFlags;
	}
	
	public String getType() {
		return b;
	}
	
	public String getReplaceValue() {
		return e;
	}
	
	public String getName() {
		return f;
	}
	
	public boolean isReplacing() {
		return d;
	}
	
	public String apply(String text) {
		if(text==null)return text;
		switch(b.toUpperCase()) {
		case "REGEX":
			Matcher m = pattern.matcher(text);
			if(m.find()) {
				if(d) {
					text = pattern.matcher(text).replaceAll(e);
				}else return null;
			}
			break;
		case "CONTAINS":
			if(text.contains(a)) {
				if(d)
					text=text.replace(a, e);
				else return null;
			}
			break;
		case "STARTS_WITH":
			if(text.startsWith(a)) {
				if(d)
					text=text.replace(a, e);
				else return null;
			}
			break;
		case "ENDS_WITH":
			if(text.endsWith(a)) {
				if(d)
					text=text.replace(a, e);
				else return null;
			}
			break;
		case "EQUALS":
			if(text.equals(a)) {
				if(d)
					text=text.replace(a, e);
				else return null;
			}
			break;
		}
		return text;
	}
}
