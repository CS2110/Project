package edu.virginia.cs2110.ghosthunter;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class GameStore {

	public static final int INIT_NUMBER_GHOSTS = 10;
	public static final double GHOST_STEP_SIZE = 1e-5;
	
	private static GameStore store;

	@SuppressWarnings("unused")
	private Context activityContext;
	private Hunter hunter;
	private ArrayList<Ghost> ghosts;

	private GameStore(Context c) {
		activityContext = c; // Keeps this object alive as long as the Activity
								// is alive
	}

	public static GameStore get(Context c) {
		if (store == null)
			store = new GameStore(c);
		return store;
	}

	public void initGame(Location loc) {
		if (hunter == null)
			hunter = new Hunter(loc);
		if (ghosts == null) {
			ghosts = new ArrayList<Ghost>();
			for (int i = 0; i < INIT_NUMBER_GHOSTS; i++) {
				Ghost g = new Ghost(hunter, 1e-5);
				ghosts.add(g);
			}
		}
	}
	
	public ArrayList<LatLng> getGhostPostions() {
		ArrayList<LatLng> ghostPositions = new ArrayList<LatLng>();
		if (ghosts != null) {
			for (Ghost g : ghosts) {
				LatLng pos = new LatLng(g.getLat(), g.getLon());
				ghostPositions.add(pos);
			}
		}
		return ghostPositions;
	}	

	public void moveHunter(Location loc) {
		if (hunter != null) {
			hunter.setLocation(loc);
		}
	}
	
	public ArrayList<LatLng> moveGhosts() {
		ArrayList<LatLng> ghostPositions = new ArrayList<LatLng>();	
		if (ghosts != null) {
			for (Ghost g : ghosts) {
				g.move();
				LatLng pos = new LatLng(g.getLat(), g.getLon());
				ghostPositions.add(pos);
			}
		}
		return ghostPositions;
	}

}