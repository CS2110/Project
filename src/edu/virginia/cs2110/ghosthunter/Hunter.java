package edu.virginia.cs2110.ghosthunter;

public class Hunter {
	private float x;
	private float y;
	private int health = 100;
	
	//Constructor for Hunter. Needs GPS coordinate input 
	public Hunter() {	
		this.health = health;
		/*
		 * this.x = ;
		 * this.y = ;
		 */
	}
	// Getters and Setters
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public int getHealth() {
		return health;
	}
}
