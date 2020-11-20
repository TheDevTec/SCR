package me.DevTec.ServerControlReloaded.Utils.Skins.mineskin;

public class SkinOptions {

	private final String     name;
	private final Model      model;
	private final Visibility visibility;

	private SkinOptions(String name, Model model, Visibility visibility) {
		this.name = name;
		this.model = model;
		this.visibility = visibility;
	}

	public String toUrlParam() {
		return "name="+name+"&model="+model.getName()+"&visibility="+visibility.getCode();
	}

	public static SkinOptions create(String name, Model model, Visibility visibility) {
		return new SkinOptions(name, model, visibility);
	}

	public static SkinOptions name(String name) {
		return new SkinOptions(name, Model.DEFAULT, Visibility.PUBLIC);
	}

	public static SkinOptions none() {
		return new SkinOptions("", Model.DEFAULT, Visibility.PUBLIC);
	}

}
