package edu.virginia.cs2110.ghosthunter;

import java.util.Random;

import android.location.Location;

public class Bone {
private Ghost ghost;
private Hunter hunter;
private Random rand;
private final double COLLISION_THRESHOLD = 5.0;
private final double CLOSEST_DISTANCE = 10;
public static final double MAX_DEGREES_AWAY = 1e-3;
private double lat, lon;

//Getters and Setters
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
// ----------------------------------------------------------------------------------------------

public Bone(Hunter player, Ghost ghost) {
	rand = new Random();
	initLocation();
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

/*
public Bone (Ghost ghost, Hunter hunter){
	this.ghost = ghost;
	this.hunter = hunter; 
	x = rand.nextFloat() + ghost.getX(); // need to find out how large to make nextDouble
	y = rand.nextFloat() + ghost.getY(); // ^
}
public double distance(Hunter hunter) {
	return Math.sqrt(Math.pow(x - hunter.getX(), 2)
			+ Math.pow(y - hunter.getY(), 2));
}
public float getX() {
	return x;
}
public void setX(float x) {
	this.x = x;
}
public float getY() {
	return y;
}
public void setY(float y) {
	this.y = y;
}
public Ghost getGhost() {
	return ghost;
}
*/
}
