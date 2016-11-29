package model;

public class ChieldDifference {
	private String name;
	private String flagIcon;

	public ChieldDifference(String name) {
		this.name = name;
		this.flagIcon = "wrench.png";
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
