package edu.virginia.cs2110.ghosthunter;

public interface GameListener {
	public static final String HUNTER = "hunter";
	public static final String GHOST = "ghost";
	public static final String BONE = "bone";
	
	public void gameOver(int score);
	public void removeGamePiece(String id, int index);
}
