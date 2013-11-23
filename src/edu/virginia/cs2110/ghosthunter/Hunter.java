package edu.virginia.cs2110.ghosthunter;

import android.location.Location;

import com.google.android.gms.maps.model.Marker;

public class Hunter {
	
	public static final int INIT_HEALTH = 3;
	
	private double lat;
	private double lon;
	private int health;
	private int score;
	
	private Marker view;
	
	//Constructor for Hunter. Needs GPS coordinate input 
	public Hunter(Location loc) {	
		this.health = INIT_HEALTH;
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
		health --;
	}
	
	public int getScore() {
		return score;
	}
	
	public Marker getView() {
		return view;
	}

	public void setView(Marker view) {
		this.view = view;
	}
	
}
