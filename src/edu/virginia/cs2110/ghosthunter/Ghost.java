package edu.virginia.cs2110.ghosthunter;

import java.util.Random;

public class Ghost {
	private double coordX;
	private double coordY;
	private double dx;
	private double dy;
	private int speed;
	private Hunter player;
	Random rand = new Random();
	
	public Ghost(Hunter player) {
		coordX = rand.nextDouble() + player.getCoordX(); // need to find out how large to make nextDouble
		coordY = rand.nextDouble() + player.getCoordY(); // ^
		dx = coordX; // reason for this is in move method
		dy = coordY;
		speed = 10; // arbitrary probably will have to change once we know how gps works
		this.player = player;
	}
	
	/**
	 * This method returns true if two ghosts occupy the same space.
	 * @param ghost
	 * @return boolean
	 */
	public boolean collisionCheck(Ghost ghost, int threshold){
		if (Math.sqrt(Math.pow(coordX - ghost.getCoordX(), 2) + Math.pow(coordY - ghost.getCoordY(), 2)) <= threshold){
			return true;
		}
		return false;
	}
		
	public void move(float elapsedTime){
		if (dx == coordX || dy == coordY) {
			dx = coordX + rand.nextDouble(); // Gotta change next double here too later
			dy = coordY + rand.nextDouble();
		}
		if (Math.pow((dx - coordX),2) > Math.pow((dy - coordY),2)){
			if (dx - coordX > 0){
				coordX = coordX + speed * elapsedTime;
			}
			else{
				coordX = coordX - speed * elapsedTime;
			}
		}
		else {
			if (dy - coordY > 0){
				coordY = coordY + speed * elapsedTime;
			}
			else {
				coordY = (coordY - speed * elapsedTime);
			}
		}
	}
	
	// Getters and Setters
	public double getCoordX() {
		return coordX;
	}
	
	public double getCoordY() {
		return coordY;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
}
