package SFCave2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * A score board that stores the highest scores
 * in a file.
 * 
 * @author Sami Purmonen and Nils Dahlberg
 * @version 2013.05.01
 */
public class ScoreBoard {
	private ArrayList<HighScore> scores;
	// Maximum number of scores to be saved.
	private int maxSize;
	// File in which the scores are stored.
	private String fileName;
	private int score;
	
	/**
	 * Create a score board.
	 * 
	 * @param size The maximum size of scores that can be stored.
	 */
	public ScoreBoard(int size) {
		this(size, "highscore.txt");
	}
	
	/**
	 * @param fileName The file in which the scores are stored.
	 */
	public ScoreBoard(int size, String fileName) {
		if (size < 1) {
			throw new IllegalArgumentException();
		}
		scores = new ArrayList<HighScore>();
		maxSize = size;
		this.fileName = fileName;
		File file = new File(fileName);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
			}
		}
		readScores();
		score = 0;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int score) {
		this.score = score;
	}
	
	/**
	 * Checks is a score should be in the score board
	 * 
	 * @param score A score to be compared to the score board
	 * @return A boolean telling if the score belongs is a high score
	 */
	public boolean isHighScore() {
		if (scores.size() < maxSize) {
			return true;
		}
		for (HighScore h : scores) {
			if (h.score < score) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Adds a new high score to the score board.
	 * 
	 * @param name The name of the person associated with the score.
	 * @param score The value of the score.
	 * @return A boolean value indicating whether the score was added or not.
	 */
	public boolean addScore(String name) {
		boolean added = false;
		if (scores.size() < maxSize) {
			scores.add(new HighScore(name, score));
			added = true;
		} else if (score > scores.get(scores.size() - 1).score) {
			scores.remove(scores.size() - 1);
			scores.add(new HighScore(name, score));
			added = true;
		}
		Collections.sort(scores);
		return added;
	}
	
	public ArrayList<HighScore> getScores() {
		return scores;
	}
	
	/**
	 * Save scoreboard to file
	 */
	public void writeScores() {
		FileWriter writer = null;
		
		try {
			writer = new FileWriter(fileName);
			for (HighScore h : scores) {
				writer.write(h.name + " " + h.score + "\n");
			}
		} catch (FileNotFoundException e) {
			System.out.println("FILE NOT FOUND");
		} catch (IOException e) {
			System.out.println("RETARDED");
		} finally {
			if (writer == null) {
				return;
			}
			try {
				writer.close();
			} catch (IOException e) {
			}
		}
	}
	
	/**
	 * Read scoreboard from file
	 */
	private void readScores() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(fileName));
			String line;
			String[] temp;
			while ((line = reader.readLine()) != null) {
				temp = line.split(" ");
				setScore(Integer.parseInt(temp[1]));
				addScore(temp[0]);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("FILE NOT FOUND");
		} catch (NumberFormatException e) {
		} catch (IOException e) {
		} finally {
			if (reader == null) {
				return;
			}
			try {
				reader.close();
			} catch (IOException e) {
			}
		}
	}
	
	/**
	 * Removes all high scores from the score board.
	 */
	public void clear() {
		scores = new ArrayList<HighScore>();
		FileWriter writer = null;
		try {
			writer = new FileWriter(fileName);
			writer.write("");
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} finally {
			if (writer == null) {
				return;
			}
			try {
				writer.close();
			} catch (IOException e) {
			}
		}
	}
}
