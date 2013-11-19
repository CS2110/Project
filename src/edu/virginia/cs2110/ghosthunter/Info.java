package edu.virginia.cs2110.ghosthunter;

import java.util.ArrayList;

public class Info {
	private ArrayList<Bone> bones = new ArrayList<Bone>();
	private Hunter hunter;
	private final int THRESHHOLD = 35;
	private int score = 0;
	private final int SCORE_INCREMENT = 10;

	public Info(Hunter hunter) {
		this.hunter = hunter;
	}
/*
	public void update(Hunter hunter) {
		for (Bone b : bones) {
			if (b.getGhost().collision(hunter)) {
				hunter.decreaseHealth();
			}
		}
		removeBones(hunter);
	}

	public void addBones(Hunter hunter) {
		bones.add(new Bone(new Ghost(hunter), hunter));
	}

	public void removeBones(Hunter hunter) {
		for (Bone b : bones) {
			if (b.distance(hunter) < THRESHHOLD) {
				bones.remove(b);
				addBones(hunter);
				addBones(hunter);
				score += SCORE_INCREMENT;
			}
		}
	}

	public int getScore() {
		return score;
	}
*/
}

