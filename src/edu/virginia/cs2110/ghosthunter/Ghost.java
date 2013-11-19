package edu.virginia.cs2110.ghosthunter;

import java.util.Random;

import android.location.Location;


public class Ghost {

	public static final double MAX_DEGREES_AWAY = 1e-3;
	
	private double lat, lon;
	private double step;
	// private final int collisionBuffer = 35;
	private final double DISTANCE_THRESHOLD = 25;
	// private final double COLLISION_THRESHOLD = 35;
	
	private Hunter player;

	public Ghost(Hunter player, double step) {
		this.lat = randLatLon(player.getLat());
		this.lon = randLatLon(player.getLon());
		this.step = step;
		this.player = player;
	}

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
	
	private double randLatLon(double hLatLon) {
		int coefficient = -1;
		boolean positive = (new Random()).nextBoolean();
		if (positive)
			coefficient = 1;
		double latLon = hLatLon + coefficient * (0.5 * Math.random() + 0.5) * MAX_DEGREES_AWAY;
		return latLon;
	}
	
	/*
	public void collision(Ghost ghost, float elapsedTime) {
		float increment = elapsedTime * collisionBuffer;
		if (ghost.getX() == x) {
		} else if (ghost.getX() > x)
			setX(getX() + increment);
		else
			setX(getX() - increment);

		if (ghost.getY() == y) {
		} else if (ghost.getY() > y)
			setY(getY() + increment);
		else
			setY(getY() - increment);
	}
	
	public boolean collision(Hunter hunter) {
		return (distance(hunter) < COLLISION_THRESHOLD);
	}
	
	*/
	public void move() {
		if (distance(player) < DISTANCE_THRESHOLD) {
			if (player.getLat() > getLat())
				setLat(getLat() + step);
			else if (player.getLat() < getLat())
				setLat(getLat() - step);
			
			if (player.getLon() > getLon())
				setLon(getLon() + step);
			else if (player.getLon() < getLon())
				setLon(getLon() - step);
		} else {
			if (Math.random() > 0.5)
				setLat(getLat() + step);
			else
				setLat(getLat() - step);

			if (Math.random() > 0.5)
				setLon(getLon() + step);
			else
				setLon(getLon() - step);
		}
	}

	public double distance(Hunter hunter) {
		float[] results = new float[3];
		Location.distanceBetween(this.lat, this.lon, hunter.getLat(), hunter.getLat(), results);
		return results[0];
	}
	/*
	public double distance(Ghost ghost) {
		return Math.sqrt(Math.pow(x - ghost.getX(), 2)
				+ Math.pow(y - ghost.getY(), 2));
	}
	// Getters and Setters
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	*/
}
