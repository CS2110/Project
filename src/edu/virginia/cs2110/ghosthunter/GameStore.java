package edu.virginia.cs2110.ghosthunter;

import android.content.Context;

public class GameStore {
	
	private static GameStore store;
	
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
	
}
