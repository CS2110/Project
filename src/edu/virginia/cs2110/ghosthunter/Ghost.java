package edu.virginia.cs2110.ghosthunter;

import java.util.Random;

import android.location.Location;


public class Ghost {

	public static final double MAX_DEGREES_AWAY = 1e-3;
	private final double CLOSEST_DISTANCE = 40.0;
	private final double DISTANCE_THRESHOLD = 60.0;
	private final double COLLISION_THRESHOLD = 5.0;
	
	private double lat, lon;
	private double step;
	// private final int collisionBuffer = 35;
	private Random rand;
	private int randDir;
	private int randIter;
	
	private Hunter hunter;

	public Ghost(Hunter player, double step) {
		rand = new Random();
		this.randDir = rand.nextInt(4);
		this.step = step;
		this.hunter = player;
		initLocation();
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
	
	private void initLocation() {
		setLat(randLatLon(hunter.getLat()));
		do {
			setLon(randLatLon(hunter.getLon()));
		} while(distanceTo(hunter) < CLOSEST_DISTANCE);
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
	*/
	public boolean collision(Hunter hunter) {
		return (distanceTo(hunter) < COLLISION_THRESHOLD);
	}
	
	public void move() {
		if (distanceTo(hunter) < DISTANCE_THRESHOLD) {
			if (hunter.getLat() > getLat())
				setLat(getLat() + step);
			else if (hunter.getLat() < getLat())
				setLat(getLat() - step);
			
			if (hunter.getLon() > getLon())
				setLon(getLon() + step);
			else if (hunter.getLon() < getLon())
				setLon(getLon() - step);
		} else {
			randomMovement();
		}
	}

	public void randomMovement() {
		double dLat = 0.0, dLon = 0.0;
		switch (randDir) {
		case 0:
			dLat = step;
			dLon = step;
			break;
		case 1:
			dLat = -step;
			dLon = step;
			break;
		case 2:
			dLat = step;
			break;
		default:
			dLon = step;
		}
		if (randIter / 100 == 0) {
			setLat(getLat() + 2.5 * dLat);
			setLon(getLon() + 2.5 * dLon);
		} else {
			setLat(getLat() - 2.5 * dLat);
			setLon(getLon() - 2.5 * dLon);
		}
		randIter++;
		if (randIter >= 200) 
			randIter = 0;
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
