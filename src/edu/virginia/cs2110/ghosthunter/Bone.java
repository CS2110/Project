package edu.virginia.cs2110.ghosthunter;

public class Bone {
private Ghost ghost;
private float x;
private float y;

public Bone (Ghost ghost){
	this.ghost = ghost;
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
}
