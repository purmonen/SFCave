package SFCave2;

/**
 * A high score that stores a name and a score that can be sorted.
 * 
 * @author Sami Purmonen and Nils Dahlberg
 * @version 2013.05.01
 */

public class HighScore implements Comparable<HighScore> {
	public String name;
	public int score;
	
	public HighScore(String name, int score) {
		this.name = name;
		this.score = score;
	}
	
	public String toString() {
		return name + ": " + score;
	}
	
	public String getName() {
		return name;
	}
	
	public int getScore() {
		return score;
	}

	@Override
	public int compareTo(HighScore highScore) {
		return highScore.score - this.score;
	}
}