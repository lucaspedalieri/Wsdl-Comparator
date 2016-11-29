package businesslogic;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class Preference {
	private Preferences preferences;

	public Preference() {

	}

	public void setPreference(String key, String value) {
		try {
			preferences = Preferences.userNodeForPackage(this.getClass());
			preferences.put(key, value);
			preferences.sync();
		} catch (BackingStoreException e) {
			System.out.println(e.getMessage());
		}
	}

	public String getPreference(String key) {
		preferences = Preferences.userNodeForPackage(this.getClass());
		return preferences.get(key, null);
	}

}
