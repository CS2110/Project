package edu.virginia.cs2110.ghosthunter;

import java.util.Random;

public class Ghost {

	private float x, y;
	private int speed = 10; // need to determine
	private final int collisionBuffer = 35;
	private final double DISTANCE_THRESHOLD = 100;
	private final double COLLISION_THRESHOLD = 35;
	private Hunter player;
	Random rand = new Random();

	public Ghost(Hunter player) {
		x = rand.nextFloat() + player.getX(); // need to find out how large to
												// make nextDouble
		y = rand.nextFloat() + player.getY(); // ^
		this.player = player;
	}

	/**
	 * This method returns true if two ghosts occupy the same space.
	 * 
	 * @param ghost
	 * @return boolean
	 */
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
	
	public void move(float elapsedTime) {
		float increment = elapsedTime * speed;
		if (distance(player) < DISTANCE_THRESHOLD) {
			if (player.getX() == x){
			}	
				else if (player.getX() > x)
				setX(getX() + increment);
			else
				setX(getX() - increment);
			
			if (player.getY()==y){
			}
			else if (player.getY() > y)
				setY(getY() + increment);
			else
				setY(getY() - increment);
		}
		
		else{
			if (Math.random() > 0.5)
				setX(getX() + increment);
			else 
				setX(getX() - increment);
			
			if (Math.random() > 0.5)
				setY(getY() + increment);
			else 
				setY(getY() - increment);
		}	
	}

	public double distance(Hunter hunter) {
		return Math.sqrt(Math.pow(x - hunter.getX(), 2)
				+ Math.pow(y - hunter.getY(), 2));
	}

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
}
