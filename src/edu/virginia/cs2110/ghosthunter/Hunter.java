package edu.virginia.cs2110.ghosthunter;

import android.location.Location;

public class Hunter {
	
	private double lat;
	private double lon;
	private int health;
	private int score;
	
	//Constructor for Hunter. Needs GPS coordinate input 
	
	public Hunter(Location loc) {	
		this.health = 0;
		this.lat = loc.getLatitude();
		this.lon = loc.getLongitude();
		this.score = 0;
	}
	
	// Getters and Setters
	
	public int getHealth() {
		return health;
	}
	
	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}
	
	public void setLocation(Location loc) {
		this.lat = loc.getLatitude();
		this.lon = loc.getLongitude();
	}
	
	public void loseHealth() {
		health -= 1;
	}
	
	public int getScore() {
		return score;
	}
	
	
	
	
}
