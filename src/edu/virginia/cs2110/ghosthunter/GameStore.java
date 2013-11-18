package edu.virginia.cs2110.ghosthunter;

import android.content.Context;
import android.location.Location;

public class GameStore {
	
	private static GameStore store;
	
	@SuppressWarnings("unused")
	private Context activityContext;
	private Hunter hunter;
	
	private GameStore(Context c) {
		activityContext = c; // Keeps this object alive as long as the Activity is alive
	}
	
	public static GameStore get(Context c) {
		if (store == null)
			store = new GameStore(c);
		return store;
	}
	
	public void initHunter(Location loc) {
		if (hunter == null) 
			hunter = new Hunter(loc);
	}
	
	public void moveHunter(Location loc) {
		if (hunter != null) {
			hunter.setLocation(loc);
		}
	}
	
}
