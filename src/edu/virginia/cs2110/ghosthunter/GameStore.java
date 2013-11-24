package edu.virginia.cs2110.ghosthunter;

import java.util.ArrayList;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class GameStore {

	public static final int INIT_NUMBER_GHOSTS = 10;
	public static final double GHOST_STEP_SIZE = 5e-7;
	public static final double PROXIMITY_DISTANCE = 20.0;
	public static final int SPAWN_RATE = 3;
	
	private static GameStore store;

	@SuppressWarnings("unused")
	private Context applicationContext;
	private Hunter hunter;
	private ArrayList<Ghost> ghosts;
	private ArrayList<Bone> bones;

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
			bones = new ArrayList<Bone>();
			for (int i = 0; i < INIT_NUMBER_GHOSTS; i++) {
				Ghost g = new Ghost(hunter, GHOST_STEP_SIZE);
				Bone b = new Bone(hunter, g);
				ghosts.add(g);
				bones.add(b);
			}
		}
	}
	
	public ArrayList<Ghost> getGhosts() {
		return ghosts;
	}
	
	public ArrayList<Bone> getBones() {
		return bones;
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
	
	public int boneHunted() {
		for (Bone b : bones) {
			if (b.collision(hunter)) {
				hunter.increaseScore();
				return bones.indexOf(b);
			}
		}
		return -1;
	}
	
	public boolean hunterKilled() {
		return (hunter.getHealth() == 0);
	}
	
	public void killBone(int index) {
		bones.remove(index);
	}
	
	public void killGhost(int index) {
		ghosts.remove(index);
	}
	
	public Bone[] spawnGhosts() {
		Bone[] b = new Bone[SPAWN_RATE];
		for (int i = 0; i < b.length; i++) {
			Ghost g = new Ghost(hunter, GHOST_STEP_SIZE);
			b[i] = new Bone(hunter, g);
			bones.add(b[i]);
			ghosts.add(g);
		}
		return b;
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
