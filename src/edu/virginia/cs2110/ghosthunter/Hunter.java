package edu.virginia.cs2110.ghosthunter;

public class Hunter {
	private double coordX;
	private double coordY;
	private int health;
	
	public Hunter() {
		// coords will = gps coords
	}
	
	public Hunter(int health) {
		
		this.health = health;
	}
	
	
	
	
	
	// Getters and Setters
	public double getCoordX() {
		return coordX;
	}
	
	public double getCoordY() {
		return coordY;
	}
	
	public int getHealth() {
		return health;
	}
}
