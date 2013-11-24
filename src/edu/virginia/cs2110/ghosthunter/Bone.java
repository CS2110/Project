package edu.virginia.cs2110.ghosthunter;

import java.util.Random;

import com.google.android.gms.maps.model.Marker;

import android.location.Location;

public class Bone {
	private Ghost ghost;
	private Hunter hunter;
	private Random rand;
	private final double COLLISION_THRESHOLD = 10.0;
	private final double CLOSEST_DISTANCE = 20.0;
	public static final double MAX_DEGREES_AWAY = 1e-3;
	private double lat, lon;
	private Marker view;

	// Getters and Setters
	public double getLat() {
		return lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public Ghost getGhost() {
		return ghost;
	}

	public Marker getView() {
		return view;
	}

	public void setView(Marker view) {
		this.view = view;
	}

	// ----------------------------------------------------------------------------------------------

	public Bone(Hunter player, Ghost g) {
		this.hunter = player;
		this.ghost = g;
		rand = new Random();
		initLocation();
	}

	private void initLocation() {
		setLat(randLatLon(hunter.getLat()));
		do {
			setLon(randLatLon(hunter.getLon()));
		} while (distanceTo(hunter) < CLOSEST_DISTANCE);
	}

	private double randLatLon(double hLatLon) {
		double latLon;
		if (rand.nextBoolean()) {
			latLon = hLatLon + rand.nextDouble() * MAX_DEGREES_AWAY;
		} else {
			latLon = hLatLon - rand.nextDouble() * MAX_DEGREES_AWAY;
		}
		return latLon;
	}

	public boolean collision(Hunter hunter) {
		return (distanceTo(hunter) < COLLISION_THRESHOLD);
	}

	public double distanceTo(Hunter hunter) {
		Location a = new Location("A");
		a.setLatitude(getLat());
		a.setLongitude(getLon());
		Location b = new Location("B");
		b.setLatitude(hunter.getLat());
		b.setLongitude(hunter.getLon());
		double d = a.distanceTo(b);
		return d;
	}
	
}
