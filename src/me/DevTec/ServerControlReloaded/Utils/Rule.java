package me.DevTec.ServerControlReloaded.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Rule {
	private final String a, b, c, e;
	private final boolean d;
	private Pattern reg;
	public Rule(String value, String type, String convert, boolean replacement, String replaceValue) {
		a=value;
		b=type;
		c=convert;
		d=replacement;
		e=replaceValue;
		if(type.equalsIgnoreCase("REGEX"))
			reg=Pattern.compile(value);
		else
			reg=null;
	}
	
	public void update() {
		if(b.equalsIgnoreCase("REGEX"))
			reg=Pattern.compile(a);
		else
			reg=null;
	}
	
	public String getValue() {
		return a;
	}
	
	public String getType() {
		return b;
	}
	
	public String getConvertType() {
		return c;
	}
	
	public String getReplaceValue() {
		return e;
	}
	
	public boolean isReplacing() {
		return d;
	}
	
	public String apply(String text) {
		String done = text;
		switch(c.toLowerCase()) {
		case "LOWERCASE":
			done=done.toLowerCase();
			break;
		case "UPPERCASE":
			done=done.toUpperCase();
			break;
		}
		switch(b.toLowerCase()) {
		case "REGEX":
			Matcher m = reg.matcher(done);
			while(m.find()) {
				if(d)
					text=text.replace(m.group(), e);
				else return null;
			}
			break;
		case "CONTAINS":
			if(done.contains(a)) {
				if(d)
					text=text.replace(a, e);
				else return null;
			}
			break;
		case "EQUALS":
			if(done.equals(a)) {
				if(d)
					text=text.replace(a, e);
				else return null;
			}
			break;
		}
		return text;
	}
}