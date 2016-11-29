package model;

public class ParentDifference {
	private String name;
	private String flagIcon;

	public ParentDifference(String name) {
		this.name = name;
		this.flagIcon = "package.png";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFlagIcon() {
		return flagIcon;
	}

	public void setFlagIcon(String flagIcon) {
		this.flagIcon = flagIcon;
	}
}
