package edu.virginia.cs2110.ghosthunter;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class GameStore {

	public static final int INIT_NUMBER_GHOSTS = 10;
	public static final double GHOST_STEP_SIZE = 5e-7;
	public static final double PROXIMITY_DISTANCE = 20.0;
	public static final int SPAWN_RATE = 2;
	
	private static GameStore store;

	@SuppressWarnings("unused")
	private Context applicationContext;
	private Hunter hunter;
	private ArrayList<Ghost> ghosts;

	private GameStore(Context c) {
		applicationContext = c.getApplicationContext(); // Keeps this object alive as long as the Activity
								// is alive
	}

	public static GameStore get(Context c) {
		if (store == null)
			store = new GameStore(c);
		return store;
	}
	
	public Hunter getHunter() {
		return hunter;
	}

	public void initGame(Location loc) {
		if (hunter == null)
			hunter = new Hunter(loc);
		if (ghosts == null) {
			ghosts = new ArrayList<Ghost>();
			for (int i = 0; i < INIT_NUMBER_GHOSTS; i++) {
				Ghost g = new Ghost(hunter, GHOST_STEP_SIZE);
				ghosts.add(g);
			}
		}
	}
	
	public ArrayList<Ghost> getGhosts() {
		return ghosts;
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
	
	public int checkProximity() {
		int near = 0;
		for (Ghost g : ghosts) {
			if (g.distanceTo(hunter) < PROXIMITY_DISTANCE) {
				near++;
			}
		}
		return near;
	}
	
	public int hunterAttacked() {
		for (Ghost g : ghosts) {
			if (g.collision(hunter)) {
				hunter.loseHealth();
				return ghosts.indexOf(g);
			}
		}
		return -1;
	}
	
	public boolean hunterKilled() {
		return (hunter.getHealth() == 0);
	}
	
	public void killGhost(int index) {
		ghosts.remove(index);
	}
	
	public Ghost[] spawnGhosts() {
		Ghost[] g = new Ghost[SPAWN_RATE];
		for (int i = 0; i < g.length; i++) {
			g[i] = new Ghost(hunter, GHOST_STEP_SIZE);
			ghosts.add(g[i]);
		}
		return g;
	}

	public void gameOver() {
		store = null;
	}
	
	// we need this so that the game can display health
	public int getHealth() {
		return hunter.getHealth();
	}
	
	// game needs this to display score
	public int getScore() {
		return hunter.getScore();
	}

}
